package com.lfl.advent2021.days.day13;

import org.junit.jupiter.api.Test;

class Day13Test {

    @Test
    void name() {
        String lines = """
                       6,10
                       0,14
                       9,10
                       0,3
                       10,4
                       4,11
                       6,0
                       6,12
                       4,1
                       0,13
                       10,12
                       3,4
                       3,0
                       8,4
                       1,10
                       2,14
                       8,10
                       9,0
                                              
                       fold along y=7
                       fold along x=5
                       """;

        Day13 day13 = new Day13();
        day13.consume(lines.lines().toList());
    }
}