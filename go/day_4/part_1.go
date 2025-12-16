package main

import (
	"fmt"
	"os"
	"strings"
)

func solve(input string) int {
	grid := []string{}
	for _, line := range strings.Split(strings.TrimSpace(input), "\n") {
		grid = append(grid, line)
	}
	rows := len(grid)
	cols := 0
	if rows > 0 {
		cols = len(grid[0])
	}

	directions := [][2]int{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}}

	accessible_count := 0

	for row := 0; row < rows; row++ {
		for col := 0; col < cols; col++ {
			if grid[row][col] == '@' {
				adjacent_rolls := 0

				for _, d := range directions {
					dr, dc := d[0], d[1]
					new_row := row + dr
					new_col := col + dc

					if new_row >= 0 && new_row < rows && new_col >= 0 && new_col < cols {
						if grid[new_row][new_col] == '@' {
							adjacent_rolls++
						}
					}
				}

				if adjacent_rolls < 4 {
					accessible_count++
				}
			}
		}
	}

	return accessible_count
}

func main() {
	input, err := os.ReadFile("../inputs/day_4.txt")
	if err != nil {
		panic(err)
	}
	result := solve(string(input))
	fmt.Println(result)
}
