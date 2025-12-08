use std::fs;

fn is_invalid_id(n: i64) -> bool {
    let s = n.to_string();
    let length = s.len();

    if s.starts_with('0') {
        return false;
    }

    for pattern_len in 1..=length / 2 {
        if length % pattern_len == 0 {
            let pattern = &s[..pattern_len];
            let repeats = length / pattern_len;

            if pattern.repeat(repeats) == s && repeats >= 2 {
                return true;
            }
        }
    }

    false
}

fn solve(input: &str) -> i64 {
    let ranges: Vec<(i64, i64)> = input
        .trim()
        .split(',')
        .map(|range_str| range_str.trim())
        .filter(|range_str| !range_str.is_empty())
        .map(|range_str| {
            let parts: Vec<&str> = range_str.split('-').collect();
            let start = parts[0].parse().unwrap();
            let end = parts[1].parse().unwrap();
            (start, end)
        })
        .collect();

    let mut sum = 0;

    for (start, end) in ranges {
        for i in start..=end {
            if is_invalid_id(i) {
                sum += i;
            }
        }
    }

    sum
}

fn main() {
    let input = fs::read_to_string("../inputs/day_2.txt").expect("Failed to read input file");
    let result = solve(&input);
    println!("{}", result);
}
