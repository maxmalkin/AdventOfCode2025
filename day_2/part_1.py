def is_invalid_id(n: int) -> bool:
    s = str(n)
    length = len(s)

    if length % 2 != 0:
        return False

    if s[0] == "0":
        return False

    mid = length // 2
    return s[:mid] == s[mid:]


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
