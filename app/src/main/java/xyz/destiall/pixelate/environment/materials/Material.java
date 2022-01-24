package xyz.destiall.pixelate.environment.materials;

import android.graphics.Bitmap;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.meta.Enchantment;
import xyz.destiall.pixelate.items.meta.ItemMeta;

/**
 * Written by Rance & Yong Hong
 */
public enum Material {
    STONE(0, 0, Tile.TileType.BACKGROUND, 10, ToolType.PICKAXE),
    WOOD(0, 1, Tile.TileType.FOREGROUND, 4, ToolType.AXE),
    GRASS(0,2, Tile.TileType.BACKGROUND, 3, ToolType.SHOVEL),
    PLANKS(0,4, Tile.TileType.FOREGROUND, 4, ToolType.AXE),
    BOOKSHELF(1,4, Tile.TileType.FOREGROUND, 4, ToolType.AXE),

    FURNACE(0, 5, Tile.TileType.FOREGROUND, 7.5f, ToolType.PICKAXE, true),
    CHEST(0, 6, Tile.TileType.FOREGROUND, 5.5f, ToolType.AXE, true),
    WORKBENCH(3, 3, Tile.TileType.FOREGROUND, 3f, ToolType.AXE),
    ENCHANT_TABLE(0, 7, Tile.TileType.FOREGROUND, 7.5f, ToolType.PICKAXE, true),

    PURPLE_WOOL(1,0, Tile.TileType.FOREGROUND, 2.7f, ToolType.SHEAR),
    CYAN_WOOL(1,1, Tile.TileType.FOREGROUND, 2.7f, ToolType.SHEAR),
    DARK_BLUE_WOOL(1,2, Tile.TileType.FOREGROUND, 2.7f, ToolType.SHEAR),
    BROWN_WOOL(1,3, Tile.TileType.FOREGROUND, 2.7f, ToolType.SHEAR),
    GLASS(1,5, Tile.TileType.FOREGROUND, 2.7f, ToolType.NONE),
    BLACK_WOOL(1,6, Tile.TileType.FOREGROUND, 2.7f, ToolType.SHEAR),
    RED_WOOL(1,7, Tile.TileType.FOREGROUND, 2.7f, ToolType.SHEAR),
    GREEN_WOOL(1,8, Tile.TileType.FOREGROUND, 2.7f, ToolType.SHEAR),
    WHITE_WOOL(2,0, Tile.TileType.FOREGROUND, 2.7f, ToolType.SHEAR),
    LIGHT_PURPLE_WOOL(2,1, Tile.TileType.FOREGROUND, 2.7f, ToolType.SHEAR),
    ORANGE_WOOL(2,2, Tile.TileType.FOREGROUND, 2.7f, ToolType.SHEAR),
    BLUE_WOOL(2,3, Tile.TileType.FOREGROUND, 2.7f, ToolType.SHEAR),
    DARK_GRAY_WOOL(2,4, Tile.TileType.FOREGROUND, 2.7f, ToolType.SHEAR),
    LIGHT_GRAY_WOOL(2,5, Tile.TileType.FOREGROUND, 2.7f, ToolType.SHEAR),
    PINK_WOOL(2,6, Tile.TileType.FOREGROUND, 2.7f, ToolType.SHEAR),
    LIME_WOOL(2,7, Tile.TileType.FOREGROUND, 2.7f, ToolType.SHEAR),
    YELLOW_WOOL(2,8, Tile.TileType.FOREGROUND, 2.7f, ToolType.SHEAR),

    GOLD_ORE(4,0, Tile.TileType.FOREGROUND, 7.5f, ToolType.PICKAXE),
    IRON_ORE(4,1, Tile.TileType.FOREGROUND, 7.5f, ToolType.PICKAXE),
    COAL_ORE(4,2, Tile.TileType.FOREGROUND, 5.f, ToolType.PICKAXE),
    LAPIS_ORE(4,4, Tile.TileType.FOREGROUND, 5.f, ToolType.PICKAXE),
    DIAMOND_ORE(4,5, Tile.TileType.FOREGROUND, 7.5f, ToolType.PICKAXE),
    EMERALD_ORE(4,6, Tile.TileType.FOREGROUND, 7.5f, ToolType.PICKAXE),
    REDSTONE_ORE(4,7, Tile.TileType.FOREGROUND, 5.f, ToolType.PICKAXE),

    IRON_BLOCK(3, 7, Tile.TileType.FOREGROUND, 10.f, ToolType.PICKAXE),
    GOLD_BLOCK(3, 2, Tile.TileType.FOREGROUND, 10.f, ToolType.PICKAXE),
    EMERALD_BLOCK(3, 6, Tile.TileType.FOREGROUND, 10.f, ToolType.PICKAXE),
    DIAMOND_BLOCK(3, 5, Tile.TileType.FOREGROUND, 10.f, ToolType.PICKAXE),
    REDSTONE_BLOCK(3, 8, Tile.TileType.FOREGROUND, 10.f, ToolType.PICKAXE),
    LAPIS_BLOCK(3, 2, Tile.TileType.FOREGROUND, 10.f, ToolType.PICKAXE),

