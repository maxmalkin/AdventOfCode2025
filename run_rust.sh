#!/bin/bash

cd rust

for day in {1..11}; do
    echo "Day $day Part 1:"
    cargo run --bin day_${day}_part_1
    echo "Day $day Part 2:"
    cargo run --bin day_${day}_part_2
done

echo "Day 12 Part 1:"
cargo run --bin day_12_part_1
