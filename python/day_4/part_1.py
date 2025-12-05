def solve(input: str) -> int:
    grid = [line for line in input.strip().split("\n")]
    rows = len(grid)
    cols = len(grid[0]) if rows > 0 else 0

    directions = [(-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1)]

    accessible_count = 0

    for row in range(rows):
        for col in range(cols):
            if grid[row][col] == "@":
                adjacent_rolls = 0

                for dr, dc in directions:
                    new_row = row + dr
                    new_col = col + dc

                    if 0 <= new_row < rows and 0 <= new_col < cols:
                        if grid[new_row][new_col] == "@":
                            adjacent_rolls += 1

                if adjacent_rolls < 4:
                    accessible_count += 1

    return accessible_count


if __name__ == "__main__":
    with open("inputs/day_4.txt", "r") as f:
        input = f.read()
        result = solve(input)
        print(result)
