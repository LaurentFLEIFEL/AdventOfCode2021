package com.lfl.advent2021.days.day1;

import com.lfl.advent2021.LinesConsumer;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.collector.Collectors2;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.eclipse.collections.impl.list.Interval;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SonarInterpreter implements LinesConsumer {
    @Override
    public void consume(List<String> lines) {
        MutableIntList depths = lines.stream()
                                     .collect(Collectors2.collectInt(Integer::parseInt,
                                                                     IntLists.mutable::empty));

        //part 1
        findNumberOfIncreased(depths);

        //part 2
        MutableIntList slidingWindows = Interval.fromTo(2, depths.size() - 1)
                                                .collectInt(index -> depths.get(index) + depths.get(index - 1) + depths.get(index - 2),
                                                            IntLists.mutable.empty());

        findNumberOfIncreased(slidingWindows);
    }

    private void findNumberOfIncreased(MutableIntList measurements) {
        MutableIntList differences = Interval.fromTo(1, measurements.size() - 1)
                                             .collectInt(index -> measurements.get(index) - measurements.get(index - 1),
                                                         IntLists.mutable.empty());

        int size = differences.select(diff -> diff > 0)
                              .size();

        log.info("Number of measurements larger than the previous measurement = {}", size);
    }
}
