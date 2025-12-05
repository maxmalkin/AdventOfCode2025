def solve(input: str) -> int:
    banks = [line.strip() for line in input.strip().split("\n") if line.strip()]

    sum = 0

    for b in banks:
        max_joltage = 0

        for i in range(len(b)):
            for j in range(i + 1, len(b)):
                joltage = int(b[i] + b[j])
                max_joltage = max(max_joltage, joltage)

        sum += max_joltage

    return sum


if __name__ == "__main__":
    with open("inputs/day_3.txt", "r") as f:
        input = f.read()
        result = solve(input)
        print(result)
