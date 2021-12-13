package xyz.destiall.pixelate.gui;

import android.graphics.Color;

import xyz.destiall.java.events.Listener;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.items.inventory.FurnaceInventory;
import xyz.destiall.pixelate.items.inventory.PlayerInventory;
import xyz.destiall.pixelate.timer.Timer;

public class HUD implements Updateable, Renderable, Listener {
    public static final HUD INSTANCE = new HUD();

    private final ViewHotbar hotbar;
    private final ViewControls buttons;
    private ViewInventory inventory;
    //private ViewFurnace inventory;
    private ViewFurnace furnaceUI;
    private ViewPaused pauseMenu;
    private HUD_DISPLAYTYPE displayType;

    public enum HUD_DISPLAYTYPE
    {
        Hotbar,
        Inventory_Crafting,
        Inventory_Furnace,
        Gamepause
    }

    private HUD() {
        buttons = new ViewControls();
        hotbar = new ViewHotbar(null);
        inventory = null;
        displayType = HUD_DISPLAYTYPE.Hotbar;
        Pixelate.HANDLER.registerListener(this);
    }

    public ViewHotbar getHotbar() {
        return hotbar;
    }

    public void setHotbar(PlayerInventory playerInventory) {
        hotbar.setInventory(playerInventory);
    }

    public void returnToGame()
    {
        if(displayType == HUD_DISPLAYTYPE.Gamepause)
        {
            Pixelate.PAUSED = false;
            displayType = HUD_DISPLAYTYPE.Hotbar;
            pauseMenu.destroy();
            pauseMenu = null;
        }
    }

    public void setPauseMenu()
    {
        displayType = HUD_DISPLAYTYPE.Gamepause;
        this.pauseMenu = new ViewPaused();
    }

    public void setInventory(PlayerInventory playerInventory) {
        displayType = HUD_DISPLAYTYPE.Inventory_Crafting;
        if (playerInventory == null) {
            if (this.inventory != null) {
                this.inventory.destroy();
            }
            this.inventory = null;
            this.displayType = HUD_DISPLAYTYPE.Hotbar;
            return;
        }
        buttons.setJoystick(false);
        buttons.setSwinging(false);
        buttons.setActuator(0, 0);
        setHotbar(playerInventory);
        this.inventory = new ViewInventory(playerInventory);
    }

    public void setFurnaceDisplay(PlayerInventory playerInventory, FurnaceInventory furnaceInventory)
    {
        displayType = HUD_DISPLAYTYPE.Inventory_Furnace;
        if (playerInventory == null || furnaceInventory == null) {
            if(this.furnaceUI != null)
                furnaceUI.destroy();
            this.furnaceUI = null;
            this.displayType = HUD_DISPLAYTYPE.Hotbar;
            return;
        }
        buttons.setJoystick(false);
        buttons.setSwinging(false);
        buttons.setActuator(0, 0);
        setHotbar(playerInventory);
        this.furnaceUI = new ViewFurnace(playerInventory, furnaceInventory);
    }

    @Override
    public void render(Screen screen) {
        switch(displayType)
        {
            case Inventory_Crafting:
                if(inventory != null)
                    inventory.render(screen);
                break;
            case Inventory_Furnace:
                if(furnaceUI != null)
                    furnaceUI.render(screen);
                break;
            case Gamepause:
                if(pauseMenu != null)
                    pauseMenu.render(screen);
                break;
            default: //Hotbar
                buttons.render(screen);
                hotbar.render(screen);
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
