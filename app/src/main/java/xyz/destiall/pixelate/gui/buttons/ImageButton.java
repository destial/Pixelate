package xyz.destiall.pixelate.gui.buttons;

import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Vector2;

public class ImageButton extends Imageable implements Button {
    private final Vector2 topleft;
    private final AABB aabb;
    private boolean pressed;

    private Runnable tap;
    private Runnable hold;
    private Runnable release;
    public ImageButton(int id, Vector2 topleft) {
        super(ResourceManager.getBitmap(id), 1, 1);
        this.topleft = topleft;
        aabb = new AABB(topleft.getX(), topleft.getY(), topleft.getX() + image.getWidth(), topleft.getY() + image.getHeight());
    }

    @Override
    public void render(Screen screen) {
        screen.draw(image, topleft.getX(), topleft.getY());
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
}
