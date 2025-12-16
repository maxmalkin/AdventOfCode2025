package main

import (
	"fmt"
	"os"
	"strings"
)

func count_adjacent_rolls(grid [][]rune, row, col int) int {
	rows := len(grid)
	cols := 0
	if rows > 0 {
		cols = len(grid[0])
	}

	directions := [][2]int{
		{-1, -1},
		{-1, 0},
		{-1, 1},
		{0, -1},
		{0, 1},
		{1, -1},
		{1, 0},
		{1, 1},
	}

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

	return adjacent_rolls
}

func find_accessible_rolls(grid [][]rune) [][2]int {
	accessible := [][2]int{}
	rows := len(grid)
	cols := 0
	if rows > 0 {
		cols = len(grid[0])
	}

	for row := 0; row < rows; row++ {
		for col := 0; col < cols; col++ {
			if grid[row][col] == '@' {
				if count_adjacent_rolls(grid, row, col) < 4 {
					accessible = append(accessible, [2]int{row, col})
				}
			}
		}
	}

	return accessible
}

func solve(input string) int {
	grid := [][]rune{}
	for _, line := range strings.Split(strings.TrimSpace(input), "\n") {
		grid = append(grid, []rune(line))
	}

	removed := 0

	for {
		accessible := find_accessible_rolls(grid)

		if len(accessible) == 0 {
			break
		}

		for _, pos := range accessible {
			row, col := pos[0], pos[1]
			grid[row][col] = '.'
		}

		removed += len(accessible)
	}

	return removed
}

func main() {
	input, err := os.ReadFile("../inputs/day_4.txt")
	if err != nil {
		panic(err)
	}
	result := solve(string(input))
	fmt.Println(result)
}
