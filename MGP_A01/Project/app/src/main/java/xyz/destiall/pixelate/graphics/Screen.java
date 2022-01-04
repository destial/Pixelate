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

    public Screen(Canvas canvas, Entity center, int width, int height) {
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        paint = new Paint();
        update(canvas, center, width, height);
    }

    public void update(Canvas canvas, Entity center, int width, int height) {
        Screen.CANVAS = canvas;
        Location gameCenter = center.getLocation();
        displayCenter.set(width / 2f, height / 2f);
        offset = displayCenter.subtract(center.getBounds().getWidth() / 2, center.getBounds().getHeight() / 2).subtract(gameCenter.getRawX(), gameCenter.getRawY());
        constraint();
    }

    public static Vector2 convert(@NonNull Vector2 worldSpace) {
        return worldSpace.clone().add(offset);
    }

    public static Vector2 convert(@NonNull Location location) {
        return convert(location.toVector());
    }

    public void draw(Bitmap image, double x, double y) {
        CANVAS.drawBitmap(image, (float) x, (float) y, null);
    }

    public void text(String text, double x, double y, double size, int color) {
        textPaint.setTextSize((float) size);
        textPaint.setColor(color);
        CANVAS.drawText(text, (float) x, (float) y, textPaint);
    }

    public void circle(double x, double y, double radius, int color) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        CANVAS.drawCircle((float) x, (float) y, (float) radius, paint);
    }

    public void ring(double x, double y, double radius, float thickness, int color) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(thickness);
        CANVAS.drawCircle((float) x, (float) y, (float) radius, paint);
    }

    public void rectangle(double x, double y, double width, double height, int color) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        CANVAS.drawRect((float) x, (float) y, (float) (x + width), (float) (y + height), paint);
    }

    public void bar(double x, double y, double width, double height, int color, int progressColor, float progress) {
        float progressWidth = (float) (width * progress);
        rectangle(x, y, width, height, color);
        if (progress > 0.f) rectangle(x, y, progressWidth, height, progressColor);
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
