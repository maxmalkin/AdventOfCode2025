import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Part1 {
    public static int solve(String input) {
        List<String> grid = new ArrayList<>();
        for (String line : input.trim().split("\n")) {
            grid.add(line);
        }
        int rows = grid.size();
        int cols = rows > 0 ? grid.get(0).length() : 0;

        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

        int accessible_count = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (grid.get(row).charAt(col) == '@') {
                    int adjacent_rolls = 0;

                    for (int[] d : directions) {
                        int dr = d[0];
                        int dc = d[1];
                        int new_row = row + dr;
                        int new_col = col + dc;

                        if (new_row >= 0 && new_row < rows && new_col >= 0 && new_col < cols) {
                            if (grid.get(new_row).charAt(new_col) == '@') {
                                adjacent_rolls++;
                            }
                        }
                    }

                    if (adjacent_rolls < 4) {
                        accessible_count++;
                    }
                }
            }
        }

        return accessible_count;
    }

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("../inputs/day_4.txt")));
        int result = solve(input);
        System.out.println(result);
    }
}
