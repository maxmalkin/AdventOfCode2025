#!/bin/bash

cd typescript

for day in {1..11}; do
    echo "Day $day Part 1:"
    npx ts-node day_$day/part_1.ts
    echo "Day $day Part 2:"
    npx ts-node day_$day/part_2.ts
done

echo "Day 12 Part 1:"
npx ts-node day_12/part_1.ts
