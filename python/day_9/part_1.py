def solve(input: str) -> int:
    lines = [line.strip() for line in input.strip().split("\n") if line.strip()]

    tiles = []
    for line in lines:
        x, y = map(int, line.split(","))
        tiles.append((x, y))

    n = len(tiles)
    max_area = 0

    for i in range(n):
        for j in range(i + 1, n):
            x1, y1 = tiles[i]
            x2, y2 = tiles[j]

            width = abs(x2 - x1) + 1
            height = abs(y2 - y1) + 1
            area = width * height

            max_area = max(max_area, area)

    return max_area


if __name__ == "__main__":
    with open("inputs/day_9.txt", "r") as f:
        input = f.read()
        result = solve(input)
        print(result)
