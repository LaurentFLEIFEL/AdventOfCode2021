package com.lfl.advent2021.days.day9;

import com.lfl.advent2021.LinesConsumer;
import com.lfl.advent2021.utils.Point;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.map.primitive.MutableObjectIntMap;
import org.eclipse.collections.impl.factory.primitive.ObjectIntMaps;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
public class BasinFinder implements LinesConsumer {
    @Override
    public void consume(List<String> lines) {
        MutableObjectIntMap<Point> map = buildMap(lines);

        part1(map);

        part2(map);
    }

    private void part2(MutableObjectIntMap<Point> map) {
        Set<Point> lowPoints = findLowPoints(map);

        int result = lowPoints.stream()
                              .mapToInt(point -> findBasinSize(map, point))
                              .boxed()
                              .sorted(Collections.reverseOrder())
                              .limit(3)
                              .mapToInt(i -> i)
                              .reduce((a, b) -> a * b)
                              .getAsInt();

        log.info("Result = {}", result);
    }

    private int findBasinSize(MutableObjectIntMap<Point> map, Point origin) {
        Set<Point> basin = Sets.mutable.of(origin);
        Set<Point> pointToExplore = Sets.mutable.of(origin);

        while (!pointToExplore.isEmpty()) {
            pointToExplore = pointToExplore.stream()
                                           .flatMap(point -> Direction.getAdjacent(point)
                                                                      .stream())
                                           .filter(map::containsKey)
                                           .filter(point -> !basin.contains(point))
                                           .filter(point -> map.get(point) < 9)
                                           .collect(toSet());

            basin.addAll(pointToExplore);
        }

        return basin.size();
    }

    private void part1(MutableObjectIntMap<Point> map) {
        int sum = findLowPoints(map)
                .stream()
                .mapToInt(map::get)
                .map(i -> i + 1)
                .sum();

        log.info("Sum = {}", sum);
    }

    private Set<Point> findLowPoints(MutableObjectIntMap<Point> map) {
        return map.keySet().stream()
                  .filter(point -> Direction.getAdjacent(point)
                                            .stream()
                                            .filter(map::containsKey)
                                            .map(map::get)
                                            .noneMatch(i -> i <= map.get(point)))
                  .collect(toSet());
    }

    private MutableObjectIntMap<Point> buildMap(List<String> lines) {
        MutableObjectIntMap<Point> map = ObjectIntMaps.mutable.empty();
        int y = 0;
        for (String line : lines) {
            List<Integer> rowHeights = Arrays.stream(line.split(""))
                                             .map(Integer::parseInt)
                                             .collect(toList());

            for (int x = 0; x < rowHeights.size(); x++) {
                map.put(Point.of(x, y), rowHeights.get(x));
            }
            y++;
        }
        return map;
    }

    private enum Direction {
        TOP(p -> Point.of(p.x(), p.y() + 1)),
        RIGHT(p -> Point.of(p.x() + 1, p.y())),
        BOTTOM(p -> Point.of(p.x(), p.y() - 1)),
        LEFT(p -> Point.of(p.x() - 1, p.y()));

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
