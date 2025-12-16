import { readFileSync } from 'fs';

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
        const target_match = line.match(/\[([.#]+)\]/);
        if (!target_match) {
            continue;
        }

        const target_str = target_match[1];
        let target = 0;
        const l = target_str.length;
        for (let i = 0; i < target_str.length; i++) {
            if (target_str[i] === '#') {
                target |= 1 << i;
            }
        }

        const button_matches = line.matchAll(/\(([\d,\s]+)\)/g);
        const buttons: number[] = [];
        const button_set = new Set<number>();
        for (const b_match of button_matches) {
            const b_str = b_match[1];
            const parts_str = b_str.split(',');
            let b_mask = 0;
            for (const p_str of parts_str) {
                const trimmed = p_str.trim();
                if (trimmed) {
                    const p = parseInt(trimmed);
                    if (p < l) {
                        b_mask |= 1 << p;
                    }
                }
            }
            if (!button_set.has(b_mask)) {
                buttons.push(b_mask);
                button_set.add(b_mask);
            }
        }

        const k = buttons.length;

        if (l <= 15) {
            const queue: [number, number][] = [[0, 0]];
            const seen = new Set<number>([0]);
            let found = false;
            let best_dist = Infinity;

            while (queue.length > 0) {
                const [curr, dist] = queue.shift()!;

                if (curr === target) {
                    best_dist = dist;
                    found = true;
                    break;
                }

                for (const b of buttons) {
                    const nxt = curr ^ b;
                    if (!seen.has(nxt)) {
                        seen.add(nxt);
                        queue.push([nxt, dist + 1]);
                    }
                }
            }

            if (found) {
                total_presses += best_dist;
            }

        } else {
            const matrix: number[] = [];
            for (let r = 0; r < l; r++) {
                let row_val = 0;
                for (let c = 0; c < k; c++) {
                    if ((buttons[c] >> r) & 1) {
                        row_val |= 1 << c;
                    }
                }
                if ((target >> r) & 1) {
                    row_val |= 1 << k;
                }
                matrix.push(row_val);
            }

            let pivot_row = 0;
            const pivots = new Map<number, number>();
            const free_cols = new Set<number>();
            for (let c = 0; c < k; c++) {
                free_cols.add(c);
            }

            for (let c = 0; c < k; c++) {
                let pivot = -1;
                for (let r = pivot_row; r < l; r++) {
                    if ((matrix[r] >> c) & 1) {
                        pivot = r;
                        break;
                    }
                }

                if (pivot !== -1) {
                    [matrix[pivot_row], matrix[pivot]] = [matrix[pivot], matrix[pivot_row]];
                    const pivot_val = matrix[pivot_row];
                    for (let r = 0; r < l; r++) {
                        if (r !== pivot_row) {
                            if ((matrix[r] >> c) & 1) {
                                matrix[r] ^= pivot_val;
                            }
                        }
                    }
                    pivots.set(pivot_row, c);
                    free_cols.delete(c);
                    pivot_row++;
                }
            }

            let possible = true;
            for (let r = pivot_row; r < l; r++) {
                if ((matrix[r] >> k) & 1) {
                    possible = false;
                    break;
                }
            }

            if (possible) {
                const free_vars = Array.from(free_cols);
                const num_free = free_vars.length;

                const pivot_equations: [number, number][] = [];
                for (let r = 0; r < pivot_row; r++) {
                    const base_val = (matrix[r] >> k) & 1;
                    let dep_mask = 0;
                    for (let i = 0; i < num_free; i++) {
                        const fv = free_vars[i];
                        if ((matrix[r] >> fv) & 1) {
                            dep_mask |= 1 << i;
                        }
                    }
                    pivot_equations.push([base_val, dep_mask]);
                }

                let min_w = Infinity;
                for (let mask = 0; mask < (1 << num_free); mask++) {
                    let w = 0;
                    for (let i = 0; i < num_free; i++) {
                        if ((mask >> i) & 1) {
                            w++;
                        }
                    }
                    if (w >= min_w) {
                        continue;
                    }

                    let curr_w = w;
                    for (const [base, dep] of pivot_equations) {
                        let bits = 0;
                        for (let i = 0; i < num_free; i++) {
                            if (((dep & mask) >> i) & 1) {
                                bits++;
                            }
                        }
                        const val = base ^ (bits & 1);
                        if (val === 1) {
                            curr_w++;
                        }
                    }

                    if (curr_w < min_w) {
                        min_w = curr_w;
                    }
                }

                total_presses += min_w;
            }
        }
    }

    return total_presses;
}

const input = readFileSync('../inputs/day_10.txt', 'utf-8');
const result = solve(input);
console.log(result);
