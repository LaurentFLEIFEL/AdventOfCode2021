package com.lfl.advent2021.days.day3;

import org.junit.jupiter.api.Test;

class DiagnosticianTest {

    @Test
    void smallTest() {
        String lines = """
                       00100
                       11110
                       10110
                       10111
                       10101
                       01111
                       00111
                       11100
                       10000
                       11001
                       00010
                       01010
                       """;

        Diagnostician diagnostician = new Diagnostician();
        diagnostician.consume(lines.lines().toList());
    }
}