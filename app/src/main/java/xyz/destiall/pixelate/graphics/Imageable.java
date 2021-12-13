package xyz.destiall.pixelate.graphics;

import android.graphics.Bitmap;

public abstract class Imageable {
    protected Bitmap image;
    protected float height;
    protected float width;
    protected int rows;
    protected int columns;
    public Imageable(Bitmap image, int rows, int columns) {
        this.image = image;
        this.rows = rows;
        this.columns = columns;
        height = image.getHeight() / (float) rows;
        width = image.getWidth() / (float) columns;
    }

    public Bitmap createSubImageAt(int row, int col) {
        return Bitmap.createBitmap(image, col * (int) width, row * (int) height, (int) width, (int) height);
    }

    public Bitmap[] createAnimation(int row) {
        Bitmap[] animation = new Bitmap[columns];
        for (int i = 0; i < columns; i++) {
            animation[i] = createSubImageAt(row, i);
        }
        return animation;
    }

    public static Bitmap createSubImageAt(Bitmap image, int rows, int cols, int row, int col) {
        int height = image.getHeight() / rows;
        int width = image.getWidth() / cols;
        return Bitmap.createBitmap(image, col * width, row * height, width, height);
    }

    public static Bitmap[] createAnimation(Bitmap source, int rows, int cols, int row) {
        Bitmap[] animation = new Bitmap[cols];
        for (int i = 0; i < cols; i++) {
            animation[i] = createSubImageAt(source, rows, cols, row, i);
        }
        return animation;
    }

    public Bitmap getImage() {
        return image;
    }

    public int getColumns() {
        return columns;
    }

    public int getHeight() {
        return (int) height;
    }

    public int getRows() {
        return rows;
    }

    public int getWidth() {
        return (int) width;
    }

    public static Bitmap scaleImage(Bitmap image, float scale) {
        return Bitmap.createScaledBitmap(image, (int) (image.getWidth() * scale), (int) (image.getHeight() * scale), false);
    }
}
