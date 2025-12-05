def solve(input: str) -> int:
    sections = input.strip().split("\n\n")

    ranges = []
    for line in sections[0].strip().split("\n"):
        if line.strip():
            start, end = map(int, line.split("-"))
            ranges.append((start, end))

    ranges.sort()

    merged = []
    for start, end in ranges:
        if merged and start <= merged[-1][1] + 1:
            merged[-1] = (merged[-1][0], max(merged[-1][1], end))
        else:
            merged.append((start, end))

    total = 0
    for start, end in merged:
        count = end - start + 1
        total += count

    return total


if __name__ == "__main__":
    with open("inputs/day_5.txt", "r") as f:
        input = f.read()
        result = solve(input)
        print(result)
