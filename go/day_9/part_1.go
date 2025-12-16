package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

func abs(x int) int {
	if x < 0 {
		return -x
	}
	return x
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
		parts := strings.Split(line, ",")
		x, _ := strconv.Atoi(parts[0])
		y, _ := strconv.Atoi(parts[1])
		tiles = append(tiles, [2]int{x, y})
	}

	n := len(tiles)
	max_area := 0

	for i := 0; i < n; i++ {
		for j := i + 1; j < n; j++ {
			x1, y1 := tiles[i][0], tiles[i][1]
			x2, y2 := tiles[j][0], tiles[j][1]

			width := abs(x2-x1) + 1
			height := abs(y2-y1) + 1
			area := width * height

			if area > max_area {
				max_area = area
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
