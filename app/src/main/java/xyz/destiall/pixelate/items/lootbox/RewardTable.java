package xyz.destiall.pixelate.items.lootbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RewardTable {

    private Map<Reward, Double> distribution;
    private double distSum;

    public RewardTable() {
        distribution = new HashMap<>();
        distSum = 0;
    }

    public void addReward(Reward m, double distribution) {
        if (this.distribution.get(m) != null) {
            distSum -= this.distribution.get(m);
        }
        this.distribution.put(m, distribution);
        distSum += distribution;
    }

    public double getSumOfEntryChances() {
        return distSum;
    }

    public Set<Reward> getRewards()
    {
        return distribution.keySet();
    }

    public double getDistributionOf(Reward m) {
        return distribution.getOrDefault(m, 0.0);
    }

    public void clearTable() {
        distribution.clear();
        distSum = 0.0;
    }

    public Reward generateReward() {
        if(distSum == 0 || distribution.isEmpty()) return null;
        double rand = Math.random();
        double ratio = 1.0f / distSum;
        double tempDist = 0;
        for (Reward i : distribution.keySet()) {
            tempDist += distribution.get(i);
            if (rand / ratio <= tempDist) {
                return i;
            }
        }
        return null;
    }

}
