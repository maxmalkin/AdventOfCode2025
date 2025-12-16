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
            let new_position = (position - distance) % 100;
            if (new_position < 0) {
                new_position += 100;
            }
            const loops = Math.floor(distance / 100);
            zeros += loops;

            const remaining = distance % 100;
            if (position > 0 && remaining >= position) {
                zeros++;
            }
            position = new_position;
        } else {
            const new_position = (position + distance) % 100;

            const loops = Math.floor(distance / 100);
            zeros += loops;

            const remaining = distance % 100;
            if (remaining > 0 && position + remaining >= 100) {
                zeros++;
            }
            position = new_position;
        }
    }

    return zeros;
}

const input = readFileSync('../inputs/day_1.txt', 'utf-8');
const result = solve(input);
console.log(result);
