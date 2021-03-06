package xyz.destiall.pixelate.gui.buttons;

import android.graphics.Bitmap;

import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.position.Vector2;

/**
 * Written by Rance
 */
public class CircleImageButton implements Button {
    private final Vector2 center;
    private final float radius;

    private Bitmap image;
    private boolean pressed;

    private Runnable tap;
    private Runnable hold;
    private Runnable release;

    public CircleImageButton(int id, Vector2 center, float scale) {
        this.image = ResourceManager.getBitmap(id);
        this.image = scale != 1f && scale != 0f ? Imageable.resizeImage(image, scale) : image;
        this.center = center;
        this.radius = image.getWidth() * 0.5f;
    }

    @Override
    public void render(Screen screen) {
        screen.draw(image, center.getX() - (image.getWidth() * 0.5), center.getY() - (image.getHeight() * 0.5));
    }

    @Override
    public void onTap(Runnable runnable) {
        this.tap = runnable;
    }

    @Override
    public void onHold(Runnable runnable) {
        this.hold = runnable;
    }

    @Override
    public void onRelease(Runnable runnable) {
        this.release = runnable;
    }

    @Override
    public void tap() {
        if (tap == null) return;
        tap.run();
    }

    @Override
    public void hold() {
        if (hold == null) return;
        hold.run();
    }

    @Override
    public void release() {
        if (release == null) return;
        release.run();
    }

    @Override
    public void setHold(boolean hold) {
        pressed = hold;
    }

    @Override
    public boolean isOn(float x, float y) {
        return center.distanceSquared(x, y) <= radius * radius;
    }

    @Override
    public boolean isHolding() {
        return pressed;
    }
}
