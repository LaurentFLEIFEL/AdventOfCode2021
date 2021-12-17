package com.lfl.advent2021.days.day17;

import com.lfl.advent2021.LinesConsumer;
import com.lfl.advent2021.utils.Point;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.Interval;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class Day17 implements LinesConsumer {
    @Override
    public void consume(List<String> lines) {
        //Interval xTargets = Interval.fromTo(20, 30);
        //Interval yTargets = Interval.fromTo(-10, -5);
        Interval xTargets = Interval.fromTo(124, 174);
        Interval yTargets = Interval.fromTo(-123, -86);

        MutableList<MutableList<Point>> eligiblePath = Lists.mutable.empty();

        for (int vxInitial = 0; vxInitial <= xTargets.max(); vxInitial++) {
            for (int vyInitial = yTargets.min(); vyInitial <= xTargets.max(); vyInitial++) {
                Point position = Point.ZERO;
                Point velocity = Point.of(vxInitial, vyInitial);
                MutableList<Point> path = Lists.mutable.empty();
                boolean isValid = false;
                while (position.x() <= xTargets.max() && position.y() >= yTargets.min()) {
                    path.add(position);
                    position = updatePosition(position, velocity);
                    velocity = updateVelocity(velocity);
                    if (!isValid) {
                        if (xTargets.contains(position.x()) && yTargets.contains(position.y())) {
                            isValid = true;
                        }
                    }
                }

                if (isValid) {
                    eligiblePath.add(path);
                }
            }
        }

        MutableList<Point> result = eligiblePath.max(Comparator.comparing(path -> path.stream()
                                                                                      .mapToInt(Point::y)
                                                                                      .max()
                                                                                      .getAsInt()));

        int highestY = result.stream()
                             .mapToInt(Point::y)
                             .max()
                             .getAsInt();

        log.info("Highest Y = {}", highestY);
        log.info("Number of velocity = {}", eligiblePath.size());
    }

    private Point updatePosition(Point position, Point velocity) {
        return Point.of(position.x() + velocity.x(), position.y() + velocity.y());
    }

    private Point updateVelocity(Point velocity) {
        return Point.of(velocity.x() - Integer.signum(velocity.x()), velocity.y() - 1);
    }
}
