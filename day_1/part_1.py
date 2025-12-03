def solve(input: str) -> int:
    rotations = [line.strip() for line in input.strip().split("\n") if line.strip()]

    position = 50
    zeros = 0

    for r in rotations:
        direction = r[0]
        distance = int(r[1:])

        if direction == "L":
            position = (position - distance) % 100
        else:
            position = (position + distance) % 100

        if position == 0:
            zeros += 1

    return zeros


if __name__ == "__main__":
    result = solve(" ")
    print(result)
