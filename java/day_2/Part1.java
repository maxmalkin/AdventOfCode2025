import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Part1 {
    public static boolean is_invalid_id(int n) {
        String s = String.valueOf(n);
        int length = s.length();

        if (length % 2 != 0) {
            return false;
        }

        if (s.charAt(0) == '0') {
            return false;
        }

        int mid = length / 2;
        return s.substring(0, mid).equals(s.substring(mid));
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
