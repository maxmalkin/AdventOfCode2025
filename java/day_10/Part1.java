import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Part1 {
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
            Pattern target_re = Pattern.compile("\\[([.#]+)\\]");
            Matcher target_match = target_re.matcher(line);
            if (!target_match.find()) {
                continue;
            }

            String target_str = target_match.group(1);
            int target = 0;
            int l = target_str.length();
            for (int i = 0; i < target_str.length(); i++) {
                if (target_str.charAt(i) == '#') {
                    target |= 1 << i;
                }
            }

            Pattern button_re = Pattern.compile("\\(([\\d,\\s]+)\\)");
            Matcher button_matcher = button_re.matcher(line);
            List<Integer> buttons = new ArrayList<>();
            Set<Integer> button_set = new HashSet<>();
            while (button_matcher.find()) {
                String b_str = button_matcher.group(1);
                String[] parts_str = b_str.split(",");
                int b_mask = 0;
                for (String p_str : parts_str) {
                    p_str = p_str.trim();
                    if (!p_str.isEmpty()) {
                        int p = Integer.parseInt(p_str);
                        if (p < l) {
                            b_mask |= 1 << p;
                        }
                    }
                }
                if (!button_set.contains(b_mask)) {
                    buttons.add(b_mask);
                    button_set.add(b_mask);
                }
            }

            int k = buttons.size();

            if (l <= 15) {
                Queue<int[]> queue = new LinkedList<>();
                queue.add(new int[]{0, 0});
                Set<Integer> seen = new HashSet<>();
                seen.add(0);
                boolean found = false;
                int best_dist = Integer.MAX_VALUE;

                while (!queue.isEmpty()) {
                    int[] curr_state = queue.poll();
                    int curr = curr_state[0];
                    int dist = curr_state[1];

                    if (curr == target) {
                        best_dist = dist;
                        found = true;
                        break;
                    }

                    for (int b : buttons) {
                        int nxt = curr ^ b;
                        if (!seen.contains(nxt)) {
                            seen.add(nxt);
                            queue.add(new int[]{nxt, dist + 1});
                        }
                    }
                }

                if (found) {
                    total_presses += best_dist;
                }

            } else {
                List<Integer> matrix = new ArrayList<>();
                for (int r = 0; r < l; r++) {
                    int row_val = 0;
                    for (int c = 0; c < k; c++) {
                        if (((buttons.get(c) >> r) & 1) == 1) {
                            row_val |= 1 << c;
                        }
                    }
                    if (((target >> r) & 1) == 1) {
                        row_val |= 1 << k;
                    }
                    matrix.add(row_val);
                }

                int pivot_row = 0;
                Map<Integer, Integer> pivots = new HashMap<>();
                Set<Integer> free_cols = new HashSet<>();
                for (int c = 0; c < k; c++) {
                    free_cols.add(c);
                }

                for (int c = 0; c < k; c++) {
                    int pivot = -1;
                    for (int r = pivot_row; r < l; r++) {
                        if (((matrix.get(r) >> c) & 1) == 1) {
                            pivot = r;
                            break;
                        }
                    }

                    if (pivot != -1) {
                        int temp = matrix.get(pivot_row);
                        matrix.set(pivot_row, matrix.get(pivot));
                        matrix.set(pivot, temp);
                        int pivot_val = matrix.get(pivot_row);
                        for (int r = 0; r < l; r++) {
                            if (r != pivot_row) {
                                if (((matrix.get(r) >> c) & 1) == 1) {
                                    matrix.set(r, matrix.get(r) ^ pivot_val);
                                }
                            }
                        }
                        pivots.put(pivot_row, c);
                        free_cols.remove(c);
                        pivot_row++;
                    }
                }

                boolean possible = true;
                for (int r = pivot_row; r < l; r++) {
                    if (((matrix.get(r) >> k) & 1) == 1) {
                        possible = false;
                        break;
                    }
                }

                if (possible) {
                    List<Integer> free_vars = new ArrayList<>(free_cols);
                    int num_free = free_vars.size();

                    List<int[]> pivot_equations = new ArrayList<>();
                    for (int r = 0; r < pivot_row; r++) {
                        int base_val = (matrix.get(r) >> k) & 1;
                        int dep_mask = 0;
                        for (int i = 0; i < num_free; i++) {
                            int fv = free_vars.get(i);
                            if (((matrix.get(r) >> fv) & 1) == 1) {
                                dep_mask |= 1 << i;
                            }
                        }
                        pivot_equations.add(new int[]{base_val, dep_mask});
                    }

                    int min_w = Integer.MAX_VALUE;
                    for (int mask = 0; mask < (1 << num_free); mask++) {
                        int w = Integer.bitCount(mask);
                        if (w >= min_w) {
                            continue;
                        }

                        int curr_w = w;
                        for (int[] eq : pivot_equations) {
                            int base = eq[0];
                            int dep = eq[1];
                            int bits = Integer.bitCount(dep & mask);
                            int val = base ^ (bits & 1);
                            if (val == 1) {
                                curr_w++;
                            }
                        }

                        if (curr_w < min_w) {
                            min_w = curr_w;
                        }
                    }

                    total_presses += min_w;
                }
            }
        }

        return total_presses;
    }

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("../inputs/day_10.txt")));
        int result = solve(input);
        System.out.println(result);
    }
}
