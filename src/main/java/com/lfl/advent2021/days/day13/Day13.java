package com.lfl.advent2021.days.day13;

import com.lfl.advent2021.LinesConsumer;
import com.lfl.advent2021.utils.Point;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@Slf4j
@Service
public class Day13 implements LinesConsumer {
    @Override
    public void consume(List<String> lines) {
        Set<Point> dots = lines.stream()
                               .takeWhile(not(String::isEmpty))
                               .map(line -> line.split(","))
                               .map(split -> Point.of(Integer.parseInt(split[0]), Integer.parseInt(split[1])))
                               .collect(Collectors.toSet());

        List<Fold> folds = lines.stream()
                                .dropWhile(not(String::isEmpty))
                                .skip(1)
                                .map(line -> line.substring(11))
                                .map(line -> line.split("="))
                                .map(split -> Fold.of(Dimension.of(split[0]), Integer.parseInt(split[1])))
                                .toList();

        fold(dots, folds.get(0));

        log.info("dots size fold 1 = {}", dots.size());

        folds.stream()
             .skip(1)
             .forEach(fold -> fold(dots, fold));

        displayDots(dots);
    }

    private void displayDots(Set<Point> dots) {
        int xMax = dots.stream()
                       .mapToInt(Point::x)
                       .max()
                       .getAsInt();

        int yMax = dots.stream()
                       .mapToInt(Point::y)
                       .max()
                       .getAsInt();

        for (int y = 0; y < yMax + 1; y++) {
            for (int x = 0; x < xMax + 1; x++) {
                Point point = Point.of(x, y);
                if (dots.contains(point)) {
                    System.out.print("#");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    private void fold(Set<Point> dots, Fold fold) {
        Set<Point> outsideDots = dots.stream()
                                     .filter(fold::isOutside)
                                     .collect(Collectors.toSet());

        dots.removeAll(outsideDots);

        outsideDots.stream()
                   .map(fold::foldPoint)
                   .forEach(dots::add);
    }

    @ToString
    private static class Fold {
        Dimension dimension;
        int value;

        public static Fold of(Dimension dimension, int value) {
            Fold fold = new Fold();
            fold.dimension = dimension;
            fold.value = value;
            return fold;
        }

        public boolean isOutside(Point point) {
            return dimension.extract(point) > value;
        }

        public Point foldPoint(Point point) {
            int valueToFold = dimension.extract(point);
            int foldedValue = value - (valueToFold - value);

            return dimension.set(point, foldedValue);
        }
    }

    private enum Dimension {
        X(Point::x, (point, i) -> Point.of(i, point.y())),
        Y(Point::y, (point, i) -> Point.of(point.x(), i));

        private final Function<Point, Integer> extractor;
        private final BiFunction<Point, Integer, Point> setter;

        Dimension(Function<Point, Integer> extractor, BiFunction<Point, Integer, Point> setter) {
            this.extractor = extractor;
            this.setter = setter;
        }

        public int extract(Point point) {
            return this.extractor.apply(point);
        }

        public Point set(Point point, int value) {
            return this.setter.apply(point, value);
        }

        private static Dimension of(String name) {
            return Arrays.stream(Dimension.values())
                         .filter(dimension -> dimension.name().equals(name.toUpperCase()))
                         .findAny()
                         .get();
        }
    }
}
