package xyz.destiall.pixelate.gui;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

import xyz.destiall.java.events.EventHandler;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.events.ControlEvent;
import xyz.destiall.pixelate.events.EventGamePause;
import xyz.destiall.pixelate.events.EventTouch;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.crafting.Recipe;
import xyz.destiall.pixelate.items.crafting.RecipeManager;
import xyz.destiall.pixelate.items.inventory.FurnaceInventory;
import xyz.destiall.pixelate.items.inventory.PlayerInventory;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Vector2;

public class ViewPaused implements View {
    private final Map<Integer, AABB> positions;
    private final HashMap<Material, Bitmap> images;
    private final Bitmap image;

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

        if(bg == null)
        {
            bg = ResourceManager.getBitmap(R.drawable.background);
            bg = Imageable.scaleImage(bg, 0.37f);
        }

        image = ResourceManager.getBitmap(R.drawable.hotbar);
        scale = (int) (image.getWidth() * 0.8);
        positions = new HashMap<>();
        images = new HashMap<>();
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
                HUD.INSTANCE.returnToGame();
            }
            else if (isOnExit(x,y))
            {
                //RETURN MAIN MENU;
                Pixelate.HANDLER.call(new EventGamePause());
                HUD.INSTANCE.returnToGame();
            }
        }

    }



    private boolean isOnResume(float x, float y) {
        return resumeButton.distance(x, y) < resumeButtonRadius;
    }

    private boolean isOnExit(float x, float y) {
        return exitButton.distance(x, y) < exitButtonRadius;
    }



    @Override
    public void destroy() {
        positions.clear();
        Pixelate.HANDLER.unregisterListener(this);
    }
}
