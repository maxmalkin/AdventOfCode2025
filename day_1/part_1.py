def solve(input: str) -> int:
    rotations = [line.strip() for line in input.strip().split("\n") if line.strip()]

    position = 50
    zero_count = 0

    for rotation in rotations:
        direction = rotation[0]
        distance = int(rotation[1:])

        if direction == "L":
            position = (position - distance) % 100
        else:  # 'R'
            position = (position + distance) % 100

        if position == 0:
            zero_count += 1

    return zero_count


if __name__ == "__main__":
    result = solve(" ")
    print(result)
