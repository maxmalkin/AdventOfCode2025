import { readFileSync } from 'fs';

function solve(input: string): number {
    const lines: string[] = [];
    for (const line of input.trim().split('\n')) {
        const trimmed = line.trim();
        if (trimmed) {
            lines.push(trimmed);
        }
    }

    const tiles: [number, number][] = [];
    for (const line of lines) {
        const parts = line.split(',');
        const x = parseInt(parts[0]);
        const y = parseInt(parts[1]);
        tiles.push([x, y]);
    }

    const n = tiles.length;
    let max_area = 0;

    for (let i = 0; i < n; i++) {
        for (let j = i + 1; j < n; j++) {
            const [x1, y1] = tiles[i];
            const [x2, y2] = tiles[j];

            const width = Math.abs(x2 - x1) + 1;
            const height = Math.abs(y2 - y1) + 1;
            const area = width * height;

            max_area = Math.max(max_area, area);
        }
    }

    return max_area;
}

const input = readFileSync('../inputs/day_9.txt', 'utf-8');
const result = solve(input);
console.log(result);
