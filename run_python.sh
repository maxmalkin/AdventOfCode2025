#!/bin/bash

cd python

for day in {1..11}; do
    echo "Day $day Part 1:"
    python3 day_$day/part_1.py
    echo "Day $day Part 2:"
    python3 day_$day/part_2.py
done

echo "Day 12 Part 1:"
python3 day_12/part_1.py
