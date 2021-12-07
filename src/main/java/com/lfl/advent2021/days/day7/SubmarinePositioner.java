package com.lfl.advent2021.days.day7;

import com.lfl.advent2021.LinesConsumer;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class SubmarinePositioner implements LinesConsumer {
    @Override
    public void consume(List<String> lines) {
        MutableIntList positions = Arrays.stream(lines.get(0).split(","))
                                         .mapToInt(Integer::parseInt)
                                         .collect(IntLists.mutable::empty,
                                                  MutableIntList::add,
                                                  MutableIntList::addAll);

        int minPosition = Integer.MAX_VALUE;
        long minFuel = Long.MAX_VALUE;

        for (int i = positions.min(); i <= positions.max(); i++) {
            int finalI = i;
            long fuel = positions.collectInt(position -> fuelConsumptionPart2(finalI, position), IntLists.mutable.empty())
                                 .sum();

            if (fuel < minFuel) {
                minFuel = fuel;
                minPosition = i;
            }
        }

        log.info("MinFuel={}, minPosition={}", minFuel, minPosition);
    }

    private int fuelConsumptionPart1(int finalI, int position) {
        return Math.abs(position - finalI);
    }

    private int fuelConsumptionPart2(int finalI, int position) {
        int diff = Math.abs(position - finalI);
        return (diff * (diff + 1)) / 2;
    }
}
