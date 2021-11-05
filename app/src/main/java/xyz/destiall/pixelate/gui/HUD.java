package xyz.destiall.pixelate.gui;

import android.graphics.Color;
import android.graphics.Paint;

import xyz.destiall.java.events.Listener;
import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.items.Inventory;
import xyz.destiall.pixelate.timer.Timer;

public class HUD implements Updateable, Renderable, Listener {
    private final ViewHotbar hotbar;
    private final ViewControls buttons;
    private final Paint textPaint;
    private ViewInventory inventory;

    public static final HUD INSTANCE = new HUD();

    private HUD() {
        buttons = new ViewControls();
        hotbar = new ViewHotbar(null);
        inventory = null;
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(60);
        Game.HANDLER.registerListener(this);
    }

    public ViewHotbar getHotbar() {
        return hotbar;
    }

    public void setHotbar(Inventory inventory) {
        hotbar.setInventory(inventory);
    }

    public void setInventory(Inventory inventory) {
        if (inventory == null) {
            if (this.inventory != null) {
                this.inventory.destroy();
            }
            this.inventory = null;
            return;
        }
        buttons.setJoystick(false);
        buttons.setMining(false);
        buttons.setActuator(0, 0);
        this.inventory = new ViewInventory(inventory);
    }

    @Override
    public void render(Screen screen) {
        if (inventory == null) {
            buttons.render(screen);
            hotbar.render(screen);
        } else {
            inventory.render(screen);
        }
        screen.getCanvas().drawText("FPS: " + Timer.getFPS(), 10, 50, textPaint);
        screen.getCanvas().drawText("Ticks: " + Timer.getTicksThisSecond(), 10, 110, textPaint);
        screen.getCanvas().drawText("Delta: " + Timer.getDeltaTime(), 10, 170, textPaint);
    }

    @Override
    public void update() {
        if (inventory == null) {
            buttons.update();
            hotbar.update();
        } else {
            inventory.update();
        }
    }

    @Override
    public void tick() {
        if (inventory != null) {
            inventory.tick();
        } else {
            buttons.tick();
            hotbar.tick();
        }
    }
}
