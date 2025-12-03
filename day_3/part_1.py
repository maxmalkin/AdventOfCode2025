def solve(input: str) -> int:
    banks = [line.strip() for line in input.strip().split("\n") if line.strip()]

    sum = 0

    for bank in banks:
        max_joltage = 0

        for i in range(len(bank)):
            for j in range(i + 1, len(bank)):
                joltage = int(bank[i] + bank[j])
                max_joltage = max(max_joltage, joltage)

        sum += max_joltage

    return sum


if __name__ == "__main__":
    result = solve(" ")
    print(result)
