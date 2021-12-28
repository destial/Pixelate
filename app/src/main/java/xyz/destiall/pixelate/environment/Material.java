package xyz.destiall.pixelate.environment;

import android.graphics.Bitmap;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.environment.tiles.EfficiencyType;
import xyz.destiall.pixelate.environment.tiles.GenericModifierType;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.items.ItemStack;

public enum Material {
    STONE(0, 0, Tile.TileType.BACKGROUND, 10, GenericModifierType.PICKAXE_EFFICIENT),
    WOOD(0, 1, Tile.TileType.FOREGROUND, 4, GenericModifierType.AXE_EFFICIENT),
    GRASS(0,2, Tile.TileType.BACKGROUND, 3, GenericModifierType.SHOVEL_EFFICIENT),
    PLANKS(0,4, Tile.TileType.FOREGROUND, 4, GenericModifierType.AXE_EFFICIENT),

    FURNACE(0, 5, Tile.TileType.FOREGROUND, 7.5f, GenericModifierType.PICKAXE_EFFICIENT, true),
    CHEST(0, 6, Tile.TileType.FOREGROUND, 7.5f, GenericModifierType.AXE_EFFICIENT, true),

    PURPLE_WOOL(1,0, Tile.TileType.FOREGROUND, 2.7f, GenericModifierType.SHEAR_EFFICIENT),
    CYAN_WOOL(1,1, Tile.TileType.FOREGROUND, 2.7f, GenericModifierType.SHEAR_EFFICIENT),
    DARK_BLUE_WOOL(1,2, Tile.TileType.FOREGROUND, 2.7f, GenericModifierType.SHEAR_EFFICIENT),
    BROWN_WOOL(1,3, Tile.TileType.FOREGROUND, 2.7f, GenericModifierType.SHEAR_EFFICIENT),
    GLASS(1,5, Tile.TileType.FOREGROUND, 2.7f, GenericModifierType.NONE),
    BLACK_WOOL(1,6, Tile.TileType.FOREGROUND, 2.7f, GenericModifierType.SHEAR_EFFICIENT),
    RED_WOOL(1,7, Tile.TileType.FOREGROUND, 2.7f, GenericModifierType.SHEAR_EFFICIENT),
    GREEN_WOOL(1,8, Tile.TileType.FOREGROUND, 2.7f, GenericModifierType.SHEAR_EFFICIENT),
    WHITE_WOOL(2,0, Tile.TileType.FOREGROUND, 2.7f, GenericModifierType.SHEAR_EFFICIENT),
    LIGHT_PURPLE_WOOL(2,1, Tile.TileType.FOREGROUND, 2.7f, GenericModifierType.SHEAR_EFFICIENT),
    ORANGE_WOOL(2,2, Tile.TileType.FOREGROUND, 2.7f, GenericModifierType.SHEAR_EFFICIENT),
    BLUE_WOOL(2,3, Tile.TileType.FOREGROUND, 2.7f, GenericModifierType.SHEAR_EFFICIENT),
    DARK_GRAY_WOOL(2,4, Tile.TileType.FOREGROUND, 2.7f, GenericModifierType.SHEAR_EFFICIENT),
    LIGHT_GRAY_WOOL(2,5, Tile.TileType.FOREGROUND, 2.7f, GenericModifierType.SHEAR_EFFICIENT),
    PINK_WOOL(2,6, Tile.TileType.FOREGROUND, 2.7f, GenericModifierType.SHEAR_EFFICIENT),
    LIME_WOOL(2,7, Tile.TileType.FOREGROUND, 2.7f, GenericModifierType.SHEAR_EFFICIENT),
    YELLOW_WOOL(2,8, Tile.TileType.FOREGROUND, 2.7f, GenericModifierType.SHEAR_EFFICIENT),

    GOLD_ORE(4,0, Tile.TileType.FOREGROUND, 10, GenericModifierType.PICKAXE_EFFICIENT),
    IRON_ORE(4,1, Tile.TileType.FOREGROUND, 10, GenericModifierType.PICKAXE_EFFICIENT),
    COAL_ORE(4,2, Tile.TileType.FOREGROUND, 10, GenericModifierType.PICKAXE_EFFICIENT),
    LAPIS_ORE(4,4, Tile.TileType.FOREGROUND, 10, GenericModifierType.PICKAXE_EFFICIENT),
    DIAMOND_ORE(4,5, Tile.TileType.FOREGROUND, 10, GenericModifierType.PICKAXE_EFFICIENT),
    EMERALD_ORE(4,6, Tile.TileType.FOREGROUND, 10, GenericModifierType.PICKAXE_EFFICIENT),
    REDSTONE_ORE(4,7, Tile.TileType.FOREGROUND, 10, GenericModifierType.PICKAXE_EFFICIENT),

