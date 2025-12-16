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

    const memo = new Map<string, number>();

    function count_paths(row: number, col: number): number {
        if (row >= rows) {
            return 1;
        }

        if (col < 0 || col >= cols) {
            return 0;
        }

        const key = `${row},${col}`;
        if (memo.has(key)) {
            return memo.get(key)!;
        }

        let result: number;
        if (grid[row][col] === '^') {
            result = count_paths(row + 1, col - 1) + count_paths(row + 1, col + 1);
        } else {
            result = count_paths(row + 1, col);
        }

        memo.set(key, result);
        return result;
    }

    return count_paths(0, start_col);
}

const input = readFileSync('../inputs/day_7.txt', 'utf-8');
const result = solve(input);
console.log(result);
