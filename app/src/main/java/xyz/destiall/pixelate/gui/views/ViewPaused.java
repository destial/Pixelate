package xyz.destiall.pixelate.gui.views;

import android.graphics.Bitmap;
import android.graphics.Color;

import xyz.destiall.utility.java.events.EventHandler;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.events.controls.ControlEvent;
import xyz.destiall.pixelate.events.controls.EventGamePause;
import xyz.destiall.pixelate.events.controls.EventTouch;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.position.Vector2;

/**
 * Written by Yong Hong
 */
public class ViewPaused implements View {
    private final Vector2 resumeButton;
    private final int resumeButtonRadius;

    private final Vector2 exitButton;
    private final int exitButtonRadius;

    private static Bitmap bg;

    private final int scale;

    public ViewPaused() {
        resumeButton = new Vector2(Pixelate.WIDTH * 0.6, Pixelate.HEIGHT * 0.6);
        resumeButtonRadius = 40;
        exitButton = new Vector2(Pixelate.WIDTH * 0.4, Pixelate.HEIGHT * 0.6);
        exitButtonRadius = 40;

        if (bg == null) {
            bg = ResourceManager.getBitmap(R.drawable.background);
            bg = Imageable.resizeImage(bg, (float) Pixelate.HEIGHT / bg.getHeight());
        }

        Bitmap image = ResourceManager.getBitmap(R.drawable.hotbar);
        scale = (int) (image.getWidth() * 0.8);
        Pixelate.HANDLER.registerListener(this);
    }

    @Override
    public void render(Screen screen) {
        screen.draw(bg, 0, 0);
        screen.circle(resumeButton.getX(), resumeButton.getY(), resumeButtonRadius, Color.GREEN);
        screen.circle(exitButton.getX(), exitButton.getY(), exitButtonRadius, Color.RED);
        screen.text("Exit", exitButton.getX() - 35 , exitButton.getY() + 90, 40, Color.WHITE);
        screen.text("Resume", resumeButton.getX() - 75, resumeButton.getY() + 90, 40, Color.WHITE);
    }

    @Override
    public void update() {}

    @EventHandler
    private void onTouch(EventTouch e) {
        float x = e.getX();
        float y = e.getY();
        if (e.getAction() == ControlEvent.Action.DOWN) {
            if (isOnResume(x, y)) {
                Pixelate.getHud().returnToGame();
            } else if (isOnExit(x,y)) {
                // RETURN MAIN MENU;
                Pixelate.HANDLER.call(new EventGamePause());
                Pixelate.getHud().returnToGame();
            }
        }
    }

    private boolean isOnResume(float x, float y) {
        return resumeButton.distanceSquared(x, y) <= resumeButtonRadius * resumeButtonRadius;
    }

    private boolean isOnExit(float x, float y) {
        return exitButton.distanceSquared(x, y) <= exitButtonRadius * exitButtonRadius;
    }

    @Override
    public void destroy() {
        Pixelate.HANDLER.unregisterListener(this);
    }
}
