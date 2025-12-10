use std::collections::{HashMap, HashSet};
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
        if line.is_empty() {
            continue;
        }
        let parts: Vec<i32> = line.split(',').map(|s| s.parse().unwrap()).collect();
        tiles.push((parts[0], parts[1]));
    }

    let n = tiles.len();

    let mut all_x: Vec<i32> = tiles.iter().map(|(x, _)| *x).collect();
    let mut all_y: Vec<i32> = tiles.iter().map(|(_, y)| *y).collect();
    all_x.sort();
    all_x.dedup();
    all_y.sort();
    all_y.dedup();

    let x_to_compressed: HashMap<i32, usize> = all_x.iter().enumerate().map(|(i, &x)| (x, i)).collect();
    let y_to_compressed: HashMap<i32, usize> = all_y.iter().enumerate().map(|(i, &y)| (y, i)).collect();

    let width = all_x.len();
    let height = all_y.len();

    let compressed_tiles: Vec<(usize, usize)> = tiles
        .iter()
        .map(|(x, y)| (*x_to_compressed.get(x).unwrap(), *y_to_compressed.get(y).unwrap()))
        .collect();

    let mut grid = vec![vec![false; width]; height];

    for &(cx, cy) in &compressed_tiles {
        grid[cy][cx] = true;
    }

    for i in 0..n {
        let (x1, y1) = tiles[i];
        let (x2, y2) = tiles[(i + 1) % n];

        let cx1 = *x_to_compressed.get(&x1).unwrap();
        let cy1 = *y_to_compressed.get(&y1).unwrap();
        let cx2 = *x_to_compressed.get(&x2).unwrap();
        let cy2 = *y_to_compressed.get(&y2).unwrap();

        if cx1 == cx2 {
            let min_cy = cy1.min(cy2);
            let max_cy = cy1.max(cy2);
            for cy in min_cy..=max_cy {
                grid[cy][cx1] = true;
            }
        } else if cy1 == cy2 {
            let min_cx = cx1.min(cx2);
            let max_cx = cx1.max(cx2);
            for cx in min_cx..=max_cx {
                grid[cy1][cx] = true;
            }
        }
    }

    fn point_in_polygon(px: i32, py: i32, tiles: &Vec<(i32, i32)>) -> bool {
        let n = tiles.len();
        let mut count = 0;
        for i in 0..n {
            let (mut x1, mut y1) = tiles[i];
            let (mut x2, mut y2) = tiles[(i + 1) % n];
            if y1 == y2 {
                continue;
            }
            if y1 > y2 {
                std::mem::swap(&mut x1, &mut x2);
                std::mem::swap(&mut y1, &mut y2);
            }
            if py < y1 || py >= y2 {
                continue;
            }
            let x_intersect = x1 as f64 + (py - y1) as f64 * (x2 - x1) as f64 / (y2 - y1) as f64;
            if x_intersect > px as f64 {
                count += 1;
            }
        }
        count % 2 == 1
    }

    let mut visited = HashSet::new();
    for cy in 0..height {
        for cx in 0..width {
            if grid[cy][cx] || visited.contains(&(cx, cy)) {
                continue;
            }
            let orig_x = all_x[cx];
            let orig_y = all_y[cy];

            if point_in_polygon(orig_x, orig_y, &tiles) {
                let mut stack = vec![(cx, cy)];
                while let Some((curr_x, curr_y)) = stack.pop() {
                    if visited.contains(&(curr_x, curr_y)) {
                        continue;
                    }
                    if curr_x >= width || curr_y >= height {
                        continue;
                    }
                    if grid[curr_y][curr_x] {
                        continue;
                    }
                    visited.insert((curr_x, curr_y));
                    grid[curr_y][curr_x] = true;

                    for (dx, dy) in [(0, 1), (0, -1), (1, 0), (-1, 0)] {
                        let nx = curr_x as i32 + dx;
                        let ny = curr_y as i32 + dy;
                        if nx >= 0 && ny >= 0 {
                            stack.push((nx as usize, ny as usize));
                        }
                    }
                }
            }
        }
    }

    let mut max_area = 0;

    for i in 0..n {
        for j in (i + 1)..n {
            let (cx1, cy1) = compressed_tiles[i];
            let (cx2, cy2) = compressed_tiles[j];

            let min_cx = cx1.min(cx2);
            let max_cx = cx1.max(cx2);
            let min_cy = cy1.min(cy2);
            let max_cy = cy1.max(cy2);

            let mut valid = true;
            for cy in min_cy..=max_cy {
                if !valid {
                    break;
                }
                for cx in min_cx..=max_cx {
                    if !grid[cy][cx] {
                        valid = false;
                        break;
                    }
                }
            }

            if valid {
                let (orig_x1, orig_y1) = tiles[i];
                let (orig_x2, orig_y2) = tiles[j];
                let actual_width = (orig_x2 - orig_x1).abs() + 1;
                let actual_height = (orig_y2 - orig_y1).abs() + 1;
                let area = actual_width * actual_height;
                max_area = max_area.max(area);
            }
        }
    }

    max_area
}

fn main() {
    let input = fs::read_to_string("../inputs/day_9.txt").unwrap();
    let result = solve(&input);
    println!("{}", result);
}
