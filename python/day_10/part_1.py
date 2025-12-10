import collections
import re


def solve(input: str) -> int:
    lines = [line.strip() for line in input.strip().split("\n") if line.strip()]
    total_presses = 0

    for line in lines:
        target_match = re.search(r"\[([.#]+)\]", line)
        if not target_match:
            continue

        target_str = target_match.group(1)
        target = 0
        l = len(target_str)
        for i, ch in enumerate(target_str):
            if ch == "#":
                target |= 1 << i

        button_strs = re.findall(r"\(([\d,\s]+)\)", line)
        buttons = []
        for b_str in button_strs:
            parts = [int(x.strip()) for x in b_str.split(",") if x.strip()]
            b_mask = 0
            for p in parts:
                if p < l:
                    b_mask |= 1 << p
            buttons.append(b_mask)

        buttons = sorted(list(set(buttons)))
        k = len(buttons)

        if l <= 15:
            queue = collections.deque([(0, 0)])
            seen = {0}
            found = False
            best_dist = float("inf")

            while queue:
                curr, dist = queue.popleft()
                if curr == target:
                    best_dist = dist
                    found = True
                    break

                for b in buttons:
                    nxt = curr ^ b
                    if nxt not in seen:
                        seen.add(nxt)
                        queue.append((nxt, dist + 1))

            if found:
                total_presses += best_dist

        else:
            matrix = []
            for r in range(l):
                row_val = 0
                for c in range(k):
                    if (buttons[c] >> r) & 1:
                        row_val |= 1 << c
                if (target >> r) & 1:
                    row_val |= 1 << k
                matrix.append(row_val)

            pivot_row = 0
            pivots = {}
            free_cols = set(range(k))

            for c in range(k):
                pivot = -1
                for r in range(pivot_row, l):
                    if (matrix[r] >> c) & 1:
                        pivot = r
                        break

                if pivot != -1:
                    matrix[pivot_row], matrix[pivot] = matrix[pivot], matrix[pivot_row]
                    pivot_val = matrix[pivot_row]
                    for r in range(l):
                        if r != pivot_row:
                            if (matrix[r] >> c) & 1:
                                matrix[r] ^= pivot_val
                    pivots[pivot_row] = c
                    free_cols.remove(c)
                    pivot_row += 1

            possible = True
            for r in range(pivot_row, l):
                if (matrix[r] >> k) & 1:
                    possible = False
                    break

            if possible:
                free_vars = list(free_cols)
                num_free = len(free_vars)

                pivot_equations = []
                for r in range(pivot_row):
                    base_val = (matrix[r] >> k) & 1
                    dep_mask = 0
                    for i, fv in enumerate(free_vars):
                        if (matrix[r] >> fv) & 1:
                            dep_mask |= 1 << i
                    pivot_equations.append((base_val, dep_mask))

                min_w = float("inf")
                for mask in range(1 << num_free):
                    w = bin(mask).count("1")
                    if w >= min_w:
                        continue

                    curr_w = w
                    for base, dep in pivot_equations:
                        val = base ^ (bin(dep & mask).count("1") & 1)
                        if val:
                            curr_w += 1

                    if curr_w < min_w:
                        min_w = curr_w

                total_presses += min_w

    return total_presses


if __name__ == "__main__":
    with open("inputs/day_10.txt", "r") as f:
        input = f.read()
        result = solve(input)
        print(result)
