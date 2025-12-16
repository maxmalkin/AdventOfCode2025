#!/bin/bash

cd java

for day in {1..11}; do
    echo "Day $day Part 1:"
    javac day_$day/Part1.java && java -cp day_$day Part1
    echo "Day $day Part 2:"
    javac day_$day/Part2.java && java -cp day_$day Part2
done

echo "Day 12 Part 1:"
javac day_12/Part1.java && java -cp day_12 Part1
