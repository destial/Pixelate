package xyz.destiall.pixelate.environment;

import java.util.Arrays;

import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.environment.tiles.Tile;

public enum Material {
    STONE(0, 0, Tile.TILE_TYPE.UNKNOWN),
    WOOD(0, 1, Tile.TILE_TYPE.FOREGROUND),
    GRASS(0,2, Tile.TILE_TYPE.BACKGROUND),
    COAL_ORE(0,3, Tile.TILE_TYPE.FOREGROUND),
    PLANKS(0,4, Tile.TILE_TYPE.FOREGROUND),
    SWORD(R.drawable.sword);

    private final Tile.TILE_TYPE tileType;
    private final int row;
    private final int column;
    private final int drawable;
    private final boolean block;

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

    Material(int drawable) {
        this(0, 0, false, drawable);
    }

    Material(int row, int column, Tile.TILE_TYPE type) {
        this(row, column, true, -1, type);
    }

    Material(int row, int column, boolean block, int drawable) {
        this(row, column, block, drawable, Tile.TILE_TYPE.UNKNOWN);
    }

    Material(int row, int column, boolean block, int drawable, Tile.TILE_TYPE type) {
        this.row = row;
        this.column = column;
        this.block = block;
        this.drawable = drawable;
        this.tileType = type;
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

    public Tile.TILE_TYPE getTileType() {
        return tileType;
    }
}
