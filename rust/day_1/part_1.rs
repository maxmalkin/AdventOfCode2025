use std::fs;

fn solve(input: &str) -> i32 {
    let rotations: Vec<&str> = input
        .trim()
        .lines()
        .map(|line| line.trim())
        .filter(|line| !line.is_empty())
        .collect();

    let mut position = 50;
    let mut zeros = 0;

    for r in rotations {
        let direction = r.chars().next().unwrap();
        let distance: i32 = r[1..].parse().unwrap();

        if direction == 'L' {
            position = (position - distance).rem_euclid(100);
        } else {
            position = (position + distance).rem_euclid(100);
        }

        if position == 0 {
            zeros += 1;
        }
    }

    zeros
}

fn main() {
    let input = fs::read_to_string("../inputs/day_1.txt").expect("Failed to read input file");
    let result = solve(&input);
    println!("{}", result);
}
