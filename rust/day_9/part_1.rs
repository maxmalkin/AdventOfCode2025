use std::fs;

fn solve(input: &str) -> i32 {
    let lines: Vec<&str> = input
        .trim()
        .split('\n')
        .map(|line| line.trim())
        .filter(|line| !line.is_empty())
        .collect();

    let mut tiles = Vec::new();
    for line in lines {
        let parts: Vec<i32> = line.split(',').map(|s| s.parse().unwrap()).collect();
        tiles.push((parts[0], parts[1]));
    }

    let n = tiles.len();
    let mut max_area = 0;

    for i in 0..n {
        for j in (i + 1)..n {
            let (x1, y1) = tiles[i];
            let (x2, y2) = tiles[j];

            let width = (x2 - x1).abs() + 1;
            let height = (y2 - y1).abs() + 1;
            let area = width * height;

            max_area = max_area.max(area);
        }
    }

    max_area
}

fn main() {
    let input = fs::read_to_string("../inputs/day_9.txt").unwrap();
    let result = solve(&input);
    println!("{}", result);
}
