package xyz.destiall.pixelate.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.position.Vector2;

public class Screen {
    public static Canvas CANVAS;
    private static Vector2 offset;
    private static final Vector2 displayCenter = new Vector2();
    private final Paint textPaint;
    private final Paint paint;
    private int drawCalls;

    public Screen(Canvas canvas, Entity center, int width, int height) {
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        paint = new Paint();
        update(canvas, center, width, height);
    }

    public void update(Canvas canvas, Entity center, int width, int height) {
        Screen.CANVAS = canvas;
        Location gameCenter = center.getLocation();
        displayCenter.set(width * 0.5, height * 0.5);
        offset = displayCenter.subtract(center.getBounds().getWidth() * 0.5, center.getBounds().getHeight() * 0.5).subtract(gameCenter.getRawX(), gameCenter.getRawY());
        constraint();

        drawCalls = 0;
    }

    public int getDrawCalls() {
        return drawCalls;
    }

    /**
     * Convert worldSpace to screenSpace
     * @param worldSpace Vector to convert
     * @return screenSpace vector
     */
    public static Vector2 convert(@NonNull Vector2 worldSpace) {
        return worldSpace.clone().add(offset);
    }

    /**
     * Convert worldSpace to screenSpace
     * @param location Location to convert
     * @return screenSpace vector
     */
    public static Vector2 convert(@NonNull Location location) {
        return convert(location.toVector());
    }

    /**
     * Convert screenSpace to worldSpace
     * @param screenSpace Vector to convert
     * @return worldSpace vector
     */
    public static Vector2 world(@NonNull Vector2 screenSpace) {
        return screenSpace.clone().subtract(offset);
    }

    /**
     * Draw an image
     * @param image Image to draw
     * @param x Top left x
     * @param y Top left y
     */
    public void draw(Bitmap image, double x, double y) {
        CANVAS.drawBitmap(image, (float) x, (float) y, null);
        drawCalls++;
    }

    /**
     * Draw a text
     * @param text Text to draw
     * @param x Top left x
     * @param y Top left y
     * @param size Size of text
     * @param color Color of text
     */
    public void text(String text, double x, double y, double size, int color) {
        textPaint.setTextSize((float) size);
        textPaint.setColor(color);
        CANVAS.drawText(text, (float) x, (float) y, textPaint);
        drawCalls++;
    }

    /**
     * Draw a circle
     * @param x Center x
     * @param y Center y
     * @param radius Radius of circle
     * @param color Color of circle
     */
    public void circle(double x, double y, double radius, int color) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        CANVAS.drawCircle((float) x, (float) y, (float) radius, paint);
        drawCalls++;
    }

    /**
     * Draw a ring
     * @param x Center x
     * @param y Center y
     * @param radius Radius of ring
     * @param thickness Thickness of ring
     * @param color Color of ring
     */
    public void ring(double x, double y, double radius, float thickness, int color) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(thickness);
        CANVAS.drawCircle((float) x, (float) y, (float) radius, paint);
        drawCalls++;
    }

    /**
     * Draw a quad
     * @param x Top left x
     * @param y Top left y
     * @param width Width of quad
     * @param height Height of quad
     * @param color Color of quad
     */
    public void quad(double x, double y, double width, double height, int color) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        CANVAS.drawRect((float) x, (float) y, (float) (x + width), (float) (y + height), paint);
        drawCalls++;
    }

    /**
     * Draw a ring quad
     * @param x Top left x
     * @param y Top left y
     * @param width Width of ring quad
     * @param height Height of ring quad
     * @param thickness Thickness of ring
     * @param color Color of ring quad
     */
    public void quadRing(double x, double y, double width, double height, float thickness, int color) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(thickness);
        CANVAS.drawRect((float) x, (float) y, (float) (x + width), (float) (y + height), paint);
        drawCalls++;
    }

    /**
     * Draw a progress bar
     * @param x Top left x
     * @param y Top left y
     * @param width Width of bar
     * @param height Height of bar
     * @param color Foundation color of bar
     * @param progressColor Progression color of bar
     * @param progress Current progress (between 0 and 1)
     */
    public void bar(double x, double y, double width, double height, int color, int progressColor, float progress) {
        quad(x, y, width, height, color);
        if (progress > 0.f) {
            float progressWidth = (float) (width * progress);
            quad(x, y, progressWidth, height, progressColor);
        }
    }

    private void constraint() {
        if (offset.getX() > Pixelate.WIDTH) {
            offset.setX(Pixelate.WIDTH);
        }
        if (offset.getY() > Pixelate.HEIGHT) {
            offset.setY(Pixelate.HEIGHT);
        }
        if (offset.getX() < -Tile.SIZE) {
            offset.setX(-Tile.SIZE);
        }
        if (offset.getY() < -Tile.SIZE * 0.25) {
            offset.setY(-Tile.SIZE * 0.25);
        }
    }
}
