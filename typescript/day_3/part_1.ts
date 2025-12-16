import { readFileSync } from 'fs';

function solve(input: string): number {
    const banks: string[] = [];
    for (const line of input.trim().split('\n')) {
        const trimmed = line.trim();
        if (trimmed) {
            banks.push(trimmed);
        }
    }

    let sum = 0;

    for (const b of banks) {
        let max_joltage = 0;

        for (let i = 0; i < b.length; i++) {
            for (let j = i + 1; j < b.length; j++) {
                const joltage = parseInt(b[i] + b[j]);
                max_joltage = Math.max(max_joltage, joltage);
            }
        }

        sum += max_joltage;
    }

    return sum;
}

const input = readFileSync('../inputs/day_3.txt', 'utf-8');
const result = solve(input);
console.log(result);
