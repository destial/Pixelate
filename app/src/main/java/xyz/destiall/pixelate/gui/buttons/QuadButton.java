package xyz.destiall.pixelate.gui.buttons;

import android.graphics.Color;

import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Vector2;

/**
 * Written by Rance
 */
public class QuadButton implements Button {
    private final Vector2 topleft;
    private final int w, h;
    private final AABB aabb;
    private boolean pressed;
    private int color;
    private String text;
    private int textSize;

    private Runnable tap;
    private Runnable hold;
    private Runnable release;

    public QuadButton(Vector2 topleft, int width, int height, int color) {
        this.color = color;
        this.topleft = topleft;
        this.w = width;
        this.h = height;
        aabb = new AABB(topleft.getX(), topleft.getY(), topleft.getX() + w, topleft.getY() + h);
        textSize = 50;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
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
        return aabb.isOverlap(x, y);
    }

    @Override
    public boolean isHolding() {
        return pressed;
    }

    @Override
    public void render(Screen screen) {
        screen.quad(topleft.getX(), topleft.getY(), w, h, color);
        if (text != null) {
            screen.text(text, topleft.getX(), topleft.getY() + textSize, textSize, Color.WHITE);
        }
    }
}
