def solve(input: str) -> int:
    lines = [line.strip() for line in input.strip().split("\n") if line.strip()]

    graph = {}
    for line in lines:
        parts = line.split(":")
        device = parts[0].strip()
        outputs = parts[1].strip().split()
        graph[device] = outputs

    memo = {}

    def count_paths(node, has_dac, has_fft):
        if node == "out":
            return 1 if (has_dac and has_fft) else 0

        if (node, has_dac, has_fft) in memo:
            return memo[(node, has_dac, has_fft)]

        if node not in graph:
            return 0

        new_has_dac = has_dac or (node == "dac")
        new_has_fft = has_fft or (node == "fft")

        total = 0
        for neighbor in graph[node]:
            total += count_paths(neighbor, new_has_dac, new_has_fft)

        memo[(node, has_dac, has_fft)] = total
        return total

    return count_paths("svr", False, False)


if __name__ == "__main__":
    with open("inputs/day_11.txt", "r") as f:
        input = f.read()
        result = solve(input)
        print(result)
