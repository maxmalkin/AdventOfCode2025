use std::collections::HashMap;
use std::fs;

fn solve(input: &str) -> i64 {
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

    let mut memo: HashMap<(usize, usize), i64> = HashMap::new();

    fn count_paths(
        row: usize,
        col: usize,
        grid: &Vec<Vec<char>>,
        rows: usize,
        cols: usize,
        memo: &mut HashMap<(usize, usize), i64>,
    ) -> i64 {
        if row >= rows {
            return 1;
        }

        if col >= cols {
            return 0;
        }

        if let Some(&result) = memo.get(&(row, col)) {
            return result;
        }

        let result = if grid[row][col] == '^' {
            let left = if col > 0 {
                count_paths(row + 1, col - 1, grid, rows, cols, memo)
            } else {
                0
            };
            let right = count_paths(row + 1, col + 1, grid, rows, cols, memo);
            left + right
        } else {
            count_paths(row + 1, col, grid, rows, cols, memo)
        };

        memo.insert((row, col), result);
        result
    }

    count_paths(0, start_col, &grid, rows, cols, &mut memo)
}

fn main() {
    let input = fs::read_to_string("../inputs/day_7.txt").expect("Failed to read input file");
    let result = solve(&input);
    println!("{}", result);
}
