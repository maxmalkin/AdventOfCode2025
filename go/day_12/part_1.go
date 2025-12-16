package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
	"time"
)

type Shape []Coord

type Coord struct {
	r, c int
}

const TIMEOUT_SECONDS = 5

func parse_input(input string) ([][]string, [][3]interface{}) {
	lines := strings.Split(input, "\n")
	shapes := make([][]string, 6)
	for i := range shapes {
		shapes[i] = []string{}
	}
	regions := [][3]interface{}{}

	i := 0
	for i < len(lines) {
		line := strings.TrimSpace(lines[i])

		if line == "" {
			i++
			continue
		}

		if strings.Contains(line, ":") && !strings.Contains(line, "x") {
			colon_pos := strings.Index(line, ":")
			prefix := line[:colon_pos]
			idx, err := strconv.Atoi(prefix)
			if err == nil && idx < 6 {
				i++
				for i < len(lines) && strings.TrimSpace(lines[i]) != "" && !strings.Contains(lines[i], ":") {
					shapes[idx] = append(shapes[idx], lines[i])
					i++
				}
				continue
			}
		}

		if strings.Contains(line, "x") && strings.Contains(line, ":") {
			parts := strings.Split(line, ":")
			if len(parts) == 2 {
				dims := strings.Split(strings.TrimSpace(parts[0]), "x")
				if len(dims) == 2 {
					w, err1 := strconv.Atoi(dims[0])
					h, err2 := strconv.Atoi(dims[1])
					if err1 == nil && err2 == nil {
						counts_str := strings.Fields(parts[1])
						counts := []int{}
						for _, c_str := range counts_str {
							c, err := strconv.Atoi(c_str)
							if err == nil {
								counts = append(counts, c)
							}
						}
						regions = append(regions, [3]interface{}{w, h, counts})
					}
				}
			}
		}

		i++
	}

	return shapes, regions
}

func get_shape_cells(shape []string) []Coord {
	cells := []Coord{}
	for r, row := range shape {
		for c, ch := range row {
			if ch == '#' {
				cells = append(cells, Coord{r, c})
			}
		}
	}
	return cells
}

func normalize_shape(cells []Coord) Shape {
	if len(cells) == 0 {
		return Shape{}
	}
	min_r := cells[0].r
	min_c := cells[0].c
	for _, cell := range cells {
		if cell.r < min_r {
			min_r = cell.r
		}
		if cell.c < min_c {
			min_c = cell.c
		}
	}
	normalized := Shape{}
	for _, cell := range cells {
		normalized = append(normalized, Coord{cell.r - min_r, cell.c - min_c})
	}
	return normalized
}

func rotate_90(cells []Coord) []Coord {
	result := []Coord{}
	for _, cell := range cells {
		result = append(result, Coord{cell.c, -cell.r})
	}
	return result
}

func flip_horizontal(cells []Coord) []Coord {
	result := []Coord{}
	for _, cell := range cells {
		result = append(result, Coord{-cell.r, cell.c})
	}
	return result
}

func get_all_orientations(shape []string) []Shape {
	cells := get_shape_cells(shape)
	orientations_map := map[string]Shape{}

	current := cells
	for i := 0; i < 4; i++ {
		norm := normalize_shape(current)
		key := fmt.Sprintf("%v", norm)
		orientations_map[key] = norm
		current = rotate_90(current)
	}

	current = flip_horizontal(cells)
	for i := 0; i < 4; i++ {
		norm := normalize_shape(current)
		key := fmt.Sprintf("%v", norm)
		orientations_map[key] = norm
		current = rotate_90(current)
	}

	orientations := []Shape{}
	for _, v := range orientations_map {
		orientations = append(orientations, v)
	}
	return orientations
}

