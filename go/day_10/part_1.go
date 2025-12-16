package main

import (
	"fmt"
	"math"
	"os"
	"regexp"
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
	total_presses := 0

	for _, line := range lines {
		target_re := regexp.MustCompile(`\[([.#]+)\]`)
		target_match := target_re.FindStringSubmatch(line)
		if target_match == nil {
			continue
		}

		target_str := target_match[1]
		target := 0
		l := len(target_str)
		for i, ch := range target_str {
			if ch == '#' {
				target |= 1 << i
			}
		}

		button_re := regexp.MustCompile(`\(([\d,\s]+)\)`)
		button_strs := button_re.FindAllStringSubmatch(line, -1)
		buttons := []int{}
		button_set := map[int]bool{}
		for _, b_match := range button_strs {
			b_str := b_match[1]
			parts_str := strings.Split(b_str, ",")
			b_mask := 0
			for _, p_str := range parts_str {
				p_str = strings.TrimSpace(p_str)
				if p_str != "" {
					p, _ := strconv.Atoi(p_str)
					if p < l {
						b_mask |= 1 << p
					}
				}
			}
			if !button_set[b_mask] {
				buttons = append(buttons, b_mask)
				button_set[b_mask] = true
			}
		}

		k := len(buttons)

		if l <= 15 {
			queue := [][2]int{{0, 0}}
			seen := map[int]bool{0: true}
			found := false
			best_dist := math.MaxInt32

			for len(queue) > 0 {
				curr := queue[0][0]
				dist := queue[0][1]
				queue = queue[1:]

				if curr == target {
					best_dist = dist
					found = true
					break
				}

				for _, b := range buttons {
					nxt := curr ^ b
					if !seen[nxt] {
						seen[nxt] = true
						queue = append(queue, [2]int{nxt, dist + 1})
					}
				}
			}

			if found {
				total_presses += best_dist
			}

		} else {
			matrix := []int{}
			for r := 0; r < l; r++ {
				row_val := 0
				for c := 0; c < k; c++ {
					if (buttons[c]>>r)&1 == 1 {
						row_val |= 1 << c
					}
				}
				if (target>>r)&1 == 1 {
					row_val |= 1 << k
				}
				matrix = append(matrix, row_val)
			}

			pivot_row := 0
			pivots := map[int]int{}
			free_cols := map[int]bool{}
			for c := 0; c < k; c++ {
				free_cols[c] = true
			}

			for c := 0; c < k; c++ {
				pivot := -1
				for r := pivot_row; r < l; r++ {
					if (matrix[r]>>c)&1 == 1 {
						pivot = r
						break
					}
				}

				if pivot != -1 {
					matrix[pivot_row], matrix[pivot] = matrix[pivot], matrix[pivot_row]
					pivot_val := matrix[pivot_row]
					for r := 0; r < l; r++ {
						if r != pivot_row {
							if (matrix[r]>>c)&1 == 1 {
								matrix[r] ^= pivot_val
							}
						}
					}
					pivots[pivot_row] = c
					delete(free_cols, c)
					pivot_row++
				}
			}

			possible := true
			for r := pivot_row; r < l; r++ {
				if (matrix[r]>>k)&1 == 1 {
					possible = false
					break
				}
			}

			if possible {
				free_vars := []int{}
				for fv := range free_cols {
					free_vars = append(free_vars, fv)
				}
				num_free := len(free_vars)

				pivot_equations := [][2]int{}
				for r := 0; r < pivot_row; r++ {
					base_val := (matrix[r] >> k) & 1
					dep_mask := 0
					for i, fv := range free_vars {
						if (matrix[r]>>fv)&1 == 1 {
							dep_mask |= 1 << i
						}
					}
					pivot_equations = append(pivot_equations, [2]int{base_val, dep_mask})
				}

				min_w := math.MaxInt32
				for mask := 0; mask < (1 << num_free); mask++ {
					w := 0
					for i := 0; i < num_free; i++ {
						if (mask>>i)&1 == 1 {
							w++
						}
					}
					if w >= min_w {
						continue
					}

					curr_w := w
					for _, eq := range pivot_equations {
						base := eq[0]
						dep := eq[1]
						bits := 0
						for i := 0; i < num_free; i++ {
							if ((dep & mask) >> i & 1) == 1 {
								bits++
							}
						}
						val := base ^ (bits & 1)
						if val == 1 {
							curr_w++
						}
					}

					if curr_w < min_w {
						min_w = curr_w
					}
				}

				total_presses += min_w
			}
		}
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
