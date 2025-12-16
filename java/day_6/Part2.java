import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Part2 {
    static class Problem {
        List<Integer> numbers;
        char operation;
        Problem(List<Integer> numbers, char operation) {
            this.numbers = numbers;
            this.operation = operation;
        }
    }

    public static int solve(String input) {
        String[] lines = input.trim().split("\n");

        String operations_line = lines[lines.length - 1];
        List<String> number_lines = new ArrayList<>();
        for (int i = 0; i < lines.length - 1; i++) {
            number_lines.add(lines[i]);
        }

        int max_width = 0;
        for (String line : lines) {
            max_width = Math.max(max_width, line.length());
        }

        for (int i = 0; i < number_lines.size(); i++) {
            while (number_lines.get(i).length() < max_width) {
                number_lines.set(i, number_lines.get(i) + " ");
            }
        }
        while (operations_line.length() < max_width) {
            operations_line += " ";
        }

        List<Integer> separator_columns = new ArrayList<>();
        for (int col = 0; col < max_width; col++) {
            boolean is_separator = true;
            for (String line : number_lines) {
                if (col < line.length() && line.charAt(col) != ' ') {
                    is_separator = false;
                    break;
                }
            }
            if (col < operations_line.length() && operations_line.charAt(col) != ' ') {
                is_separator = false;
            }

            if (is_separator) {
                separator_columns.add(col);
            }
        }

        List<Integer> boundaries = new ArrayList<>();
        boundaries.add(0);
        boundaries.addAll(separator_columns);
        boundaries.add(max_width);

        List<Problem> problems = new ArrayList<>();
        for (int i = 0; i < boundaries.size() - 1; i++) {
            int start = boundaries.get(i);
            int end = boundaries.get(i + 1);

            Character operation = null;
            for (int col = start; col < end; col++) {
                if (operations_line.charAt(col) == '*' || operations_line.charAt(col) == '+') {
                    operation = operations_line.charAt(col);
                    break;
                }
            }

            if (operation == null) {
                continue;
            }

            List<String> reversed_lines = new ArrayList<>();
            for (String line : number_lines) {
                String segment = line.substring(start, end);
                String reversed_segment = new StringBuilder(segment).reverse().toString();
                reversed_lines.add(reversed_segment);
            }

            List<Integer> numbers = new ArrayList<>();
            int segment_width = end - start;

            for (int col = 0; col < segment_width; col++) {
                StringBuilder digits = new StringBuilder();
                for (String reversed_line : reversed_lines) {
                    if (col < reversed_line.length() && reversed_line.charAt(col) != ' ') {
                        digits.append(reversed_line.charAt(col));
                    }
                }

                if (digits.length() > 0) {
                    try {
                        numbers.add(Integer.parseInt(digits.toString()));
                    } catch (NumberFormatException e) {
                    }
                }
            }

            if (!numbers.isEmpty()) {
                problems.add(new Problem(numbers, operation));
            }
        }

        int total = 0;

        for (Problem problem : problems) {
            if (problem.operation == '*') {
                int result = 1;
                for (int num : problem.numbers) {
                    result *= num;
                }
                total += result;
            } else {
                int result = 0;
                for (int num : problem.numbers) {
                    result += num;
                }
                total += result;
            }
        }

        return total;
    }

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("../inputs/day_6.txt")));
        int result = solve(input);
        System.out.println(result);
    }
}
