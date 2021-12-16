package com.lfl.advent2021.days.day16;

import org.junit.jupiter.api.Test;

class Day16Test {

    @Test
    void name() {
        String lines = """
                       8A004A801A8002F478
                       """;

        Day16 day16 = new Day16();
        day16.consume(lines.lines().toList());
    }

    @Test
    void name2() {
        String lines = """
                       620080001611562C8802118E34
                       """;

        Day16 day16 = new Day16();
        day16.consume(lines.lines().toList());
    }

    @Test
    void name3() {
        String lines = """
                       C0015000016115A2E0802F182340
                       """;

        Day16 day16 = new Day16();
        day16.consume(lines.lines().toList());
    }

    @Test
    void name4() {
        String lines = """
                       A0016C880162017C3686B18A3D4780
                       """;

        Day16 day16 = new Day16();
        day16.consume(lines.lines().toList());
    }
}