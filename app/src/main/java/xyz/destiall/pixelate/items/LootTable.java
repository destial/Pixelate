package xyz.destiall.pixelate.items;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

import xyz.destiall.pixelate.environment.materials.Material;

public class LootTable {
    private static LootTable instance = null;
    private LootTable() {}

    SplittableRandom ran = new SplittableRandom();

    public static LootTable getInstance() {
        if (instance == null)
            instance = new LootTable();
        return instance;
    }

    public List<ItemStack> getDrops(Material type, int luck) {
        List<ItemStack> items = new ArrayList<>();
        if (type == Material.COAL_ORE) {
            items.add(new ItemStack(Material.COAL, 1 + ran.nextInt(0, luck + 1)));
        }

        if (items.isEmpty()) items.add(new ItemStack(type, 1));
        return items;
    }

    public int getXPDrops(Material type, int luck)
    {
        int amt = 0;
        switch(type)
        {
            case COAL_ORE:
                amt = 20;
                break;
            case REDSTONE_ORE:
                amt = 30;
                break;
            case LAPIS_ORE:
                amt = 30;
                break;
            case DIAMOND_ORE:
                amt = 50;
            case EMERALD_ORE:
                amt = 65;
        }
        return (int)(amt * (1+luck*0.2));
    }
}
