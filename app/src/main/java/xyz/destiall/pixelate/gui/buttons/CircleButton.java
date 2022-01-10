package xyz.destiall.pixelate.gui.buttons;

import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.position.Vector2;

/**
 * Written by Rance
 */
public class CircleButton implements Button {
    private final Vector2 center;
    private final float radius;
    private int color;
    private boolean pressed;

    private Runnable tap;
    private Runnable hold;
    private Runnable release;

    public CircleButton(Vector2 center, float radius, int color) {
        this.center = center;
        this.radius = radius;
        this.color = color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void render(Screen screen) {
        screen.circle(center.getX(), center.getY(), radius * (pressed ? 0.9f : 1), color);
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
