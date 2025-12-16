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

	if length%2 != 0 {
		return false
	}

	if s[0] == '0' {
		return false
	}

	mid := length / 2
	return s[:mid] == s[mid:]
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
