package xyz.destiall.pixelate.pathfinding.algorithms;

import java.util.LinkedList;

import xyz.destiall.pixelate.position.Location;

public abstract class Algorithm {
    protected LinkedList<Location> path;

    public Algorithm() {
        path = new LinkedList<>();
    }

    public abstract LinkedList<Location> findNewPath(Location start, Location end);

    public boolean pathExists() {
        return (path.size() > 0);
    }

    public void clearPath() {
        path.clear();
    }

    public LinkedList<Location> getCurrentPath() {
        return path;
    }
}
