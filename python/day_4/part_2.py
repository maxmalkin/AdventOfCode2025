def solve(input: str) -> int:
    def count_adjacent_rolls(grid, row, col):
        rows = len(grid)
        cols = len(grid[0]) if rows > 0 else 0

        directions = [
            (-1, -1),
            (-1, 0),
            (-1, 1),
            (0, -1),
            (0, 1),
            (1, -1),
            (1, 0),
            (1, 1),
        ]

        adjacent_rolls = 0
        for dr, dc in directions:
            new_row = row + dr
            new_col = col + dc
            if 0 <= new_row < rows and 0 <= new_col < cols:
                if grid[new_row][new_col] == "@":
                    adjacent_rolls += 1

        return adjacent_rolls

    def find_accessible_rolls(grid):
        accessible = []
        rows = len(grid)
        cols = len(grid[0]) if rows > 0 else 0

        for row in range(rows):
            for col in range(cols):
                if grid[row][col] == "@":
                    if count_adjacent_rolls(grid, row, col) < 4:
                        accessible.append((row, col))

        return accessible

    grid = [list(line) for line in input.strip().split("\n")]

    removed = 0

    while True:
        accessible = find_accessible_rolls(grid)

        if not accessible:
            break

        for row, col in accessible:
            grid[row][col] = "."

        removed += len(accessible)

    return removed


if __name__ == "__main__":
    with open("inputs/day_4.txt", "r") as f:
        input = f.read()
        result = solve(input)
        print(result)
