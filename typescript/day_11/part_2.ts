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

    function count_paths(node: string, has_dac: boolean, has_fft: boolean): number {
        if (node === 'out') {
            return (has_dac && has_fft) ? 1 : 0;
        }

        const key = `${node},${has_dac},${has_fft}`;
        if (memo.has(key)) {
            return memo.get(key)!;
        }

        if (!graph.has(node)) {
            return 0;
        }

        const new_has_dac = has_dac || (node === 'dac');
        const new_has_fft = has_fft || (node === 'fft');

        let total = 0;
        for (const neighbor of graph.get(node)!) {
            total += count_paths(neighbor, new_has_dac, new_has_fft);
        }

        memo.set(key, total);
        return total;
    }

    return count_paths('svr', false, false);
}

const input = readFileSync('../inputs/day_11.txt', 'utf-8');
const result = solve(input);
console.log(result);
