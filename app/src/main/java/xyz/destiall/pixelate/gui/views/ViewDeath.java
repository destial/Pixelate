package xyz.destiall.pixelate.gui.views;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import xyz.destiall.java.events.EventHandler;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.events.controls.ControlEvent;
import xyz.destiall.pixelate.events.controls.EventGamePause;
import xyz.destiall.pixelate.events.controls.EventTouch;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.gui.HUD;
import xyz.destiall.pixelate.position.Vector2;
import xyz.destiall.pixelate.score.Scoreboard;
import xyz.destiall.pixelate.states.StateGame;
import xyz.destiall.pixelate.utils.FormatterUtils;

/**
 * Written by Yong Hong
 */
public class ViewDeath implements View {
    private final Vector2 respawnButton;
    private final int respawnButtonRadius;

    private final Vector2 exitButton;
    private final int exitButtonRadius;

    private final Vector2 scoreboardButton;
    private final int scoreboardButtonRadius;

    private static Bitmap bg;

    private final int scale;
    private boolean displayScoreboard;
    List<String> scoreboardValues;

    public ViewDeath() {
        respawnButton = new Vector2(Pixelate.WIDTH * 0.6, Pixelate.HEIGHT * 0.6);
        respawnButtonRadius = 40;
        exitButton = new Vector2(Pixelate.WIDTH * 0.4, Pixelate.HEIGHT * 0.6);
        exitButtonRadius = 40;
        scoreboardButton = new Vector2(Pixelate.WIDTH * 0.5, Pixelate.HEIGHT * 0.75);
        scoreboardButtonRadius = 40;

        displayScoreboard = false;
        scoreboardValues = new ArrayList<String>();


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

        screen.circle(scoreboardButton.getX(), scoreboardButton.getY(), scoreboardButtonRadius, Color.CYAN);
        screen.text("Scoreboard", scoreboardButton.getX() - 100, scoreboardButton.getY() + 90, 40, Color.WHITE);

        if(displayScoreboard)
        {
            screen.bar(Pixelate.WIDTH * 0.11, Pixelate.HEIGHT * 0.095, Pixelate.WIDTH * 0.8, Pixelate.HEIGHT * 0.6, Color.DKGRAY, Color.DKGRAY, 1.0f);
            for(int i =0 ; i < scoreboardValues.size(); ++i)
            {
                String split[] = scoreboardValues.get(i).split(";");
                String date = FormatterUtils.formatTime(Long.parseLong(split[1]), "EST");
                screen.bar(Pixelate.WIDTH * 0.2, Pixelate.HEIGHT * 0.16 + Pixelate.WIDTH * i * 0.065, Pixelate.WIDTH * 0.6, 10, Color.YELLOW, Color.YELLOW, 1.f);
                String displayText = (i+1)+". " + split[0] + " with " + Float.parseFloat(split[2]) + " score. (" + date+")";
                screen.text(displayText, Pixelate.WIDTH * 0.505f - (displayText.length()-1) * 12.5, Pixelate.HEIGHT * 0.14 + Pixelate.WIDTH * i * 0.065, 50, Color.WHITE);
            }

        }


    }

    @Override
    public void update() {}

    @EventHandler
    private void onTouch(EventTouch e) {
        float x = e.getX();
        float y = e.getY();
        if (e.getAction() == ControlEvent.Action.DOWN) {
            if (isOnRespawn(x, y)) {
                HUD.INSTANCE.returnToGame();
                ((StateGame)Pixelate.getGSM().getState("Game")).getPlayer().respawn();
            } else if (isOnExit(x,y)) {
                // RETURN MAIN MENU;
                Pixelate.HANDLER.call(new EventGamePause());
                HUD.INSTANCE.returnToGame();
            }
        }else if (e.getAction() == ControlEvent.Action.UP)
        {
            if (isOnScoreboard(x,y)) {
                displayScoreboard = !displayScoreboard;
                System.out.println("triggered");
                if(displayScoreboard)
                    scoreboardValues = Scoreboard.getInstance().getTopScores(5, false);
            }
        }
    }

    private boolean isOnScoreboard(float x, float y) {
        return scoreboardButton.distanceSquared(x, y) <= scoreboardButtonRadius * scoreboardButtonRadius;
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