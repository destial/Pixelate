package xyz.destiall.pixelate.graphics;

import android.graphics.Bitmap;

public abstract class Imageable {
    protected Bitmap image;
    protected int height;
    protected int width;
    protected int rows;
    protected int columns;
    public Imageable(Bitmap image, int rows, int columns) {
        this.image = image;
        this.rows = rows;
        this.columns = columns;
        height = image.getHeight() / rows;
        width = image.getWidth() / columns;
    }

    public Bitmap createSubImageAt(int row, int col) {
        return Bitmap.createBitmap(image, col * width, row * height, width, height);
    }

    public Bitmap[] createAnimation(int row) {
        Bitmap[] animation = new Bitmap[columns];
        for (int i = 0; i < columns; i++) {
            animation[i] = createSubImageAt(row, i);
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
        return height;
    }

    public int getRows() {
        return rows;
    }

    public int getWidth() {
        return width;
    }

    public static Bitmap scaleImage(Bitmap image, float scale) {
        return Bitmap.createScaledBitmap(image, (int) (image.getWidth() * scale), (int) (image.getHeight() * scale), false);
    }
}
