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
		n := len(b)
		k := 12
		result := []rune{}
		start := 0

		for i := 0; i < k; i++ {
			end := n - k + i
			max_digit := rune('0')
			for j := start; j <= end; j++ {
				if b[j] > byte(max_digit) {
					max_digit = rune(b[j])
				}
			}

			for j := start; j <= end; j++ {
				if rune(b[j]) == max_digit {
					result = append(result, max_digit)
					start = j + 1
					break
				}
			}
		}

		joltage, _ := strconv.Atoi(string(result))
		sum += joltage
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
