package com.lfl.advent2021.days.day6;

import com.lfl.advent2021.LinesConsumer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.map.primitive.MutableIntLongMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.eclipse.collections.impl.factory.primitive.IntLongMaps;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class LanternfishPopulationSimulator implements LinesConsumer {
    @Override
    public void consume(List<String> lines) {
        MutableIntList day0 = Arrays.stream(lines.get(0).split(","))
                                    .mapToInt(Integer::parseInt)
                                    .collect(IntLists.mutable::empty,
                                             MutableIntList::add,
                                             MutableIntList::addAll);

        LanternfishPopulation lanternfishPopulation = LanternfishPopulation.of(day0);

        int turn = 256;

        for (int day = 0; day < turn; day++) {
            lanternfishPopulation.passADay();
        }

        log.info("Total population = {}", lanternfishPopulation.getTotalPopulation());

    }

    private static class LanternfishPopulation {
        @Getter
        private final MutableIntLongMap population = IntLongMaps.mutable.from(
                Lists.mutable.of(0, 1, 2, 3, 4, 5, 6, 7, 8),
                i -> i,
                i -> 0
                                                                             );

        public static LanternfishPopulation of(MutableIntList day0) {
            LanternfishPopulation lanternfishPopulation = new LanternfishPopulation();
            MutableIntLongMap population = lanternfishPopulation.population;
            day0.forEach(age -> population.put(age, population.get(age) + 1));
            return lanternfishPopulation;
        }

        public void passADay() {
            long zeroPopulation = population.get(0);
            population.reject((key, value) -> key == 0)
                      .forEachKeyValue((key, value) -> population.put(key - 1, value));
            population.put(8, zeroPopulation);
            population.put(6, population.get(6) + zeroPopulation);
        }

        public long getTotalPopulation() {
            return population.values().sum();
        }
    }
}
