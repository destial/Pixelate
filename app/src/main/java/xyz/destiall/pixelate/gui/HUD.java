package xyz.destiall.pixelate.gui;

import android.graphics.Color;

import xyz.destiall.java.events.Listener;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.environment.tiles.containers.FurnanceTile;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.items.inventory.ChestInventory;
import xyz.destiall.pixelate.items.inventory.PlayerInventory;
import xyz.destiall.pixelate.timer.Timer;

public class HUD implements Updateable, Renderable, Listener {
    public static final HUD INSTANCE = new HUD();

    private final ViewHotbar hotbar;
    private final ViewControls buttons;
    private ViewInventory inventory;
    private ViewFurnace furnace;
    private ViewChest chest;
    private ViewPaused pauseMenu;
    private DisplayType displayType;

    public enum DisplayType {
        GAME_VIEW,
        PLAYER_INVENTORY,
        FURNACE_INVENTORY,
        CHEST_INVENTORY,
        PAUSE_GAME
    }

    private HUD() {
        buttons = new ViewControls();
        hotbar = new ViewHotbar(null);
        inventory = null;
        displayType = DisplayType.GAME_VIEW;
        Pixelate.HANDLER.registerListener(this);
    }

    public ViewHotbar getHotbar() {
        return hotbar;
    }

    public void setHotbar(PlayerInventory playerInventory) {
        hotbar.setInventory(playerInventory);
    }

    public void returnToGame() {
        if (displayType == DisplayType.PAUSE_GAME) {
            Pixelate.PAUSED = false;
            displayType = DisplayType.GAME_VIEW;
            pauseMenu.destroy();
            pauseMenu = null;
        }
    }

    public void setPauseMenu() {
        displayType = DisplayType.PAUSE_GAME;
        this.pauseMenu = new ViewPaused();
    }

    public void setInventory(PlayerInventory playerInventory) {
        displayType = DisplayType.PLAYER_INVENTORY;
        if (playerInventory == null) {
            if (this.inventory != null) {
                this.inventory.destroy();
            }
            this.inventory = null;
            this.displayType = DisplayType.GAME_VIEW;
            return;
        }
        buttons.setJoystick(false);
        buttons.setSwinging(false);
        buttons.setActuator(0, 0);
        setHotbar(playerInventory);
        this.inventory = new ViewInventory(playerInventory);
    }

    public void setFurnaceDisplay(PlayerInventory playerInventory, FurnanceTile tile) {
        displayType = DisplayType.FURNACE_INVENTORY;
        if (playerInventory == null || tile == null) {
            if (this.furnace != null) furnace.destroy();
            this.furnace = null;
            this.displayType = DisplayType.GAME_VIEW;
            return;
        }
        buttons.setJoystick(false);
        buttons.setSwinging(false);
        buttons.setActuator(0, 0);
        setHotbar(playerInventory);
        this.furnace = new ViewFurnace(playerInventory, tile);
    }

    public void setChestDisplay(PlayerInventory playerInventory, ChestInventory chestInventory) {
        displayType = DisplayType.CHEST_INVENTORY;
        if (playerInventory == null || chestInventory == null) {
            if (this.chest != null) chest.destroy();
            this.chest = null;
            this.displayType = DisplayType.GAME_VIEW;
            return;
        }
        buttons.setJoystick(false);
        buttons.setSwinging(false);
        buttons.setActuator(0, 0);
        setHotbar(playerInventory);
        this.chest = new ViewChest(playerInventory, chestInventory);
    }

    @Override
    public void render(Screen screen) {
        switch (displayType) {
            case PLAYER_INVENTORY:
                if (inventory != null)
                    inventory.render(screen);
                break;
            case FURNACE_INVENTORY:
                if (furnace != null)
                    furnace.render(screen);
                break;
            case CHEST_INVENTORY:
                if (chest != null)
                    chest.render(screen);
                break;
            case PAUSE_GAME:
                if (pauseMenu != null)
                    pauseMenu.render(screen);
                break;
            default: // Game View
                buttons.render(screen);
                hotbar.render(screen);
        }
        screen.text("FPS: " + Timer.getFPS(), 10, 50, 60, Color.WHITE);
        screen.text("Delta: " + Timer.getDeltaTime(), 10, 110, 60, Color.WHITE);
    }

    @Override
    public void update() {
        switch (displayType) {
            case PLAYER_INVENTORY:
                if (inventory != null)
                    inventory.update();
                break;
            case FURNACE_INVENTORY:
                if (furnace != null)
                    furnace.update();
                break;
            case CHEST_INVENTORY:
                if (chest != null)
                    chest.update();
                break;
            case PAUSE_GAME:
                if (pauseMenu != null)
                    pauseMenu.update();
                break;
            default: // Game View
                buttons.update();
                hotbar.update();
        }
    }
}
