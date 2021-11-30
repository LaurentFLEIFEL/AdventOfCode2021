package com.lfl.advent2021.utils;

record Point4(int x, int y, int z, int t) {
    public static final Point4 ZERO = Point4.of(0, 0, 0, 0);

    public static Point4 of(int x, int y, int z, int t) {
        return new Point4(x, y, z, t);
    }

    public int distance1(Point4 other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z) + Math.abs(t - other.t);
    }

    public int module1() {
        return distance1(ZERO);
    }

    public double distance2(Point4 other) {
        return Math.sqrt(Math.pow(x - other.x, 2d) + Math.pow(y - other.y, 2d) + Math.pow(z - other.z, 2d) + Math.pow(t - other.t, 2d));
    }

    public double module2() {
        return distance2(ZERO);
    }

    public Point4 translate(Point4 origin) {
        return Point4.of(this.x - origin.x, this.y - origin.y, this.z - origin.z, this.t - origin.t);
    }
}