import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Part1 {
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

        List<Integer> ingredient_ids = new ArrayList<>();
        for (String line : sections[1].trim().split("\n")) {
            line = line.trim();
            if (!line.isEmpty()) {
                try {
                    int id = Integer.parseInt(line);
                    ingredient_ids.add(id);
                } catch (NumberFormatException e) {
                }
            }
        }

        int fresh_count = 0;
        for (int id : ingredient_ids) {
            boolean is_fresh = false;
            for (int[] r : ranges) {
                int start = r[0];
                int end = r[1];
                if (start <= id && id <= end) {
                    is_fresh = true;
                    break;
                }
            }
            if (is_fresh) {
                fresh_count++;
            }
        }

        return fresh_count;
    }

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("../inputs/day_5.txt")));
        int result = solve(input);
        System.out.println(result);
    }
}
