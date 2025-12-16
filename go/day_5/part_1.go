package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

func solve(input string) int {
	sections := strings.Split(strings.TrimSpace(input), "\n\n")

	ranges := [][2]int{}
	for _, line := range strings.Split(strings.TrimSpace(sections[0]), "\n") {
		if strings.TrimSpace(line) != "" {
			parts := strings.Split(line, "-")
			start, _ := strconv.Atoi(parts[0])
			end, _ := strconv.Atoi(parts[1])
			ranges = append(ranges, [2]int{start, end})
		}
	}

	ingredient_ids := []int{}
	for _, line := range strings.Split(strings.TrimSpace(sections[1]), "\n") {
		line = strings.TrimSpace(line)
		if line != "" {
			id, err := strconv.Atoi(line)
			if err == nil {
				ingredient_ids = append(ingredient_ids, id)
			}
		}
	}

	fresh_count := 0
	for _, id := range ingredient_ids {
		is_fresh := false
		for _, r := range ranges {
			start, end := r[0], r[1]
			if start <= id && id <= end {
				is_fresh = true
				break
			}
		}
		if is_fresh {
			fresh_count++
		}
	}

	return fresh_count
}

func main() {
	input, err := os.ReadFile("../inputs/day_5.txt")
	if err != nil {
		panic(err)
	}
	result := solve(string(input))
	fmt.Println(result)
}
