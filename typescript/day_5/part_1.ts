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

    const ingredient_ids: number[] = [];
    for (const line of sections[1].trim().split('\n')) {
        const trimmed = line.trim();
        if (trimmed && /^\d+$/.test(trimmed)) {
            ingredient_ids.push(parseInt(trimmed));
        }
    }

    let fresh_count = 0;
    for (const id of ingredient_ids) {
        let is_fresh = false;
        for (const [start, end] of ranges) {
            if (start <= id && id <= end) {
                is_fresh = true;
                break;
            }
        }
        if (is_fresh) {
            fresh_count++;
        }
    }

    return fresh_count;
}

const input = readFileSync('../inputs/day_5.txt', 'utf-8');
const result = solve(input);
console.log(result);
