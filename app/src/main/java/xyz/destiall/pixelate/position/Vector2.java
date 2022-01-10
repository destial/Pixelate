package xyz.destiall.pixelate.position;

import androidx.annotation.NonNull;

import xyz.destiall.pixelate.errors.DivideByZeroException;

/**
 * Written by Rance
 */
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

    public Vector2 setX(double x) {
        this.x = x;
        return this;
    }

    public Vector2 setY(double y) {
        this.y = y;
        return this;
    }

    public Vector2 set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2 set(Vector2 other) {
        x = other.x;
        y = other.y;
        return this;
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
        x += other.x;
        y += other.y;
        return this;
    }

    public Vector2 subtract(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2 subtract(Vector2 other) {
        x -= other.x;
        y -= other.y;
        return this;
    }

    public Vector2 divide(double n) throws DivideByZeroException {
        if (n == 0d) throw new DivideByZeroException();
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

    public double distance(Vector2 other) {
        return distance(other.x, other.y);
    }

    public double distance(double x, double y) {
        return lengthOf(this.x - x, this.y - y);
    }

    public double distanceSquared(Vector2 other) {
        return distanceSquared(other.getX(), other.getY());
    }

    public double distanceSquared(double x, double y) {
        return Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2);
    }

    public boolean isZero() {
        return x == 0d && y == 0d;
    }

    public double length() {
        if (isZero()) return 0d;
        return lengthOf(x, y);
    }

    public Vector2 normalize() throws DivideByZeroException {
        if (isZero()) throw new DivideByZeroException();
        this.x = x / length();
        this.y = y / length();
        return this;
    }

    public Vector2 getNormal() {
        if (isZero()) return new Vector2(0, 0);
        return new Vector2(x / length(), y / length());
    }

    public void setZero() {
        x = 0;
        y = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 vector = (Vector2) o;
        return vector.x == x && vector.y == y;
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
