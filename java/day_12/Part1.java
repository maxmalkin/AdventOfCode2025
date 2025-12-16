import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Part1 {
    static class Coord {
        int r, c;
        Coord(int r, int c) {
            this.r = r;
            this.c = c;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coord coord = (Coord) o;
            return r == coord.r && c == coord.c;
        }
        @Override
        public int hashCode() {
            return 31 * r + c;
        }
    }

    static class Region {
        int width, height;
        List<Integer> counts;
        Region(int width, int height, List<Integer> counts) {
            this.width = width;
            this.height = height;
            this.counts = counts;
        }
    }

    static final int TIMEOUT_SECONDS = 5;

    public static Object[] parse_input(String input) {
        String[] lines = input.split("\n");
        List<List<String>> shapes = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            shapes.add(new ArrayList<>());
        }
        List<Region> regions = new ArrayList<>();

        int i = 0;
        while (i < lines.length) {
            String line = lines[i].trim();

            if (line.isEmpty()) {
                i++;
                continue;
            }

            if (line.contains(":") && !line.contains("x")) {
                int colon_pos = line.indexOf(":");
                String prefix = line.substring(0, colon_pos);
                try {
                    int idx = Integer.parseInt(prefix);
                    if (idx < 6) {
                        i++;
                        while (i < lines.length && !lines[i].trim().isEmpty() && !lines[i].contains(":")) {
                            shapes.get(idx).add(lines[i]);
                            i++;
                        }
                        continue;
                    }
                } catch (NumberFormatException e) {
                }
            }

            if (line.contains("x") && line.contains(":")) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String[] dims = parts[0].trim().split("x");
                    if (dims.length == 2) {
                        try {
                            int w = Integer.parseInt(dims[0]);
                            int h = Integer.parseInt(dims[1]);
                            List<Integer> counts = new ArrayList<>();
                            for (String c_str : parts[1].trim().split("\\s+")) {
                                counts.add(Integer.parseInt(c_str));
                            }
                            regions.add(new Region(w, h, counts));
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }

            i++;
        }

        return new Object[]{shapes, regions};
    }

    public static List<Coord> get_shape_cells(List<String> shape) {
        List<Coord> cells = new ArrayList<>();
        for (int r = 0; r < shape.size(); r++) {
            String row = shape.get(r);
            for (int c = 0; c < row.length(); c++) {
                if (row.charAt(c) == '#') {
                    cells.add(new Coord(r, c));
                }
            }
        }
        return cells;
    }

    public static List<Coord> normalize_shape(List<Coord> cells) {
        if (cells.isEmpty()) {
            return new ArrayList<>();
        }
        int min_r = cells.get(0).r;
        int min_c = cells.get(0).c;
        for (Coord cell : cells) {
            min_r = Math.min(min_r, cell.r);
            min_c = Math.min(min_c, cell.c);
        }
        List<Coord> normalized = new ArrayList<>();
        for (Coord cell : cells) {
            normalized.add(new Coord(cell.r - min_r, cell.c - min_c));
        }
        return normalized;
    }

    public static List<Coord> rotate_90(List<Coord> cells) {
        List<Coord> result = new ArrayList<>();
        for (Coord cell : cells) {
            result.add(new Coord(cell.c, -cell.r));
        }
        return result;
    }

    public static List<Coord> flip_horizontal(List<Coord> cells) {
        List<Coord> result = new ArrayList<>();
        for (Coord cell : cells) {
            result.add(new Coord(-cell.r, cell.c));
        }
        return result;
    }

    public static List<List<Coord>> get_all_orientations(List<String> shape) {
        List<Coord> cells = get_shape_cells(shape);
        Set<String> orientations_set = new HashSet<>();
        List<List<Coord>> orientations = new ArrayList<>();

        List<Coord> current = cells;
        for (int i = 0; i < 4; i++) {
            List<Coord> norm = normalize_shape(current);
            String key = norm.toString();
            if (!orientations_set.contains(key)) {
                orientations_set.add(key);
                orientations.add(norm);
            }
            current = rotate_90(current);
        }

        current = flip_horizontal(cells);
        for (int i = 0; i < 4; i++) {
            List<Coord> norm = normalize_shape(current);
            String key = norm.toString();
            if (!orientations_set.contains(key)) {
                orientations_set.add(key);
                orientations.add(norm);
            }
            current = rotate_90(current);
        }

        return orientations;
    }

    public static boolean can_place(boolean[] grid, List<Coord> shape, int start_r, int start_c, int width, int height) {
        for (Coord coord : shape) {
            int r = start_r + coord.r;
            int c = start_c + coord.c;
            if (r < 0 || r >= height || c < 0 || c >= width) {
                return false;
            }
            int idx = r * width + c;
            if (grid[idx]) {
                return false;
            }
        }
        return true;
    }

    public static void place(boolean[] grid, List<Coord> shape, int start_r, int start_c, int width) {
        for (Coord coord : shape) {
            int r = start_r + coord.r;
            int c = start_c + coord.c;
            int idx = r * width + c;
            grid[idx] = true;
        }
    }

    public static void unplace(boolean[] grid, List<Coord> shape, int start_r, int start_c, int width) {
        for (Coord coord : shape) {
            int r = start_r + coord.r;
            int c = start_c + coord.c;
            int idx = r * width + c;
            grid[idx] = false;
        }
    }

    public static Coord find_first_empty(boolean[] grid, int width, int height) {
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (!grid[r * width + c]) {
                    return new Coord(r, c);
                }
            }
        }
        return null;
    }

    static long start_time;

    public static boolean backtrack(boolean[] grid, List<List<List<Coord>>> sorted_presents, int idx, int width, int height, int remaining_cells) {
        if (System.currentTimeMillis() - start_time > TIMEOUT_SECONDS * 1000) {
            return false;
        }

        if (idx == sorted_presents.size()) {
            return true;
        }

        int remaining_area = 0;
        for (int i = idx; i < sorted_presents.size(); i++) {
            remaining_area += sorted_presents.get(i).get(0).size();
        }
        if (remaining_area > remaining_cells) {
            return false;
        }

        Coord pos = find_first_empty(grid, width, height);
        if (pos == null) {
            return false;
        }

        int start_r = pos.r;
        int start_c = pos.c;
        List<List<Coord>> shape_variants = sorted_presents.get(idx);

        for (List<Coord> variant : shape_variants) {
            if (can_place(grid, variant, start_r, start_c, width, height)) {
                place(grid, variant, start_r, start_c, width);
                int new_remaining = remaining_cells - variant.size();
                if (backtrack(grid, sorted_presents, idx + 1, width, height, new_remaining)) {
                    return true;
                }
                unplace(grid, variant, start_r, start_c, width);
            }
        }

        for (List<Coord> variant : shape_variants) {
            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {
                    if (r == start_r && c == start_c) {
                        continue;
                    }
                    if (can_place(grid, variant, r, c, width, height)) {
                        place(grid, variant, r, c, width);
                        int new_remaining = remaining_cells - variant.size();
                        if (backtrack(grid, sorted_presents, idx + 1, width, height, new_remaining)) {
                            return true;
                        }
                        unplace(grid, variant, r, c, width);
                    }
                }
            }
        }

        return false;
    }

    public static boolean solve_region(int width, int height, List<List<List<Coord>>> presents) {
        int total_area = 0;
        for (List<List<Coord>> p : presents) {
            total_area += p.get(0).size();
        }
        int grid_area = width * height;

        if (total_area > grid_area) {
            return false;
        }

        List<List<List<Coord>>> sorted_presents = new ArrayList<>(presents);
        sorted_presents.sort((a, b) -> Integer.compare(b.get(0).size(), a.get(0).size()));

        boolean[] grid = new boolean[width * height];
        start_time = System.currentTimeMillis();

        return backtrack(grid, sorted_presents, 0, width, height, grid_area);
    }

    public static int solve(String input) {
        Object[] parsed = parse_input(input);
        @SuppressWarnings("unchecked")
        List<List<String>> shapes = (List<List<String>>) parsed[0];
        @SuppressWarnings("unchecked")
        List<Region> regions = (List<Region>) parsed[1];

        int count = 0;
        for (Region region : regions) {
            int width = region.width;
            int height = region.height;
            List<Integer> counts = region.counts;

            List<List<List<Coord>>> presents = new ArrayList<>();

            for (int shape_idx = 0; shape_idx < counts.size(); shape_idx++) {
                int cnt = counts.get(shape_idx);
                if (shape_idx < shapes.size() && !shapes.get(shape_idx).isEmpty()) {
                    List<List<Coord>> orientations = get_all_orientations(shapes.get(shape_idx));
                    for (int i = 0; i < cnt; i++) {
                        presents.add(orientations);
                    }
                }
            }

            if (solve_region(width, height, presents)) {
                count++;
            }
        }

        return count;
    }

    public static void main(String[] args) throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("../inputs/day_12.txt")));
        int result = solve(input);
        System.out.println(result);
    }
}
