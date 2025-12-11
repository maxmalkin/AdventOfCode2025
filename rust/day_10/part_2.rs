use std::fs;

fn solve_machine(buttons: &Vec<Vec<usize>>, joltages: &Vec<i64>) -> i64 {
    let n_buttons = buttons.len();
    let n_counters = joltages.len();

    if n_buttons == 0 || n_counters == 0 {
        return -1;
    }

    let mut min_presses = i64::MAX;

    fn backtrack(
        buttons: &Vec<Vec<usize>>,
        joltages: &Vec<i64>,
        current_sums: &mut Vec<i64>,
        button_counts: &mut Vec<i64>,
        total_presses: i64,
        button_idx: usize,
        min_presses: &mut i64,
        iterations: &mut i64,
    ) -> bool {
        *iterations += 1;
        if *iterations > 10000 {
            return false;
        }

        if total_presses >= *min_presses {
            return true;
        }

        if button_idx == buttons.len() {
            if current_sums == joltages {
                *min_presses = total_presses;
            }
            return true;
        }

        let max_presses_for_button = joltages.iter().max().unwrap_or(&0) + 1;

        for count in 0..=max_presses_for_button {
            for &counter_idx in &buttons[button_idx] {
                if counter_idx < current_sums.len() {
                    current_sums[counter_idx] += count;
                }
            }
            button_counts[button_idx] = count;

            let mut over_limit = false;
            for i in 0..current_sums.len() {
                if current_sums[i] > joltages[i] {
                    over_limit = true;
                    break;
                }
            }

            if !over_limit {
                if !backtrack(
                    buttons,
                    joltages,
                    current_sums,
                    button_counts,
                    total_presses + count,
                    button_idx + 1,
                    min_presses,
                    iterations,
                ) {
                    for &counter_idx in &buttons[button_idx] {
                        if counter_idx < current_sums.len() {
                            current_sums[counter_idx] -= count;
                        }
                    }
                    button_counts[button_idx] = 0;
                    return false;
                }
            }

            for &counter_idx in &buttons[button_idx] {
                if counter_idx < current_sums.len() {
                    current_sums[counter_idx] -= count;
                }
            }
            button_counts[button_idx] = 0;
        }

        true
    }

    let mut current_sums = vec![0; n_counters];
    let mut button_counts = vec![0; n_buttons];
    let mut iterations = 0;

    backtrack(
        buttons,
        joltages,
        &mut current_sums,
        &mut button_counts,
        0,
        0,
        &mut min_presses,
        &mut iterations,
    );

    if min_presses == i64::MAX {
        -1
    } else {
        min_presses
    }
}

fn solve(input: &str) -> i64 {
    let lines: Vec<&str> = input
        .trim()
        .split('\n')
        .map(|line| line.trim())
        .filter(|line| !line.is_empty())
        .collect();

    let mut total_presses = 0;

    for line in lines {
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
                    let button: Vec<usize> = button_str
                        .split(',')
                        .filter_map(|s| s.trim().parse().ok())
                        .collect();
                    buttons.push(button);
                }
                i = j + 1;
            } else {
                i += 1;
            }
        }

        let joltage_start = line.find('{');
        let joltage_end = line.find('}');
        if joltage_start.is_none() || joltage_end.is_none() {
            continue;
        }

        let joltage_str = &line[joltage_start.unwrap() + 1..joltage_end.unwrap()];
        let joltages: Vec<i64> = joltage_str
            .split(',')
            .filter_map(|s| s.trim().parse().ok())
            .collect();

        let min_presses = solve_machine(&buttons, &joltages);
        if min_presses >= 0 {
            total_presses += min_presses;
        }
    }

    total_presses
}

fn main() {
    let input = fs::read_to_string("../inputs/day_10.txt").unwrap();
    let result = solve(&input);
    println!("{}", result);
}
