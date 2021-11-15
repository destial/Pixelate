package xyz.destiall.pixelate.position;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.tiles.Tile;

public class Location implements Cloneable {
    private double x;
    private double y;
    private World world;
    private final Vector2 vector;
    public Location(double x, double y, World world) {
        this.x = x;
        this.y = y;
        this.world = world;
        vector = new Vector2(x, y);
    }

    public Location(double x, double y) {
        this(x, y, null);
    }

    public Location add(Vector2 vector) {
        x += vector.getX();
        y += vector.getY();
        return this;
    }

    public Location subtract(Vector2 vector2) {
        return add(-vector2.getX(), -vector2.getY());
    }

    public Location subtract(double x, double y) {
        return add(-x, -y);
    }

    public Location add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Location set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public void setWorld(@Nullable World world) {
        this.world = world;
    }

    @Nullable
    public World getWorld() {
        return world;
    }

    public Vector2 toVector() {
        vector.set(x, y);
        return vector;
    }

    public Tile getTile() {
        if (world == null) return null;
        return world.findTile(this);
    }

    public int getX() {
        return (int)x;
    }

    public int getY() {
        return (int)y;
    }

    public double getRawX() {
        return x;
    }

    public double getRawY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Double.compare(location.x, x) == 0 && Double.compare(location.y, y) == 0 && Objects.equals(world, location.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, world, vector);
    }

    @NonNull
    public Location clone() {
        return new Location(x, y, world);
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", world=" + world +
                '}';
    }
}
