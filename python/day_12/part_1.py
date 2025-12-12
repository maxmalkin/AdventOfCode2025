import time


def solve(input: str) -> int:
    lines = [line for line in input.strip().split("\n")]

    shapes = [[] for _ in range(6)]
    regions = []

    i = 0
    while i < len(lines):
        line = lines[i].strip()

        if not line:
            i += 1
            continue

        if ":" in line and "x" not in line:
            colon_pos = line.find(":")
            prefix = line[:colon_pos]
            if prefix.isdigit():
                idx = int(prefix)
                if idx < 6:
                    i += 1
                    while i < len(lines) and lines[i].strip() and ":" not in lines[i]:
                        shapes[idx].append(lines[i])
                        i += 1
                    continue

        if "x" in line and ":" in line:
            parts = line.split(":")
            if len(parts) == 2:
                dims = parts[0].strip().split("x")
                if len(dims) == 2:
                    try:
                        w = int(dims[0])
                        h = int(dims[1])
                        counts = [int(x) for x in parts[1].split()]
                        regions.append((w, h, counts))
                    except:
                        pass

        i += 1

    def get_shape_cells(shape):
        cells = []
        for r, row in enumerate(shape):
            for c, ch in enumerate(row):
                if ch == "#":
                    cells.append((r, c))
        return cells

    def normalize_shape(cells):
        if not cells:
            return []
        min_r = min(r for r, c in cells)
        min_c = min(c for r, c in cells)
        normalized = [(r - min_r, c - min_c) for r, c in cells]
        normalized.sort()
        return tuple(normalized)

    def rotate_90(cells):
        return [(c, -r) for r, c in cells]

    def flip_horizontal(cells):
        return [(-r, c) for r, c in cells]

    def get_all_orientations(shape):
        cells = get_shape_cells(shape)
        orientations = set()

        current = cells
        for _ in range(4):
            orientations.add(normalize_shape(current))
            current = rotate_90(current)

        current = flip_horizontal(cells)
        for _ in range(4):
            orientations.add(normalize_shape(current))
            current = rotate_90(current)

        return list(orientations)

    def can_place(grid, shape, start_r, start_c, width, height):
        for dr, dc in shape:
            r = start_r + dr
            c = start_c + dc
            if r < 0 or r >= height or c < 0 or c >= width:
                return False
            idx = r * width + c
            if grid[idx]:
                return False
        return True

    def place(grid, shape, start_r, start_c, width):
        for dr, dc in shape:
            r = start_r + dr
            c = start_c + dc
            idx = r * width + c
            grid[idx] = True

    def unplace(grid, shape, start_r, start_c, width):
        for dr, dc in shape:
            r = start_r + dr
            c = start_c + dc
            idx = r * width + c
            grid[idx] = False

    def find_first_empty(grid, width, height):
        for r in range(height):
            for c in range(width):
                if not grid[r * width + c]:
                    return (r, c)
        return None

    def solve_region(width, height, presents):
        total_area = sum(len(p[0]) for p in presents)
        grid_area = width * height

        if total_area > grid_area:
            return False

        sorted_presents = sorted(presents, key=lambda p: -len(p[0]))

        grid = [False] * (width * height)
        start_time = time.time()
        timeout_seconds = 5

        def backtrack(idx, remaining_cells):
            if time.time() - start_time > timeout_seconds:
                return False

            if idx == len(sorted_presents):
                return True

            remaining_area = sum(len(p[0]) for p in sorted_presents[idx:])
            if remaining_area > remaining_cells:
                return False

            pos = find_first_empty(grid, width, height)
            if pos is None:
                return False

            start_r, start_c = pos
            shape_variants = sorted_presents[idx]

            for variant in shape_variants:
                if can_place(grid, variant, start_r, start_c, width, height):
                    place(grid, variant, start_r, start_c, width)
                    new_remaining = remaining_cells - len(variant)
                    if backtrack(idx + 1, new_remaining):
                        return True
                    unplace(grid, variant, start_r, start_c, width)

            for variant in shape_variants:
                for r in range(height):
                    for c in range(width):
                        if (r, c) == (start_r, start_c):
                            continue
                        if can_place(grid, variant, r, c, width, height):
                            place(grid, variant, r, c, width)
                            new_remaining = remaining_cells - len(variant)
                            if backtrack(idx + 1, new_remaining):
                                return True
                            unplace(grid, variant, r, c, width)

            return False

        return backtrack(0, grid_area)

    count = 0
    for width, height, counts in regions:
        presents = []

        for shape_idx, cnt in enumerate(counts):
            if shape_idx < len(shapes) and shapes[shape_idx]:
                orientations = get_all_orientations(shapes[shape_idx])
                for _ in range(cnt):
                    presents.append(orientations)

        if solve_region(width, height, presents):
            count += 1

    return count


if __name__ == "__main__":
    with open("inputs/day_12.txt", "r") as f:
        input = f.read()
        result = solve(input)
        print(result)
