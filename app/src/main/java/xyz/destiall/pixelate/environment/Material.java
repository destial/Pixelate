package xyz.destiall.pixelate.environment;

import xyz.destiall.pixelate.R;

public enum Material {
    STONE(0, 0),
    WOOD(0, 1),
    SWORD(R.drawable.sword);

    private final int row;
    private final int column;
    private final boolean block;
    private final int drawable;

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
