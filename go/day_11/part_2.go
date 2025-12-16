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

	memo := map[[3]interface{}]int{}

	var count_paths func(node string, has_dac bool, has_fft bool) int
	count_paths = func(node string, has_dac bool, has_fft bool) int {
		if node == "out" {
			if has_dac && has_fft {
				return 1
			}
			return 0
		}

		key := [3]interface{}{node, has_dac, has_fft}
		if val, ok := memo[key]; ok {
			return val
		}

		neighbors, ok := graph[node]
		if !ok {
			return 0
		}

		new_has_dac := has_dac || (node == "dac")
		new_has_fft := has_fft || (node == "fft")

		total := 0
		for _, neighbor := range neighbors {
			total += count_paths(neighbor, new_has_dac, new_has_fft)
		}

		memo[key] = total
		return total
	}

	return count_paths("svr", false, false)
}

func main() {
	input, err := os.ReadFile("../inputs/day_11.txt")
	if err != nil {
		panic(err)
	}
	result := solve(string(input))
	fmt.Println(result)
}
