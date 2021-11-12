package xyz.destiall.pixelate.environment;

import java.util.Arrays;

import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.R;

public enum Material {
    STONE(0, 0),
    WOOD(0, 1),
    GRASS(0,2),
    COAL_ORE(0,3),
    PLANKS(0,4),
    SWORD(R.drawable.sword);

    private final int row;
    private final int column;
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
        columns += 1;
        rows += 1;
    }

    Material(int drawable) {
        row = 0;
        column = 0;
        block = false;
        this.drawable = drawable;
    }

    Material(int row, int column) {
        this.row = row;
        this.column = column;
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
