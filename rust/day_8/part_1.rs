use std::collections::HashMap;
use std::fs;

fn solve(input: &str) -> i32 {
    let lines: Vec<&str> = input
        .trim()
        .split('\n')
        .map(|line| line.trim())
        .filter(|line| !line.is_empty())
        .collect();

    let mut boxes = Vec::new();
    for line in lines {
        let parts: Vec<i32> = line.split(',').map(|s| s.parse().unwrap()).collect();
        boxes.push((parts[0], parts[1], parts[2]));
    }

    let n = boxes.len();

    let mut distances = Vec::new();
    for i in 0..n {
        for j in (i + 1)..n {
            let (x1, y1, z1) = boxes[i];
            let (x2, y2, z2) = boxes[j];
            let dist = ((((x2 - x1) as i64).pow(2) + ((y2 - y1) as i64).pow(2) + ((z2 - z1) as i64).pow(2)) as f64).sqrt();
            distances.push((dist, i, j));
        }
    }

    distances.sort_by(|a, b| a.0.partial_cmp(&b.0).unwrap());

    let mut parent: Vec<usize> = (0..n).collect();

    fn find(parent: &mut Vec<usize>, x: usize) -> usize {
        if parent[x] != x {
            parent[x] = find(parent, parent[x]);
        }
        parent[x]
    }

    fn union(parent: &mut Vec<usize>, x: usize, y: usize) {
        let px = find(parent, x);
        let py = find(parent, y);
        if px != py {
            parent[px] = py;
        }
    }

    for i in 0..1000.min(distances.len()) {
        let (_, a, b) = distances[i];
        union(&mut parent, a, b);
    }

    let mut circuit_sizes: HashMap<usize, i32> = HashMap::new();
    for i in 0..n {
        let root = find(&mut parent, i);
        *circuit_sizes.entry(root).or_insert(0) += 1;
    }

    let mut sizes: Vec<i32> = circuit_sizes.values().copied().collect();
    sizes.sort_by(|a, b| b.cmp(a));

    sizes[0] * sizes[1] * sizes[2]
}

fn main() {
    let input = fs::read_to_string("../inputs/day_8.txt").unwrap();
    let result = solve(&input);
    println!("{}", result);
}
