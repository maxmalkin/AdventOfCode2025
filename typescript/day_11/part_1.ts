import { readFileSync } from 'fs';

function solve(input: string): number {
    const lines: string[] = [];
    for (const line of input.trim().split('\n')) {
        const trimmed = line.trim();
        if (trimmed) {
            lines.push(trimmed);
        }
    }

    const graph = new Map<string, string[]>();
    for (const line of lines) {
        const parts = line.split(':');
        const device = parts[0].trim();
        const outputs = parts[1].trim().split(/\s+/);
        graph.set(device, outputs);
    }

    const memo = new Map<string, number>();

    function count_paths(node: string): number {
        if (node === 'out') {
            return 1;
        }

        if (memo.has(node)) {
            return memo.get(node)!;
        }

        if (!graph.has(node)) {
            return 0;
        }

        let total = 0;
        for (const neighbor of graph.get(node)!) {
            total += count_paths(neighbor);
        }

        memo.set(node, total);
        return total;
    }

    return count_paths('you');
}

const input = readFileSync('../inputs/day_11.txt', 'utf-8');
const result = solve(input);
console.log(result);
