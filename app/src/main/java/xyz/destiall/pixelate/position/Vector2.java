package xyz.destiall.pixelate.position;

import androidx.annotation.NonNull;

import java.util.Objects;

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

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vector2 other) {
        this.x = other.x;
        this.y = other.y;
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

    public Vector2 normalized() {
        if (isZero()) return this;
        this.x = x / length();
        this.y = y / length();
        return this;
    }

    public Vector2 normalize() {
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
        Vector2 vector2 = (Vector2) o;
        return Double.compare(vector2.x, x) == 0 && Double.compare(vector2.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
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
