#!/bin/bash

run_python() {
    cd python
    for day in {1..11}; do
        echo "Day $day Part 1:"
        python3 day_$day/part_1.py
        echo "Day $day Part 2:"
        python3 day_$day/part_2.py
    done
    echo "Day 12 Part 1:"
    python3 day_12/part_1.py
    cd ..
}

run_rust() {
    cd rust
    for day in {1..11}; do
        echo "Day $day Part 1:"
        cargo run --bin day_${day}_part_1
        echo "Day $day Part 2:"
        cargo run --bin day_${day}_part_2
    done
    echo "Day 12 Part 1:"
    cargo run --bin day_12_part_1
    cd ..
}

run_java() {
    cd java
    for day in {1..11}; do
        echo "Day $day Part 1:"
        javac day_$day/Part1.java && java -cp day_$day Part1
        echo "Day $day Part 2:"
        javac day_$day/Part2.java && java -cp day_$day Part2
    done
    echo "Day 12 Part 1:"
    javac day_12/Part1.java && java -cp day_12 Part1
    cd ..
}

run_typescript() {
    cd typescript
    for day in {1..11}; do
        echo "Day $day Part 1:"
        pnpx ts-node day_$day/part_1.ts
        echo "Day $day Part 2:"
        pnpx ts-node day_$day/part_2.ts
    done
    echo "Day 12 Part 1:"
    pnpx ts-node day_12/part_1.ts
    cd ..
}

if [ $# -eq 0 ]; then
    echo "Usage: ./run.sh [--py] [--rs] [--go] [--java] [--ts]"
    exit 1
fi

for arg in "$@"; do
    case $arg in
        --py)
            run_python
            ;;
        --rs)
            run_rust
            ;;
        --go)
            run_go
            ;;
        --java)
            run_java
            ;;
        --ts)
            run_typescript
            ;;
        *)
            echo "error at $arg"
            echo "flags are --py, --rs, --go, --java, --ts"
            exit 1
            ;;
    esac
done
