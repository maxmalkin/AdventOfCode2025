package main

import (
	"fmt"
	"math"
	"os"
	"sort"
	"strconv"
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

	boxes := [][3]int{}
	for _, line := range lines {
		parts := strings.Split(line, ",")
		x, _ := strconv.Atoi(parts[0])
		y, _ := strconv.Atoi(parts[1])
		z, _ := strconv.Atoi(parts[2])
		boxes = append(boxes, [3]int{x, y, z})
	}

	n := len(boxes)

	distances := []struct {
		dist float64
		i, j int
	}{}

	for i := 0; i < n; i++ {
		for j := i + 1; j < n; j++ {
			x1, y1, z1 := boxes[i][0], boxes[i][1], boxes[i][2]
			x2, y2, z2 := boxes[j][0], boxes[j][1], boxes[j][2]
			dist := math.Sqrt(float64((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1) + (z2-z1)*(z2-z1)))
			distances = append(distances, struct {
				dist float64
				i, j int
			}{dist, i, j})
		}
	}

	sort.Slice(distances, func(i, j int) bool {
		return distances[i].dist < distances[j].dist
	})

	parent := make([]int, n)
	for i := range parent {
		parent[i] = i
	}

	var find func(x int) int
	find = func(x int) int {
		if parent[x] != x {
			parent[x] = find(parent[x])
		}
		return parent[x]
	}

	union := func(x, y int) {
		px := find(x)
		py := find(y)
		if px != py {
			parent[px] = py
		}
	}

	limit := 1000
	if len(distances) < limit {
		limit = len(distances)
	}
	for i := 0; i < limit; i++ {
		a := distances[i].i
		b := distances[i].j
		union(a, b)
	}

	circuit_sizes := map[int]int{}
	for i := 0; i < n; i++ {
		root := find(i)
		circuit_sizes[root]++
	}

	sizes := []int{}
	for _, size := range circuit_sizes {
		sizes = append(sizes, size)
	}

	sort.Slice(sizes, func(i, j int) bool {
		return sizes[i] > sizes[j]
	})

	return sizes[0] * sizes[1] * sizes[2]
}

func main() {
	input, err := os.ReadFile("../inputs/day_8.txt")
	if err != nil {
		panic(err)
	}
	result := solve(string(input))
	fmt.Println(result)
}
