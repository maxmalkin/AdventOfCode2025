import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Part1 {
    static Map<String, List<String>> graph;
    static Map<String, Integer> memo;

    public static int count_paths(String node) {
        if (node.equals("out")) {
            return 1;
        }

        if (memo.containsKey(node)) {
            return memo.get(node);
        }

        if (!graph.containsKey(node)) {
            return 0;
        }

        int total = 0;
        for (String neighbor : graph.get(node)) {
            total += count_paths(neighbor);
        }

        memo.put(node, total);
        return total;
    }

    public static int solve(String input) {
        List<String> lines = new ArrayList<>();
        for (String line : input.trim().split("\n")) {
            line = line.trim();
            if (!line.isEmpty()) {
                lines.add(line);
            }
        }

        graph = new HashMap<>();
        for (String line : lines) {
            String[] parts = line.split(":");
            String device = parts[0].trim();
            String[] outputs = parts[1].trim().split("\\s+");
            List<String> outputList = new ArrayList<>();
            for (String output : outputs) {
                outputList.add(output);
            }
            graph.put(device, outputList);
        }

        memo = new HashMap<>();

        return count_paths("you");
    }

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("../inputs/day_11.txt")));
        int result = solve(input);
        System.out.println(result);
    }
}
