def solve(input: str) -> int:
    lines = input.strip().split("\n")

    operations_line = lines[-1]
    number_lines = lines[:-1]

    max_width = max(len(line) for line in lines)

    number_lines = [line.ljust(max_width) for line in number_lines]
    operations_line = operations_line.ljust(max_width)

    separator_columns = []
    for col in range(max_width):
        is_separator = True
        for line in number_lines:
            if col < len(line) and line[col] != " ":
                is_separator = False
                break
        if col < len(operations_line) and operations_line[col] != " ":
            is_separator = False

        if is_separator:
            separator_columns.append(col)

    boundaries = [0] + separator_columns + [max_width]

    problems = []
    for i in range(len(boundaries) - 1):
        start = boundaries[i]
        end = boundaries[i + 1]

        operation = None
        for col in range(start, end):
            if operations_line[col] in ["*", "+"]:
                operation = operations_line[col]
                break

        if operation is None:
            continue

        reversed_lines = []
        for line in number_lines:
            segment = line[start:end]
            reversed_segment = segment[::-1]
            reversed_lines.append(reversed_segment)

        numbers = []
        segment_width = end - start

        for col in range(segment_width):
            digits = []
            for reversed_line in reversed_lines:
                if col < len(reversed_line) and reversed_line[col] != " ":
                    digits.append(reversed_line[col])

            if digits:
                num_str = "".join(digits)
                try:
                    numbers.append(int(num_str))
                except:
                    pass

        if numbers:
            problems.append((numbers, operation))

    total = 0

    for numbers, operation in problems:
        if operation == "*":
            result = 1
            for num in numbers:
                result *= num
        else:
            result = sum(numbers)

        total += result

    return total


if __name__ == "__main__":
    with open("inputs/day_6.txt", "r") as f:
        input = f.read()
        result = solve(input)
        print(result)
