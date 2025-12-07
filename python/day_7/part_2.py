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

    memo = {}

    def count_paths(row, col):
        if row >= rows:
            return 1

        if col < 0 or col >= cols:
            return 0

        if (row, col) in memo:
            return memo[(row, col)]

        if grid[row][col] == "^":
            result = count_paths(row + 1, col - 1) + count_paths(row + 1, col + 1)
        else:
            result = count_paths(row + 1, col)

        memo[(row, col)] = result
        return result

    return count_paths(0, start_col)


if __name__ == "__main__":
    with open("inputs/day_7.txt", "r") as f:
        input = f.read()
        result = solve(input)
        print(result)
