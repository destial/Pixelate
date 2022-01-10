package xyz.destiall.pixelate.modules;

import java.util.LinkedList;

import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.modular.Component;
import xyz.destiall.pixelate.pathfinding.algorithms.Algorithm;
import xyz.destiall.pixelate.position.Location;

/**
 * Written by Yong Hong
 */
public class PathFindingModule implements Component<Entity> {
    private transient Entity self;
    private Long previousUpdate = System.currentTimeMillis();
    private double pathUpdateFrequency;
    private Algorithm algo;

    public PathFindingModule() {}

    public PathFindingModule(Entity entity, Algorithm algo, double pathUpdateFrequency) {
        this.self = entity;
        this.pathUpdateFrequency = pathUpdateFrequency;
        previousUpdate -= (long) (pathUpdateFrequency * 1000);
        this.algo = algo;
    }

    public LinkedList<Location> getPath(Location end) {
        if (previousUpdate + (long) (pathUpdateFrequency * 1000) < System.currentTimeMillis())
            return algo.findNewPath(self.getLocation(), end);
        else
            return algo.getCurrentPath();
    }

    @Override
    public void setParent(Entity self) {
        this.self = self;
    }

    @Override
    public Entity getParent() {
        return self;
    }

    @Override
    public void update() {}

    @Override
    public void destroy() {}
}