    MOSSY_COBBLESTONE(5,0, Tile.TileType.FOREGROUND, 10, GenericModifierType.PICKAXE_EFFICIENT),
    COBBLESTONE(5,8, Tile.TileType.FOREGROUND, 10, GenericModifierType.PICKAXE_EFFICIENT),

    TNT(3, 4, Tile.TileType.FOREGROUND, 0.5f, GenericModifierType.NONE),

    DIAMOND_SWORD(R.drawable.diamond_sword, GenericModifierType.NONE, EfficiencyType.DIAMOND_TIER),
    GOLD_SWORD(R.drawable.gold_sword, GenericModifierType.NONE, EfficiencyType.GOLD_TIER),
    IRON_SWORD(R.drawable.iron_sword, GenericModifierType.NONE, EfficiencyType.IRON_TIER),
    STONE_SWORD(R.drawable.stone_sword, GenericModifierType.NONE, EfficiencyType.STONE_TIER),
    WOODEN_SWORD(R.drawable.wood_sword, GenericModifierType.NONE, EfficiencyType.WOOD_TIER),

    DIAMOND_AXE(R.drawable.diamond_axe, GenericModifierType.AXE_EFFICIENT, EfficiencyType.DIAMOND_TIER),
    GOLD_AXE(R.drawable.gold_axe, GenericModifierType.AXE_EFFICIENT, EfficiencyType.GOLD_TIER),
    IRON_AXE(R.drawable.iron_axe, GenericModifierType.AXE_EFFICIENT, EfficiencyType.IRON_TIER),
    STONE_AXE(R.drawable.stone_axe, GenericModifierType.AXE_EFFICIENT, EfficiencyType.STONE_TIER),
    WOODEN_AXE(R.drawable.wood_axe, GenericModifierType.AXE_EFFICIENT, EfficiencyType.WOOD_TIER),

    DIAMOND_PICKAXE(R.drawable.diamond_pickaxe, GenericModifierType.PICKAXE_EFFICIENT, EfficiencyType.DIAMOND_TIER),
    GOLD_PICKAXE(R.drawable.gold_pickaxe, GenericModifierType.PICKAXE_EFFICIENT, EfficiencyType.GOLD_TIER),
    IRON_PICKAXE(R.drawable.iron_pickaxe, GenericModifierType.PICKAXE_EFFICIENT, EfficiencyType.IRON_TIER),
    STONE_PICKAXE(R.drawable.stone_pickaxe, GenericModifierType.PICKAXE_EFFICIENT, EfficiencyType.STONE_TIER),
    WOODEN_PICKAXE(R.drawable.wood_pickaxe, GenericModifierType.PICKAXE_EFFICIENT, EfficiencyType.WOOD_TIER),

    //Other items
    COAL(R.drawable.coal, GenericModifierType.NONE, EfficiencyType.NONE),
    STICK(R.drawable.stick, GenericModifierType.NONE, EfficiencyType.NONE),

    ;

    private final Tile.TileType tileType;
    private final int row;
    private final int column;
    private final int drawable;
    private final boolean block;
    private final boolean container;

    private final float defaultBreakDuration;
    private final GenericModifierType genericModifierType;
    private final EfficiencyType efficiencyType;
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
    Material(int drawable, GenericModifierType genericModifierType, EfficiencyType efficiencyType) {
        this(-1, -1, false, drawable, Tile.TileType.UNKNOWN, -1, genericModifierType, efficiencyType, false);
    }

    //For materials
    Material(int row, int column, Tile.TileType type, float defaultBreakDuration, GenericModifierType genericModifierType) {
        this(row, column, true, -1, type, defaultBreakDuration, genericModifierType, EfficiencyType.NONE, false);
    }

    Material(int row, int column, boolean block, int drawable, Tile.TileType type, float defaultBreakDuration, GenericModifierType genericModifierType, EfficiencyType efficiencyType, boolean container) {
        this.row = row;
        this.column = column;
        this.block = block;
        this.drawable = drawable;
        this.tileType = type;
        this.defaultBreakDuration = defaultBreakDuration;
        this.genericModifierType = genericModifierType;
        this.efficiencyType = efficiencyType;
        this.container = container;
        this.image = null;
    }

    Material(int row, int column, Tile.TileType type, float defaultBreakDuration, GenericModifierType genericModifierType, boolean container) {
        this(row, column, true, -1, type, defaultBreakDuration, genericModifierType, EfficiencyType.NONE, container);
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
    public GenericModifierType getGenericModifierType()
    {
        return genericModifierType;
    }

    /**
     * Get the level of efficiency of this tool.
     * @return The efficiency tier
     */
    public EfficiencyType getEfficiencyTier()
    {
        return efficiencyType;
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
        if (itemMaterial.getGenericModifierType() == genericModifierType) {
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
        return getRequiredMineDuration(item.getType());
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
