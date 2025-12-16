import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Part2 {
    static class Key {
        int row, col;
        Key(int row, int col) {
            this.row = row;
            this.col = col;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return row == key.row && col == key.col;
        }
        @Override
        public int hashCode() {
            return 31 * row + col;
        }
    }

    static List<char[]> grid;
    static int rows;
    static int cols;
    static Map<Key, Integer> memo;

    public static int count_paths(int row, int col) {
        if (row >= rows) {
            return 1;
        }

        if (col < 0 || col >= cols) {
            return 0;
        }

        Key key = new Key(row, col);
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        int result;
        if (grid.get(row)[col] == '^') {
            result = count_paths(row + 1, col - 1) + count_paths(row + 1, col + 1);
        } else {
            result = count_paths(row + 1, col);
        }

        memo.put(key, result);
        return result;
    }

    public static int solve(String input) {
        String[] lines = input.trim().split("\n");
        grid = new ArrayList<>();
        for (String line : lines) {
            grid.add(line.toCharArray());
        }
        rows = grid.size();
        cols = rows > 0 ? grid.get(0).length : 0;

        int start_col = -1;
        for (int col = 0; col < cols; col++) {
            if (grid.get(0)[col] == 'S') {
                start_col = col;
                break;
            }
        }

        memo = new HashMap<>();

        return count_paths(0, start_col);
    }

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("../inputs/day_7.txt")));
        int result = solve(input);
        System.out.println(result);
    }
}
