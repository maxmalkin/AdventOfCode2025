import { readFileSync } from 'fs';

function solve(input: string): number {
    const sections = input.trim().split('\n\n');

    const ranges: [number, number][] = [];
    for (const line of sections[0].trim().split('\n')) {
        if (line.trim()) {
            const parts = line.split('-');
            const start = parseInt(parts[0]);
            const end = parseInt(parts[1]);
            ranges.push([start, end]);
        }
    }

    ranges.sort((a, b) => a[0] - b[0]);

    const merged: [number, number][] = [];
    for (const [start, end] of ranges) {
        if (merged.length > 0 && start <= merged[merged.length - 1][1] + 1) {
            merged[merged.length - 1][1] = Math.max(merged[merged.length - 1][1], end);
        } else {
            merged.push([start, end]);
        }
    }

    let total = 0;
    for (const [start, end] of merged) {
        const count = end - start + 1;
        total += count;
    }

    return total;
}

const input = readFileSync('../inputs/day_5.txt', 'utf-8');
const result = solve(input);
console.log(result);
