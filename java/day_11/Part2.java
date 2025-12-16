import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Part2 {
    static class Key {
        String node;
        boolean has_dac, has_fft;
        Key(String node, boolean has_dac, boolean has_fft) {
            this.node = node;
            this.has_dac = has_dac;
            this.has_fft = has_fft;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return has_dac == key.has_dac && has_fft == key.has_fft && node.equals(key.node);
        }
        @Override
        public int hashCode() {
            int result = node.hashCode();
            result = 31 * result + (has_dac ? 1 : 0);
            result = 31 * result + (has_fft ? 1 : 0);
            return result;
        }
    }

    static Map<String, List<String>> graph;
    static Map<Key, Integer> memo;

    public static int count_paths(String node, boolean has_dac, boolean has_fft) {
        if (node.equals("out")) {
            return (has_dac && has_fft) ? 1 : 0;
        }

        Key key = new Key(node, has_dac, has_fft);
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        if (!graph.containsKey(node)) {
            return 0;
        }

        boolean new_has_dac = has_dac || node.equals("dac");
        boolean new_has_fft = has_fft || node.equals("fft");

        int total = 0;
        for (String neighbor : graph.get(node)) {
            total += count_paths(neighbor, new_has_dac, new_has_fft);
        }

        memo.put(key, total);
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

        return count_paths("svr", false, false);
    }

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("../inputs/day_11.txt")));
        int result = solve(input);
        System.out.println(result);
    }
}
