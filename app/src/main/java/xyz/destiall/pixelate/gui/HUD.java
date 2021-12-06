package xyz.destiall.pixelate.gui;

import android.graphics.Color;

import xyz.destiall.java.events.Listener;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.items.Inventory;
import xyz.destiall.pixelate.timer.Timer;

public class HUD implements Updateable, Renderable, Listener {
    public static final HUD INSTANCE = new HUD();

    private final ViewHotbar hotbar;
    private final ViewControls buttons;
    private ViewInventory inventory;

    private HUD() {
        buttons = new ViewControls();
        hotbar = new ViewHotbar(null);
        inventory = null;
        Pixelate.HANDLER.registerListener(this);
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
        screen.text("FPS: " + Timer.getFPS(), 10, 50, 60, Color.WHITE);
        screen.text("Ticks: " + Timer.getTicksThisSecond(), 10, 110, 60, Color.WHITE);
        screen.text("Delta: " + Timer.getDeltaTime(), 10, 170, 60, Color.WHITE);
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
        if (inventory == null) {
            buttons.tick();
            hotbar.tick();
        } else {
            inventory.tick();
        }
    }
}
