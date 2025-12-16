import { readFileSync } from 'fs';

function solve(input: string): number {
    const grid: string[] = [];
    for (const line of input.trim().split('\n')) {
        grid.push(line);
    }
    const rows = grid.length;
    const cols = rows > 0 ? grid[0].length : 0;

    const directions: [number, number][] = [[-1, -1], [-1, 0], [-1, 1], [0, -1], [0, 1], [1, -1], [1, 0], [1, 1]];

    let accessible_count = 0;

    for (let row = 0; row < rows; row++) {
        for (let col = 0; col < cols; col++) {
            if (grid[row][col] === '@') {
                let adjacent_rolls = 0;

                for (const [dr, dc] of directions) {
                    const new_row = row + dr;
                    const new_col = col + dc;

                    if (new_row >= 0 && new_row < rows && new_col >= 0 && new_col < cols) {
                        if (grid[new_row][new_col] === '@') {
                            adjacent_rolls++;
                        }
                    }
                }

                if (adjacent_rolls < 4) {
                    accessible_count++;
                }
            }
        }
    }

    return accessible_count;
}

const input = readFileSync('../inputs/day_4.txt', 'utf-8');
const result = solve(input);
console.log(result);
