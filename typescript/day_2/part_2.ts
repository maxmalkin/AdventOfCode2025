import { readFileSync } from 'fs';

function is_invalid_id(n: number): boolean {
    const s = n.toString();
    const length = s.length;

    if (s[0] === '0') {
        return false;
    }

    for (let pattern_len = 1; pattern_len <= Math.floor(length / 2); pattern_len++) {
        if (length % pattern_len === 0) {
            const pattern = s.substring(0, pattern_len);
            const repeats = length / pattern_len;

            let result = '';
            for (let i = 0; i < repeats; i++) {
                result += pattern;
            }

            if (result === s && repeats >= 2) {
                return true;
            }
        }
    }

    return false;
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
