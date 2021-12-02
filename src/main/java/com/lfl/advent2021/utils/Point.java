package com.lfl.advent2021.utils;

import org.apache.commons.math3.complex.Complex;

public record Point(int x, int y) {
    public static final Point ZERO = Point.of(0, 0);

    public static Point of(int x, int y) {
        return new Point(x, y);
    }

    public int distance1(Point other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }

    public int module1() {
        return distance1(ZERO);
    }

    public double distance2(Point other) {
        return Math.sqrt(Math.pow(x - other.x, 2d) + Math.pow(y - other.y, 2d));
    }

    public double module2() {
        return distance2(ZERO);
    }

    public double argument(Point origin) {
        Point t = this.translate(origin);
        if (t.x == 0 && t.y < 0) {
            return 0d;
        }
        Complex complex = new Complex(t.x, -t.y);
        double argument = complex.getArgument() - Math.PI / 2d;
        if (argument < 0) {
            argument += 2d * Math.PI;
        }
        return 2d * Math.PI - argument;
    }

    public Point translate(Point origin) {
        return Point.of(this.x - origin.x, this.y - origin.y);
    }
}