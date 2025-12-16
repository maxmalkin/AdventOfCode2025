package main

import (
	"fmt"
	"os"
	"sort"
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

	sort.Slice(ranges, func(i, j int) bool {
		return ranges[i][0] < ranges[j][0]
	})

	merged := [][2]int{}
	for _, r := range ranges {
		start, end := r[0], r[1]
		if len(merged) > 0 && start <= merged[len(merged)-1][1]+1 {
			if end > merged[len(merged)-1][1] {
				merged[len(merged)-1][1] = end
			}
		} else {
			merged = append(merged, [2]int{start, end})
		}
	}

	total := 0
	for _, r := range merged {
		start, end := r[0], r[1]
		count := end - start + 1
		total += count
	}

	return total
}

func main() {
	input, err := os.ReadFile("../inputs/day_5.txt")
	if err != nil {
		panic(err)
	}
	result := solve(string(input))
	fmt.Println(result)
}
