package xyz.destiall.pixelate.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.position.Vector2;

public class Screen {
    private static Canvas canvas;
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
        Screen.canvas = canvas;
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
        canvas.drawBitmap(image, (float) x, (float) y, null);
    }

    public void text(String text, double x, double y, double size, int color) {
        textPaint.setTextSize((float) size);
        textPaint.setColor(color);
        canvas.drawText(text, (float) x, (float) y, textPaint);
    }

    public void circle(double x, double y, double radius, int color) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle((float) x, (float) y, (float) radius, paint);
    }

    public void ring(double x, double y, double radius, float thickness, int color) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(thickness);
        canvas.drawCircle((float) x, (float) y, (float) radius, paint);
    }

    private void constraint() {
        if (offset.getX() > Pixelate.WIDTH) {
            offset.setX(Pixelate.WIDTH);
        }
        if (offset.getY() > Pixelate.HEIGHT) {
            offset.setY(Pixelate.HEIGHT);
        }
        if (offset.getX() < -Pixelate.WIDTH / 2f) {
            offset.setX(-Pixelate.WIDTH / 2f);
        }
        if (offset.getY() < -Pixelate.HEIGHT / 2f) {
            offset.setY(-Pixelate.HEIGHT / 2f);
        }
    }
}
