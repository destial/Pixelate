package xyz.destiall.pixelate.gui;

import android.graphics.Color;

import xyz.destiall.java.events.Listener;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.items.inventory.PlayerInventory;
import xyz.destiall.pixelate.timer.Timer;

public class HUD implements Updateable, Renderable, Listener {
    public static final HUD INSTANCE = new HUD();

    private final ViewHotbar hotbar;
    private final ViewControls buttons;
    //private ViewInventory playerInventory;
    private ViewFurnace inventory;
    private ViewFurnace furnaceUI;

    private HUD() {
        buttons = new ViewControls();
        hotbar = new ViewHotbar(null);
        inventory = null;
        Pixelate.HANDLER.registerListener(this);
    }

    public ViewHotbar getHotbar() {
        return hotbar;
    }

    public void setHotbar(PlayerInventory playerInventory) {
        hotbar.setInventory(playerInventory);
    }

    public void setInventory(PlayerInventory playerInventory) {
        if (playerInventory == null) {
            if (this.inventory != null) {
                this.inventory.destroy();
            }
            this.inventory = null;
            return;
        }
        buttons.setJoystick(false);
        buttons.setMining(false);
        buttons.setActuator(0, 0);
        //this.playerInventory = new ViewInventory(playerInventory);
        this.inventory = new ViewFurnace(playerInventory);
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
        screen.text("Delta: " + Timer.getDeltaTime(), 10, 110, 60, Color.WHITE);
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
}
