package xyz.destiall.pixelate.position;

import xyz.destiall.pixelate.environment.tiles.Tile;

public class AABB {
    private final Vector2 min;
    private final Vector2 max;
    public AABB(double minx, double miny, double maxx, double maxy) {
        min = new Vector2(Math.min(minx, maxx), Math.min(miny, maxy));
        max = new Vector2(Math.max(minx, maxx), Math.max(miny, maxy));
    }

    public void setMaxX(double x) {
        max.setX(x);
    }

    public void setMinX(double x) {
        min.setX(x);
    }

    public void setMaxY(double y) {
        max.setY(y);
    }

    public void setMinY(double y) {
        min.setY(y);
    }

    public void setMin(double x, double y) {
        min.set(x, y);
    }

    public void setMax(double x, double y) {
        max.set(x, y);
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

    public boolean isOverlap(double x, double y) {
        return x >= min.getX() &&
                x <= max.getX() &&
                y >= min.getY() &&
                y <= max.getY();
    }

    public boolean isOverlap(AABB other) {
        return min.getX() <= other.getMax().getX() &&
                max.getX() >= other.getMin().getX() &&
                min.getY() <= other.getMax().getY() &&
                max.getY() >= other.getMin().getY();
    }

    public boolean isOverlap(Tile tile) {
        return min.getX() <= tile.getVector().getX() + Tile.SIZE &&
                max.getX() >= tile.getVector().getX() &&
                min.getY() <= tile.getVector().getY() + Tile.SIZE &&
                max.getY() >= tile.getVector().getY();
    }

    public static boolean isOverlap(int x, int y, Tile tile) {
        return x <= tile.getVector().getX() + Tile.SIZE &&
                x >= tile.getVector().getX() &&
                y <= tile.getVector().getY() + Tile.SIZE &&
                y >= tile.getVector().getY();
    }

    public static boolean isOverlap(Location location, Tile tile) {
        return isOverlap(location.getX(), location.getY(), tile);
    }
}
