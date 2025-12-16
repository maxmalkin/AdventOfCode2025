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
        const n = b.length;
        const k = 12;
        const result: string[] = [];
        let start = 0;

        for (let i = 0; i < k; i++) {
            const end = n - k + i;
            let max_digit = '0';
            for (let j = start; j <= end; j++) {
                if (b[j] > max_digit) {
                    max_digit = b[j];
                }
            }

            for (let j = start; j <= end; j++) {
                if (b[j] === max_digit) {
                    result.push(max_digit);
                    start = j + 1;
                    break;
                }
            }
        }

        const joltage = parseInt(result.join(''));
        sum += joltage;
    }

    return sum;
}

const input = readFileSync('../inputs/day_3.txt', 'utf-8');
const result = solve(input);
console.log(result);
