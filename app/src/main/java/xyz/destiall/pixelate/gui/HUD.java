package xyz.destiall.pixelate.gui;

import xyz.destiall.utility.java.events.Listener;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.environment.tiles.containers.EnchantTableTile;
import xyz.destiall.pixelate.environment.tiles.containers.AnvilTile;
import xyz.destiall.pixelate.environment.tiles.containers.FurnanceTile;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.gui.views.ViewAnvil;
import xyz.destiall.pixelate.gui.views.ViewChest;
import xyz.destiall.pixelate.gui.views.ViewControls;
import xyz.destiall.pixelate.gui.views.ViewCraftingTable;
import xyz.destiall.pixelate.gui.views.ViewCreative;
import xyz.destiall.pixelate.gui.views.ViewDeath;
import xyz.destiall.pixelate.gui.views.ViewEnchantingTable;
import xyz.destiall.pixelate.gui.views.ViewFurnace;
import xyz.destiall.pixelate.gui.views.ViewHotbar;
import xyz.destiall.pixelate.gui.views.ViewInventory;
import xyz.destiall.pixelate.gui.views.ViewPaused;
import xyz.destiall.pixelate.gui.views.ViewShop;
import xyz.destiall.pixelate.items.inventory.ChestInventory;
import xyz.destiall.pixelate.items.inventory.PlayerInventory;

/**
 * Written by Rance & Yong Hong
 */
public class HUD implements Updateable, Renderable, Listener {
    private final ViewHotbar hotbar;
    private final ViewControls buttons;
    private ViewInventory inventory;
    private ViewFurnace furnace;
    private ViewChest chest;
    private ViewPaused pauseMenu;
    private ViewCreative creative;
    private ViewDeath respawnMenu;
    private ViewShop shopMenu;
    private ViewCraftingTable craftingTable;
    private ViewEnchantingTable enchantingTable;
    private ViewAnvil anvil;
    private DisplayType displayType;

    public enum DisplayType {
        GAME_VIEW,
        PLAYER_INVENTORY,
        FURNACE_INVENTORY,
        CHEST_INVENTORY,
        CREATIVE_INVENTORY,
        CRAFTING_TABLE,
        ENCHANTING_TABLE,
        ANVIL_INVENTORY,
        PAUSE_GAME,
        RESPAWN_MENU,
        SHOP_MENU
    }

    public HUD() {
        buttons = new ViewControls();
        hotbar = new ViewHotbar(null);
        inventory = null;
        displayType = DisplayType.GAME_VIEW;
        Pixelate.HANDLER.registerListener(this);
    }

    public void destroy() {
        if (buttons != null) buttons.destroy();
        if (hotbar != null) hotbar.destroy();
        if (inventory != null) inventory.destroy();
        if (furnace != null) furnace.destroy();
        if (creative != null) creative.destroy();
        if (chest != null) chest.destroy();
        if (craftingTable != null) craftingTable.destroy();
        if (shopMenu != null) shopMenu.destroy();
        if (respawnMenu != null) respawnMenu.destroy();
        if (pauseMenu != null) pauseMenu.destroy();
        if (enchantingTable != null) enchantingTable.destroy();
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

    public void setEnchantingTable(EnchantTableTile tile, PlayerInventory playerInventory) {
        if (playerInventory == null) {
            if (enchantingTable != null) {
                enchantingTable.destroy();
            }
            enchantingTable = null;
            displayType = DisplayType.GAME_VIEW;
            return;
        }
        enchantingTable = new ViewEnchantingTable(tile, playerInventory);
        displayType = DisplayType.ENCHANTING_TABLE;
    }

    public void setCraftingTable(PlayerInventory playerInventory) {
        if (playerInventory == null) {
            if (craftingTable != null) {
                craftingTable.destroy();
                craftingTable = null;
            }
            displayType = DisplayType.GAME_VIEW;
            return;
        }
        craftingTable = new ViewCraftingTable(playerInventory);
        displayType = DisplayType.CRAFTING_TABLE;
    }

    public void returnToGame() {
        if (displayType == DisplayType.PAUSE_GAME) {
            Pixelate.PAUSED = false;
            displayType = DisplayType.GAME_VIEW;
            if (pauseMenu != null) {
                pauseMenu.destroy();
                pauseMenu = null;
            }
        } else if (displayType == DisplayType.RESPAWN_MENU) {
            displayType = DisplayType.GAME_VIEW;
            if (respawnMenu != null) {
                respawnMenu.destroy();
                respawnMenu = null;
            }
        } else if (displayType == DisplayType.SHOP_MENU) {
            displayType = DisplayType.GAME_VIEW;
            if (shopMenu != null) {
                shopMenu.destroy();
                shopMenu = null;
            }
        }
    }

    public void setRespawnMenu() {
        displayType = DisplayType.RESPAWN_MENU;
        this.respawnMenu = new ViewDeath();
    }

    public void setPauseMenu() {
        displayType = DisplayType.PAUSE_GAME;
        this.pauseMenu = new ViewPaused();
    }

    public void setShopMenu() {
        displayType = DisplayType.SHOP_MENU;
        this.shopMenu = new ViewShop();
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

    public void setAnvilDisplay(PlayerInventory playerInventory, AnvilTile tile) {
        if (playerInventory == null || tile == null) {
            if (anvil != null) anvil.destroy();
            anvil = null;
            displayType = DisplayType.GAME_VIEW;
            return;
        }
        displayType = DisplayType.ANVIL_INVENTORY;
        buttons.setJoystick(false);
        setHotbar(playerInventory);
        anvil = new ViewAnvil(playerInventory, tile);
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
            case CRAFTING_TABLE:
                if (craftingTable != null)
                    craftingTable.render(screen);
                break;
            case ENCHANTING_TABLE:
                if (enchantingTable != null)
                    enchantingTable.render(screen);
                break;
            case PAUSE_GAME:
                if (pauseMenu != null)
                    pauseMenu.render(screen);
                break;
            case RESPAWN_MENU:
                if (respawnMenu != null)
                    respawnMenu.render(screen);
                break;
            case SHOP_MENU:
                if (shopMenu != null)
                    shopMenu.render(screen);
                break;
            case ANVIL_INVENTORY:
                if(anvil != null)
                    anvil.render(screen);
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
            case CRAFTING_TABLE:
                if (craftingTable != null)
                    craftingTable.update();
                break;
            case ENCHANTING_TABLE:
                if (enchantingTable != null)
                    enchantingTable.update();
                break;
            case PAUSE_GAME:
                if (pauseMenu != null)
                    pauseMenu.update();
                break;
            case RESPAWN_MENU:
                if (respawnMenu != null)
                    respawnMenu.update();
                break;
            case SHOP_MENU:
                if (shopMenu != null)
                    shopMenu.update();
                break;
            case ANVIL_INVENTORY:
                if (anvil != null)
                    anvil.update();
                break;
            default: // Game View
                buttons.update();
                hotbar.update();
        }
    }
}
