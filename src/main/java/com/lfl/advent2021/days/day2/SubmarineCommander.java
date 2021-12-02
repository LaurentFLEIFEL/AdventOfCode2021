package com.lfl.advent2021.days.day2;

import com.lfl.advent2021.LinesConsumer;
import com.lfl.advent2021.utils.Point;
import com.lfl.advent2021.utils.Point3;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.list.MutableList;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.eclipse.collections.impl.collector.Collectors2.toList;

@Slf4j
@Service
public class SubmarineCommander implements LinesConsumer {

    @Override
    public void consume(List<String> lines) {
        MutableList<Command> commands = lines.stream()
                                             .map(Command::of)
                                             .collect(toList());

        //part1
        Function<Point, Point> finalCommand = commands.stream()
                                                      .map(command -> (Function<Point, Point>) command::apply)
                                                      .reduce(Function::andThen)
                                                      .get();

        Point destination = finalCommand.apply(Point.ZERO);
        log.info("Final result = {}", destination.x() * -destination.y());

        //part2
        Function<Point3, Point3> finalCommand2 = commands.stream()
                                                         .map(command -> (Function<Point3, Point3>) command::apply2)
                                                         .reduce(Function::andThen)
                                                         .get();

        Point3 destination2 = finalCommand2.apply(Point3.ZERO);
        log.info("Final result = {}", destination2.x() * -destination2.y());
    }

    private static record Command(Direction direction, int quantity) {
        public static Command of(String command) {
            String[] split = command.split(" ");
            return new Command(Direction.of(split[0]), Integer.parseInt(split[1]));
        }

        public Point apply(Point origin) {
            return this.direction.move(origin, this.quantity);
        }

        public Point3 apply2(Point3 origin) {
            return this.direction.move2(origin, this.quantity);
        }
    }

    private enum Direction {
        FORWARD((origin, quantity) -> Point.of(origin.x() + quantity, origin.y()),
                (origin, quantity) -> Point3.of(origin.x() + quantity, origin.y() + origin.z() * quantity, origin.z())),
        DOWN((origin, quantity) -> Point.of(origin.x(), origin.y() - quantity),
             (origin, quantity) -> Point3.of(origin.x(), origin.y(), origin.z() - quantity)),
        UP((origin, quantity) -> Point.of(origin.x(), origin.y() + quantity),
           (origin, quantity) -> Point3.of(origin.x(), origin.y(), origin.z() + quantity));

        private final BiFunction<Point, Integer, Point> mover;
        private final BiFunction<Point3, Integer, Point3> mover2;

        Direction(BiFunction<Point, Integer, Point> mover,
                  BiFunction<Point3, Integer, Point3> mover2) {
            this.mover = mover;
            this.mover2 = mover2;
        }

        public static Direction of(String direction) {
            return Direction.valueOf(direction.toUpperCase());
        }

        public Point move(Point origin, int quantity) {
            return this.mover.apply(origin, quantity);
        }

        public Point3 move2(Point3 origin, int quantity) {
            return this.mover2.apply(origin, quantity);
        }
    }
}
