package com.lfl.advent2021.days.day12;

import org.junit.jupiter.api.Test;

class Day12Test {

    @Test
    void name() {
        String lines = """
                       start-A
                       start-b
                       A-c
                       A-b
                       b-d
                       A-end
                       b-end
                       """;

        Day12 day12 = new Day12();
        day12.consume(lines.lines().toList());
    }

    @Test
    void name2() {
        String lines = """
                       dc-end
                       HN-start
                       start-kj
                       dc-start
                       dc-HN
                       LN-dc
                       HN-end
                       kj-sa
                       kj-HN
                       kj-dc
                       """;

        Day12 day12 = new Day12();
        day12.consume(lines.lines().toList());
    }

    @Test
    void name3() {
        String lines = """
                       fs-end
                       he-DX
                       fs-he
                       start-DX
                       pj-DX
                       end-zg
                       zg-sl
                       zg-pj
                       pj-he
                       RW-he
                       fs-DX
                       pj-RW
                       zg-RW
                       start-pj
                       he-WI
                       zg-he
                       pj-fs
                       start-RW
                       """;

        Day12 day12 = new Day12();
        day12.consume(lines.lines().toList());
    }
}