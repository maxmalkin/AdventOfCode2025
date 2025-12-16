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
			new_position := (position - distance) % 100
			if new_position < 0 {
				new_position += 100
			}
			loops := distance / 100
			zeros += loops

			remaining := distance % 100
			if position > 0 && remaining >= position {
				zeros++
			}
			position = new_position
		} else {
			new_position := (position + distance) % 100

			loops := distance / 100
			zeros += loops

			remaining := distance % 100
			if remaining > 0 && position+remaining >= 100 {
				zeros++
			}
			position = new_position
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
