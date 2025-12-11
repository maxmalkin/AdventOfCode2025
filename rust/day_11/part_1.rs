use std::collections::HashMap;
use std::fs;

fn solve(input: &str) -> i64 {
    let lines: Vec<&str> = input
        .trim()
        .split('\n')
        .map(|line| line.trim())
        .filter(|line| !line.is_empty())
        .collect();

    let mut graph: HashMap<String, Vec<String>> = HashMap::new();
    for line in lines {
        let parts: Vec<&str> = line.split(':').collect();
        let device = parts[0].trim().to_string();
        let outputs: Vec<String> = parts[1]
            .trim()
            .split_whitespace()
            .map(|s| s.to_string())
            .collect();
        graph.insert(device, outputs);
    }

    let mut memo: HashMap<String, i64> = HashMap::new();

    fn count_paths(node: &str, graph: &HashMap<String, Vec<String>>, memo: &mut HashMap<String, i64>) -> i64 {
        if node == "out" {
            return 1;
        }

        if let Some(&count) = memo.get(node) {
            return count;
        }

        if !graph.contains_key(node) {
            return 0;
        }

        let mut total = 0;
        if let Some(neighbors) = graph.get(node) {
            for neighbor in neighbors {
                total += count_paths(neighbor, graph, memo);
            }
        }

        memo.insert(node.to_string(), total);
        total
    }

    count_paths("you", &graph, &mut memo)
}

fn main() {
    let input = fs::read_to_string("../inputs/day_11.txt").unwrap();
    let result = solve(&input);
    println!("{}", result);
}
