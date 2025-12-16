package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

func solve(input string) int {
	banks := []string{}
	for _, line := range strings.Split(strings.TrimSpace(input), "\n") {
		line = strings.TrimSpace(line)
		if line != "" {
			banks = append(banks, line)
		}
	}

	sum := 0

	for _, b := range banks {
		max_joltage := 0

		for i := 0; i < len(b); i++ {
			for j := i + 1; j < len(b); j++ {
				joltage, _ := strconv.Atoi(string(b[i]) + string(b[j]))
				if joltage > max_joltage {
					max_joltage = joltage
				}
			}
		}

		sum += max_joltage
	}

	return sum
}

func main() {
	input, err := os.ReadFile("../inputs/day_3.txt")
	if err != nil {
		panic(err)
	}
	result := solve(string(input))
	fmt.Println(result)
}
