import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Part2 {
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
            int n = b.length();
            int k = 12;
            StringBuilder result = new StringBuilder();
            int start = 0;

            for (int i = 0; i < k; i++) {
                int end = n - k + i;
                char max_digit = '0';
                for (int j = start; j <= end; j++) {
                    if (b.charAt(j) > max_digit) {
                        max_digit = b.charAt(j);
                    }
                }

                for (int j = start; j <= end; j++) {
                    if (b.charAt(j) == max_digit) {
                        result.append(max_digit);
                        start = j + 1;
                        break;
                    }
                }
            }

            int joltage = Integer.parseInt(result.toString());
            sum += joltage;
        }

        return sum;
    }

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("../inputs/day_3.txt")));
        int result = solve(input);
        System.out.println(result);
    }
}
