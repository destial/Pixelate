package xyz.destiall.pixelate.environment;

import java.util.Arrays;

import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.environment.tiles.Tile;

public enum Material {
    STONE(0, 0, Tile.TILE_TYPE.BACKGROUND),
    WOOD(0, 1, Tile.TILE_TYPE.FOREGROUND),
    GRASS(0,2, Tile.TILE_TYPE.BACKGROUND),
    COAL_ORE(0,3, Tile.TILE_TYPE.FOREGROUND),
    PLANKS(0,4, Tile.TILE_TYPE.FOREGROUND),
    SWORD(R.drawable.sword);

    private final int row;
    private final int column;
    private Tile.TILE_TYPE tileType;
    private final boolean block;
    private final int drawable;

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
    }

    Material(int drawable) {
        row = 0;
        column = 0;
        this.tileType = Tile.TILE_TYPE.UNKNOWN;
        block = false;
        this.drawable = drawable;
    }

    Material(int row, int column, Tile.TILE_TYPE type) {
        this.row = row;
        this.column = column;
        this.tileType = type;
        block = true;
        drawable = -1;
    }

    Material(int row, int column, boolean block, int drawable) {
        this.row = row;
        this.column = column;
        this.block = block;
        this.drawable = drawable;
    }

    public static int getColumns() {
        return columns;
    }

    public static int getRows() {
        return rows;
    }

    public Tile.TILE_TYPE getTileType() {return tileType;}

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
}
