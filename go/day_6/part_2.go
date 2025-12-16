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

		reversed_lines := []string{}
		for _, line := range number_lines {
			segment := line[start:end]
			reversed_segment := ""
			for i := len(segment) - 1; i >= 0; i-- {
				reversed_segment += string(segment[i])
			}
			reversed_lines = append(reversed_lines, reversed_segment)
		}

		numbers := []int{}
		segment_width := end - start

		for col := 0; col < segment_width; col++ {
			digits := []rune{}
			for _, reversed_line := range reversed_lines {
				if col < len(reversed_line) && reversed_line[col] != ' ' {
					digits = append(digits, rune(reversed_line[col]))
				}
			}

			if len(digits) > 0 {
				num_str := string(digits)
				num, err := strconv.Atoi(num_str)
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
