import { readFileSync } from 'fs';

function solve_machine(buttons: number[][], joltages: number[]): number {
    const n_buttons = buttons.length;
    const n_counters = joltages.length;

    let min_presses = Infinity;

    let max_tries = 1000000;
    if (n_buttons <= 10) {
        max_tries = 1 << (n_buttons * 4);
        if (max_tries > 1000000) {
            max_tries = 1000000;
        }
    }

    for (let attempt = 0; attempt < max_tries; attempt++) {
        const x: number[] = [];
        let total = 0;
        let temp_attempt = attempt;
        for (let i = 0; i < n_buttons; i++) {
            x.push(temp_attempt % 100);
            temp_attempt = Math.floor(temp_attempt / 100);
            total += x[i];
        }

        if (total >= min_presses) {
            continue;
        }

        let valid = true;
        for (let counter_idx = 0; counter_idx < n_counters; counter_idx++) {
            let counter_sum = 0;
            for (let btn_idx = 0; btn_idx < n_buttons; btn_idx++) {
                const found = buttons[btn_idx].includes(counter_idx);
                if (found) {
                    counter_sum += x[btn_idx];
                }
            }
            if (counter_sum !== joltages[counter_idx]) {
                valid = false;
                break;
            }
        }

        if (valid && total < min_presses) {
            min_presses = total;
        }
    }

    if (min_presses === Infinity) {
        return -1;
    }
    return min_presses;
}

function solve(input: string): number {
    const lines: string[] = [];
    for (const line of input.trim().split('\n')) {
        const trimmed = line.trim();
        if (trimmed) {
            lines.push(trimmed);
        }
    }

    let total_presses = 0;

    for (const line of lines) {
        const buttons: number[][] = [];
        const button_matches = line.matchAll(/\(([0-9,]+)\)/g);
        for (const match of button_matches) {
            const button: number[] = [];
            for (const x_str of match[1].split(',')) {
                button.push(parseInt(x_str));
            }
            buttons.push(button);
        }

        const joltage_match = line.match(/\{([0-9,]+)\}/);
        const joltages: number[] = [];
        if (joltage_match) {
            for (const x_str of joltage_match[1].split(',')) {
                joltages.push(parseInt(x_str));
            }
        }

        const min_presses = solve_machine(buttons, joltages);
        total_presses += min_presses;
    }

    return total_presses;
}

const input = readFileSync('../inputs/day_10.txt', 'utf-8');
const result = solve(input);
console.log(result);
