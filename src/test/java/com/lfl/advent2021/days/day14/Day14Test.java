package com.lfl.advent2021.days.day14;

import org.junit.jupiter.api.Test;

class Day14Test {

    @Test
    void name() {
        String lines = """
                       NNCB
                                              
                       CH -> B
                       HH -> N
                       CB -> H
                       NH -> C
                       HB -> C
                       HC -> B
                       HN -> C
                       NN -> C
                       BH -> H
                       NC -> B
                       NB -> B
                       BN -> B
                       BB -> N
                       BC -> B
                       CC -> N
                       CN -> C
                       """;

        Day14 day14 = new Day14();
        day14.consume(lines.lines().toList());
    }

    @Test
    void name2() {
        String lines = """
                       NN
                                              
                       CH -> B
                       HH -> N
                       CB -> H
                       NH -> C
                       HB -> C
                       HC -> B
                       HN -> C
                       NN -> C
                       BH -> H
                       NC -> B
                       NB -> B
                       BN -> B
                       BB -> N
                       BC -> B
                       CC -> N
                       CN -> C
                       """;

        Day14 day14 = new Day14();
        day14.consume(lines.lines().toList());
    }
}