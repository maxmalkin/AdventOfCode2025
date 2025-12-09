def solve(input: str) -> int:
    lines = [line.strip() for line in input.strip().split("\n") if line.strip()]

    tiles = []
    for line in lines:
        if not line:
            continue
        x, y = map(int, line.split(","))
        tiles.append((x, y))

    n = len(tiles)

    all_x = sorted(set(x for x, y in tiles))
    all_y = sorted(set(y for x, y in tiles))

    x_to_compressed = {x: i for i, x in enumerate(all_x)}
    y_to_compressed = {y: i for i, y in enumerate(all_y)}

    width = len(all_x)
    height = len(all_y)

    # Create compressed tile list and set
    compressed_tiles = [(x_to_compressed[x], y_to_compressed[y]) for x, y in tiles]
    compressed_red_set = set(compressed_tiles)

    grid = [[False] * width for _ in range(height)]

    # Mark red tiles
    for cx, cy in compressed_tiles:
        grid[cy][cx] = True

    for i in range(n):
        x1, y1 = tiles[i]
        x2, y2 = tiles[(i + 1) % n]

        cx1, cy1 = x_to_compressed[x1], y_to_compressed[y1]
        cx2, cy2 = x_to_compressed[x2], y_to_compressed[y2]

        if cx1 == cx2:
            for cy in range(min(cy1, cy2), max(cy1, cy2) + 1):
                grid[cy][cx1] = True
        elif cy1 == cy2:  # horizontal edge
            for cx in range(min(cx1, cx2), max(cx1, cx2) + 1):
                grid[cy1][cx] = True

    def point_in_polygon(px, py):
        count = 0
        for i in range(n):
            x1, y1 = tiles[i]
            x2, y2 = tiles[(i + 1) % n]
            if y1 == y2:
                continue
            if y1 > y2:
                x1, y1, x2, y2 = x2, y2, x1, y1
            if py < y1 or py >= y2:
                continue
            x_intersect = x1 + (py - y1) * (x2 - x1) / (y2 - y1)
            if x_intersect > px:
                count += 1
        return count % 2 == 1

    visited = set()
    for cy in range(height):
        for cx in range(width):
            if grid[cy][cx] or (cx, cy) in visited:
                continue
            orig_x, orig_y = all_x[cx], all_y[cy]

            if point_in_polygon(orig_x, orig_y):
                stack = [(cx, cy)]
                while stack:
                    curr_x, curr_y = stack.pop()
                    if (curr_x, curr_y) in visited:
                        continue
                    if curr_x < 0 or curr_x >= width or curr_y < 0 or curr_y >= height:
                        continue
                    if grid[curr_y][curr_x]:
                        continue
                    visited.add((curr_x, curr_y))
                    grid[curr_y][curr_x] = True

                    for dx, dy in [(0, 1), (0, -1), (1, 0), (-1, 0)]:
                        stack.append((curr_x + dx, curr_y + dy))

    max_area = 0

    for i in range(n):
        for j in range(i + 1, n):
            cx1, cy1 = compressed_tiles[i]
            cx2, cy2 = compressed_tiles[j]

            min_cx = min(cx1, cx2)
            max_cx = max(cx1, cx2)
            min_cy = min(cy1, cy2)
            max_cy = max(cy1, cy2)

            valid = True
            for cy in range(min_cy, max_cy + 1):
                if not valid:
                    break
                for cx in range(min_cx, max_cx + 1):
                    if not grid[cy][cx]:
                        valid = False
                        break

            if valid:
                orig_x1, orig_y1 = tiles[i]
                orig_x2, orig_y2 = tiles[j]
                actual_width = abs(orig_x2 - orig_x1) + 1
                actual_height = abs(orig_y2 - orig_y1) + 1
                area = actual_width * actual_height
                max_area = max(max_area, area)

    return max_area


if __name__ == "__main__":
    with open("inputs/day_9.txt", "r") as f:
        input = f.read()
        result = solve(input)
        print(result)
