import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Part2 {
    public static boolean is_invalid_id(int n) {
        String s = String.valueOf(n);
        int length = s.length();

        if (s.charAt(0) == '0') {
            return false;
        }

        for (int pattern_len = 1; pattern_len <= length / 2; pattern_len++) {
            if (length % pattern_len == 0) {
                String pattern = s.substring(0, pattern_len);
                int repeats = length / pattern_len;

                StringBuilder result = new StringBuilder();
                for (int i = 0; i < repeats; i++) {
                    result.append(pattern);
                }

                if (result.toString().equals(s) && repeats >= 2) {
                    return true;
                }
            }
        }

        return false;
    }

    public static int solve(String input) {
        List<int[]> ranges = new ArrayList<>();
        for (String range_str : input.trim().split(",")) {
            range_str = range_str.trim();
            if (!range_str.isEmpty()) {
                String[] parts = range_str.split("-");
                int start = Integer.parseInt(parts[0]);
                int end = Integer.parseInt(parts[1]);
                ranges.add(new int[]{start, end});
            }
        }

        int sum = 0;

        for (int[] r : ranges) {
            int start = r[0];
            int end = r[1];
            for (int i = start; i <= end; i++) {
                if (is_invalid_id(i)) {
                    sum += i;
                }
            }
        }

        return sum;
    }

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("../inputs/day_2.txt")));
        int result = solve(input);
        System.out.println(result);
    }
}
