import { readFileSync } from 'fs';

function point_in_polygon(tiles: [number, number][], px: number, py: number): boolean {
    const n = tiles.length;
    let count = 0;
    for (let i = 0; i < n; i++) {
        let [x1, y1] = tiles[i];
        let [x2, y2] = tiles[(i + 1) % n];
        if (y1 === y2) {
            continue;
        }
        if (y1 > y2) {
            [x1, y1, x2, y2] = [x2, y2, x1, y1];
        }
        if (py < y1 || py >= y2) {
            continue;
        }
        const x_intersect = x1 + (py - y1) * (x2 - x1) / (y2 - y1);
        if (x_intersect > px) {
            count++;
        }
    }
    return count % 2 === 1;
}

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
        if (!line) {
            continue;
        }
        const parts = line.split(',');
        const x = parseInt(parts[0]);
        const y = parseInt(parts[1]);
        tiles.push([x, y]);
    }

    const n = tiles.length;

    const all_x = Array.from(new Set(tiles.map(([x]) => x))).sort((a, b) => a - b);
    const all_y = Array.from(new Set(tiles.map(([, y]) => y))).sort((a, b) => a - b);

    const x_to_compressed = new Map<number, number>();
    const y_to_compressed = new Map<number, number>();
    all_x.forEach((x, i) => x_to_compressed.set(x, i));
    all_y.forEach((y, i) => y_to_compressed.set(y, i));

    const width = all_x.length;
    const height = all_y.length;

    const compressed_tiles: [number, number][] = [];
    for (const [x, y] of tiles) {
        compressed_tiles.push([x_to_compressed.get(x)!, y_to_compressed.get(y)!]);
    }

    const grid: boolean[][] = Array.from({ length: height }, () => Array(width).fill(false));

    for (const [cx, cy] of compressed_tiles) {
        grid[cy][cx] = true;
    }

    for (let i = 0; i < n; i++) {
        const [x1, y1] = tiles[i];
        const [x2, y2] = tiles[(i + 1) % n];

        const cx1 = x_to_compressed.get(x1)!;
        const cy1 = y_to_compressed.get(y1)!;
        const cx2 = x_to_compressed.get(x2)!;
        const cy2 = y_to_compressed.get(y2)!;

        if (cx1 === cx2) {
            for (let cy = Math.min(cy1, cy2); cy <= Math.max(cy1, cy2); cy++) {
                grid[cy][cx1] = true;
            }
        } else if (cy1 === cy2) {
            for (let cx = Math.min(cx1, cx2); cx <= Math.max(cx1, cx2); cx++) {
                grid[cy1][cx] = true;
            }
        }
    }

    const visited = new Set<string>();
    for (let cy = 0; cy < height; cy++) {
        for (let cx = 0; cx < width; cx++) {
            const key = `${cx},${cy}`;
            if (grid[cy][cx] || visited.has(key)) {
                continue;
            }
            const orig_x = all_x[cx];
            const orig_y = all_y[cy];

            if (point_in_polygon(tiles, orig_x, orig_y)) {
                const stack: [number, number][] = [[cx, cy]];
                while (stack.length > 0) {
                    const [curr_x, curr_y] = stack.pop()!;
                    const curr_key = `${curr_x},${curr_y}`;
                    if (visited.has(curr_key)) {
                        continue;
                    }
                    if (curr_x < 0 || curr_x >= width || curr_y < 0 || curr_y >= height) {
                        continue;
                    }
                    if (grid[curr_y][curr_x]) {
                        continue;
                    }
                    visited.add(curr_key);
                    grid[curr_y][curr_x] = true;

                    for (const [dx, dy] of [[0, 1], [0, -1], [1, 0], [-1, 0]]) {
                        stack.push([curr_x + dx, curr_y + dy]);
                    }
                }
            }
        }
    }

    let max_area = 0;

    for (let i = 0; i < n; i++) {
        for (let j = i + 1; j < n; j++) {
            const [cx1, cy1] = compressed_tiles[i];
            const [cx2, cy2] = compressed_tiles[j];

            const min_cx = Math.min(cx1, cx2);
            const max_cx = Math.max(cx1, cx2);
            const min_cy = Math.min(cy1, cy2);
            const max_cy = Math.max(cy1, cy2);

            let valid = true;
            for (let cy = min_cy; cy <= max_cy && valid; cy++) {
                for (let cx = min_cx; cx <= max_cx; cx++) {
                    if (!grid[cy][cx]) {
                        valid = false;
                        break;
                    }
                }
            }

            if (valid) {
                const [orig_x1, orig_y1] = tiles[i];
                const [orig_x2, orig_y2] = tiles[j];
                const actual_width = Math.abs(orig_x2 - orig_x1) + 1;
                const actual_height = Math.abs(orig_y2 - orig_y1) + 1;
                const area = actual_width * actual_height;
                max_area = Math.max(max_area, area);
            }
        }
    }

    return max_area;
}

const input = readFileSync('../inputs/day_9.txt', 'utf-8');
const result = solve(input);
console.log(result);
