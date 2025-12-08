def solve(input: str) -> int:
    lines = [line.strip() for line in input.strip().split("\n") if line.strip()]

    boxes = []
    for line in lines:
        x, y, z = map(int, line.split(","))
        boxes.append((x, y, z))

    n = len(boxes)

    distances = []
    for i in range(n):
        for j in range(i + 1, n):
            x1, y1, z1 = boxes[i]
            x2, y2, z2 = boxes[j]
            dist = ((x2 - x1) ** 2 + (y2 - y1) ** 2 + (z2 - z1) ** 2) ** 0.5
            distances.append((dist, i, j))

    distances.sort()

    parent = list(range(n))

    def find(x):
        if parent[x] != x:
            parent[x] = find(parent[x])
        return parent[x]

    def union(x, y):
        px = find(x)
        py = find(y)
        if px != py:
            parent[px] = py
            return True
        return False

    for dist, a, b in distances:
        if union(a, b):
            roots = set(find(i) for i in range(n))
            if len(roots) == 1:
                x1, y1, z1 = boxes[a]
                x2, y2, z2 = boxes[b]
                return x1 * x2

    return 0


if __name__ == "__main__":
    with open("inputs/day_8.txt", "r") as f:
        input = f.read()
        result = solve(input)
        print(result)