    MOSSY_COBBLESTONE(5,0, Tile.TileType.FOREGROUND, 10, ToolType.PICKAXE),
    COBBLESTONE(5,8, Tile.TileType.FOREGROUND, 10, ToolType.PICKAXE),
    SAND(5, 7, Tile.TileType.FOREGROUND, 3, ToolType.SHOVEL),

    TNT(3, 4, Tile.TileType.FOREGROUND, 0.5f, ToolType.NONE),

    DIAMOND_SWORD(R.drawable.diamond_sword, ToolType.SWORD, EfficiencyTier.DIAMOND, 500),
    GOLD_SWORD(R.drawable.gold_sword, ToolType.SWORD, EfficiencyTier.GOLD, 50),
    IRON_SWORD(R.drawable.iron_sword, ToolType.SWORD, EfficiencyTier.IRON, 250),
    STONE_SWORD(R.drawable.stone_sword, ToolType.SWORD, EfficiencyTier.STONE, 100),
    WOODEN_SWORD(R.drawable.wood_sword, ToolType.SWORD, EfficiencyTier.WOOD, 50),

    DIAMOND_AXE(R.drawable.diamond_axe, ToolType.AXE, EfficiencyTier.DIAMOND, 500),
    GOLD_AXE(R.drawable.gold_axe, ToolType.AXE, EfficiencyTier.GOLD, 50),
    IRON_AXE(R.drawable.iron_axe, ToolType.AXE, EfficiencyTier.IRON, 250),
    STONE_AXE(R.drawable.stone_axe, ToolType.AXE, EfficiencyTier.STONE, 100),
    WOODEN_AXE(R.drawable.wood_axe, ToolType.AXE, EfficiencyTier.WOOD, 50),

    DIAMOND_PICKAXE(R.drawable.diamond_pickaxe, ToolType.PICKAXE, EfficiencyTier.DIAMOND, 200),
    GOLD_PICKAXE(R.drawable.gold_pickaxe, ToolType.PICKAXE, EfficiencyTier.GOLD, 50),
    IRON_PICKAXE(R.drawable.iron_pickaxe, ToolType.PICKAXE, EfficiencyTier.IRON, 250),
    STONE_PICKAXE(R.drawable.stone_pickaxe, ToolType.PICKAXE, EfficiencyTier.STONE, 100),
    WOODEN_PICKAXE(R.drawable.wood_pickaxe, ToolType.PICKAXE, EfficiencyTier.WOOD, 50),

    //Other items
    COAL(R.drawable.coal, ToolType.NONE, EfficiencyTier.NONE, -1),
    REDSTONE(R.drawable.redstone_dust, ToolType.NONE, EfficiencyTier.NONE, -1),
    LAPIS(R.drawable.lapis_lazuli, ToolType.NONE, EfficiencyTier.NONE, -1),
    DIAMOND(R.drawable.diamond, ToolType.NONE, EfficiencyTier.DIAMOND, -1),
    EMERALD(R.drawable.emerald, ToolType.NONE, EfficiencyTier.NONE, -1),
    IRON_INGOT(R.drawable.iron_ingot, ToolType.NONE, EfficiencyTier.IRON, -1),
    GOLD_INGOT(R.drawable.gold_ingot, ToolType.NONE, EfficiencyTier.GOLD, -1),
    STICK(R.drawable.stick, ToolType.NONE, EfficiencyTier.NONE, -1),
    GUNPOWDER(R.drawable.gunpowder, ToolType.NONE, EfficiencyTier.NONE, -1),
    BONE(R.drawable.bone, ToolType.NONE, EfficiencyTier.NONE, -1),
    ROTTEN_FLESH(R.drawable.rotten_flesh, ToolType.NONE, EfficiencyTier.NONE, -1),
    BOOK(R.drawable.book_normal, ToolType.NONE, EfficiencyTier.NONE, -1),

    ;

    private final Tile.TileType tileType;
    private final int row;
    private final int column;
    private final int drawable;
    private final boolean block;
    private final boolean container;

    private final float defaultBreakDuration;
    private final int maxDurability;
    private final ToolType toolType;
    private final EfficiencyTier efficiencyTier;
    private Bitmap image;

    private static int columns;
    private static int rows;

    static {
        for (Material material : Material.values()) {
            if (material.getColumn() > columns) {
                columns = material.getColumn();
            }
            if (material.getRow() > rows) {
                rows = material.getRow();
            }
        }
        columns += 1;
        rows += 1;
    }

    //Items
    Material(int drawable, ToolType toolType, EfficiencyTier efficiencyTier, int maxDurability) {
        this(-1, -1, false, drawable, Tile.TileType.UNKNOWN, -1, toolType, efficiencyTier, false, maxDurability);
    }

