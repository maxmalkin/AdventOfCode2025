import { readFileSync } from 'fs';

interface Problem {
    numbers: number[];
    operation: string;
}

function solve(input: string): number {
    const lines = input.trim().split('\n');

    const operations_line = lines[lines.length - 1];
    const number_lines = lines.slice(0, lines.length - 1);

    let max_width = 0;
    for (const line of lines) {
        max_width = Math.max(max_width, line.length);
    }

    const padded_number_lines: string[] = [];
    for (const line of number_lines) {
        padded_number_lines.push(line.padEnd(max_width, ' '));
    }
    const padded_operations_line = operations_line.padEnd(max_width, ' ');

    const separator_columns: number[] = [];
    for (let col = 0; col < max_width; col++) {
        let is_separator = true;
        for (const line of padded_number_lines) {
            if (col < line.length && line[col] !== ' ') {
                is_separator = false;
                break;
            }
        }
        if (col < padded_operations_line.length && padded_operations_line[col] !== ' ') {
            is_separator = false;
        }

        if (is_separator) {
            separator_columns.push(col);
        }
    }

    const boundaries = [0, ...separator_columns, max_width];

    const problems: Problem[] = [];
    for (let i = 0; i < boundaries.length - 1; i++) {
        const start = boundaries[i];
        const end = boundaries[i + 1];

        let operation: string | null = null;
        for (let col = start; col < end; col++) {
            if (padded_operations_line[col] === '*' || padded_operations_line[col] === '+') {
                operation = padded_operations_line[col];
                break;
            }
        }

        if (operation === null) {
            continue;
        }

        const reversed_lines: string[] = [];
        for (const line of padded_number_lines) {
            const segment = line.substring(start, end);
            const reversed_segment = segment.split('').reverse().join('');
            reversed_lines.push(reversed_segment);
        }

        const numbers: number[] = [];
        const segment_width = end - start;

        for (let col = 0; col < segment_width; col++) {
            let digits = '';
            for (const reversed_line of reversed_lines) {
                if (col < reversed_line.length && reversed_line[col] !== ' ') {
                    digits += reversed_line[col];
                }
            }

            if (digits.length > 0) {
                const num = parseInt(digits);
                if (!isNaN(num)) {
                    numbers.push(num);
                }
            }
        }

        if (numbers.length > 0) {
            problems.push({ numbers, operation });
        }
    }

    let total = 0;

    for (const problem of problems) {
        if (problem.operation === '*') {
            let result = 1;
            for (const num of problem.numbers) {
                result *= num;
            }
            total += result;
        } else {
            let result = 0;
            for (const num of problem.numbers) {
                result += num;
            }
            total += result;
        }
    }

    return total;
}

const input = readFileSync('../inputs/day_6.txt', 'utf-8');
const result = solve(input);
console.log(result);
