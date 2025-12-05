def solve(input: str) -> int:
    sections = input.strip().split("\n\n")

    ranges = []
    for line in sections[0].strip().split("\n"):
        if line.strip():
            start, end = map(int, line.split("-"))
            ranges.append((start, end))

    ingredient_ids = []
    for line in sections[1].strip().split("\n"):
        line = line.strip()
        if line and line.isdigit():
            ingredient_ids.append(int(line))

    fresh_count = 0
    for id in ingredient_ids:
        is_fresh = False
        for start, end in ranges:
            if start <= id <= end:
                is_fresh = True
                break
        if is_fresh:
            fresh_count += 1

    return fresh_count


if __name__ == "__main__":
    with open("inputs/day_5.txt", "r") as f:
        input = f.read()
        result = solve(input)
        print(result)
