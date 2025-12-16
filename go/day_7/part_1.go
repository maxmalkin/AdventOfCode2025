package main

import (
	"fmt"
	"os"
	"strings"
)

func solve(input string) int {
	lines := strings.Split(strings.TrimSpace(input), "\n")
	grid := [][]rune{}
	for _, line := range lines {
		grid = append(grid, []rune(line))
	}
	rows := len(grid)
	cols := 0
	if rows > 0 {
		cols = len(grid[0])
	}

	start_col := -1
	for col := 0; col < cols; col++ {
		if grid[0][col] == 'S' {
			start_col = col
			break
		}
	}

	beams := map[int]bool{start_col: true}
	split_count := 0

	for row := 1; row < rows; row++ {
		new_beams := map[int]bool{}

		for col := range beams {
			if col >= 0 && col < cols {
				if grid[row][col] == '^' {
					split_count++
					if col-1 >= 0 {
						new_beams[col-1] = true
					}
					if col+1 < cols {
						new_beams[col+1] = true
					}
				} else {
					new_beams[col] = true
				}
			}
		}

		beams = new_beams
	}

	return split_count
}

func main() {
	input, err := os.ReadFile("../inputs/day_7.txt")
	if err != nil {
		panic(err)
	}
	result := solve(string(input))
	fmt.Println(result)
}
