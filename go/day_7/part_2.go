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

	memo := map[[2]int]int{}

	var count_paths func(row, col int) int
	count_paths = func(row, col int) int {
		if row >= rows {
			return 1
		}

		if col < 0 || col >= cols {
			return 0
		}

		key := [2]int{row, col}
		if val, ok := memo[key]; ok {
			return val
		}

		result := 0
		if grid[row][col] == '^' {
			result = count_paths(row+1, col-1) + count_paths(row+1, col+1)
		} else {
			result = count_paths(row+1, col)
		}

		memo[key] = result
		return result
	}

	return count_paths(0, start_col)
}

func main() {
	input, err := os.ReadFile("../inputs/day_7.txt")
	if err != nil {
		panic(err)
	}
	result := solve(string(input))
	fmt.Println(result)
}
