use std::fs;

fn solve(input: &str) -> i32 {
    let sections: Vec<&str> = input.trim().split("\n\n").collect();

    let mut ranges = Vec::new();
    for line in sections[0].trim().lines() {
        let line = line.trim();
        if !line.is_empty() {
            let parts: Vec<&str> = line.split('-').collect();
            let start: i32 = parts[0].parse().unwrap();
            let end: i32 = parts[1].parse().unwrap();
            ranges.push((start, end));
        }
    }

    ranges.sort();

    let mut merged = Vec::new();
    for (start, end) in ranges {
        if !merged.is_empty() && start <= merged.last().unwrap().1 + 1 {
            let last_idx = merged.len() - 1;
            merged[last_idx].1 = merged[last_idx].1.max(end);
        } else {
            merged.push((start, end));
        }
    }

    let mut total = 0;
    for (start, end) in merged {
        let count = end - start + 1;
        total += count;
    }

    total
}

fn main() {
    let input = fs::read_to_string("inputs/day_5.txt").expect("Failed to read input file");
    let result = solve(&input);
    println!("{}", result);
}
