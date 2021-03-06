package xyz.destiall.pixelate.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Written by Rance
 */
public abstract class Imageable {
    protected transient Bitmap image;
    protected float height;
    protected float width;
    protected int rows;
    protected int columns;

    public Imageable() {}

    public Imageable(Bitmap image, int rows, int columns) {
        setImage(image, rows, columns);
    }

    public void setImage(Bitmap image, int rows, int columns) {
        this.image = image;
        this.rows = rows;
        this.columns = columns;
        height = image.getHeight() / (float) rows;
        width = image.getWidth() / (float) columns;
    }

    /**
     * Create a cropped image at the requested row and column
     * @param row The row
     * @param col The column
     * @return The cropped image
     */
    public Bitmap createSubImageAt(int row, int col) {
        return Bitmap.createBitmap(image, col * (int) width, row * (int) height, (int) width, (int) height);
    }

    /**
     * Create an animation from the requested row
     * @param row The row
     * @return The animation array
     */
    public Bitmap[] createAnimation(int row) {
        Bitmap[] animation = new Bitmap[columns];
        for (int i = 0; i < columns; i++) {
            animation[i] = createSubImageAt(row, i);
        }
        return animation;
    }

    /**
     * Get the image representing this object
     * @return The image
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * Get the total columns of this object
     * @return The columns
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Get the average height of each sub image
     * @return The average height
     */
    public int getHeight() {
        return (int) height;
    }

    /**
     * Get the total rows of this object
     * @return The rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Get the average width of each sub image
     * @return The average width
     */
    public int getWidth() {
        return (int) width;
    }

    /**
     * Create a cropped image from the given source image
     * @param image The source image
     * @param rows The total rows
     * @param cols The total columns
     * @param row The row to crop
     * @param col The column to crop
     * @return The cropped image
     */
    public static Bitmap createSubImageAt(Bitmap image, int rows, int cols, int row, int col) {
        float height = image.getHeight() / (float) rows;
        float width = image.getWidth() /  (float) cols;
        return Bitmap.createBitmap(image, (int) (col * width), (int) (row * height), (int) width, (int) height);
    }

    /**
     * Create an animation from the requested row from the given source image
     * @param source The source image
     * @param rows The total rows
     * @param cols The total columns
     * @param row The row to animate
     * @return The animation array
     */
    public static Bitmap[] createAnimation(Bitmap source, int rows, int cols, int row) {
        Bitmap[] animation = new Bitmap[cols];
        for (int i = 0; i < cols; i++) {
            animation[i] = createSubImageAt(source, rows, cols, row, i);
        }
        return animation;
    }

    /**
     * Create an animation from the requested row from the given source image
     * @param source The source image
     * @param rows The total rows
     * @param cols The total columns
     * @param row The row to animate
     * @param scale The scale of the source
     * @return The animation array
     */
    public static Bitmap[] createAnimation(Bitmap source, int rows, int cols, int row, float scale) {
        Bitmap[] animation = new Bitmap[cols];
        Bitmap scaled = resizeImage(source, scale);
        for (int i = 0; i < cols; i++) {
            animation[i] = createSubImageAt(scaled, rows, cols, row, i);
        }
        return animation;
    }

    /**
     * Create an animation from the requested row from the given source image
     * @param source The source image
     * @param rows The total rows
     * @param cols The total columns
     * @param row The row to animate
     * @param width The desired frame width
     * @param height The desired frame height
     * @return The animation array
     */
    public static Bitmap[] createAnimation(Bitmap source, int rows, int cols, int row, int width, int height) {
        Bitmap[] animation = new Bitmap[cols];
        for (int i = 0; i < cols; i++) {
            animation[i] = resizeImage(createSubImageAt(source, rows, cols, row, i), width, height);
        }
        return animation;
    }

    /**
     * Resize/Scale an image
     * @param image The image to resize
     * @param scale The scalar value
     * @return The resized image
     */
    public static Bitmap resizeImage(Bitmap image, float scale) {
        return Bitmap.createScaledBitmap(image, (int) (image.getWidth() * scale), (int) (image.getHeight() * scale), false);
    }

    /**
     * Resize/Scale an image
     * @param image The image to resize
     * @param width The desired width
     * @param height The desired height
     * @return The resized image
     */
    public static Bitmap resizeImage(Bitmap image, int width, int height) {
        return Bitmap.createScaledBitmap(image, width, height, false);
    }
}
