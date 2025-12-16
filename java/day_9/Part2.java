import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Part2 {
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
            if (line.isEmpty()) {
                continue;
            }
            String[] parts = line.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            tiles.add(new int[]{x, y});
        }

        int n = tiles.size();

        Set<Integer> x_set = new HashSet<>();
        Set<Integer> y_set = new HashSet<>();
        for (int[] tile : tiles) {
            x_set.add(tile[0]);
            y_set.add(tile[1]);
        }

        List<Integer> all_x = new ArrayList<>(x_set);
        List<Integer> all_y = new ArrayList<>(y_set);
        all_x.sort(Integer::compare);
        all_y.sort(Integer::compare);

        Map<Integer, Integer> x_to_compressed = new HashMap<>();
        Map<Integer, Integer> y_to_compressed = new HashMap<>();
        for (int i = 0; i < all_x.size(); i++) {
            x_to_compressed.put(all_x.get(i), i);
        }
        for (int i = 0; i < all_y.size(); i++) {
            y_to_compressed.put(all_y.get(i), i);
        }

        int width = all_x.size();
        int height = all_y.size();

        List<int[]> compressed_tiles = new ArrayList<>();
        for (int[] tile : tiles) {
            int x = tile[0];
            int y = tile[1];
            compressed_tiles.add(new int[]{x_to_compressed.get(x), y_to_compressed.get(y)});
        }

        boolean[][] grid = new boolean[height][width];

        for (int[] ct : compressed_tiles) {
            int cx = ct[0];
            int cy = ct[1];
            grid[cy][cx] = true;
        }

        for (int i = 0; i < n; i++) {
            int x1 = tiles.get(i)[0], y1 = tiles.get(i)[1];
            int x2 = tiles.get((i + 1) % n)[0], y2 = tiles.get((i + 1) % n)[1];

            int cx1 = x_to_compressed.get(x1), cy1 = y_to_compressed.get(y1);
            int cx2 = x_to_compressed.get(x2), cy2 = y_to_compressed.get(y2);

            if (cx1 == cx2) {
                for (int cy = Math.min(cy1, cy2); cy <= Math.max(cy1, cy2); cy++) {
                    grid[cy][cx1] = true;
                }
            } else if (cy1 == cy2) {
                for (int cx = Math.min(cx1, cx2); cx <= Math.max(cx1, cx2); cx++) {
                    grid[cy1][cx] = true;
                }
            }
        }

        Set<String> visited = new HashSet<>();
        for (int cy = 0; cy < height; cy++) {
            for (int cx = 0; cx < width; cx++) {
                String key = cx + "," + cy;
                if (grid[cy][cx] || visited.contains(key)) {
                    continue;
                }
                int orig_x = all_x.get(cx);
                int orig_y = all_y.get(cy);

                if (point_in_polygon(tiles, orig_x, orig_y)) {
                    Stack<int[]> stack = new Stack<>();
                    stack.push(new int[]{cx, cy});
                    while (!stack.isEmpty()) {
                        int[] curr = stack.pop();
                        int curr_x = curr[0];
                        int curr_y = curr[1];
                        String curr_key = curr_x + "," + curr_y;
                        if (visited.contains(curr_key)) {
                            continue;
                        }
                        if (curr_x < 0 || curr_x >= width || curr_y < 0 || curr_y >= height) {
                            continue;
                        }
                        if (grid[curr_y][curr_x]) {
                            continue;
                        }
                        visited.add(curr_key);
                        grid[curr_y][curr_x] = true;

                        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
                        for (int[] d : dirs) {
                            stack.push(new int[]{curr_x + d[0], curr_y + d[1]});
                        }
                    }
                }
            }
        }

        int max_area = 0;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int cx1 = compressed_tiles.get(i)[0], cy1 = compressed_tiles.get(i)[1];
                int cx2 = compressed_tiles.get(j)[0], cy2 = compressed_tiles.get(j)[1];

                int min_cx = Math.min(cx1, cx2);
                int max_cx = Math.max(cx1, cx2);
                int min_cy = Math.min(cy1, cy2);
                int max_cy = Math.max(cy1, cy2);

                boolean valid = true;
                for (int cy = min_cy; cy <= max_cy && valid; cy++) {
                    for (int cx = min_cx; cx <= max_cx; cx++) {
                        if (!grid[cy][cx]) {
                            valid = false;
                            break;
                        }
                    }
                }

                if (valid) {
                    int orig_x1 = tiles.get(i)[0], orig_y1 = tiles.get(i)[1];
                    int orig_x2 = tiles.get(j)[0], orig_y2 = tiles.get(j)[1];
                    int actual_width = Math.abs(orig_x2 - orig_x1) + 1;
                    int actual_height = Math.abs(orig_y2 - orig_y1) + 1;
                    int area = actual_width * actual_height;
                    max_area = Math.max(max_area, area);
                }
            }
        }

        return max_area;
    }

    static boolean point_in_polygon(List<int[]> tiles, int px, int py) {
        int n = tiles.size();
        int count = 0;
        for (int i = 0; i < n; i++) {
            int x1 = tiles.get(i)[0], y1 = tiles.get(i)[1];
            int x2 = tiles.get((i + 1) % n)[0], y2 = tiles.get((i + 1) % n)[1];
            if (y1 == y2) {
                continue;
            }
            if (y1 > y2) {
                int tmp = x1; x1 = x2; x2 = tmp;
                tmp = y1; y1 = y2; y2 = tmp;
            }
            if (py < y1 || py >= y2) {
                continue;
            }
            double x_intersect = x1 + (double)(py - y1) * (x2 - x1) / (y2 - y1);
            if (x_intersect > px) {
                count++;
            }
        }
        return count % 2 == 1;
    }

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("../inputs/day_9.txt")));
        int result = solve(input);
        System.out.println(result);
    }
}
