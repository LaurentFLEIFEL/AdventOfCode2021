package com.lfl.advent2021.days.day11;

import com.lfl.advent2021.LinesConsumer;
import com.lfl.advent2021.utils.Point;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.map.primitive.MutableObjectIntMap;
import org.eclipse.collections.impl.factory.primitive.ObjectIntMaps;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
public class Day11 implements LinesConsumer {
    @Override
    public void consume(List<String> lines) {
        MutableObjectIntMap<Point> map = buildMap(lines);

        int maxStep = 1000;
        int sum100 = 0;
        int stepAllFlashes = -1;

        for (int i = 0; i < maxStep; i++) {
            int flashes = doStep(map);
            if (i < 100) {
                sum100 += flashes;
            }

            if (flashes == 100) {
                stepAllFlashes = i + 1;
                break;
            }
        }

        log.info("sum 100 steps = {}", sum100);
        log.info("Step when all flashes = {}", stepAllFlashes);
    }

    private int doStep(MutableObjectIntMap<Point> map) {
        map.updateValues((point, energy) -> energy + 1);

        int nbOfChange = 1;
        int flashes = 0;
        while (nbOfChange > 0) {
            List<Point> flashingPoints = map.keySet()
                                            .stream()
                                            .filter(point -> map.get(point) > 9)
                                            .toList();

            flashes += flashingPoints.size();

            flashingPoints.forEach(point -> map.put(point, 0));

            List<Point> changedPoints = flashingPoints.stream()
                                                      .map(Direction::getAdjacent)
                                                      .flatMap(Set::stream)
                                                      .filter(map::containsKey)
                                                      .filter(point -> map.get(point) != 0)
                                                      .peek(point -> map.updateValue(point, -2, value -> value + 1))
                                                      .toList();

            nbOfChange = changedPoints.size();

        }

        return flashes;
    }

    private MutableObjectIntMap<Point> buildMap(List<String> lines) {
        MutableObjectIntMap<Point> map = ObjectIntMaps.mutable.empty();
        int y = 0;
        for (String line : lines) {
            List<Integer> rowHeights = Arrays.stream(line.split(""))
                                             .map(Integer::parseInt)
                                             .toList();

            for (int x = 0; x < rowHeights.size(); x++) {
                map.put(Point.of(x, y), rowHeights.get(x));
            }
            y++;
        }
        return map;
    }

    private enum Direction {
        TOP(p -> Point.of(p.x(), p.y() + 1)),
        TOP_RIGHT(p -> Point.of(p.x() + 1, p.y() + 1)),
        RIGHT(p -> Point.of(p.x() + 1, p.y())),
        BOTTOM_RIGHT(p -> Point.of(p.x() + 1, p.y() - 1)),
        BOTTOM(p -> Point.of(p.x(), p.y() - 1)),
        BOTTOM_LEFT(p -> Point.of(p.x() - 1, p.y() - 1)),
        LEFT(p -> Point.of(p.x() - 1, p.y())),
        TOP_LEFT(p -> Point.of(p.x() - 1, p.y() + 1));

        private final Function<Point, Point> directional;

        Direction(Function<Point, Point> directional) {
            this.directional = directional;
        }

        public static Set<Point> getAdjacent(Point p) {
            return Arrays.stream(Direction.values())
                         .map(direction -> direction.directional.apply(p))
                         .collect(toSet());
        }
    }
}
