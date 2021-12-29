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

public enum Material {
    STONE(0, 0, Tile.TileType.BACKGROUND, 10, ToolType.PICKAXE),
    WOOD(0, 1, Tile.TileType.FOREGROUND, 4, ToolType.AXE),
    GRASS(0,2, Tile.TileType.BACKGROUND, 3, ToolType.SHOVEL),
    PLANKS(0,4, Tile.TileType.FOREGROUND, 4, ToolType.AXE),

    FURNACE(0, 5, Tile.TileType.FOREGROUND, 7.5f, ToolType.PICKAXE, true),
    CHEST(0, 6, Tile.TileType.FOREGROUND, 7.5f, ToolType.AXE, true),

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

    GOLD_ORE(4,0, Tile.TileType.FOREGROUND, 10, ToolType.PICKAXE),
    IRON_ORE(4,1, Tile.TileType.FOREGROUND, 10, ToolType.PICKAXE),
    COAL_ORE(4,2, Tile.TileType.FOREGROUND, 10, ToolType.PICKAXE),
    LAPIS_ORE(4,4, Tile.TileType.FOREGROUND, 10, ToolType.PICKAXE),
    DIAMOND_ORE(4,5, Tile.TileType.FOREGROUND, 10, ToolType.PICKAXE),
    EMERALD_ORE(4,6, Tile.TileType.FOREGROUND, 10, ToolType.PICKAXE),
    REDSTONE_ORE(4,7, Tile.TileType.FOREGROUND, 10, ToolType.PICKAXE),

    MOSSY_COBBLESTONE(5,0, Tile.TileType.FOREGROUND, 10, ToolType.PICKAXE),
    COBBLESTONE(5,8, Tile.TileType.FOREGROUND, 10, ToolType.PICKAXE),

    TNT(3, 4, Tile.TileType.FOREGROUND, 0.5f, ToolType.NONE),

    DIAMOND_SWORD(R.drawable.diamond_sword, ToolType.SWORD, EfficiencyTier.DIAMOND),
    GOLD_SWORD(R.drawable.gold_sword, ToolType.SWORD, EfficiencyTier.GOLD),
    IRON_SWORD(R.drawable.iron_sword, ToolType.SWORD, EfficiencyTier.IRON),
    STONE_SWORD(R.drawable.stone_sword, ToolType.SWORD, EfficiencyTier.STONE),
    WOODEN_SWORD(R.drawable.wood_sword, ToolType.SWORD, EfficiencyTier.WOOD),

    DIAMOND_AXE(R.drawable.diamond_axe, ToolType.AXE, EfficiencyTier.DIAMOND),
    GOLD_AXE(R.drawable.gold_axe, ToolType.AXE, EfficiencyTier.GOLD),
    IRON_AXE(R.drawable.iron_axe, ToolType.AXE, EfficiencyTier.IRON),
    STONE_AXE(R.drawable.stone_axe, ToolType.AXE, EfficiencyTier.STONE),
    WOODEN_AXE(R.drawable.wood_axe, ToolType.AXE, EfficiencyTier.WOOD),

    DIAMOND_PICKAXE(R.drawable.diamond_pickaxe, ToolType.PICKAXE, EfficiencyTier.DIAMOND),
    GOLD_PICKAXE(R.drawable.gold_pickaxe, ToolType.PICKAXE, EfficiencyTier.GOLD),
    IRON_PICKAXE(R.drawable.iron_pickaxe, ToolType.PICKAXE, EfficiencyTier.IRON),
    STONE_PICKAXE(R.drawable.stone_pickaxe, ToolType.PICKAXE, EfficiencyTier.STONE),
    WOODEN_PICKAXE(R.drawable.wood_pickaxe, ToolType.PICKAXE, EfficiencyTier.WOOD),

    //Other items
    COAL(R.drawable.coal, ToolType.NONE, EfficiencyTier.NONE),
    STICK(R.drawable.stick, ToolType.NONE, EfficiencyTier.NONE),

    ;

    private final Tile.TileType tileType;
    private final int row;
    private final int column;
    private final int drawable;
    private final boolean block;
    private final boolean container;

    private final float defaultBreakDuration;
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
    Material(int drawable, ToolType toolType, EfficiencyTier efficiencyTier) {
        this(-1, -1, false, drawable, Tile.TileType.UNKNOWN, -1, toolType, efficiencyTier, false);
    }

    //For materials
    Material(int row, int column, Tile.TileType type, float defaultBreakDuration, ToolType toolType) {
        this(row, column, true, -1, type, defaultBreakDuration, toolType, EfficiencyTier.NONE, false);
    }

    Material(int row, int column, boolean block, int drawable, Tile.TileType type, float defaultBreakDuration, ToolType toolType, EfficiencyTier efficiencyTier, boolean container) {
        this.row = row;
        this.column = column;
        this.block = block;
        this.drawable = drawable;
        this.tileType = type;
        this.defaultBreakDuration = defaultBreakDuration;
        this.toolType = toolType;
        this.efficiencyTier = efficiencyTier;
        this.container = container;
        this.image = null;
    }

    Material(int row, int column, Tile.TileType type, float defaultBreakDuration, ToolType toolType, boolean container) {
        this(row, column, true, -1, type, defaultBreakDuration, toolType, EfficiencyTier.NONE, container);
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
            if (drawable != -1)
                image = ResourceManager.getBitmap(drawable);
            else
                image = Imageable.createSubImageAt(Pixelate.getTileMap(), rows, columns, row, column);
        }
        return image;
    }
}
