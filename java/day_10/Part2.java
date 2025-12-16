import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Part2 {
    public static int solve_machine(List<List<Integer>> buttons, List<Integer> joltages) {
        int n_buttons = buttons.size();
        int n_counters = joltages.size();

        int min_presses = Integer.MAX_VALUE;

        int max_tries = 1000000;
        if (n_buttons <= 10) {
            max_tries = 1 << (n_buttons * 4);
            if (max_tries > 1000000) {
                max_tries = 1000000;
            }
        }

        for (int attempt = 0; attempt < max_tries; attempt++) {
            int[] x = new int[n_buttons];
            int total = 0;
            int temp_attempt = attempt;
            for (int i = 0; i < n_buttons; i++) {
                x[i] = temp_attempt % 100;
                temp_attempt /= 100;
                total += x[i];
            }

            if (total >= min_presses) {
                continue;
            }

            boolean valid = true;
            for (int counter_idx = 0; counter_idx < n_counters; counter_idx++) {
                int counter_sum = 0;
                for (int btn_idx = 0; btn_idx < n_buttons; btn_idx++) {
                    boolean found = false;
                    for (int c : buttons.get(btn_idx)) {
                        if (c == counter_idx) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        counter_sum += x[btn_idx];
                    }
                }
                if (counter_sum != joltages.get(counter_idx)) {
                    valid = false;
                    break;
                }
            }

            if (valid && total < min_presses) {
                min_presses = total;
            }
        }

        if (min_presses == Integer.MAX_VALUE) {
            return -1;
        }
        return min_presses;
    }

    public static int solve(String input) {
        List<String> lines = new ArrayList<>();
        for (String line : input.trim().split("\n")) {
            line = line.trim();
            if (!line.isEmpty()) {
                lines.add(line);
            }
        }

        int total_presses = 0;

        for (String line : lines) {
            List<List<Integer>> buttons = new ArrayList<>();
            Pattern button_re = Pattern.compile("\\(([0-9,]+)\\)");
            Matcher button_matcher = button_re.matcher(line);
            while (button_matcher.find()) {
                List<Integer> button = new ArrayList<>();
                for (String x_str : button_matcher.group(1).split(",")) {
                    button.add(Integer.parseInt(x_str));
                }
                buttons.add(button);
            }

            Pattern joltage_re = Pattern.compile("\\{([0-9,]+)\\}");
            Matcher joltage_match = joltage_re.matcher(line);
            List<Integer> joltages = new ArrayList<>();
            if (joltage_match.find()) {
                for (String x_str : joltage_match.group(1).split(",")) {
                    joltages.add(Integer.parseInt(x_str));
                }
            }

            int min_presses = solve_machine(buttons, joltages);
            total_presses += min_presses;
        }

        return total_presses;
    }

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("../inputs/day_10.txt")));
        int result = solve(input);
        System.out.println(result);
    }
}
