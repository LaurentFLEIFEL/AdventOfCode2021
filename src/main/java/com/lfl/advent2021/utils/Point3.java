package com.lfl.advent2021.utils;

record Point3(int x, int y, int z) {
    public static final Point3 ZERO = Point3.of(0, 0, 0);

    public static Point3 of(int x, int y, int z) {
        return new Point3(x, y, z);
    }

    public int distance1(Point3 other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z);
    }

    public int module1() {
        return distance1(ZERO);
    }

    public double distance2(Point3 other) {
        return Math.sqrt(Math.pow(x - other.x, 2d) + Math.pow(y - other.y, 2d) + Math.pow(z - other.z, 2d));
    }

    public double module2() {
        return distance2(ZERO);
    }

    public Point3 translate(Point3 origin) {
        return Point3.of(this.x - origin.x, this.y - origin.y, this.z - origin.z);
    }
}