use std::fs;

fn solve(input: &str) -> i64 {
    let lines: Vec<&str> = input.trim().split('\n').collect();

    let operations_line = lines[lines.len() - 1];
    let number_lines = &lines[..lines.len() - 1];

    let max_width = lines.iter().map(|line| line.len()).max().unwrap_or(0);

    let number_lines: Vec<String> = number_lines
        .iter()
        .map(|line| format!("{:width$}", line, width = max_width))
        .collect();
    let operations_line = format!("{:width$}", operations_line, width = max_width);

    let mut separator_columns = Vec::new();
    for col in 0..max_width {
        let mut is_separator = true;
        for line in &number_lines {
            if col < line.len() && line.chars().nth(col).unwrap() != ' ' {
                is_separator = false;
                break;
            }
        }
        if col < operations_line.len() && operations_line.chars().nth(col).unwrap() != ' ' {
            is_separator = false;
        }

        if is_separator {
            separator_columns.push(col);
        }
    }

    let mut boundaries = vec![0];
    boundaries.extend(&separator_columns);
    boundaries.push(max_width);

    let mut problems = Vec::new();
    for i in 0..boundaries.len() - 1 {
        let start = boundaries[i];
        let end = boundaries[i + 1];

        let mut operation = None;
        for col in start..end {
            let ch = operations_line.chars().nth(col).unwrap();
            if ch == '*' || ch == '+' {
                operation = Some(ch);
                break;
            }
        }

        if operation.is_none() {
            continue;
        }

        let mut numbers = Vec::new();
        for line in &number_lines {
            let segment = &line[start..end].trim();
            if !segment.is_empty() {
                if let Ok(num) = segment.parse::<i32>() {
                    numbers.push(num);
                }
            }
        }

        if !numbers.is_empty() {
            problems.push((numbers, operation.unwrap()));
        }
    }

    let mut total: i64 = 0;

    for (numbers, operation) in problems {
        let result: i64 = if operation == '*' {
            let mut result: i64 = 1;
            for num in numbers {
                result *= num as i64;
            }
            result
        } else {
            numbers.iter().map(|&n| n as i64).sum()
        };

        total += result;
    }

    total
}

fn main() {
    let input = fs::read_to_string("../inputs/day_6.txt").expect("Failed to read input file");
    let result = solve(&input);
    println!("{}", result);
}
