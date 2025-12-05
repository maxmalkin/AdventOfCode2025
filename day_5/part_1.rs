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

    let mut ingredient_ids = Vec::new();
    for line in sections[1].trim().lines() {
        let line = line.trim();
        if !line.is_empty() && line.chars().all(|c| c.is_numeric()) {
            ingredient_ids.push(line.parse::<i32>().unwrap());
        }
    }

    let mut fresh_count = 0;
    for id in ingredient_ids {
        let mut is_fresh = false;
        for (start, end) in &ranges {
            if id >= *start && id <= *end {
                is_fresh = true;
                break;
            }
        }
        if is_fresh {
            fresh_count += 1;
        }
    }

    fresh_count
}

fn main() {
    let input = fs::read_to_string("inputs/day_5.txt").expect("Failed to read input file");
    let result = solve(&input);
    println!("{}", result);
}
