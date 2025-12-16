package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

func is_invalid_id(n int) bool {
	s := strconv.Itoa(n)
	length := len(s)

	if s[0] == '0' {
		return false
	}

	for pattern_len := 1; pattern_len <= length/2; pattern_len++ {
		if length%pattern_len == 0 {
			pattern := s[:pattern_len]
			repeats := length / pattern_len

			result := ""
			for i := 0; i < repeats; i++ {
				result += pattern
			}

			if result == s && repeats >= 2 {
				return true
			}
		}
	}

	return false
}

func solve(input string) int {
	ranges := [][2]int{}
	for _, range_str := range strings.Split(strings.TrimSpace(input), ",") {
		range_str = strings.TrimSpace(range_str)
		if range_str != "" {
			parts := strings.Split(range_str, "-")
			start, _ := strconv.Atoi(parts[0])
			end, _ := strconv.Atoi(parts[1])
			ranges = append(ranges, [2]int{start, end})
		}
	}

	sum := 0

	for _, r := range ranges {
		start, end := r[0], r[1]
		for i := start; i <= end; i++ {
			if is_invalid_id(i) {
				sum += i
			}
		}
	}

	return sum
}

func main() {
	input, err := os.ReadFile("../inputs/day_2.txt")
	if err != nil {
		panic(err)
	}
	result := solve(string(input))
	fmt.Println(result)
}
