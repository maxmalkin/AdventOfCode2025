package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

func solve(input string) int {
	rotations := []string{}
	for _, line := range strings.Split(strings.TrimSpace(input), "\n") {
		line = strings.TrimSpace(line)
		if line != "" {
			rotations = append(rotations, line)
		}
	}

	position := 50
	zeros := 0

	for _, r := range rotations {
		direction := r[0]
		distance, _ := strconv.Atoi(r[1:])

		if direction == 'L' {
			position = (position - distance) % 100
			if position < 0 {
				position += 100
			}
		} else {
			position = (position + distance) % 100
		}

		if position == 0 {
			zeros++
		}
	}

	return zeros
}

func main() {
	input, err := os.ReadFile("../inputs/day_1.txt")
	if err != nil {
		panic(err)
	}
	result := solve(string(input))
	fmt.Println(result)
}
