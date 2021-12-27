package xyz.destiall.pixelate.position;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.states.StateGame;

public class Location {
    private double x;
    private double y;
    private String world;
    private final transient Vector2 vector;
    public Location() {
        vector = new Vector2(x, y);
    }

    public Location(double x, double y, World world) {
        this.x = x;
        this.y = y;
        this.world = world != null ? world.getName() : null;
        vector = new Vector2(x, y);
    }

    public Location(double x, double y, String world) {
        this.x = x;
        this.y = y;
        this.world = world;
        vector = new Vector2(x, y);
    }

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
        vector = new Vector2(x, y);
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

    public double distance(Location location) {
        return distance(location.toVector());
    }

    public double distanceSquared(Location location) {
        return toVector().distanceSquared(location.toVector());
    }

    public double distance(Vector2 vector) {
        return toVector().distance(vector);
    }

    public double distance(double x, double y) {
        return toVector().distance(x, y);
    }

    public Location setWorld(@Nullable World world) {
        this.world = world != null ? world.getName() : null;
        return this;
    }

    @Nullable
    public World getWorld() {
        return ((StateGame) Pixelate.getGSM().getCurrentState()).getWorldManager().getWorlds().get(world);
    }

    public Vector2 toVector() {
        vector.set(x, y);
        return vector;
    }

    public Tile getTile() {
        if (world == null) return null;
        return getWorld().findTile(this);
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
        return location.x == x && location.y == y && location.world.equals(world);
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
                ", world=" + (world != null ? getWorld().getName() : null) +
                '}';
    }
}
