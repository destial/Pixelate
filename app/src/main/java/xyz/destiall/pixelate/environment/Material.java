package xyz.destiall.pixelate.environment;

import java.util.Arrays;

import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.environment.tiles.EfficiencyType;
import xyz.destiall.pixelate.environment.tiles.GenericModifierType;
import xyz.destiall.pixelate.environment.tiles.Tile;

public enum Material {
    STONE(0, 0, Tile.TileType.BACKGROUND, 10, GenericModifierType.PICKAXE_EFFICIENT),
    WOOD(0, 1, Tile.TileType.FOREGROUND, 4, GenericModifierType.AXE_EFFICIENT),
    GRASS(0,2, Tile.TileType.BACKGROUND, 3, GenericModifierType.SHOVEL_EFFICIENT),
    COAL_ORE(0,3, Tile.TileType.FOREGROUND, 10, GenericModifierType.PICKAXE_EFFICIENT),
    PLANKS(0,4, Tile.TileType.FOREGROUND, 4, GenericModifierType.AXE_EFFICIENT),

    //GenericModifierType for items is: On what material does it work better on, EfficiencyType is the multiplied effect of tier of item.
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

        ;

    private final Tile.TileType tileType;
    private final int row;
    private final int column;
    private final int drawable;
    private final boolean block;

    private float defaultBreakDuration;
    private GenericModifierType genericModifierType;
    private EfficiencyType efficiencyType;

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
        this(0, 0, false, drawable, Tile.TileType.UNKNOWN, -1, genericModifierType, efficiencyType);
    }

    //For materials
    Material(int row, int column, Tile.TileType type, float defaultBreakDuration, GenericModifierType genericModifierType) {
        this(row, column, true, -1, type, defaultBreakDuration, genericModifierType, EfficiencyType.NONE);
    }

    Material(int row, int column, boolean block, int drawable, Tile.TileType type, float defaultBreakDuration, GenericModifierType genericModifierType, EfficiencyType efficiencyType) {
        this.row = row;
        this.column = column;
        this.block = block;
        this.drawable = drawable;
        this.tileType = type;
        this.defaultBreakDuration = defaultBreakDuration;
        this.genericModifierType = genericModifierType;
        this.efficiencyType = efficiencyType;
    }

    public static int getColumns() {
        return columns;
    }

    public static int getRows() {
        return rows;
    }

    public static long amtOfBlocks() {
        return Arrays.stream(values()).filter(Material::isBlock).count();
    }

    public float getRequiredMineDuration(Material itemMaterial)
    {
        //Can speed up mining since this tool is for the right typ eof block
        if(itemMaterial.getGenericModifierType() == this.genericModifierType)
        {
            float breakDuration = this.defaultBreakDuration / itemMaterial.getEfficiencyTier().getMultiplier();
            return breakDuration;
        }
        return this.defaultBreakDuration;
    }

    public GenericModifierType getGenericModifierType()
    {
        return genericModifierType;
    }

    public EfficiencyType getEfficiencyTier()
    {
        return efficiencyType;
    }

    public int getDrawable() {
        return drawable;
    }

    public boolean isBlock() {
        return block;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public Tile.TileType getTileType() {
        return tileType;
    }
}
