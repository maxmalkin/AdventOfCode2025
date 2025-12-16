import { readFileSync } from 'fs';

interface Distance {
    dist: number;
    i: number;
    j: number;
}

function solve(input: string): number {
    const lines: string[] = [];
    for (const line of input.trim().split('\n')) {
        const trimmed = line.trim();
        if (trimmed) {
            lines.push(trimmed);
        }
    }

    const boxes: [number, number, number][] = [];
    for (const line of lines) {
        const parts = line.split(',');
        const x = parseInt(parts[0]);
        const y = parseInt(parts[1]);
        const z = parseInt(parts[2]);
        boxes.push([x, y, z]);
    }

    const n = boxes.length;

    const distances: Distance[] = [];
    for (let i = 0; i < n; i++) {
        for (let j = i + 1; j < n; j++) {
            const [x1, y1, z1] = boxes[i];
            const [x2, y2, z2] = boxes[j];
            const dist = Math.sqrt((x2 - x1) ** 2 + (y2 - y1) ** 2 + (z2 - z1) ** 2);
            distances.push({ dist, i, j });
        }
    }

    distances.sort((a, b) => a.dist - b.dist);

    const parent = Array.from({ length: n }, (_, i) => i);

    function find(x: number): number {
        if (parent[x] !== x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    function union(x: number, y: number): void {
        const px = find(x);
        const py = find(y);
        if (px !== py) {
            parent[px] = py;
        }
    }

    const limit = Math.min(1000, distances.length);
    for (let i = 0; i < limit; i++) {
        const { i: a, j: b } = distances[i];
        union(a, b);
    }

    const circuit_sizes = new Map<number, number>();
    for (let i = 0; i < n; i++) {
        const root = find(i);
        circuit_sizes.set(root, (circuit_sizes.get(root) || 0) + 1);
    }

    const sizes = Array.from(circuit_sizes.values()).sort((a, b) => b - a);

    return sizes[0] * sizes[1] * sizes[2];
}

const input = readFileSync('../inputs/day_8.txt', 'utf-8');
const result = solve(input);
console.log(result);
