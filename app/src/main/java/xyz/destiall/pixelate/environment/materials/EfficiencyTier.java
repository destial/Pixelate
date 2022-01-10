package xyz.destiall.pixelate.environment.materials;

/**
 * Written by Yong Hong
 */
public enum EfficiencyTier {
    WOOD(1.2f),
    STONE(2f),
    IRON(2.2f),
    DIAMOND(4.f),
    GOLD(4.f),
    NONE(1.0f);

    private final float multiplier;

    EfficiencyTier(float multiplier)
    {
     this.multiplier = multiplier;
    }

    public float getMultiplier()
    {
        return multiplier;
    }
}