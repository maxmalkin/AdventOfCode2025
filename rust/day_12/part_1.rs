use std::collections::HashSet;
use std::fs;
use std::time::{Duration, Instant};

type Shape = Vec<(i32, i32)>;

const TIMEOUT_SECONDS: u64 = 5;

fn parse_input(input: &str) -> (Vec<Vec<String>>, Vec<(usize, usize, Vec<usize>)>) {
    let lines: Vec<&str> = input.lines().collect();
    let mut shapes: Vec<Vec<String>> = vec![Vec::new(); 6];
    let mut regions = Vec::new();

    let mut i = 0;
    while i < lines.len() {
        let line = lines[i].trim();

        if line.is_empty() {
            i += 1;
            continue;
        }

        if line.contains(':') && !line.contains('x') {
            if let Some(colon_pos) = line.find(':') {
                let prefix = &line[..colon_pos];
                if let Ok(idx) = prefix.parse::<usize>() {
                    if idx < 6 {
                        i += 1;
                        while i < lines.len()
                            && !lines[i].trim().is_empty()
                            && !lines[i].contains(':')
                        {
                            shapes[idx].push(lines[i].to_string());
                            i += 1;
                        }
                        continue;
                    }
                }
            }
        }

        if line.contains('x') && line.contains(':') {
            let parts: Vec<&str> = line.split(':').collect();
            if parts.len() == 2 {
                let dims: Vec<&str> = parts[0].trim().split('x').collect();
                if dims.len() == 2 {
                    if let (Ok(w), Ok(h)) = (dims[0].parse::<usize>(), dims[1].parse::<usize>()) {
                        let counts: Vec<usize> = parts[1]
                            .split_whitespace()
                            .filter_map(|s| s.parse().ok())
                            .collect();
                        regions.push((w, h, counts));
                    }
                }
            }
        }

        i += 1;
    }

    (shapes, regions)
}

fn get_shape_cells(shape: &[String]) -> Vec<(i32, i32)> {
    let mut cells = Vec::new();
    for (r, row) in shape.iter().enumerate() {
        for (c, ch) in row.chars().enumerate() {
            if ch == '#' {
                cells.push((r as i32, c as i32));
            }
        }
    }
    cells
}

fn normalize_shape(cells: &[(i32, i32)]) -> Shape {
    if cells.is_empty() {
        return Vec::new();
    }
    let min_r = cells.iter().map(|(r, _)| *r).min().unwrap();
    let min_c = cells.iter().map(|(_, c)| *c).min().unwrap();
    let mut normalized: Vec<_> = cells.iter().map(|(r, c)| (r - min_r, c - min_c)).collect();
    normalized.sort();
    normalized
}

fn rotate_90(cells: &[(i32, i32)]) -> Vec<(i32, i32)> {
    cells.iter().map(|(r, c)| (*c, -*r)).collect()
}

fn flip_horizontal(cells: &[(i32, i32)]) -> Vec<(i32, i32)> {
    cells.iter().map(|(r, c)| (-*r, *c)).collect()
}

fn get_all_orientations(shape: &[String]) -> Vec<Shape> {
    let cells = get_shape_cells(shape);
    let mut orientations = HashSet::new();

    let mut current = cells.clone();
    for _ in 0..4 {
        orientations.insert(normalize_shape(&current));
        current = rotate_90(&current);
    }

    current = flip_horizontal(&cells);
    for _ in 0..4 {
        orientations.insert(normalize_shape(&current));
        current = rotate_90(&current);
    }

    orientations.into_iter().collect()
}

fn can_place(
    grid: &[bool],
    shape: &Shape,
    start_r: i32,
    start_c: i32,
    width: usize,
    height: usize,
) -> bool {
    for (dr, dc) in shape {
        let r = start_r + dr;
        let c = start_c + dc;
        if r < 0 || r >= height as i32 || c < 0 || c >= width as i32 {
            return false;
        }
        let idx = (r as usize) * width + (c as usize);
        if grid[idx] {
            return false;
        }
    }
    true
}

