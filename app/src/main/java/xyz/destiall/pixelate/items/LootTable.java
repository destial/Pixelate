package xyz.destiall.pixelate.items;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

import xyz.destiall.pixelate.environment.Material;

public class LootTable {
    private static LootTable instance = null;

    SplittableRandom ran = new SplittableRandom();

    public static LootTable getInstance()
    {
        if(instance == null)
            instance = new LootTable();
        return instance;
    }

    private LootTable()
    {

    }

    public List<ItemStack> getDrops(Material type, int luck)
    {
        List<ItemStack> items = new ArrayList<ItemStack>();
        switch(type)
        {
            case COAL_ORE:
                items.add(new ItemStack(Material.COAL,1 + ran.nextInt(0, luck+1)));
                break;
        }
        if(items.isEmpty())
            items.add(new ItemStack(type, 1));

        return items;
    }
}
