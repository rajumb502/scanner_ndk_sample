package com.qatapultt.tryscannerndk.models;

public class Point {
    public final double X;
    public final double Y;

    Point(double x, double y) {
        this.X = x;
        this.Y = y;
    }

    Point(double[] coords) {
        this(coords[0], coords[1]);
    }
}
