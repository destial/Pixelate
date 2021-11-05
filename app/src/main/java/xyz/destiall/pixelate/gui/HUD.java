package xyz.destiall.pixelate.gui;

import xyz.destiall.java.events.Listener;
import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.items.Inventory;

public class HUD implements Updateable, Renderable, Listener {

    private final ViewHotbar hotbar;
    private final ViewControls buttons;
    private ViewInventory inventory;

    public static final HUD INSTANCE = new HUD();

    private HUD() {
        buttons = new ViewControls();
        hotbar = new ViewHotbar(null);
        inventory = null;
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
