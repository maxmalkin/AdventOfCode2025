use std::fs;

fn solve(input: &str) -> i32 {
    let grid: Vec<&str> = input.trim().lines().collect();
    let rows = grid.len();
    let cols = if rows > 0 { grid[0].len() } else { 0 };

    let directions = [
        (-1, -1),
        (-1, 0),
        (-1, 1),
        (0, -1),
        (0, 1),
        (1, -1),
        (1, 0),
        (1, 1),
    ];

    let mut accessible_count = 0;

    for row in 0..rows {
        let row_chars: Vec<char> = grid[row].chars().collect();
        for col in 0..cols {
            if row_chars[col] == '@' {
                let mut adjacent_rolls = 0;

                for (dr, dc) in directions.iter() {
                    let new_row = row as i32 + dr;
                    let new_col = col as i32 + dc;

                    if new_row >= 0
                        && new_row < rows as i32
                        && new_col >= 0
                        && new_col < cols as i32
                    {
                        let new_row_chars: Vec<char> = grid[new_row as usize].chars().collect();
                        if new_row_chars[new_col as usize] == '@' {
                            adjacent_rolls += 1;
                        }
                    }
                }

                if adjacent_rolls < 4 {
                    accessible_count += 1;
                }
            }
        }
    }

    accessible_count
}

fn main() {
    let input = fs::read_to_string("../inputs/day_4.txt").expect("Failed to read input file");
    let result = solve(&input);
    println!("{}", result);
}
