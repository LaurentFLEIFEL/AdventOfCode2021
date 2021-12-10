package com.lfl.advent2021.days.day10;

import org.junit.jupiter.api.Test;

class SyntaxScorerTest {

    @Test
    void name() {
        String lines = """
                       [({(<(())[]>[[{[]{<()<>>
                       [(()[<>])]({[<{<<[]>>(
                       {([(<{}[<>[]}>{[]{[(<()>
                       (((({<>}<{<{<>}{[]{[]{}
                       [[<[([]))<([[{}[[()]]]
                       [{[{({}]{}}([{[{{{}}([]
                       {<[[]]>}<{[{[{[]{()[[[]
                       [<(<(<(<{}))><([]([]()
                       <{([([[(<>()){}]>(<<{{
                       <{([{{}}[<[[[<>{}]]]>[]]
                       """;

        SyntaxScorer syntaxScorer = new SyntaxScorer();
        syntaxScorer.consume(lines.lines().toList());
    }
}