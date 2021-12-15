package com.lfl.advent2021.days.day15;

import org.junit.jupiter.api.Test;

class Day15Test {
    @Test
    void name() {
        String lines = """
                       1163751742
                       1381373672
                       2136511328
                       3694931569
                       7463417111
                       1319128137
                       1359912421
                       3125421639
                       1293138521
                       2311944581
                       """;

        Day15 day15 = new Day15();
        day15.consume(lines.lines().toList());
    }
}