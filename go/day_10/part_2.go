package main

import (
	"fmt"
	"math"
	"os"
	"regexp"
	"strconv"
	"strings"
)

func solve_machine(buttons [][]int, joltages []int) int {
	n_buttons := len(buttons)
	n_counters := len(joltages)

	min_presses := math.MaxInt32

	max_tries := 1000000
	if n_buttons <= 10 {
		max_tries = 1 << (n_buttons * 4)
		if max_tries > 1000000 {
			max_tries = 1000000
		}
	}

	for attempt := 0; attempt < max_tries; attempt++ {
		x := make([]int, n_buttons)
		total := 0
		for i := range x {
			x[i] = attempt % 100
			attempt /= 100
			total += x[i]
		}

		if total >= min_presses {
			continue
		}

		valid := true
		for counter_idx := 0; counter_idx < n_counters; counter_idx++ {
			counter_sum := 0
			for btn_idx := 0; btn_idx < n_buttons; btn_idx++ {
				found := false
				for _, c := range buttons[btn_idx] {
					if c == counter_idx {
						found = true
						break
					}
				}
				if found {
					counter_sum += x[btn_idx]
				}
			}
			if counter_sum != joltages[counter_idx] {
				valid = false
				break
			}
		}

		if valid && total < min_presses {
			min_presses = total
		}
	}

	if min_presses == math.MaxInt32 {
		return -1
	}
	return min_presses
}

func solve(input string) int {
	lines := []string{}
	for _, line := range strings.Split(strings.TrimSpace(input), "\n") {
		line = strings.TrimSpace(line)
		if line != "" {
			lines = append(lines, line)
		}
	}

	total_presses := 0

	for _, line := range lines {
		buttons := [][]int{}
		button_re := regexp.MustCompile(`\(([0-9,]+)\)`)
		for _, match := range button_re.FindAllStringSubmatch(line, -1) {
			button := []int{}
			for _, x_str := range strings.Split(match[1], ",") {
				x, _ := strconv.Atoi(x_str)
				button = append(button, x)
			}
			buttons = append(buttons, button)
		}

		joltage_re := regexp.MustCompile(`\{([0-9,]+)\}`)
		joltage_match := joltage_re.FindStringSubmatch(line)
		joltages := []int{}
		if joltage_match != nil {
			for _, x_str := range strings.Split(joltage_match[1], ",") {
				x, _ := strconv.Atoi(x_str)
				joltages = append(joltages, x)
			}
		}

		min_presses := solve_machine(buttons, joltages)
		total_presses += min_presses
	}

	return total_presses
}

func main() {
	input, err := os.ReadFile("../inputs/day_10.txt")
	if err != nil {
		panic(err)
	}
	result := solve(string(input))
	fmt.Println(result)
}
