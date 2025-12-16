import { readFileSync } from 'fs';

interface Coord {
    r: number;
    c: number;
}

type Shape = Coord[];

interface Region {
    width: number;
    height: number;
    counts: number[];
}

const TIMEOUT_SECONDS = 5;

function parse_input(input: string): [string[][], Region[]] {
    const lines = input.split('\n');
    const shapes: string[][] = Array.from({ length: 6 }, () => []);
    const regions: Region[] = [];

    let i = 0;
    while (i < lines.length) {
        const line = lines[i].trim();

        if (!line) {
            i++;
            continue;
        }

        if (line.includes(':') && !line.includes('x')) {
            const colon_pos = line.indexOf(':');
            const prefix = line.substring(0, colon_pos);
            const idx = parseInt(prefix);
            if (!isNaN(idx) && idx < 6) {
                i++;
                while (i < lines.length && lines[i].trim() && !lines[i].includes(':')) {
                    shapes[idx].push(lines[i]);
                    i++;
                }
                continue;
            }
        }

        if (line.includes('x') && line.includes(':')) {
            const parts = line.split(':');
            if (parts.length === 2) {
                const dims = parts[0].trim().split('x');
                if (dims.length === 2) {
                    const w = parseInt(dims[0]);
                    const h = parseInt(dims[1]);
                    if (!isNaN(w) && !isNaN(h)) {
                        const counts = parts[1].trim().split(/\s+/).map(s => parseInt(s)).filter(n => !isNaN(n));
                        regions.push({ width: w, height: h, counts });
                    }
                }
            }
        }

        i++;
    }

    return [shapes, regions];
}

function get_shape_cells(shape: string[]): Coord[] {
    const cells: Coord[] = [];
    for (let r = 0; r < shape.length; r++) {
        const row = shape[r];
        for (let c = 0; c < row.length; c++) {
            if (row[c] === '#') {
                cells.push({ r, c });
            }
        }
    }
    return cells;
}

function normalize_shape(cells: Coord[]): Shape {
    if (cells.length === 0) {
        return [];
    }
    const min_r = Math.min(...cells.map(c => c.r));
    const min_c = Math.min(...cells.map(c => c.c));
    return cells.map(({ r, c }) => ({ r: r - min_r, c: c - min_c }));
}

function rotate_90(cells: Coord[]): Coord[] {
    return cells.map(({ r, c }) => ({ r: c, c: -r }));
}

function flip_horizontal(cells: Coord[]): Coord[] {
    return cells.map(({ r, c }) => ({ r: -r, c }));
}

function get_all_orientations(shape: string[]): Shape[] {
    const cells = get_shape_cells(shape);
    const orientations_set = new Set<string>();
    const orientations: Shape[] = [];

    let current = cells;
    for (let i = 0; i < 4; i++) {
        const norm = normalize_shape(current);
        const key = JSON.stringify(norm);
        if (!orientations_set.has(key)) {
            orientations_set.add(key);
            orientations.push(norm);
        }
        current = rotate_90(current);
    }

    current = flip_horizontal(cells);
    for (let i = 0; i < 4; i++) {
        const norm = normalize_shape(current);
        const key = JSON.stringify(norm);
        if (!orientations_set.has(key)) {
            orientations_set.add(key);
            orientations.push(norm);
        }
        current = rotate_90(current);
    }

    return orientations;
}

function can_place(grid: boolean[], shape: Shape, start_r: number, start_c: number, width: number, height: number): boolean {
    for (const { r: dr, c: dc } of shape) {
        const r = start_r + dr;
        const c = start_c + dc;
        if (r < 0 || r >= height || c < 0 || c >= width) {
            return false;
        }
        const idx = r * width + c;
        if (grid[idx]) {
            return false;
        }
    }
    return true;
}

function place(grid: boolean[], shape: Shape, start_r: number, start_c: number, width: number): void {
    for (const { r: dr, c: dc } of shape) {
        const r = start_r + dr;
        const c = start_c + dc;
        const idx = r * width + c;
        grid[idx] = true;
    }
}

function unplace(grid: boolean[], shape: Shape, start_r: number, start_c: number, width: number): void {
    for (const { r: dr, c: dc } of shape) {
        const r = start_r + dr;
        const c = start_c + dc;
        const idx = r * width + c;
        grid[idx] = false;
    }
}

function find_first_empty(grid: boolean[], width: number, height: number): Coord | null {
    for (let r = 0; r < height; r++) {
        for (let c = 0; c < width; c++) {
            if (!grid[r * width + c]) {
                return { r, c };
            }
        }
    }
    return null;
}

let start_time: number;

function backtrack(grid: boolean[], sorted_presents: Shape[][], idx: number, width: number, height: number, remaining_cells: number): boolean {
    if (Date.now() - start_time > TIMEOUT_SECONDS * 1000) {
        return false;
    }

    if (idx === sorted_presents.length) {
        return true;
    }

    let remaining_area = 0;
    for (let i = idx; i < sorted_presents.length; i++) {
        remaining_area += sorted_presents[i][0].length;
    }
    if (remaining_area > remaining_cells) {
        return false;
    }

    const pos = find_first_empty(grid, width, height);
    if (!pos) {
        return false;
    }

    const { r: start_r, c: start_c } = pos;
    const shape_variants = sorted_presents[idx];

    for (const variant of shape_variants) {
        if (can_place(grid, variant, start_r, start_c, width, height)) {
            place(grid, variant, start_r, start_c, width);
            const new_remaining = remaining_cells - variant.length;
            if (backtrack(grid, sorted_presents, idx + 1, width, height, new_remaining)) {
                return true;
            }
            unplace(grid, variant, start_r, start_c, width);
        }
    }

    for (const variant of shape_variants) {
        for (let r = 0; r < height; r++) {
            for (let c = 0; c < width; c++) {
                if (r === start_r && c === start_c) {
                    continue;
                }
                if (can_place(grid, variant, r, c, width, height)) {
                    place(grid, variant, r, c, width);
                    const new_remaining = remaining_cells - variant.length;
                    if (backtrack(grid, sorted_presents, idx + 1, width, height, new_remaining)) {
                        return true;
                    }
                    unplace(grid, variant, r, c, width);
                }
            }
        }
    }

    return false;
}

function solve_region(width: number, height: number, presents: Shape[][]): boolean {
    const total_area = presents.reduce((sum, p) => sum + p[0].length, 0);
    const grid_area = width * height;

    if (total_area > grid_area) {
        return false;
    }

    const sorted_presents = [...presents].sort((a, b) => b[0].length - a[0].length);

    const grid = new Array(width * height).fill(false);
    start_time = Date.now();

    return backtrack(grid, sorted_presents, 0, width, height, grid_area);
}

function solve(input: string): number {
    const [shapes, regions] = parse_input(input);

    let count = 0;
    for (const { width, height, counts } of regions) {
        const presents: Shape[][] = [];

        for (let shape_idx = 0; shape_idx < counts.length; shape_idx++) {
            const cnt = counts[shape_idx];
            if (shape_idx < shapes.length && shapes[shape_idx].length > 0) {
                const orientations = get_all_orientations(shapes[shape_idx]);
                for (let i = 0; i < cnt; i++) {
                    presents.push(orientations);
                }
            }
        }

        if (solve_region(width, height, presents)) {
            count++;
        }
    }

    return count;
}

const input = readFileSync('../inputs/day_12.txt', 'utf-8');
const result = solve(input);
console.log(result);
