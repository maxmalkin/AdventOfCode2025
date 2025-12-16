import { readFileSync } from 'fs';

function solve(input: string): number {
    const lines = input.trim().split('\n');
    const grid: string[][] = [];
    for (const line of lines) {
        grid.push(line.split(''));
    }
    const rows = grid.length;
    const cols = rows > 0 ? grid[0].length : 0;

    let start_col = -1;
    for (let col = 0; col < cols; col++) {
        if (grid[0][col] === 'S') {
            start_col = col;
            break;
        }
    }

    let beams = new Set<number>([start_col]);
    let split_count = 0;

    for (let row = 1; row < rows; row++) {
        const new_beams = new Set<number>();

        for (const col of beams) {
            if (col >= 0 && col < cols) {
                if (grid[row][col] === '^') {
                    split_count++;
                    if (col - 1 >= 0) {
                        new_beams.add(col - 1);
                    }
                    if (col + 1 < cols) {
                        new_beams.add(col + 1);
                    }
                } else {
                    new_beams.add(col);
                }
            }
        }

        beams = new_beams;
    }

    return split_count;
}

const input = readFileSync('../inputs/day_7.txt', 'utf-8');
const result = solve(input);
console.log(result);
