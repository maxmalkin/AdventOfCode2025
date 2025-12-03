def solve(input: str) -> int:
    banks = [line.strip() for line in input.strip().split("\n") if line.strip()]

    sum = 0

    for b in banks:
        n = len(b)
        k = 12
        result = []
        start = 0

        for i in range(k):
            end = n - k + i
            max_digit = max(b[start : end + 1])

            for j in range(start, end + 1):
                if b[j] == max_digit:
                    result.append(max_digit)
                    start = j + 1
                    break

        joltage = int("".join(result))
        sum += joltage

    return sum


if __name__ == "__main__":
    with open("inputs/day_3.txt", "r") as f:
        input = f.read()
        result = solve(input)
        print(result)
