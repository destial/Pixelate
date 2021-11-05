package xyz.destiall.pixelate.position;

import androidx.annotation.NonNull;

public class Vector2 implements Cloneable {
    private double x;
    private double y;

    public Vector2() {}

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double dot(Vector2 other) {
        if (isZero()) return 0;
        return (x * other.x) + (y * other.y);
    }

    public Vector2 add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2 add(Vector2 other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Vector2 subtract(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2 subtract(Vector2 other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    public Vector2 divide(double n) {
        if (n == 0d) return this;
        x /= n;
        y /= n;
        return this;
    }


    public Vector2 multiply(double n) {
        x *= n;
        y *= n;
        return this;
    }

    public Vector2 inverse() {
        x = -x;
        y = -y;
        return this;
    }

    public boolean isZero() {
        return x == 0d && y == 0d;
    }

    public double length() {
        if (isZero()) return 0d;
        return Math.sqrt((x * x) + (y * y));
    }

    public Vector2 normalise() {
        if (isZero()) return this;
        this.x = x / length();
        this.y = y / length();
        return this;
    }

    public void setZero() {
        x = 0;
        y = 0;
    }

    @NonNull
    public Vector2 clone() {
        return new Vector2(x, y);
    }


    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public static double lengthOf(double x, double y) {
        return Math.sqrt((x * x) + (y * y));
    }
}
