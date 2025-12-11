def solve(input: str) -> int:
    lines = [line.strip() for line in input.strip().split("\n") if line.strip()]

    graph = {}
    for line in lines:
        parts = line.split(":")
        device = parts[0].strip()
        outputs = parts[1].strip().split()
        graph[device] = outputs

    memo = {}

    def count_paths(node):
        if node == "out":
            return 1

        if node in memo:
            return memo[node]

        if node not in graph:
            return 0

        total = 0
        for neighbor in graph[node]:
            total += count_paths(neighbor)

        memo[node] = total
        return total

    return count_paths("you")


if __name__ == "__main__":
    with open("inputs/day_11.txt", "r") as f:
        input = f.read()
        result = solve(input)
        print(result)
