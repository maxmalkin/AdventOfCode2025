use std::collections::HashSet;
use std::fs;

fn solve(input: &str) -> i32 {
    let lines: Vec<&str> = input.trim().split('\n').collect();
    let grid: Vec<Vec<char>> = lines.iter().map(|line| line.chars().collect()).collect();
    let rows = grid.len();
    let cols = if rows > 0 { grid[0].len() } else { 0 };

    let mut start_col = 0;
    for col in 0..cols {
        if grid[0][col] == 'S' {
            start_col = col;
            break;
        }
    }

    let mut beams: HashSet<usize> = HashSet::new();
    beams.insert(start_col);
    let mut split_count = 0;

    for row in 1..rows {
        let mut new_beams: HashSet<usize> = HashSet::new();

        for &col in &beams {
            if col < cols {
                if grid[row][col] == '^' {
                    split_count += 1;
                    if col > 0 {
                        new_beams.insert(col - 1);
                    }
                    if col + 1 < cols {
                        new_beams.insert(col + 1);
                    }
                } else {
                    new_beams.insert(col);
                }
            }
        }

        beams = new_beams;
    }

    split_count
}

fn main() {
    let input = fs::read_to_string("../inputs/day_7.txt").expect("Failed to read input file");
    let result = solve(&input);
    println!("{}", result);
}
