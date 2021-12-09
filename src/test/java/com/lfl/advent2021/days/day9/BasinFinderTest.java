package com.lfl.advent2021.days.day9;

import org.junit.jupiter.api.Test;

class BasinFinderTest {

    @Test
    void name() {
        String lines = """
                       2199943210
                       3987894921
                       9856789892
                       8767896789
                       9899965678
                       """;

        BasinFinder basinFinder = new BasinFinder();
        basinFinder.consume(lines.lines().toList());
    }
}