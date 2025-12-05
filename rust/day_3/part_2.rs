use std::fs;

fn solve(input: &str) -> i64 {
    let banks: Vec<&str> = input
        .trim()
        .lines()
        .map(|line| line.trim())
        .filter(|line| !line.is_empty())
        .collect();

    let mut sum: i64 = 0;

    for b in banks {
        let chars: Vec<char> = b.chars().collect();
        let n = chars.len();
        let k = 12;
        let mut result = Vec::new();
        let mut start = 0;

        for i in 0..k {
            let end = n - k + i;
            let max_digit = chars[start..=end].iter().max().unwrap();

            for j in start..=end {
                if &chars[j] == max_digit {
                    result.push(*max_digit);
                    start = j + 1;
                    break;
                }
            }
        }

        let joltage_str: String = result.iter().collect();
        let joltage: i64 = joltage_str.parse().unwrap();
        sum += joltage;
    }

    sum
}

fn main() {
    let input = fs::read_to_string("../inputs/day_3.txt").expect("Failed to read input file");
    let result = solve(&input);
    println!("{}", result);
}