func can_place(grid []bool, shape Shape, start_r, start_c, width, height int) bool {
	for _, coord := range shape {
		r := start_r + coord.r
		c := start_c + coord.c
		if r < 0 || r >= height || c < 0 || c >= width {
			return false
		}
		idx := r*width + c
		if grid[idx] {
			return false
		}
	}
	return true
}

func place(grid []bool, shape Shape, start_r, start_c, width int) {
	for _, coord := range shape {
		r := start_r + coord.r
		c := start_c + coord.c
		idx := r*width + c
		grid[idx] = true
	}
}

func unplace(grid []bool, shape Shape, start_r, start_c, width int) {
	for _, coord := range shape {
		r := start_r + coord.r
		c := start_c + coord.c
		idx := r*width + c
		grid[idx] = false
	}
}

func find_first_empty(grid []bool, width, height int) *Coord {
	for r := 0; r < height; r++ {
		for c := 0; c < width; c++ {
			if !grid[r*width+c] {
				return &Coord{r, c}
			}
		}
	}
	return nil
}

func solve_region(width, height int, presents [][]Shape) bool {
	total_area := 0
	for _, p := range presents {
		total_area += len(p[0])
	}
	grid_area := width * height

	if total_area > grid_area {
		return false
	}

	sorted_presents := make([][]Shape, len(presents))
	copy(sorted_presents, presents)

	for i := 0; i < len(sorted_presents)-1; i++ {
		for j := i + 1; j < len(sorted_presents); j++ {
			if len(sorted_presents[i][0]) < len(sorted_presents[j][0]) {
				sorted_presents[i], sorted_presents[j] = sorted_presents[j], sorted_presents[i]
			}
		}
	}

	grid := make([]bool, width*height)
	start_time := time.Now()

	var backtrack func(idx, remaining_cells int) bool
	backtrack = func(idx, remaining_cells int) bool {
		if time.Since(start_time) > TIMEOUT_SECONDS*time.Second {
			return false
		}

		if idx == len(sorted_presents) {
			return true
		}

		remaining_area := 0
		for i := idx; i < len(sorted_presents); i++ {
			remaining_area += len(sorted_presents[i][0])
		}
		if remaining_area > remaining_cells {
			return false
		}

		pos := find_first_empty(grid, width, height)
		if pos == nil {
			return false
		}

		start_r, start_c := pos.r, pos.c
		shape_variants := sorted_presents[idx]

		for _, variant := range shape_variants {
			if can_place(grid, variant, start_r, start_c, width, height) {
				place(grid, variant, start_r, start_c, width)
				new_remaining := remaining_cells - len(variant)
				if backtrack(idx+1, new_remaining) {
					return true
				}
				unplace(grid, variant, start_r, start_c, width)
			}
		}

		for _, variant := range shape_variants {
			for r := 0; r < height; r++ {
				for c := 0; c < width; c++ {
					if r == start_r && c == start_c {
						continue
					}
					if can_place(grid, variant, r, c, width, height) {
						place(grid, variant, r, c, width)
						new_remaining := remaining_cells - len(variant)
						if backtrack(idx+1, new_remaining) {
							return true
						}
						unplace(grid, variant, r, c, width)
					}
				}
			}
		}

		return false
	}

	return backtrack(0, grid_area)
}

func solve(input string) int {
	shapes, regions := parse_input(input)

	count := 0
	for _, region := range regions {
		width := region[0].(int)
		height := region[1].(int)
		counts := region[2].([]int)

		presents := [][]Shape{}

		for shape_idx, cnt := range counts {
			if shape_idx < len(shapes) && len(shapes[shape_idx]) > 0 {
				orientations := get_all_orientations(shapes[shape_idx])
				for i := 0; i < cnt; i++ {
					presents = append(presents, orientations)
				}
			}
		}

		if solve_region(width, height, presents) {
			count++
		}
	}

	return count
}

func main() {
	input, err := os.ReadFile("../inputs/day_12.txt")
	if err != nil {
		panic(err)
	}
	result := solve(string(input))
	fmt.Println(result)
}
