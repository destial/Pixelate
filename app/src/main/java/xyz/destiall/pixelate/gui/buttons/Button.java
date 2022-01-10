package xyz.destiall.pixelate.gui.buttons;

import xyz.destiall.pixelate.graphics.Renderable;

/**
 * Written by Rance
 */
public interface Button extends Renderable {
    void onTap(Runnable runnable);
    void onHold(Runnable runnable);
    void onRelease(Runnable runnable);

    void tap();
    void hold();
    void release();
    void setHold(boolean hold);
    boolean isOn(float x, float y);
    boolean isHolding();
}
