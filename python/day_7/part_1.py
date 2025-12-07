def solve(input: str) -> int:
    lines = input.strip().split("\n")
    grid = [list(line) for line in lines]
    rows = len(grid)
    cols = len(grid[0]) if rows > 0 else 0

    start_col = -1
    for col in range(cols):
        if grid[0][col] == "S":
            start_col = col
            break

    beams = {start_col}
    split_count = 0

    for row in range(1, rows):
        new_beams = set()

        for col in beams:
            if 0 <= col < cols:
                if grid[row][col] == "^":
                    split_count += 1
                    if col - 1 >= 0:
                        new_beams.add(col - 1)
                    if col + 1 < cols:
                        new_beams.add(col + 1)
                else:
                    new_beams.add(col)

        beams = new_beams

    return split_count


if __name__ == "__main__":
    with open("inputs/day_7.txt", "r") as f:
        input = f.read()
        result = solve(input)
        print(result)
