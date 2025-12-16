#!/bin/bash

cd go

for day in {1..11}; do
    echo "Day $day Part 1:"
    go run day_$day/part_1.go
    echo "Day $day Part 2:"
    go run day_$day/part_2.go
done

echo "Day 12 Part 1:"
go run day_12/part_1.go
