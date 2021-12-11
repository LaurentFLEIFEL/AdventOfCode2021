package com.lfl.advent2021.days.day11;

import org.junit.jupiter.api.Test;

class Day11Test {

    @Test
    void name() {
        String lines = """
                       5483143223
                       2745854711
                       5264556173
                       6141336146
                       6357385478
                       4167524645
                       2176841721
                       6882881134
                       4846848554
                       5283751526
                       """;

        Day11 day11 = new Day11();
        day11.consume(lines.lines().toList());
    }
}