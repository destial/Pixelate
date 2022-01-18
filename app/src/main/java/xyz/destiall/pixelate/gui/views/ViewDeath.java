package xyz.destiall.pixelate.gui.views;

import android.graphics.Bitmap;
import android.graphics.Color;

import xyz.destiall.java.events.EventHandler;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.events.controls.ControlEvent;
import xyz.destiall.pixelate.events.controls.EventGamePause;
import xyz.destiall.pixelate.events.controls.EventTouch;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.position.Vector2;
import xyz.destiall.pixelate.states.StateGame;

/**
 * Written by Yong Hong
 */
public class ViewDeath implements View {
    private final Vector2 respawnButton;
    private final int respawnButtonRadius;

    private final Vector2 exitButton;
    private final int exitButtonRadius;

    private static Bitmap bg;

    private final int scale;

    public ViewDeath() {
        respawnButton = new Vector2(Pixelate.WIDTH * 0.6, Pixelate.HEIGHT * 0.6);
        respawnButtonRadius = 40;
        exitButton = new Vector2(Pixelate.WIDTH * 0.4, Pixelate.HEIGHT * 0.6);
        exitButtonRadius = 40;

        if (bg == null) {
            bg = ResourceManager.getBitmap(R.drawable.deathscreen);
            bg = Imageable.resizeImage(bg, (float) Pixelate.HEIGHT / bg.getHeight());
        }

        Bitmap image = ResourceManager.getBitmap(R.drawable.hotbar);
        scale = (int) (image.getWidth() * 0.8);
        Pixelate.HANDLER.registerListener(this);
    }

    @Override
    public void render(Screen screen) {
        screen.draw(bg, 0, 0);
        screen.circle(respawnButton.getX(), respawnButton.getY(), respawnButtonRadius, Color.GREEN);
        screen.circle(exitButton.getX(), exitButton.getY(), exitButtonRadius, Color.RED);
        screen.text("Exit", exitButton.getX() - 35 , exitButton.getY() + 90, 40, Color.WHITE);
        screen.text("Respawn", respawnButton.getX() - 75, respawnButton.getY() + 90, 40, Color.WHITE);

        String death = "You Died!";
        screen.text(death, Pixelate.WIDTH * 0.5f - (death.length() * 20), Pixelate.HEIGHT - Pixelate.HEIGHT * 0.72, 80, Color.WHITE);

        String score = "Score:";
        screen.text(score, Pixelate.WIDTH * 0.5f - ((score.length()+2) * 15), Pixelate.HEIGHT - Pixelate.HEIGHT * 0.65, 60, Color.WHITE);

        String numScore = String.valueOf(((StateGame)Pixelate.getGSM().getState("Game")).getPlayer().getScore());
        screen.text(numScore, Pixelate.WIDTH * 0.5f + (score.length() * 0.5 + 2) * 15, Pixelate.HEIGHT - Pixelate.HEIGHT * 0.65, 60, Color.GREEN);
    }

    @Override
    public void update() {}

    @EventHandler
    private void onTouch(EventTouch e) {
        float x = e.getX();
        float y = e.getY();
        if (e.getAction() == ControlEvent.Action.DOWN) {
            if (isOnRespawn(x, y)) {
                Pixelate.getHud().returnToGame();
                ((StateGame)Pixelate.getGSM().getState("Game")).getPlayer().respawn();
            } else if (isOnExit(x,y)) {
                // RETURN MAIN MENU;
                Pixelate.HANDLER.call(new EventGamePause());
                Pixelate.getHud().returnToGame();
            }
        }
    }

    private boolean isOnRespawn(float x, float y) {
        return respawnButton.distanceSquared(x, y) <= respawnButtonRadius * respawnButtonRadius;
    }

    private boolean isOnExit(float x, float y) {
        return exitButton.distanceSquared(x, y) <= exitButtonRadius * exitButtonRadius;
    }

    @Override
    public void destroy() {
        Pixelate.HANDLER.unregisterListener(this);
    }
}