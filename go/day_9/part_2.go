package main

import (
	"fmt"
	"os"
	"sort"
	"strconv"
	"strings"
)

func abs(x int) int {
	if x < 0 {
		return -x
	}
	return x
}

func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}

func max(a, b int) int {
	if a > b {
		return a
	}
	return b
}

func solve(input string) int {
	lines := []string{}
	for _, line := range strings.Split(strings.TrimSpace(input), "\n") {
		line = strings.TrimSpace(line)
		if line != "" {
			lines = append(lines, line)
		}
	}

	tiles := [][2]int{}
	for _, line := range lines {
		if line == "" {
			continue
		}
		parts := strings.Split(line, ",")
		x, _ := strconv.Atoi(parts[0])
		y, _ := strconv.Atoi(parts[1])
		tiles = append(tiles, [2]int{x, y})
	}

	n := len(tiles)

	all_x := []int{}
	all_y := []int{}
	x_set := map[int]bool{}
	y_set := map[int]bool{}

	for _, tile := range tiles {
		x, y := tile[0], tile[1]
		if !x_set[x] {
			all_x = append(all_x, x)
			x_set[x] = true
		}
		if !y_set[y] {
			all_y = append(all_y, y)
			y_set[y] = true
		}
	}

	sort.Ints(all_x)
	sort.Ints(all_y)

	x_to_compressed := map[int]int{}
	y_to_compressed := map[int]int{}
	for i, x := range all_x {
		x_to_compressed[x] = i
	}
	for i, y := range all_y {
		y_to_compressed[y] = i
	}

	width := len(all_x)
	height := len(all_y)

	compressed_tiles := [][2]int{}
	for _, tile := range tiles {
		x, y := tile[0], tile[1]
		compressed_tiles = append(compressed_tiles, [2]int{x_to_compressed[x], y_to_compressed[y]})
	}

	grid := make([][]bool, height)
	for i := range grid {
		grid[i] = make([]bool, width)
	}

	for _, ct := range compressed_tiles {
		cx, cy := ct[0], ct[1]
		grid[cy][cx] = true
	}

	for i := 0; i < n; i++ {
		x1, y1 := tiles[i][0], tiles[i][1]
		x2, y2 := tiles[(i+1)%n][0], tiles[(i+1)%n][1]

		cx1, cy1 := x_to_compressed[x1], y_to_compressed[y1]
		cx2, cy2 := x_to_compressed[x2], y_to_compressed[y2]

		if cx1 == cx2 {
			for cy := min(cy1, cy2); cy <= max(cy1, cy2); cy++ {
				grid[cy][cx1] = true
			}
		} else if cy1 == cy2 {
			for cx := min(cx1, cx2); cx <= max(cx1, cx2); cx++ {
				grid[cy1][cx] = true
			}
		}
	}

	point_in_polygon := func(px, py int) bool {
		count := 0
		for i := 0; i < n; i++ {
			x1, y1 := tiles[i][0], tiles[i][1]
			x2, y2 := tiles[(i+1)%n][0], tiles[(i+1)%n][1]
			if y1 == y2 {
				continue
			}
			if y1 > y2 {
				x1, y1, x2, y2 = x2, y2, x1, y1
			}
			if py < y1 || py >= y2 {
				continue
			}
			x_intersect := float64(x1) + float64(py-y1)*float64(x2-x1)/float64(y2-y1)
			if x_intersect > float64(px) {
				count++
			}
		}
		return count%2 == 1
	}

	visited := map[[2]int]bool{}
	for cy := 0; cy < height; cy++ {
		for cx := 0; cx < width; cx++ {
			if grid[cy][cx] || visited[[2]int{cx, cy}] {
				continue
			}
			orig_x, orig_y := all_x[cx], all_y[cy]

			if point_in_polygon(orig_x, orig_y) {
				stack := [][2]int{{cx, cy}}
				for len(stack) > 0 {
					curr := stack[len(stack)-1]
					stack = stack[:len(stack)-1]
					curr_x, curr_y := curr[0], curr[1]
					if visited[[2]int{curr_x, curr_y}] {
						continue
					}
					if curr_x < 0 || curr_x >= width || curr_y < 0 || curr_y >= height {
						continue
					}
					if grid[curr_y][curr_x] {
						continue
					}
					visited[[2]int{curr_x, curr_y}] = true
					grid[curr_y][curr_x] = true

					for _, d := range [][2]int{{0, 1}, {0, -1}, {1, 0}, {-1, 0}} {
						stack = append(stack, [2]int{curr_x + d[0], curr_y + d[1]})
					}
				}
			}
		}
	}

	max_area := 0

	for i := 0; i < n; i++ {
		for j := i + 1; j < n; j++ {
			cx1, cy1 := compressed_tiles[i][0], compressed_tiles[i][1]
			cx2, cy2 := compressed_tiles[j][0], compressed_tiles[j][1]

			min_cx := min(cx1, cx2)
			max_cx := max(cx1, cx2)
			min_cy := min(cy1, cy2)
			max_cy := max(cy1, cy2)

			valid := true
			for cy := min_cy; cy <= max_cy; cy++ {
				if !valid {
					break
				}
				for cx := min_cx; cx <= max_cx; cx++ {
					if !grid[cy][cx] {
						valid = false
						break
					}
				}
			}

			if valid {
				orig_x1, orig_y1 := tiles[i][0], tiles[i][1]
				orig_x2, orig_y2 := tiles[j][0], tiles[j][1]
				actual_width := abs(orig_x2-orig_x1) + 1
				actual_height := abs(orig_y2-orig_y1) + 1
				area := actual_width * actual_height
				if area > max_area {
					max_area = area
				}
			}
		}
	}

	return max_area
}

func main() {
	input, err := os.ReadFile("../inputs/day_9.txt")
	if err != nil {
		panic(err)
	}
	result := solve(string(input))
	fmt.Println(result)
}
