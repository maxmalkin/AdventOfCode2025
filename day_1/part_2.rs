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

        let new_position;
        if direction == 'L' {
            new_position = (position - distance).rem_euclid(100);
            let loops = distance / 100;
            zeros += loops;

            let remaining = distance % 100;
            if position > 0 && remaining >= position {
                zeros += 1;
            }
        } else {
            new_position = (position + distance).rem_euclid(100);
            let loops = distance / 100;
            zeros += loops;

            let remaining = distance % 100;
            if remaining > 0 && position + remaining >= 100 {
                zeros += 1;
            }
        }

        position = new_position;
    }

    zeros
}

fn main() {
    let input = fs::read_to_string("inputs/day_1.txt").expect("Failed to read input file");
    let result = solve(&input);
    println!("{}", result);
}
