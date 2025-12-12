def solve(input: str) -> int:
    lines = [line for line in input.strip().split("\n")]

    shapes = {}
    regions = []

    i = 0
    while i < len(lines):
        line = lines[i].strip()

        if not line:
            i += 1
            continue

        if ":" in line and "x" not in line:
            parts = line.split(":")
            if parts[0].isdigit():
                idx = int(parts[0])
                shape_lines = []
                i += 1
                while i < len(lines) and lines[i].strip() and ":" not in lines[i]:
                    shape_lines.append(lines[i])
                    i += 1
                shapes[idx] = shape_lines
                continue

        if "x" in line and ":" in line:
            parts = line.split(":")
            dims = parts[0].strip()
            width, height = map(int, dims.split("x"))
            counts = list(map(int, parts[1].strip().split()))
            regions.append((width, height, counts))

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
        return tuple(sorted((r - min_r, c - min_c) for r, c in cells))

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
            r, c = start_r + dr, start_c + dc
            if r < 0 or r >= height or c < 0 or c >= width:
                return False
            if grid[r][c]:
                return False
        return True

    def place(grid, shape, start_r, start_c):
        for dr, dc in shape:
            r, c = start_r + dr, start_c + dc
            grid[r][c] = True

    def unplace(grid, shape, start_r, start_c):
        for dr, dc in shape:
            r, c = start_r + dr, start_c + dc
            grid[r][c] = False

    def solve_region(width, height, presents):
        grid = [[False] * width for _ in range(height)]

        def backtrack(idx):
            if idx == len(presents):
                return True

            shape_variants = presents[idx]

            for variant in shape_variants:
                for r in range(height):
                    for c in range(width):
                        if can_place(grid, variant, r, c, width, height):
                            place(grid, variant, r, c)
                            if backtrack(idx + 1):
                                return True
                            unplace(grid, variant, r, c)

            return False

        return backtrack(0)

    count = 0
    for width, height, counts in regions:
        presents = []
        for shape_idx, cnt in enumerate(counts):
            if shape_idx in shapes:
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
