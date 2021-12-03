package com.lfl.advent2021.days.day3;

import com.lfl.advent2021.LinesConsumer;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.block.factory.Predicates;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import static org.eclipse.collections.impl.collector.Collectors2.toList;

@Slf4j
@Service
public class Diagnostician implements LinesConsumer {

    @Override
    public void consume(List<String> lines) {
        MutableList<MutableIntList> binaryNumbers = lines.stream()
                                                         .map(line -> Arrays.stream(line.split(""))
                                                                            .mapToInt(Integer::parseInt)
                                                                            .collect(IntLists.mutable::empty,
                                                                                     MutableIntList::add,
                                                                                     MutableIntList::addAll))
                                                         .collect(toList());

        PowerConsumptionDiagnostician powerConsumptionDiagnostician = new PowerConsumptionDiagnostician(binaryNumbers);

        long gammaRate = powerConsumptionDiagnostician.getGammaRate();
        long epsilonRate = powerConsumptionDiagnostician.getEpsilonRate();
        log.info("Gamma rate = {}", gammaRate);
        log.info("Epsilon rate = {}", epsilonRate);
        log.info("Power consumption = {}", gammaRate * epsilonRate);

        LifeSupportRater lifeSupportRater = new LifeSupportRater(binaryNumbers);

        long oxygenGeneratorRating = lifeSupportRater.oxygenGeneratorRating();
        long co2ScrubberRating = lifeSupportRater.co2ScrubberRating();
        log.info("Oxygen generator rating = {}", oxygenGeneratorRating);
        log.info("CO2 scrubber rating = {}", co2ScrubberRating);
        log.info("Life support rating= {}", oxygenGeneratorRating * co2ScrubberRating);
    }

    private static class PowerConsumptionDiagnostician {

        private final MutableIntList oneCounters;
        private final int binaryNumberSize;
        private final int numbersSize;

        private PowerConsumptionDiagnostician(MutableList<MutableIntList> binaryNumbers) {
            binaryNumberSize = binaryNumbers.get(0).size();
            numbersSize = binaryNumbers.size();
            oneCounters = IntArrayList.newWithNValues(binaryNumberSize, 0);
            binaryNumbers.forEach(number -> IntStream.range(0, binaryNumberSize)
                                                     .filter(index -> number.get(index) == 1)
                                                     .forEach(index -> oneCounters.set(index, oneCounters.get(index) + 1)));
        }

        public long getGammaRate() {
            return findRating(0);
        }

        public long getEpsilonRate() {
            return findRating(1);
        }

        private long findRating(int baseValue) {
            MutableIntList rating = IntArrayList.newWithNValues(binaryNumberSize, baseValue);
            IntStream.range(0, rating.size())
                     .filter(index -> (oneCounters.get(index) > numbersSize - oneCounters.get(index)))
                     .forEach(index -> rating.set(index, 1 - baseValue));
            return convertToLong(rating);
        }
    }

    private record LifeSupportRater(MutableList<MutableIntList> binaryNumbers) {

        public long oxygenGeneratorRating() {
            return findRating(buildCriteria(1));
        }

        public long co2ScrubberRating() {
            return findRating(buildCriteria(0));
        }

        private static BiFunction<Integer, MutableList<MutableIntList>, Predicate<? super MutableIntList>> buildCriteria(int equalityValue) {
            return (index, selected) -> list -> {
                int sumOfOnes = selected.stream()
                                        .mapToInt(number -> number.get(index))
                                        .sum();

                if (sumOfOnes >= selected.size() - sumOfOnes) {
                    return list.get(index) == equalityValue;
                }
                return list.get(index) == (1 - equalityValue);
            };
        }

        private long findRating(BiFunction<Integer, MutableList<MutableIntList>, Predicate<? super MutableIntList>> bitCriteria) {
            MutableList<MutableIntList> selected = binaryNumbers.select(Predicates.alwaysTrue());

            int index = 0;
            while (selected.size() > 1) {
                selected = selected.select(bitCriteria.apply(index, selected));
                index++;
            }

            return convertToLong(selected.get(0));
        }
    }

    private static long convertToLong(MutableIntList number) {
        BitSet bitSet = new BitSet();
        number.reverseThis();
        IntStream.range(0, number.size())
                 .filter(index -> number.get(index) == 1)
                 .forEach(bitSet::set);
        return bitSet.toLongArray()[0];
    }
}
