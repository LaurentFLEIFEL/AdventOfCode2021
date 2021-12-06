package com.lfl.advent2021.days.day6;

import org.junit.jupiter.api.Test;

class LanternfishPopulationSimulatorTest {

    @Test
    void name() {
        String lines = """
                       3,4,3,1,2
                       """;

        LanternfishPopulationSimulator lanternfishPopulationSimulator = new LanternfishPopulationSimulator();
        lanternfishPopulationSimulator.consume(lines.lines().toList());
    }
}