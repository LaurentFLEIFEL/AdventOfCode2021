package com.lfl.advent2021.days.day8;

import com.lfl.advent2021.LinesConsumer;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.block.factory.Predicates;
import org.eclipse.collections.impl.factory.primitive.IntObjectMaps;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static org.eclipse.collections.impl.collector.Collectors2.toList;
import static org.eclipse.collections.impl.collector.Collectors2.toSet;

@Slf4j
@Service
public class SevenSignalRecomposer implements LinesConsumer {
    @Override
    public void consume(List<String> lines) {
        long sum = lines.stream()
                        .mapToLong(this::handleSignalPart1)
                        .sum();

        log.info("Sum part 1 = {}", sum);

        long sum2 = lines.stream()
                         .mapToLong(this::handleSignalPart2)
                         .sum();

        log.info("Sum part 2 = {}", sum2);
    }

    private long handleSignalPart1(String line) {
        String[] split = line.split("\\|");
        String[] output = split[1].trim().split(" ");

        return Arrays.stream(output)
                     .filter(s -> s.length() == 2 || s.length() == 4 || s.length() == 3 || s.length() == 7)
                     .count();

    }

    private long handleSignalPart2(String line) {
        String[] split = line.split("\\|");
        MutableSet<String> sevenSignals = Arrays.stream(split[0].trim().split(" "))
                                                .collect(toSet());
        String[] output = split[1].trim().split(" ");

        MutableIntObjectMap<String> digits = IntObjectMaps.mutable.empty();

        digits.put(1, sevenSignals.detect(s -> s.length() == 2));

        digits.put(4, sevenSignals.detect(s -> s.length() == 4));

        digits.put(7, sevenSignals.detect(s -> s.length() == 3));

        digits.put(8, sevenSignals.detect(s -> s.length() == 7));

        digits.put(9, sevenSignals.select(digit -> containsAllOf(digit, digits.get(4)))
                                  .detect(Predicates.not(digits::containsValue)));

        digits.put(0, sevenSignals.select(digit -> containsAllOf(digit, digits.get(1)))
                                  .reject(digits::containsValue)
                                  .detect(digit -> digit.length() == 6));

        digits.put(3, sevenSignals.select(digit -> containsAllOf(digit, digits.get(1)))
                                  .reject(digits::containsValue)
                                  .detect(digit -> digit.length() == 5));

        digits.put(6, sevenSignals.reject(digits::containsValue)
                                  .detect(s -> s.length() == 6));

//        5:      6:      7:      8:      9:
//        aaaa    aaaa    aaaa    aaaa    aaaa
//       b    .  b    .  .    c  b    c  b    c
//       b    .  b    .  .    c  b    c  b    c
//        dddd    dddd    ....    dddd    dddd
//       .    f  e    f  .    f  e    f  .    f
//       .    f  e    f  .    f  e    f  .    f
//        gggg    gggg    ....    gggg    gggg
        int f = digits.get(1).chars()
                      .filter(c -> digits.get(6).chars().anyMatch(c2 -> c == c2))
                      .findAny().getAsInt();

        digits.put(5, sevenSignals.reject(digits::containsValue)
                                  .detect(digit -> digit.chars().anyMatch(c -> c == f)));

        digits.put(2, sevenSignals.detect(Predicates.not(digits::containsValue)));

        List<Integer> outputDigits = Arrays.stream(output)
                                           .map(s -> digits.keySet()
                                                           .select(i -> containsAllOf(s, digits.get(i)))
                                                           .detectIfNone(i -> s.length() == digits.get(i).length(), -1))
                                           .collect(toList());

        long result = 0;

        long multiplier = 1;
        for (int i = outputDigits.size() - 1; i >= 0; i--) {
            result += multiplier * outputDigits.get(i);
            multiplier *= 10;
        }

        return result;
    }

    private boolean containsAllOf(String digit, String s) {
        return s.chars()
                .allMatch(c -> digit.chars()
                                    .anyMatch(c2 -> c2 == c));
    }
}
