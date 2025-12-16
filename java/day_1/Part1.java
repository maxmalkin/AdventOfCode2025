import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Part1 {
    public static int solve(String input) {
        List<String> rotations = new ArrayList<>();
        for (String line : input.trim().split("\n")) {
            line = line.trim();
            if (!line.isEmpty()) {
                rotations.add(line);
            }
        }

        int position = 50;
        int zeros = 0;

        for (String r : rotations) {
            char direction = r.charAt(0);
            int distance = Integer.parseInt(r.substring(1));

            if (direction == 'L') {
                position = (position - distance) % 100;
                if (position < 0) {
                    position += 100;
                }
            } else {
                position = (position + distance) % 100;
            }

            if (position == 0) {
                zeros++;
            }
        }

        return zeros;
    }

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("../inputs/day_1.txt")));
        int result = solve(input);
        System.out.println(result);
    }
}
