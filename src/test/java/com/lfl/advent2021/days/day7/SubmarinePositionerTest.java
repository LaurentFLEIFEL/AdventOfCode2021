package com.lfl.advent2021.days.day7;

import org.junit.jupiter.api.Test;

class SubmarinePositionerTest {

    @Test
    void name() {
        String lines = """
                       16,1,2,0,4,2,7,1,2,14
                       """;

        SubmarinePositioner submarinePositioner = new SubmarinePositioner();
        submarinePositioner.consume(lines.lines().toList());
    }
}