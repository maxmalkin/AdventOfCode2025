def is_invalid_id(n: int) -> bool:
    s = str(n)
    length = len(s)

    if s[0] == "0":
        return False

    for pattern_len in range(1, length // 2 + 1):
        if length % pattern_len == 0:
            pattern = s[:pattern_len]
            repeats = length // pattern_len

            if pattern * repeats == s and repeats >= 2:
                return True

    return False


def solve(input: str) -> int:
    ranges = []
    for range_str in input.strip().split(","):
        range_str = range_str.strip()
        if range_str:
            start, end = map(int, range_str.split("-"))
            ranges.append((start, end))

    sum = 0

    for start, end in ranges:
        for i in range(start, end + 1):
            if is_invalid_id(i):
                sum += i

    return sum


if __name__ == "__main__":
    with open("day_2.txt", "r") as f:
        input = f.read()
        result = solve(input)
        print(result)