    //For materials
    Material(int row, int column, Tile.TileType type, float defaultBreakDuration, ToolType toolType) {
        this(row, column, true, -1, type, defaultBreakDuration, toolType, EfficiencyTier.NONE, false, -1);
    }

    Material(int row, int column, Tile.TileType type, float defaultBreakDuration, ToolType toolType, boolean container) {
        this(row, column, true, -1, type, defaultBreakDuration, toolType, EfficiencyTier.NONE, container, -1);
    }

    Material(int row, int column, boolean block, int drawable, Tile.TileType type, float defaultBreakDuration, ToolType toolType, EfficiencyTier efficiencyTier, boolean container, int maxDurability) {
        this.row = row;
        this.column = column;
        this.block = block;
        this.drawable = drawable;
        this.tileType = type;
        this.defaultBreakDuration = defaultBreakDuration;
        this.toolType = toolType;
        this.efficiencyTier = efficiencyTier;
        this.container = container;
        this.maxDurability = maxDurability;
        this.image = null;
    }

    /**
     * Get the formatted name of this material
     * @return A cleaned and pretty name
     */
    public String getName() {
        String[] args = name().toLowerCase().split("_");
        String[] newargs = new String[args.length];
        int i = 0;
        for (String a : args) {
            newargs[i++] = a.substring(0, 1).toUpperCase() + a.substring(1);
        }
        return String.join(" ", newargs);
    }

    /**
     * Get the Material from a name
     * @param name The material name
     * @return The material, or null if not found
     */
    public static Material getFromName(String name) {
        try {
            return valueOf(name.toUpperCase().replace(" ", "_"));
        } catch (Exception ignored) {}
        return null;
    }

    /**
     * Get the amount of columns used in the tile map
     * @return The columns
     */
    public static int getColumns() {
        return columns;
    }

    /**
     * Get the amount of rows used in the tile map
     * @return  The rows
     */
    public static int getRows() {
        return rows;
    }

    /**
     * Get the generic modifier of this material. Used in checking tool efficiency when breaking blocks.
     * @return The generic modifier
     */
    public ToolType getToolType()
    {
        return toolType;
    }

    /**
     * Get the level of efficiency of this tool.
     * @return The efficiency tier
     */
    public EfficiencyTier getEfficiencyTier()
    {
        return efficiencyTier;
    }

    /**
     * Get the maximum durability of this item
     * @return The max durability if it is a tool, otherwise -1
     */
    public int getMaxDurability() {
        return isTool() ? maxDurability : -1;
    }

    /**
     * Get the drawable id of this material.
     * @return The drawable id or -1 if from the tile map
     */
    public int getDrawable() {
        return drawable;
    }

    /**
     * Get the column of this material in the tile map
     * @return The column or -1 if it has a drawable
     */
    public int getColumn() {
        return column;
    }

    /**
     * Get the row of this material in the tile map
     * @return The row or -1 if it has a drawable
     */
    public int getRow() {
        return row;
    }

    /**
     * Get the tile type of this material
     * @return Foreground or background
     */
    public Tile.TileType getTileType() {
        return tileType;
    }

    /**
     * Get the required mining duration of this material
     * @param itemMaterial The material to check against
     * @return The duration to mine
     */
    public float getRequiredMineDuration(Material itemMaterial) {
        if (itemMaterial.getToolType() == toolType) {
            return defaultBreakDuration / itemMaterial.getEfficiencyTier().getMultiplier();
        }
        return defaultBreakDuration;
    }

    /**
     * Get the required mining duration of this material
     * @param item The item to check against
     * @return The duration to mine
     */
    public float getRequiredMineDuration(ItemStack item) {
        if (item == null) return defaultBreakDuration;
        float dur = getRequiredMineDuration(item.getType());
        ItemMeta meta = item.getItemMeta();
        int level = meta.getEnchantLevel(Enchantment.DIG_SPEED);
        return level != -1 ? Math.max(dur - level * 0.4f, 0f) : dur;
    }

    /**
     * If this material is a container
     * @return true or false
     */
    public boolean isContainer() {
        return container;
    }

    /**
     * If this material is a block
     * @return true or false
     */
    public boolean isBlock() {
        return block;
    }

    /**
     * IF this material is a tool
     * @return true or false
     */
    public boolean isTool() {
        return !isBlock() && !isContainer() && getToolType() != ToolType.NONE;
    }

    /**
     * Get the image represented by this material
     * @return The bitmap image
     */
    public Bitmap getImage() {
        if (image == null) {
            if (drawable != -1) {
                image = ResourceManager.getBitmap(drawable);
                image = Imageable.resizeImage(image, Tile.SIZE, Tile.SIZE);
            }
            else
                image = Imageable.createSubImageAt(Pixelate.getTileMap(), rows, columns, row, column);
        }
        return image;
    }
}
