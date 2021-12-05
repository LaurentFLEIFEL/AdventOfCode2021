package com.lfl.advent2021.days.day5;

import com.lfl.advent2021.LinesConsumer;
import com.lfl.advent2021.utils.Point;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.list.Interval;
import org.springframework.stereotype.Service;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static org.eclipse.collections.impl.collector.Collectors2.toSet;

@Slf4j
@Service
public class HydrothermalVentDetector implements LinesConsumer {
    @Override
    public void consume(List<String> lines) {
        MutableSet<Segment> segments = lines.stream()
                                            .map(Segment::of)
                                            .collect(toSet());
        log.info("Parsing finished");

        IntSummaryStatistics xSummaryStatistics = segments.stream()
                                                          .flatMap(segment -> Stream.of(segment.getA().x(), segment.getB().x()))
                                                          .mapToInt(i -> i)
                                                          .summaryStatistics();
        IntSummaryStatistics ySummaryStatistics = segments.stream()
                                                          .flatMap(segment -> Stream.of(segment.getA().y(), segment.getB().y()))
                                                          .mapToInt(i -> i)
                                                          .summaryStatistics();
        int minX = xSummaryStatistics.getMin();
        int maxX = xSummaryStatistics.getMax();
        int minY = ySummaryStatistics.getMin();
        int maxY = ySummaryStatistics.getMax();
        log.info("Statistics finished");

        List<List<String>> map = Lists.mutable.ofInitialCapacity(maxY + 1);
        int result = 0;
        for (int y = minY; y < maxY + 1; y++) {
            List<String> row = Lists.mutable.ofInitialCapacity(maxX + 1);
            for (int x = minX; x < maxX + 1; x++) {
                Point point = Point.of(x, y);
                long count = segments.stream()
                                     .filter(segment -> segment.isIncludedPart2(point))
                                     .count();

                if (count > 1) {
                    result++;
                }
                row.add((count == 0) ? "." : "" + count);
            }
            map.add(row);
            log.info("Line {} finished", y);
        }

        String mapToString = map.stream()
                          .map(row -> String.join(" ", row))
                          .collect(Collectors.joining("\n"));

        log.info("Map =\n{}", mapToString);
        log.info("Result = {}", result);
    }

    @Getter
    @EqualsAndHashCode
    private static final class Segment {
        private final Point a;
        private final Point b;
        private final double slope;
        private final Interval xIntervals;
        private final Interval yIntervals;

        private Segment(Point a, Point b) {
            this.a = a;
            this.b = b;

            if (a.x() == b.x()) {
                slope = Double.MAX_VALUE;
            } else {
                slope = ((double) (b.y() - a.y())) / ((double) (b.x() - a.x()));
            }

            xIntervals = Interval.fromTo(Math.min(a.x(), b.x()), Math.max(a.x(), b.x()));
            yIntervals = Interval.fromTo(Math.min(a.y(), b.y()), Math.max(a.y(), b.y()));
        }

        public static Segment of(String line) {
            String[] splitPoints = line.split(" -> ");
            Point a = Point.of(parseInt(splitPoints[0].split(",")[0]),
                               parseInt(splitPoints[0].split(",")[1]));
            Point b = Point.of(parseInt(splitPoints[1].split(",")[0]),
                               parseInt(splitPoints[1].split(",")[1]));
            return new Segment(a, b);
        }

        public boolean isIncludedPart1(Point point) {
            if (a.x() != b.x() && a.y() != b.y()) {
                return false;
            }
            return isInRectangle(point);
        }

        public boolean isIncludedPart2(Point point) {
            if (a.equals(point)) {
                return true;
            }
            //slope must be equal (slope is only, 0, -1, 1 or infinity)
            double slope;
            if (a.x() == point.x()) {
                slope = Double.MAX_VALUE;
            } else {
                slope = ((double) (point.y() - a.y())) / ((double) (point.x() - a.x()));
            }

            if (this.slope != slope) {
                return false;
            }

            return isInRectangle(point);
        }

        //point should be in the rectangle (can be flat) defined by the points
        private boolean isInRectangle(Point point) {
            return xIntervals.contains(point.x()) && yIntervals.contains(point.y());
        }
    }
}
