def solve(input: str) -> int:
    lines = input.strip().split("\n")

    region_start = 0
    for i, line in enumerate(lines):
        if "x" in line and ":" in line:
            region_start = i
            break

    shapes = []
    i = 0
    while i < region_start:
        line = lines[i]
        if ":" in line:
            shape = []
            i += 1
            while i < region_start and ":" not in lines[i]:
                if lines[i].strip():
                    shape.append(lines[i])
                i += 1
            if shape:
                shapes.append(shape)
        else:
            i += 1

    regions = []
    for i in range(region_start, len(lines)):
        line = lines[i].strip()
        if not line:
            continue
        parts = line.split(":")
        size_parts = parts[0].strip().split("x")
        width = int(size_parts[0])
        height = int(size_parts[1])
        counts = list(map(int, parts[1].strip().split()))
        regions.append((width, height, counts))

    def get_shape_cells(shape):
        cells = []
        for r, row in enumerate(shape):
            for c, ch in enumerate(row):
                if ch == "#":
                    cells.append((r, c))
        return cells

    def normalize_cells(cells):
        if not cells:
            return []
        min_r = min(r for r, c in cells)
        min_c = min(c for r, c in cells)
        return sorted([(r - min_r, c - min_c) for r, c in cells])

    def get_all_orientations(shape):
        cells = get_shape_cells(shape)
        orientations = set()

        current = cells
        for _ in range(4):
            orientations.add(tuple(normalize_cells(current)))
            current = [(c, -r) for r, c in current]

        current = [(r, -c) for r, c in cells]
        for _ in range(4):
            orientations.add(tuple(normalize_cells(current)))
            current = [(c, -r) for r, c in current]

        return [list(o) for o in orientations]

    def can_place(grid, cells, row, col):
        height = len(grid)
        width = len(grid[0])

        for dr, dc in cells:
            r, c = row + dr, col + dc
            if r < 0 or r >= height or c < 0 or c >= width:
                return False
            if grid[r][c]:
                return False
        return True

    def place(grid, cells, row, col, value):
        for dr, dc in cells:
            r, c = row + dr, col + dc
            grid[r][c] = value

    def backtrack(grid, presents_to_place, all_orientations):
        if not presents_to_place:
            return True

        shape_idx = presents_to_place[0]
        remaining = presents_to_place[1:]

        height = len(grid)
        width = len(grid[0])

        for orientation in all_orientations[shape_idx]:
            for r in range(height):
                for c in range(width):
                    if can_place(grid, orientation, r, c):
                        place(grid, orientation, r, c, True)
                        if backtrack(grid, remaining, all_orientations):
                            return True
                        place(grid, orientation, r, c, False)

        return False

    def can_fit_all(width, height, counts, shapes):
        grid = [[False] * width for _ in range(height)]

        all_orientations = []
        for shape in shapes:
            all_orientations.append(get_all_orientations(shape))

        presents_to_place = []
        for shape_idx, count in enumerate(counts):
            for _ in range(count):
                presents_to_place.append(shape_idx)

        return backtrack(grid, presents_to_place, all_orientations)

    count = 0
    for width, height, counts in regions:
        if can_fit_all(width, height, counts, shapes):
            count += 1

    return count


if __name__ == "__main__":
    with open("inputs/day_12.txt", "r") as f:
        input = f.read()
        result = solve(input)
        print(result)
