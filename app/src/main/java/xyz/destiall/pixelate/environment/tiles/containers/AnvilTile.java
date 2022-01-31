package xyz.destiall.pixelate.environment.tiles.containers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.inventory.AnvilInventory;
import xyz.destiall.pixelate.items.inventory.FurnaceInventory;
import xyz.destiall.pixelate.timer.Timer;

/**
 * Written by Yong Hong
 */
public class AnvilTile extends ContainerTile {

    static HashMap<Material, List<Material>> additivePairs = new HashMap<Material, List<Material>>();
    static {
        //Additive pairs
        {
            List<Material> mats = new ArrayList<Material>();
            mats.add(Material.DIAMOND);

            additivePairs.put(Material.DIAMOND_SWORD, mats);
            additivePairs.put(Material.DIAMOND_PICKAXE, mats);
            additivePairs.put(Material.DIAMOND_AXE, mats);
        }


    }

    public void updateReturnItem()
    {
        ItemStack repairItem = getInventory().getRepairItemSlot();
        ItemStack additiveItem = getInventory().getAdditiveSlot();

        if(repairItem == null || additiveItem == null)
        {
            return;
        }
        System.out.println(repairItem.getType().toString() + " , " + additiveItem.getType().toString());
        ItemStack newItem = null;
        switch(repairItem.getType()) {
            case DIAMOND_AXE:
            case DIAMOND_PICKAXE:
            case DIAMOND_SWORD:
            {
                if(additiveItem.getType() == Material.DIAMOND)
                {
                    if(repairItem.getItemMeta().getDurability() < repairItem.getType().getMaxDurability())
                    {
                        ItemStack repairClone = repairItem.clone();
                        int itemDur =  (int) (repairClone.getItemMeta().getDurability() - repairItem.getType().getMaxDurability() * 0.3);
                        if(itemDur > repairItem.getType().getMaxDurability()) itemDur = repairItem.getType().getMaxDurability();
                        repairClone.getItemMeta().setDurability(itemDur);
                        newItem = repairClone;
                    }
                }
            }

            break;
        }
        getInventory().setResultSlot(newItem);
    }

    public boolean isAnAdditive(ItemStack item)  {
        ItemStack repairItem = getInventory().getRepairItemSlot();
        if(repairItem != null)
        {
            if(additivePairs.containsKey(repairItem.getType()))
            {
                if(additivePairs.get(repairItem.getType()).contains(item.getType()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isARepairItem(ItemStack item)
    {
        return additivePairs.containsKey(item.getType());
    }

    //Main Functions
    protected AnvilTile() {}

    public AnvilTile(int x, int y, World world) {
        super(x, y, Material.ANVIL, world, Material.ANVIL.getTileType(), new AnvilInventory());
    }

    @Override
    public AnvilInventory getInventory()
    {
        return (AnvilInventory) this.tileInventory;
    }


    @SuppressWarnings("all")
    @Override
    public void update() {

    }
}
