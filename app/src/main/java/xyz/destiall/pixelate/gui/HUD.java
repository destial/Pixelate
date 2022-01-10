package xyz.destiall.pixelate.gui;

import xyz.destiall.java.events.Listener;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.environment.tiles.containers.FurnanceTile;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.items.inventory.ChestInventory;
import xyz.destiall.pixelate.items.inventory.PlayerInventory;

/**
 * Written by Rance & Yong Hong
 */
public class HUD implements Updateable, Renderable, Listener {
    public static HUD INSTANCE = get();

    private final ViewHotbar hotbar;
    private final ViewControls buttons;
    private ViewInventory inventory;
    private ViewFurnace furnace;
    private ViewChest chest;
    private ViewPaused pauseMenu;
    private ViewCreative creative;
    private DisplayType displayType;

    public enum DisplayType {
        GAME_VIEW,
        PLAYER_INVENTORY,
        FURNACE_INVENTORY,
        CHEST_INVENTORY,
        CREATIVE_INVENTORY,
        PAUSE_GAME
    }

    private static HUD get() {
        if (INSTANCE == null) {
            new HUD();
        }
        return INSTANCE;
    }

    private HUD() {
        INSTANCE = this;
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

    public void setCreative(PlayerInventory playerInventory) {
        if (playerInventory == null) {
            if (creative != null) {
                creative.destroy();
                creative = null;
            }
            displayType = DisplayType.GAME_VIEW;
            return;
        }
        displayType = DisplayType.CREATIVE_INVENTORY;
        creative = new ViewCreative(playerInventory);
    }

    public void returnToGame() {
        if (displayType == DisplayType.PAUSE_GAME) {
            Pixelate.PAUSED = false;
            displayType = DisplayType.GAME_VIEW;
            if (pauseMenu != null) {
                pauseMenu.destroy();
                pauseMenu = null;
            }
        }
    }

    public void setPauseMenu() {
        displayType = DisplayType.PAUSE_GAME;
        this.pauseMenu = new ViewPaused();
    }

    public void setInventory(PlayerInventory playerInventory) {
        if (playerInventory == null) {
            if (inventory != null) {
                inventory.destroy();
            }
            inventory = null;
            displayType = DisplayType.GAME_VIEW;
            return;
        }
        displayType = DisplayType.PLAYER_INVENTORY;
        buttons.setJoystick(false);
        buttons.setActuator(0, 0);
        setHotbar(playerInventory);
        inventory = new ViewInventory(playerInventory);
    }

    public void setFurnaceDisplay(PlayerInventory playerInventory, FurnanceTile tile) {
        if (playerInventory == null || tile == null) {
            if (furnace != null) furnace.destroy();
            furnace = null;
            displayType = DisplayType.GAME_VIEW;
            return;
        }
        displayType = DisplayType.FURNACE_INVENTORY;
        buttons.setJoystick(false);
        buttons.setActuator(0, 0);
        setHotbar(playerInventory);
        furnace = new ViewFurnace(playerInventory, tile);
    }

    public void setChestDisplay(PlayerInventory playerInventory, ChestInventory chestInventory) {
        displayType = DisplayType.CHEST_INVENTORY;
        if (playerInventory == null || chestInventory == null) {
            if (chest != null) chest.destroy();
            chest = null;
            displayType = DisplayType.GAME_VIEW;
            return;
        }
        buttons.setJoystick(false);
        buttons.setActuator(0, 0);
        setHotbar(playerInventory);
        chest = new ViewChest(playerInventory, chestInventory);
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
            case CREATIVE_INVENTORY:
                if (creative != null)
                    creative.render(screen);
                break;
            case PAUSE_GAME:
                if (pauseMenu != null)
                    pauseMenu.render(screen);
                break;
            default: // Game View
                buttons.render(screen);
                hotbar.render(screen);
        }
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
            case CREATIVE_INVENTORY:
                if (creative != null)
                    creative.update();
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
