package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

func solve(input string) int {
	lines := strings.Split(strings.TrimSpace(input), "\n")

	operations_line := lines[len(lines)-1]
	number_lines := lines[:len(lines)-1]

	max_width := 0
	for _, line := range lines {
		if len(line) > max_width {
			max_width = len(line)
		}
	}

	for i := range number_lines {
		for len(number_lines[i]) < max_width {
			number_lines[i] += " "
		}
	}
	for len(operations_line) < max_width {
		operations_line += " "
	}

	separator_columns := []int{}
	for col := 0; col < max_width; col++ {
		is_separator := true
		for _, line := range number_lines {
			if col < len(line) && line[col] != ' ' {
				is_separator = false
				break
			}
		}
		if col < len(operations_line) && operations_line[col] != ' ' {
			is_separator = false
		}

		if is_separator {
			separator_columns = append(separator_columns, col)
		}
	}

	boundaries := []int{0}
	boundaries = append(boundaries, separator_columns...)
	boundaries = append(boundaries, max_width)

	problems := []struct {
		numbers   []int
		operation rune
	}{}

	for i := 0; i < len(boundaries)-1; i++ {
		start := boundaries[i]
		end := boundaries[i+1]

		operation := rune(0)
		for col := start; col < end; col++ {
			if operations_line[col] == '*' || operations_line[col] == '+' {
				operation = rune(operations_line[col])
				break
			}
		}

		if operation == 0 {
			continue
		}

		numbers := []int{}
		for _, line := range number_lines {
			segment := strings.TrimSpace(line[start:end])
			if segment != "" {
				num, err := strconv.Atoi(segment)
				if err == nil {
					numbers = append(numbers, num)
				}
			}
		}

		if len(numbers) > 0 {
			problems = append(problems, struct {
				numbers   []int
				operation rune
			}{numbers, operation})
		}
	}

	total := 0

	for _, problem := range problems {
		if problem.operation == '*' {
			result := 1
			for _, num := range problem.numbers {
				result *= num
			}
			total += result
		} else {
			result := 0
			for _, num := range problem.numbers {
				result += num
			}
			total += result
		}
	}

	return total
}

func main() {
	input, err := os.ReadFile("../inputs/day_6.txt")
	if err != nil {
		panic(err)
	}
	result := solve(string(input))
	fmt.Println(result)
}
