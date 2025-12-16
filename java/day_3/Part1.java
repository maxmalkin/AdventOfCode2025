import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Part1 {
    public static int solve(String input) {
        List<String> banks = new ArrayList<>();
        for (String line : input.trim().split("\n")) {
            line = line.trim();
            if (!line.isEmpty()) {
                banks.add(line);
            }
        }

        int sum = 0;

        for (String b : banks) {
            int max_joltage = 0;

            for (int i = 0; i < b.length(); i++) {
                for (int j = i + 1; j < b.length(); j++) {
                    int joltage = Integer.parseInt("" + b.charAt(i) + b.charAt(j));
                    max_joltage = Math.max(max_joltage, joltage);
                }
            }

            sum += max_joltage;
        }

        return sum;
    }

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("../inputs/day_3.txt")));
        int result = solve(input);
        System.out.println(result);
    }
}
