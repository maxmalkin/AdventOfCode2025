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

    let mut memo: HashMap<(String, bool, bool), i64> = HashMap::new();

    fn count_paths(
        node: &str,
        has_dac: bool,
        has_fft: bool,
        graph: &HashMap<String, Vec<String>>,
        memo: &mut HashMap<(String, bool, bool), i64>,
    ) -> i64 {
        if node == "out" {
            return if has_dac && has_fft { 1 } else { 0 };
        }

        let key = (node.to_string(), has_dac, has_fft);
        if let Some(&count) = memo.get(&key) {
            return count;
        }

        if !graph.contains_key(node) {
            return 0;
        }

        let new_has_dac = has_dac || node == "dac";
        let new_has_fft = has_fft || node == "fft";

        let mut total = 0;
        if let Some(neighbors) = graph.get(node) {
            for neighbor in neighbors {
                total += count_paths(neighbor, new_has_dac, new_has_fft, graph, memo);
            }
        }

        memo.insert(key, total);
        total
    }

    count_paths("svr", false, false, &graph, &mut memo)
}

fn main() {
    let input = fs::read_to_string("../inputs/day_11.txt").unwrap();
    let result = solve(&input);
    println!("{}", result);
}
