import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Part2 {
    public static int solve(String input) {
        String[] sections = input.trim().split("\n\n");

        List<int[]> ranges = new ArrayList<>();
        for (String line : sections[0].trim().split("\n")) {
            if (!line.trim().isEmpty()) {
                String[] parts = line.split("-");
                int start = Integer.parseInt(parts[0]);
                int end = Integer.parseInt(parts[1]);
                ranges.add(new int[]{start, end});
            }
        }

        Collections.sort(ranges, (a, b) -> Integer.compare(a[0], b[0]));

        List<int[]> merged = new ArrayList<>();
        for (int[] r : ranges) {
            int start = r[0];
            int end = r[1];
            if (!merged.isEmpty() && start <= merged.get(merged.size() - 1)[1] + 1) {
                int[] last = merged.get(merged.size() - 1);
                last[1] = Math.max(last[1], end);
            } else {
                merged.add(new int[]{start, end});
            }
        }

        int total = 0;
        for (int[] r : merged) {
            int start = r[0];
            int end = r[1];
            int count = end - start + 1;
            total += count;
        }

        return total;
    }

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("../inputs/day_5.txt")));
        int result = solve(input);
        System.out.println(result);
    }
}
