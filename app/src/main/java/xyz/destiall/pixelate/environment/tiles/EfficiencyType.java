package xyz.destiall.pixelate.environment.tiles;

public enum EfficiencyType {
    WOOD_TIER(1.2f),
    STONE_TIER(2f),
    IRON_TIER(2.2f),
    DIAMOND_TIER(4.f),
    GOLD_TIER(4.f),
    NONE(1.0f);

    private final float multiplier;

    EfficiencyType(float multiplier)
    {
     this.multiplier = multiplier;
    }

    public float getMultiplier()
    {
        return multiplier;
    }
}