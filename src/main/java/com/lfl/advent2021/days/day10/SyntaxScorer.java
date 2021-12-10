package com.lfl.advent2021.days.day10;

import com.lfl.advent2021.LinesConsumer;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.map.primitive.ImmutableCharCharMap;
import org.eclipse.collections.api.map.primitive.ImmutableCharIntMap;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.primitive.CharCharMaps;
import org.eclipse.collections.impl.factory.primitive.CharIntMaps;
import org.eclipse.collections.impl.tuple.Tuples;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class SyntaxScorer implements LinesConsumer {

    private static final ImmutableCharCharMap characterMap = CharCharMaps.immutable.with('<', '>')
                                                                                   .newWithKeyValue('[', ']')
                                                                                   .newWithKeyValue('(', ')')
                                                                                   .newWithKeyValue('{', '}');

    private static final ImmutableCharIntMap corruptionScoreMap = CharIntMaps.immutable.with('>', 25137)
                                                                                       .newWithKeyValue(']', 57)
                                                                                       .newWithKeyValue(')', 3)
                                                                                       .newWithKeyValue('}', 1197);

    private static final ImmutableCharIntMap completionScoreMap = CharIntMaps.immutable.with('>', 4)
                                                                                       .newWithKeyValue(']', 2)
                                                                                       .newWithKeyValue(')', 1)
                                                                                       .newWithKeyValue('}', 3);

    @Override
    public void consume(List<String> lines) {

        List<Pair<Integer, Long>> scores = lines.stream()
                                                .map(this::findScores)
                                                .collect(toList());

        int corruptionScore = scores.stream()
                                    .mapToInt(Pair::getOne)
                                    .sum();

        log.info("Corruption score = {}", corruptionScore);

        long[] completionScores = scores.stream()
                                        .mapToLong(Pair::getTwo)
                                        .filter(i -> i != 0)
                                        .sorted()
                                        .toArray();

        log.info("Completion score = {}", completionScores[completionScores.length / 2]);
    }

    private Pair<Integer, Long> findScores(String line) {
        Deque<Character> stack = new ArrayDeque<>(line.length());
        int corruptionScore = 0;

        for (int index = 0; index < line.length(); index++) {
            char c = line.charAt(index);
            if (characterMap.keySet().contains(c)) {
                stack.add(c);
                continue;
            }

            if (stack.isEmpty()) {
                throw new IllegalStateException("Closing character found while empty stack: " + c);
            }
            char last = stack.getLast();
            char expected = characterMap.get(last);

            if (c == expected) {
                stack.removeLast();
                continue;
            }

            //corrupted, empty stack
            log.debug("Expected {}, but found {} instead.", expected, c);
            corruptionScore += corruptionScoreMap.get(c);
            stack.clear();
            break;
        }
        long completionScore = 0;

        while (!stack.isEmpty()) {
            completionScore *= 5L;
            completionScore += completionScoreMap.get(characterMap.get(stack.removeLast()));
        }

        return Tuples.pair(corruptionScore, completionScore);
    }
}