fn place(grid: &mut [bool], shape: &Shape, start_r: i32, start_c: i32, width: usize) {
    for (dr, dc) in shape {
        let r = start_r + dr;
        let c = start_c + dc;
        let idx = (r as usize) * width + (c as usize);
        grid[idx] = true;
    }
}

fn unplace(grid: &mut [bool], shape: &Shape, start_r: i32, start_c: i32, width: usize) {
    for (dr, dc) in shape {
        let r = start_r + dr;
        let c = start_c + dc;
        let idx = (r as usize) * width + (c as usize);
        grid[idx] = false;
    }
}

fn find_first_empty(grid: &[bool], width: usize, height: usize) -> Option<(i32, i32)> {
    for r in 0..height {
        for c in 0..width {
            if !grid[r * width + c] {
                return Some((r as i32, c as i32));
            }
        }
    }
    None
}

fn solve_region(width: usize, height: usize, presents: &[Vec<Shape>]) -> bool {
    let total_area: usize = presents.iter().map(|p| p[0].len()).sum();
    let grid_area = width * height;

    if total_area > grid_area {
        return false;
    }

    let mut sorted_presents = presents.to_vec();
    sorted_presents.sort_by_key(|p| std::cmp::Reverse(p[0].len()));

    let mut grid = vec![false; width * height];
    let start_time = Instant::now();

    fn backtrack(
        grid: &mut [bool],
        presents: &[Vec<Shape>],
        idx: usize,
        width: usize,
        height: usize,
        remaining_cells: usize,
        start_time: Instant,
    ) -> bool {
        if start_time.elapsed() > Duration::from_secs(TIMEOUT_SECONDS) {
            return false;
        }

        if idx == presents.len() {
            return true;
        }

        let remaining_area: usize = presents[idx..].iter().map(|p| p[0].len()).sum();
        if remaining_area > remaining_cells {
            return false;
        }

        if let Some((start_r, start_c)) = find_first_empty(grid, width, height) {
            let shape_variants = &presents[idx];

            for variant in shape_variants {
                if can_place(grid, variant, start_r, start_c, width, height) {
                    place(grid, variant, start_r, start_c, width);
                    let new_remaining = remaining_cells - variant.len();
                    if backtrack(
                        grid,
                        presents,
                        idx + 1,
                        width,
                        height,
                        new_remaining,
                        start_time,
                    ) {
                        return true;
                    }
                    unplace(grid, variant, start_r, start_c, width);
                }
            }

            for variant in shape_variants {
                for r in 0..height as i32 {
                    for c in 0..width as i32 {
                        if (r, c) == (start_r, start_c) {
                            continue;
                        }
                        if can_place(grid, variant, r, c, width, height) {
                            place(grid, variant, r, c, width);
                            let new_remaining = remaining_cells - variant.len();
                            if backtrack(
                                grid,
                                presents,
                                idx + 1,
                                width,
                                height,
                                new_remaining,
                                start_time,
                            ) {
                                return true;
                            }
                            unplace(grid, variant, r, c, width);
                        }
                    }
                }
            }
        }

        false
    }

    backtrack(
        &mut grid,
        &sorted_presents,
        0,
        width,
        height,
        grid_area,
        start_time,
    )
}

fn solve(input: &str) -> usize {
    let (shapes, regions) = parse_input(input);

    let mut count = 0;
    for (width, height, counts) in regions {
        let mut presents = Vec::new();

        for (shape_idx, &cnt) in counts.iter().enumerate() {
            if shape_idx < shapes.len() && !shapes[shape_idx].is_empty() {
                let orientations = get_all_orientations(&shapes[shape_idx]);
                for _ in 0..cnt {
                    presents.push(orientations.clone());
                }
            }
        }

        if solve_region(width, height, &presents) {
            count += 1;
        }
    }

    count
}

fn main() {
    let input = fs::read_to_string("../inputs/day_12.txt").expect("Failed to read input file");
    let result = solve(&input);
    println!("{}", result);
}
