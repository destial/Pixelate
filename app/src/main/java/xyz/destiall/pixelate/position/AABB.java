package xyz.destiall.pixelate.position;

import xyz.destiall.pixelate.environment.tiles.Tile;

public class AABB {
    private final Vector2 min;
    private final Vector2 max;
    public AABB(double minx, double miny, double maxx, double maxy) {
        min = new Vector2(minx, miny);
        max = new Vector2(maxx, maxy);
    }

    public Vector2 getMax() {
        return max;
    }

    public Vector2 getMin() {
        return min;
    }

    public double getWidth() {
        return max.getX() - min.getX();
    }

    public double getHeight() {
        return max.getY() - min.getY();
    }

    public boolean isAABB(double x, double y) {
        return x > min.getX() &&
                x < max.getX() &&
                y > min.getY() &&
                y < max.getY();
    }

    public boolean isAABB(Tile tile) {
        return getMin().getX() < tile.getLocation().getX() + Tile.SIZE &&
                getMax().getX() > tile.getLocation().getX() &&
                getMin().getY() < tile.getLocation().getY() + Tile.SIZE &&
                getMax().getY() > tile.getLocation().getY();
    }

    public static boolean isAABB(int x, int y, Tile tile) {
        return x < tile.getLocation().getX() + Tile.SIZE &&
                x > tile.getLocation().getX() &&
                y < tile.getLocation().getY() + Tile.SIZE &&
                y > tile.getLocation().getY();
    }

    public static boolean isAABB(Location location, Tile tile) {
        return isAABB(location.getX(), location.getY(), tile);
    }
}
