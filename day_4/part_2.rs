use std::fs;

fn count_adjacent_rolls(grid: &Vec<Vec<char>>, row: usize, col: usize) -> i32 {
    let rows = grid.len();
    let cols = if rows > 0 { grid[0].len() } else { 0 };

    let directions = [
        (-1, -1), (-1, 0), (-1, 1),
        (0, -1),           (0, 1),
        (1, -1),  (1, 0),  (1, 1),
    ];

    let mut adjacent_rolls = 0;
    for (dr, dc) in directions.iter() {
        let new_row = row as i32 + dr;
        let new_col = col as i32 + dc;
        if new_row >= 0 && new_row < rows as i32 && new_col >= 0 && new_col < cols as i32 {
            if grid[new_row as usize][new_col as usize] == '@' {
                adjacent_rolls += 1;
            }
        }
    }

    adjacent_rolls
}

fn find_accessible_rolls(grid: &Vec<Vec<char>>) -> Vec<(usize, usize)> {
    let mut accessible = Vec::new();
    let rows = grid.len();
    let cols = if rows > 0 { grid[0].len() } else { 0 };

    for row in 0..rows {
        for col in 0..cols {
            if grid[row][col] == '@' {
                if count_adjacent_rolls(grid, row, col) < 4 {
                    accessible.push((row, col));
                }
            }
        }
    }

    accessible
}

fn solve(input: &str) -> i32 {
    let mut grid: Vec<Vec<char>> = input
        .trim()
        .lines()
        .map(|line| line.chars().collect())
        .collect();

    let mut removed = 0;

    loop {
        let accessible = find_accessible_rolls(&grid);

        if accessible.is_empty() {
            break;
        }

        for (row, col) in accessible.iter() {
            grid[*row][*col] = '.';
        }

        removed += accessible.len() as i32;
    }

    removed
}

fn main() {
    let input = fs::read_to_string("inputs/day_4.txt").expect("Failed to read input file");
    let result = solve(&input);
    println!("{}", result);
}
