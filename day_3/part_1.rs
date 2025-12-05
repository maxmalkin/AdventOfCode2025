use std::fs;

fn solve(input: &str) -> i32 {
    let banks: Vec<&str> = input
        .trim()
        .lines()
        .map(|line| line.trim())
        .filter(|line| !line.is_empty())
        .collect();

    let mut sum = 0;

    for b in banks {
        let mut max_joltage = 0;
        let chars: Vec<char> = b.chars().collect();

        for i in 0..chars.len() {
            for j in (i + 1)..chars.len() {
                let joltage_str = format!("{}{}", chars[i], chars[j]);
                let joltage: i32 = joltage_str.parse().unwrap();
                max_joltage = max_joltage.max(joltage);
            }
        }

        sum += max_joltage;
    }

    sum
}

fn main() {
    let input = fs::read_to_string("inputs/day_3.txt").expect("Failed to read input file");
    let result = solve(&input);
    println!("{}", result);
}
