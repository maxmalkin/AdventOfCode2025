def solve(input: str) -> int:
    rotations = [line.strip() for line in input.strip().split("\n") if line.strip()]

    position = 50
    zeros = 0

    for r in rotations:
        direction = r[0]
        distance = int(r[1:])

        if direction == "L":
            new_position = (position - distance) % 100
            loops = distance // 100
            zeros += loops

            remaining = distance % 100
            if position > 0 and remaining >= position:
                zeros += 1
        else:
            new_position = (position + distance) % 100

            loops = distance // 100
            zeros += loops

            remaining = distance % 100
            if remaining > 0 and position + remaining >= 100:
                zeros += 1

        position = new_position

    return zeros


if __name__ == "__main__":
    with open("inputs/day_1.txt", "r") as f:
        input = f.read()
        result = solve(input)
        print(result)
