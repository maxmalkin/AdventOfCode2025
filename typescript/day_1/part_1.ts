import { readFileSync } from 'fs';

function solve(input: string): number {
    const rotations: string[] = [];
    for (const line of input.trim().split('\n')) {
        const trimmed = line.trim();
        if (trimmed) {
            rotations.push(trimmed);
        }
    }

    let position = 50;
    let zeros = 0;

    for (const r of rotations) {
        const direction = r[0];
        const distance = parseInt(r.substring(1));

        if (direction === 'L') {
            position = (position - distance) % 100;
            if (position < 0) {
                position += 100;
            }
        } else {
            position = (position + distance) % 100;
        }

        if (position === 0) {
            zeros++;
        }
    }

    return zeros;
}

const input = readFileSync('../inputs/day_1.txt', 'utf-8');
const result = solve(input);
console.log(result);
