package com.lfl.advent2021.days.day20;

import com.lfl.advent2021.LinesConsumer;
import com.lfl.advent2021.utils.Point;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.map.MutableMap;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class Day20 implements LinesConsumer {
    private static final Function<Point, List<Point>> arounder = point -> Lists.mutable.with(Point.of(point.x() - 1, point.y() - 1))
                                                                                       .with(Point.of(point.x(), point.y() - 1))
                                                                                       .with(Point.of(point.x() + 1, point.y() - 1))
                                                                                       .with(Point.of(point.x() - 1, point.y()))
                                                                                       .with(Point.of(point.x(), point.y()))
                                                                                       .with(Point.of(point.x() + 1, point.y()))
                                                                                       .with(Point.of(point.x() - 1, point.y() + 1))
                                                                                       .with(Point.of(point.x(), point.y() + 1))
                                                                                       .with(Point.of(point.x() + 1, point.y() + 1));

    @Override
    public void consume(List<String> lines) {
        String lineEnhancementAlgorithm = lines.get(0);

        List<String> lines2 = lines.stream()
                                   .skip(2)
                                   .collect(Collectors.toList());

        MutableMap<Point, Pixel> picture = Maps.mutable.empty();

        for (int y = 0; y < lines2.size(); y++) {
            String line = lines2.get(y);
            for (int x = 0; x < line.length(); x++) {
                picture.put(Point.of(x, y), Pixel.of("" + line.charAt(x)));
            }
        }
        int turnMax = 50;
        for (int turn = 0; turn < turnMax; turn++) {

            Pixel Default = turn % 2 == 0 ? Pixel.DARK : Pixel.LIGHT;
            MutableMap<Point, Pixel> picture2 = Maps.mutable.empty();
            int xMin = picture.keySet().stream().mapToInt(Point::x).min().getAsInt();
            int xMax = picture.keySet().stream().mapToInt(Point::x).max().getAsInt();
            int yMin = picture.keySet().stream().mapToInt(Point::y).min().getAsInt();
            int yMax = picture.keySet().stream().mapToInt(Point::y).max().getAsInt();

            for (int y = yMin - 2; y <= yMax + 2; y++) {
                for (int x = xMin - 2; x <= xMax + 2; x++) {
                    Point point = Point.of(x, y);
                    //System.out.print(picture.getOrDefault(point, Default).getCode());

                    String binary = arounder.apply(point)
                                            .stream()
                                            .map(p -> picture.getOrDefault(p, Default))
                                            .map(Pixel::getBinary)
                                            .collect(Collectors.joining());

                    int index = Integer.parseInt(binary, 2);

                    picture2.put(point, Pixel.of("" + lineEnhancementAlgorithm.charAt(index)));
                }
                //System.out.println();
            }
           //System.out.println();

            picture.clear();
            picture.putAll(picture2);
        }

        long count = picture.values().stream().filter(Pixel.LIGHT::equals).count();
        System.out.println("count = " + count);
    }

    @Getter
    private enum Pixel {
        LIGHT("#", "1"),
        DARK(".", "0");

        private final String code;
        private final String binary;

        Pixel(String code, String binary) {
            this.code = code;
            this.binary = binary;
        }

        public static Pixel of(String code) {
            return Arrays.stream(Pixel.values())
                         .filter(p -> p.code.equals(code))
                         .findAny().get();
        }
    }
}
