import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Part1 {
    static class Distance {
        double dist;
        int i, j;
        Distance(double dist, int i, int j) {
            this.dist = dist;
            this.i = i;
            this.j = j;
        }
    }

    static int[] parent;

    public static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public static void union(int x, int y) {
        int px = find(x);
        int py = find(y);
        if (px != py) {
            parent[px] = py;
        }
    }

    public static int solve(String input) {
        List<String> lines = new ArrayList<>();
        for (String line : input.trim().split("\n")) {
            line = line.trim();
            if (!line.isEmpty()) {
                lines.add(line);
            }
        }

        List<int[]> boxes = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int z = Integer.parseInt(parts[2]);
            boxes.add(new int[]{x, y, z});
        }

        int n = boxes.size();

        List<Distance> distances = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int x1 = boxes.get(i)[0], y1 = boxes.get(i)[1], z1 = boxes.get(i)[2];
                int x2 = boxes.get(j)[0], y2 = boxes.get(j)[1], z2 = boxes.get(j)[2];
                double dist = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1));
                distances.add(new Distance(dist, i, j));
            }
        }

        distances.sort((a, b) -> Double.compare(a.dist, b.dist));

        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }

        int limit = Math.min(1000, distances.size());
        for (int i = 0; i < limit; i++) {
            int a = distances.get(i).i;
            int b = distances.get(i).j;
            union(a, b);
        }

        Map<Integer, Integer> circuit_sizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = find(i);
            circuit_sizes.put(root, circuit_sizes.getOrDefault(root, 0) + 1);
        }

        List<Integer> sizes = new ArrayList<>(circuit_sizes.values());
        sizes.sort((a, b) -> b - a);

        return sizes.get(0) * sizes.get(1) * sizes.get(2);
    }

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("../inputs/day_8.txt")));
        int result = solve(input);
        System.out.println(result);
    }
}
