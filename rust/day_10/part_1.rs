use std::collections::{HashSet, VecDeque};
use std::fs;

fn solve(input: &str) -> i32 {
    let lines: Vec<&str> = input
        .trim()
        .split('\n')
        .map(|line| line.trim())
        .filter(|line| !line.is_empty())
        .collect();

    let mut total_presses = 0;

    for line in lines {
        let target_start = line.find('[');
        let target_end = line.find(']');
        if target_start.is_none() || target_end.is_none() {
            continue;
        }

        let target_str = &line[target_start.unwrap() + 1..target_end.unwrap()];
        let mut target = 0u32;
        let l = target_str.len();
        for (i, ch) in target_str.chars().enumerate() {
            if ch == '#' {
                target |= 1 << i;
            }
        }

        let mut buttons = Vec::new();
        let mut i = 0;
        while i < line.len() {
            if line.chars().nth(i) == Some('(') {
                let mut j = i + 1;
                while j < line.len() && line.chars().nth(j) != Some(')') {
                    j += 1;
                }
                if j < line.len() {
                    let button_str = &line[i + 1..j];
                    let parts: Vec<usize> = button_str
                        .split(',')
                        .filter_map(|s| s.trim().parse().ok())
                        .collect();
                    let mut b_mask = 0u32;
                    for p in parts {
                        if p < l {
                            b_mask |= 1 << p;
                        }
                    }
                    buttons.push(b_mask);
                }
                i = j + 1;
            } else {
                i += 1;
            }
        }

        buttons.sort();
        buttons.dedup();
        let k = buttons.len();

        if l <= 15 {
            let mut queue = VecDeque::new();
            queue.push_back((0u32, 0));
            let mut seen = HashSet::new();
            seen.insert(0u32);
            let mut found = false;
            let mut best_dist = i32::MAX;

            while let Some((curr, dist)) = queue.pop_front() {
                if curr == target {
                    best_dist = dist;
                    found = true;
                    break;
                }

                for &b in &buttons {
                    let nxt = curr ^ b;
                    if !seen.contains(&nxt) {
                        seen.insert(nxt);
                        queue.push_back((nxt, dist + 1));
                    }
                }
            }

            if found {
                total_presses += best_dist;
            }
        } else {
            let mut matrix = vec![0u64; l];
            for r in 0..l {
                let mut row_val = 0u64;
                for c in 0..k {
                    if (buttons[c] >> r) & 1 != 0 {
                        row_val |= 1 << c;
                    }
                }
                if (target >> r) & 1 != 0 {
                    row_val |= 1 << k;
                }
                matrix[r] = row_val;
            }

            let mut pivot_row = 0;
            let mut pivots = vec![None; l];
            let mut free_cols: HashSet<usize> = (0..k).collect();

            for c in 0..k {
                let mut pivot = None;
                for r in pivot_row..l {
                    if (matrix[r] >> c) & 1 != 0 {
                        pivot = Some(r);
                        break;
                    }
                }

                if let Some(pivot_r) = pivot {
                    matrix.swap(pivot_row, pivot_r);
                    let pivot_val = matrix[pivot_row];
                    for r in 0..l {
                        if r != pivot_row {
                            if (matrix[r] >> c) & 1 != 0 {
                                matrix[r] ^= pivot_val;
                            }
                        }
                    }
                    pivots[pivot_row] = Some(c);
                    free_cols.remove(&c);
                    pivot_row += 1;
                }
            }

            let mut possible = true;
            for r in pivot_row..l {
                if (matrix[r] >> k) & 1 != 0 {
                    possible = false;
                    break;
                }
            }

            if possible {
                let free_vars: Vec<usize> = free_cols.iter().copied().collect();
                let num_free = free_vars.len();

                let mut pivot_equations = Vec::new();
                for r in 0..pivot_row {
                    let base_val = (matrix[r] >> k) & 1;
                    let mut dep_mask = 0u64;
                    for (i, &fv) in free_vars.iter().enumerate() {
                        if (matrix[r] >> fv) & 1 != 0 {
                            dep_mask |= 1 << i;
                        }
                    }
                    pivot_equations.push((base_val, dep_mask));
                }

                let mut min_w = i32::MAX;
                for mask in 0..(1 << num_free) {
                    let w = (mask as u64).count_ones() as i32;
                    if w >= min_w {
                        continue;
                    }

                    let mut curr_w = w;
                    for &(base, dep) in &pivot_equations {
                        let val = base ^ (((dep & mask).count_ones() & 1) as u64);
                        if val != 0 {
                            curr_w += 1;
                        }
                    }

                    if curr_w < min_w {
                        min_w = curr_w;
                    }
                }

                total_presses += min_w;
            }
        }
    }

    total_presses
}

fn main() {
    let input = fs::read_to_string("../inputs/day_10.txt").unwrap();
    let result = solve(&input);
    println!("{}", result);
}
