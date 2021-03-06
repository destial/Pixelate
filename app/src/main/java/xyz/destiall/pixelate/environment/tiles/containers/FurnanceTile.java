package xyz.destiall.pixelate.environment.tiles.containers;

import java.util.HashMap;

import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.inventory.FurnaceInventory;
import xyz.destiall.pixelate.timer.Timer;

/**
 * Written by Yong Hong
 */
public class FurnanceTile extends ContainerTile {
    private float burnerRemainingTime = 0.f;
    private float lastBurnerMaxTime = 0.f;

    private float timeToSmelt = 4.f;
    private float smeltProgress = timeToSmelt;

    static HashMap<Material, Material> smeltable = new HashMap<>();
    static HashMap<Material, Float> burnRate = new HashMap<>();
    static {
        //Smeltable List
        smeltable.put(Material.COAL_ORE, Material.COAL);
        smeltable.put(Material.IRON_ORE, Material.IRON_INGOT);
        smeltable.put(Material.GOLD_ORE, Material.GOLD_INGOT);
        smeltable.put(Material.COBBLESTONE, Material.STONE);
        smeltable.put(Material.WOOD, Material.COAL);

        //Burn Rate
        burnRate.put(Material.COAL, 8.1f);
    }

    public static boolean isASmeltable(ItemStack item)  {
        return smeltable.containsKey(item.getType());
    }

    public static boolean isABurner(ItemStack item)
    {
        return burnRate.containsKey(item.getType());
    }

    //Main Functions
    protected FurnanceTile() {}

    //TODO: Add the ViewFurnance making sure things are of smeltable and burnable, and also make this update called when tiles update loop is ran.
    public FurnanceTile(int x, int y, World world) {
        super(x, y, Material.FURNACE, world, Material.FURNACE.getTileType(), new FurnaceInventory());
    }

    @Override
    public FurnaceInventory getInventory()
    {
        return (FurnaceInventory) this.tileInventory;
    }

    public float getTimeToSmelt() {
        return timeToSmelt;
    }

    public void setTimeToSmelt(float newTime) {
        timeToSmelt = newTime;
    }

    public float getSmeltProgress() {
        return smeltProgress;
    }

    public float getBurnerRemainingPercentage() {
        if (lastBurnerMaxTime == 0.0) return 0;
        return burnerRemainingTime / lastBurnerMaxTime;
    }

    @SuppressWarnings("all")
    @Override
    public void update() {
        FurnaceInventory inventory = getInventory();
        burnerRemainingTime -= Timer.getDeltaTime();
        if (burnerRemainingTime < 0) burnerRemainingTime = 0;

        ItemStack burner = inventory.getBurnerSlot();
        if (burner != null && isABurner(burner)) {
            if (burnerRemainingTime <= 0 && inventory.getToSmeltSlot() != null) {
                burner.removeAmount(1);
                lastBurnerMaxTime = burnRate.get(burner.getType());
                burnerRemainingTime += burnRate.get(burner.getType());
                if (burner.getAmount() == 0) inventory.setBurnerSlot(null);
            }
        }

        if (burnerRemainingTime > 0) {
            ItemStack toSmelt = inventory.getToSmeltSlot();
            if (toSmelt != null && isASmeltable(toSmelt)) {
                ItemStack processed = inventory.getProcessedSlot();
                if (processed != null && smeltable.get(toSmelt.getType()) != processed.getType()) return;
                smeltProgress -= Timer.getDeltaTime();
                if (smeltProgress <= 0) {
                    smeltProgress = timeToSmelt;
                    if (processed != null && smeltable.get(toSmelt.getType()) == processed.getType()) {
                        processed.addAmount(1);
                    } else {
                        processed = new ItemStack(smeltable.get(toSmelt.getType()));
                        inventory.setProcessedSlot(processed);
                    }
                    toSmelt.removeAmount(1);
                    if (toSmelt.getAmount() == 0) inventory.setToSmeltSlot(null);
                }
            }
        }
    }
}
