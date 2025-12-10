import re

from pulp import *


def solve_machine(buttons, joltages):
    n_buttons = len(buttons)
    n_counters = len(joltages)

    prob = LpProblem("MinimizeButtonPresses", LpMinimize)

    x = [LpVariable(f"button_{i}", lowBound=0, cat="Integer") for i in range(n_buttons)]

    prob += lpSum(x)

    for counter_idx in range(n_counters):
        counter_sum = lpSum(
            [
                x[btn_idx]
                for btn_idx, button in enumerate(buttons)
                if counter_idx in button
            ]
        )
        prob += counter_sum == joltages[counter_idx]

    prob.solve(PULP_CBC_CMD(msg=0))

    if prob.status == 1:  # Optimal solution found
        return int(sum([v.varValue for v in x]))

    return -1


def solve(input: str) -> int:
    lines = [line.strip() for line in input.strip().split("\n") if line.strip()]

    total_presses = 0

    for line in lines:
        buttons = []
        for match in re.finditer(r"\(([0-9,]+)\)", line):
            button = [int(x) for x in match.group(1).split(",")]
            buttons.append(button)

        joltage_match = re.search(r"\{([0-9,]+)\}", line)
        joltages = [int(x) for x in joltage_match.group(1).split(",")]

        min_presses = solve_machine(buttons, joltages)
        total_presses += min_presses

    return total_presses


if __name__ == "__main__":
    with open("inputs/day_10.txt", "r") as f:
        input_data = f.read()
        result = solve(input_data)
        print(result)
