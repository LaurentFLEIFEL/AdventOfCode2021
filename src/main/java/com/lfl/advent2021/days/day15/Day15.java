package com.lfl.advent2021.days.day15;

import com.lfl.advent2021.LinesConsumer;
import com.lfl.advent2021.utils.Point;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.map.primitive.MutableObjectIntMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.primitive.ObjectIntMaps;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
public class Day15 implements LinesConsumer {

    private MutableObjectIntMap<Point> cave;
    private MutableObjectIntMap<Point> distances;
    private MutableMap<Point, Point> previous;

    @Override
    public void consume(List<String> lines) {
        lines = prepareForPart2(lines);
        log.info("Lines prepared");
        cave = buildCave(lines);

        Point start = Point.ZERO;
        Point end = Point.of(lines.size() - 1, lines.get(lines.size() - 1).length() - 1);

        doDjikstra(start);

        MutableList<Point> path = findPath(start, end);

        int sum = path.stream()
                      .mapToInt(cave::get)
                      .sum();
        log.info("sum = {}", sum);
    }

    private MutableList<Point> findPath(Point start, Point end) {
        MutableList<Point> path = Lists.mutable.withInitialCapacity(cave.size());
        Point p = end;

        while (!p.equals(start)) {
            path.add(p);
            p = previous.get(p);
        }
        return path;
    }

    private void doDjikstra(Point start) {
        previous = Maps.mutable.withInitialCapacity(cave.size());
        distances = ObjectIntMaps.mutable.withInitialCapacity(cave.size());
        MutableSet<Point> visited = Sets.mutable.withInitialCapacity(cave.size());
        MutableSet<Point> toVisit = Sets.mutable.withInitialCapacity(cave.size());
        distances.put(start, 0);
        toVisit.add(start);

        log.info("cave size = {}", cave.size());

        while (visited.size() < cave.size()) {
            if (visited.size() % 1000 == 0) {
                log.info("visited size = {}", visited.size());
                log.info("toVisit size = {}", toVisit.size());
            }

            Point min = findMin(toVisit);
            visited.add(min);
            toVisit.remove(min);
            Direction.getAdjacent(min)
                     .stream()
                     .filter(cave::containsKey)
                     .peek(p -> updateDistances(min, p))
                     .filter(p -> !visited.contains(p))
                     .forEach(toVisit::add);
        }
    }

    private MutableObjectIntMap<Point> buildCave(List<String> lines) {
        MutableObjectIntMap<Point> cave = ObjectIntMaps.mutable.withInitialCapacity(lines.size() * lines.size());

        for (int y = 0; y < lines.size(); y++) {
            for (int x = 0; x < lines.get(y).length(); x++) {
                cave.put(Point.of(x, y), Integer.parseInt(lines.get(y).charAt(x) + ""));
            }
        }
        return cave;
    }

    private List<String> prepareForPart2(List<String> lines) {
        List<String> result = lines.stream()
                                   .map(this::prepareLine)
                                   .collect(Collectors.toList());

        int totalSize = lines.size() * 5;

        for (int index = lines.size(); index < totalSize; index++) {
            String line = result.get(index - lines.size());
            String newLine = Arrays.stream(line.split(""))
                                   .map(Integer::parseInt)
                                   .map(i -> i + 1)
                                   .map(i -> (i == 10) ? 1 : i)
                                   .map(i -> i + "")
                                   .collect(Collectors.joining());
            result.add(newLine);
        }
        return result;
    }

    private String prepareLine(String line) {
        List<Integer> integers = Arrays.stream(line.split(""))
                                       .map(Integer::parseInt)
                                       .toList();
        StringBuilder result = new StringBuilder(line);

        for (int step = 0; step < 4; step++) {
            integers = integers.stream()
                               .map(i -> i + 1)
                               .map(i -> (i == 10) ? 1 : i)
                               .toList();

            result.append(integers.stream()
                                  .map(i -> i + "")
                                  .collect(Collectors.joining()));
        }
        return result.toString();
    }

    private Point findMin(MutableSet<Point> nonVisited) {
        int min = Integer.MAX_VALUE;
        Point result = null;
        for (Point point : nonVisited) {
            if (distances.getIfAbsent(point, Integer.MAX_VALUE) < min) {
                min = distances.get(point);
                result = point;
            }
        }

        return result;
    }

    private void updateDistances(Point p1, Point p2) {
        if (distances.getIfAbsent(p2, Integer.MAX_VALUE) > distances.getIfAbsent(p1, Integer.MAX_VALUE) + cave.get(p2)) {
            distances.put(p2, distances.getIfAbsent(p1, Integer.MAX_VALUE) + cave.get(p2));
            previous.put(p2, p1);
        }
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
