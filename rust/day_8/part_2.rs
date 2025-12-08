use std::collections::HashSet;
use std::fs;

fn solve(input: &str) -> i64 {
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

    fn union(parent: &mut Vec<usize>, x: usize, y: usize) -> bool {
        let px = find(parent, x);
        let py = find(parent, y);
        if px != py {
            parent[px] = py;
            return true;
        }
        false
    }

    for (_, a, b) in distances {
        if union(&mut parent, a, b) {
            let roots: HashSet<usize> = (0..n).map(|i| find(&mut parent, i)).collect();
            if roots.len() == 1 {
                let (x1, _, _) = boxes[a];
                let (x2, _, _) = boxes[b];
                return (x1 as i64) * (x2 as i64);
            }
        }
    }

    0
}

fn main() {
    let input = fs::read_to_string("../inputs/day_8.txt").unwrap();
    let result = solve(&input);
    println!("{}", result);
}
