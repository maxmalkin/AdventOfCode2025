import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Part1 {
    public static int solve(String input) {
        List<String> lines = new ArrayList<>();
        for (String line : input.trim().split("\n")) {
            line = line.trim();
            if (!line.isEmpty()) {
                lines.add(line);
            }
        }

        List<int[]> tiles = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            tiles.add(new int[]{x, y});
        }

        int n = tiles.size();
        int max_area = 0;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int x1 = tiles.get(i)[0], y1 = tiles.get(i)[1];
                int x2 = tiles.get(j)[0], y2 = tiles.get(j)[1];

                int width = Math.abs(x2 - x1) + 1;
                int height = Math.abs(y2 - y1) + 1;
                int area = width * height;

                max_area = Math.max(max_area, area);
            }
        }

        return max_area;
    }

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("../inputs/day_9.txt")));
        int result = solve(input);
        System.out.println(result);
    }
}
