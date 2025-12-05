use std::fs;

fn is_invalid_id(n: i32) -> bool {
    let s = n.to_string();
    let length = s.len();

    if length % 2 != 0 {
        return false;
    }

    if s.starts_with('0') {
        return false;
    }

    let mid = length / 2;
    &s[..mid] == &s[mid..]
}

fn solve(input: &str) -> i32 {
    let ranges: Vec<(i32, i32)> = input
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
