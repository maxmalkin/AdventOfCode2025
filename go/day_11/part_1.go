package main

import (
	"fmt"
	"os"
	"strings"
)

func solve(input string) int {
	lines := []string{}
	for _, line := range strings.Split(strings.TrimSpace(input), "\n") {
		line = strings.TrimSpace(line)
		if line != "" {
			lines = append(lines, line)
		}
	}

	graph := map[string][]string{}
	for _, line := range lines {
		parts := strings.Split(line, ":")
		device := strings.TrimSpace(parts[0])
		outputs := strings.Fields(strings.TrimSpace(parts[1]))
		graph[device] = outputs
	}

	memo := map[string]int{}

	var count_paths func(node string) int
	count_paths = func(node string) int {
		if node == "out" {
			return 1
		}

		if val, ok := memo[node]; ok {
			return val
		}

		neighbors, ok := graph[node]
		if !ok {
			return 0
		}

		total := 0
		for _, neighbor := range neighbors {
			total += count_paths(neighbor)
		}

		memo[node] = total
		return total
	}

	return count_paths("you")
}

func main() {
	input, err := os.ReadFile("../inputs/day_11.txt")
	if err != nil {
		panic(err)
	}
	result := solve(string(input))
	fmt.Println(result)
}
