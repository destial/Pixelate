package xyz.destiall.pixelate.pathfinding;

import java.util.LinkedList;

import xyz.destiall.pixelate.modular.Module;
import xyz.destiall.pixelate.pathfinding.algorithms.Algorithm;
import xyz.destiall.pixelate.position.Location;

public class PathFindingAI implements Module {

    Long previousUpdate = System.currentTimeMillis();
    double pathUpdateFrequency;
    Algorithm algo;

    public PathFindingAI(Algorithm algo, double pathUpdateFrequency) {
        this.pathUpdateFrequency = pathUpdateFrequency;
        previousUpdate -= (long) (this.pathUpdateFrequency * 1000);
        this.algo = algo;
    }

    public LinkedList<Location> getPath(Location start, Location end) {
        if(previousUpdate + (long) (pathUpdateFrequency * 1000) < System.currentTimeMillis())
            return algo.findNewPath(start, end);
        else
            return algo.getCurrentPath();
    }

    @Override
    public void update() {

    }

    @Override
    public void destroy() {

    }
}
