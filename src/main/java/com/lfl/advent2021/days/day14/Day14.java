package com.lfl.advent2021.days.day14;

import com.lfl.advent2021.LinesConsumer;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.map.primitive.MutableObjectLongMap;
import org.eclipse.collections.impl.collector.Collectors2;
import org.eclipse.collections.impl.factory.primitive.ObjectLongMaps;
import org.eclipse.collections.impl.utility.ArrayIterate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class Day14 implements LinesConsumer {

    private final static int stepMax = 10;

    @Override
    public void consume(List<String> lines) {
        MutableList<String> template = ArrayIterate.collect(lines.get(0).split(""), s -> s);
        MutableMap<String, String> insertionRules = lines.stream()
                                                         .skip(2)
                                                         .map(line -> line.split(" -> "))
                                                         .collect(Collectors2.toMap(split -> split[0],
                                                                                    split -> split[1]));

        MutableObjectLongMap<String> pairs = buildPairs(template);
        pairs = doSteps(insertionRules, pairs);

        MutableObjectLongMap<String> frequencies = computeFrequencies(pairs);

        log.info("frequencies = {}", frequencies);
        log.info("Result = {}", (frequencies.max() - frequencies.min()) / 2L);
    }

    private MutableObjectLongMap<String> buildPairs(MutableList<String> template) {
        MutableObjectLongMap<String> pairs = ObjectLongMaps.mutable.empty();
        for (int index = 0; index < template.size() - 1; index++) {
            pairs.updateValue(template.get(index) + template.get(index + 1), 0, i -> i + 1);
        }
        return pairs;
    }

    private MutableObjectLongMap<String> computeFrequencies(MutableObjectLongMap<String> pairs) {
        MutableObjectLongMap<String> frequencies = ObjectLongMaps.mutable.empty();
        pairs.forEachKeyValue((pair, number) -> {
            frequencies.updateValue(pair.charAt(0) + "", 0, i -> i + number);
            frequencies.updateValue(pair.charAt(1) + "", 0, i -> i + number);
        });
        return frequencies;
    }

    private MutableObjectLongMap<String> doSteps(MutableMap<String, String> insertionRules, MutableObjectLongMap<String> pairs) {
        for (int step = 0; step < stepMax; step++) {
            MutableObjectLongMap<String> pairs2 = ObjectLongMaps.mutable.empty();

            pairs.forEachKeyValue((pair, number) -> {
                String toInsert = insertionRules.get(pair);

                pairs2.updateValue(pair.charAt(0) + toInsert, 0, i -> i + number);
                pairs2.updateValue(toInsert + pair.charAt(1), 0, i -> i + number);
            });

            pairs = pairs2;
        }
        return pairs;
    }

}
