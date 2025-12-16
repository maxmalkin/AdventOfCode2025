import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Part1 {
    public static int solve(String input) {
        String[] lines = input.trim().split("\n");
        List<char[]> grid = new ArrayList<>();
        for (String line : lines) {
            grid.add(line.toCharArray());
        }
        int rows = grid.size();
        int cols = rows > 0 ? grid.get(0).length : 0;

        int start_col = -1;
        for (int col = 0; col < cols; col++) {
            if (grid.get(0)[col] == 'S') {
                start_col = col;
                break;
            }
        }

        Set<Integer> beams = new HashSet<>();
        beams.add(start_col);
        int split_count = 0;

        for (int row = 1; row < rows; row++) {
            Set<Integer> new_beams = new HashSet<>();

            for (int col : beams) {
                if (col >= 0 && col < cols) {
                    if (grid.get(row)[col] == '^') {
                        split_count++;
                        if (col - 1 >= 0) {
                            new_beams.add(col - 1);
                        }
                        if (col + 1 < cols) {
                            new_beams.add(col + 1);
                        }
                    } else {
                        new_beams.add(col);
                    }
                }
            }

            beams = new_beams;
        }

        return split_count;
    }

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("../inputs/day_7.txt")));
        int result = solve(input);
        System.out.println(result);
    }
}
