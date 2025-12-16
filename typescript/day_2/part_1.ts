import { readFileSync } from 'fs';

function is_invalid_id(n: number): boolean {
    const s = n.toString();
    const length = s.length;

    if (length % 2 !== 0) {
        return false;
    }

    if (s[0] === '0') {
        return false;
    }

    const mid = Math.floor(length / 2);
    return s.substring(0, mid) === s.substring(mid);
}

function solve(input: string): number {
    const ranges: [number, number][] = [];
    for (const range_str of input.trim().split(',')) {
        const trimmed = range_str.trim();
        if (trimmed) {
            const parts = trimmed.split('-');
            const start = parseInt(parts[0]);
            const end = parseInt(parts[1]);
            ranges.push([start, end]);
        }
    }

    let sum = 0;

    for (const [start, end] of ranges) {
        for (let i = start; i <= end; i++) {
            if (is_invalid_id(i)) {
                sum += i;
            }
        }
    }

    return sum;
}

const input = readFileSync('../inputs/day_2.txt', 'utf-8');
const result = solve(input);
console.log(result);
