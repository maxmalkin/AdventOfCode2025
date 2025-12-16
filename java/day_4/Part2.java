import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Part2 {
    public static int count_adjacent_rolls(char[][] grid, int row, int col) {
        int rows = grid.length;
        int cols = rows > 0 ? grid[0].length : 0;

        int[][] directions = {
            {-1, -1},
            {-1, 0},
            {-1, 1},
            {0, -1},
            {0, 1},
            {1, -1},
            {1, 0},
            {1, 1}
        };

        int adjacent_rolls = 0;
        for (int[] d : directions) {
            int dr = d[0];
            int dc = d[1];
            int new_row = row + dr;
            int new_col = col + dc;
            if (new_row >= 0 && new_row < rows && new_col >= 0 && new_col < cols) {
                if (grid[new_row][new_col] == '@') {
                    adjacent_rolls++;
                }
            }
        }

        return adjacent_rolls;
    }

    public static List<int[]> find_accessible_rolls(char[][] grid) {
        List<int[]> accessible = new ArrayList<>();
        int rows = grid.length;
        int cols = rows > 0 ? grid[0].length : 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (grid[row][col] == '@') {
                    if (count_adjacent_rolls(grid, row, col) < 4) {
                        accessible.add(new int[]{row, col});
                    }
                }
            }
        }

        return accessible;
    }

    public static int solve(String input) {
        List<String> lines = new ArrayList<>();
        for (String line : input.trim().split("\n")) {
            lines.add(line);
        }

        int rows = lines.size();
        int cols = rows > 0 ? lines.get(0).length() : 0;
        char[][] grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        int removed = 0;

        while (true) {
            List<int[]> accessible = find_accessible_rolls(grid);

            if (accessible.isEmpty()) {
                break;
            }

            for (int[] pos : accessible) {
                int row = pos[0];
                int col = pos[1];
                grid[row][col] = '.';
            }

            removed += accessible.size();
        }

        return removed;
    }

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("../inputs/day_4.txt")));
        int result = solve(input);
        System.out.println(result);
    }
}
