package xyz.destiall.pixelate.gui.views;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

import xyz.destiall.utility.java.events.EventHandler;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.environment.tiles.containers.FurnanceTile;
import xyz.destiall.pixelate.events.controls.ControlEvent;
import xyz.destiall.pixelate.events.controls.EventTouch;
import xyz.destiall.pixelate.graphics.Glint;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.inventory.FurnaceInventory;
import xyz.destiall.pixelate.items.inventory.PlayerInventory;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Vector2;
import xyz.destiall.pixelate.utils.ViewUtils;

/**
 * Written by Rance & Yong Hong
 */
public class ViewFurnace implements View {
    private final Map<Integer, AABB> positions;
    private final HashMap<Material, Bitmap> images;
    private final PlayerInventory playerInventory;
    private final FurnaceInventory furnaceInventory;
    private final FurnanceTile furnace;
    private final Bitmap image;
    private final Vector2 exitButton;
    private final int exitButtonRadius;
    private final int scale;

    private ItemStack dragging;
    private int draggingSlot;
    private int draggingX;
    private int draggingY;

    public ViewFurnace(PlayerInventory playerInventory, FurnanceTile tile) {
        this.furnace = tile;
        this.playerInventory = playerInventory;
        this.furnaceInventory = tile.getInventory();
        exitButton = new Vector2(Pixelate.WIDTH - 100, 100);
        exitButtonRadius = 40;
        image = ResourceManager.getBitmap(R.drawable.hotbar);
        scale = (int) (image.getWidth() * 0.8);
        positions = new HashMap<>();
        images = new HashMap<>();
        Pixelate.HANDLER.registerListener(this);
    }

    @Override
    public void render(Screen screen) {
        screen.circle(exitButton.getX(), exitButton.getY(), exitButtonRadius, Color.RED);
        int startingCrafting = Pixelate.WIDTH / 2 - image.getWidth();
        int posX, posY;
        ItemStack item;

        // Smelting slot
        posX = startingCrafting;
        posY = (int)(Pixelate.HEIGHT * 0.1f);
        if (!positions.containsKey(98)) {
            positions.put(98, new AABB(posX, posY, posX + image.getWidth(), posY + image.getHeight()));
        }
        screen.draw(image, posX, posY);
        item = furnaceInventory.getToSmeltSlot();
        if (item != null) {
            Bitmap image;
            if (images.containsKey(item.getType())) {
                image = images.get(item.getType());
            } else {
                image = Bitmap.createScaledBitmap(item.getImage(), scale, scale, true);
                images.put(item.getType(), image);
            }
            int drawX, drawY;
            if (item == dragging) {
                drawX = (int) (draggingX - image.getWidth() / 2f);
                drawY = (int) (draggingY - image.getHeight() / 2f);
                ViewUtils.displayItemDescription(screen, this.image, drawX, drawY, item);
            } else {
                drawX = posX + 15;
                drawY = posY + 15;
            }
            screen.draw(image, drawX, drawY);
            if (item.getAmount() > 1) {
                screen.text("" + item.getAmount(),
                        drawX + this.image.getWidth() / 2f,
                        drawY + this.image.getHeight() / 2f,
                        40, Color.WHITE);
            }
            ItemStack.renderInventory(screen, item, drawX, drawY);
        }

        posY = (int)(Pixelate.HEIGHT * 0.1f) + (int) (image.getWidth() * 1.5);
        // Progress slot
        screen.bar(posX + image.getWidth() * 0.1, posY - (int)(image.getWidth() * 0.45), Tile.SIZE, 25, Color.GRAY, Color.BLUE, furnace.getSmeltProgress() / furnace.getTimeToSmelt());
        screen.bar(posX + image.getWidth() * 0.1, posY - (int)(image.getWidth() * 0.25), Tile.SIZE, 25, Color.GRAY, Color.YELLOW, furnace.getBurnerRemainingPercentage());


        // Burner slot
        if (!positions.containsKey(99)) {
            positions.put(99, new AABB(posX, posY, posX + image.getWidth(), posY + image.getHeight()));
        }
        screen.draw(image, posX, posY);
        item = furnaceInventory.getBurnerSlot();
        if(item != null) {
            Bitmap image;
            if (images.containsKey(item.getType())) {
                image = images.get(item.getType());
            } else {
                image = Bitmap.createScaledBitmap(item.getImage(), scale, scale, true);
                images.put(item.getType(), image);
            }
            int drawX, drawY;
            if (item == dragging) {
                drawX = (int) (draggingX - image.getWidth() / 2f);
                drawY = (int) (draggingY - image.getHeight() / 2f);
                ViewUtils.displayItemDescription(screen, this.image, drawX, drawY, item);
            } else {
                drawX = posX + 15;
                drawY = posY + 15;
            }
            screen.draw(image, drawX, drawY);
            if (item.getAmount() > 1) {
                screen.text("" + item.getAmount(),
                        drawX + this.image.getWidth() / 2f,
                        drawY + this.image.getHeight() / 2f,
                        40, Color.WHITE);
            }
            ItemStack.renderInventory(screen, item, drawX, drawY);
        }

        // Output slot
        int cOutX = startingCrafting + (3 * image.getWidth());
        int cOutY = (int)(Pixelate.HEIGHT * 0.1f) + (image.getWidth());
        if (!positions.containsKey(100)) {
            positions.put(100, new AABB(cOutX, cOutY, cOutX + image.getWidth(), cOutY + image.getHeight()));
        }
        posX = cOutX;
        posY = cOutY;
        screen.draw(image, posX, posY);
        item = furnaceInventory.getProcessedSlot();
        if (item != null) {
            Bitmap image;
            if (images.containsKey(item.getType())) {
                image = images.get(item.getType());
            } else {
                image = Bitmap.createScaledBitmap(item.getImage(), scale, scale, true);
                images.put(item.getType(), image);
            }

            int drawX, drawY;
            if (item == dragging) {
                drawX = (int) (draggingX - image.getWidth() / 2f);
                drawY = (int) (draggingY - image.getHeight() / 2f);
                ViewUtils.displayItemDescription(screen, this.image, drawX, drawY, item);
            } else {
                drawX = posX + 15;
                drawY = posY + 15;
            }
            screen.draw(image, drawX, drawY);
            if (item.getAmount() > 1) {
                screen.text("" + item.getAmount(),
                        drawX + this.image.getWidth() / 2f,
                        drawY + this.image.getHeight() / 2f,
                        40, Color.WHITE);
            }
            ItemStack.renderInventory(screen, item, drawX, drawY);
        }

        // Player inventory (bottom)
        int starting = (int) (Pixelate.WIDTH / 2 - image.getWidth() * 4.5);
        int i = 0;
        for (int y = 0; y < (playerInventory.getSize() / 9); y++) {
            for (int x = 0; x < 9; x++) {
                posX = starting + (x * image.getWidth());
                posY = (int)(Pixelate.HEIGHT * 0.5f) + (y * image.getHeight());
                screen.draw(image, posX, posY);
                item = playerInventory.getItem(i);
                if (item != null) {
                    Bitmap image;
                    if (images.containsKey(item.getType())) {
                        image = images.get(item.getType());
                    } else {
                        image = Bitmap.createScaledBitmap(item.getImage(), scale, scale, true);
                        images.put(item.getType(), image);
                    }
                    int drawX, drawY;
                    if (item == dragging) {
                        drawX = (int) (draggingX - image.getWidth() / 2f);
                        drawY = (int) (draggingY - image.getHeight() / 2f);
                        ViewUtils.displayItemDescription(screen, this.image, drawX, drawY, item);
                    } else {
                        drawX = posX + 15;
                        drawY = posY + 15;
                    }
                    screen.draw(image, drawX, drawY);
                    if (item.getAmount() > 1) {
                        screen.text("" + item.getAmount(),
                                drawX + this.image.getWidth() / 2f,
                                drawY + this.image.getHeight() / 2f,
                                40, Color.WHITE);
                    }
                    ItemStack.renderInventory(screen, item, drawX, drawY);
                }
                if (!positions.containsKey(i)) {
                    positions.put(i, new AABB(posX, posY, posX + image.getWidth(), posY + image.getHeight()));
                }
                i++;
            }
        }
    }

    @Override
    public void update() {
        Glint.INSTANCE.update();
    }

    @EventHandler
    private void onTouch(EventTouch e) {
        float x = e.getX();
        float y = e.getY();
        if (e.getAction() == ControlEvent.Action.DOWN) {
            int slot = getSlot(x, y);
            if (slot == 100) {
                ItemStack processed = furnaceInventory.getProcessedSlot();
                if(processed != null) {
                    playerInventory.addItem(processed);
                    furnaceInventory.setProcessedSlot(null);
                }
                return;
            }
            if (isOnExit(x, y)) {
                Pixelate.getHud().setFurnaceDisplay(null, null);
            }
            return;
        }
        if (e.getAction() == ControlEvent.Action.MOVE) {
            if (dragging != null) {
                draggingX = (int) x;
                draggingY = (int) y;
            } else {
                ItemStack item = getItem(x, y);
                if (item != null) {
                    dragging = item;
                    draggingX = (int) x;
                    draggingY = (int) y;
                    draggingSlot = getSlot(x, y);
                }
            }
            return;
        }
        if (e.getAction() == ControlEvent.Action.UP) {
            if (dragging != null) {
                int slot = getSlot(x, y);
                if (slot == -1 || slot == 100 || draggingSlot == slot) {
                    dragging = null;
                    return;
                }
                ItemStack itemStack = getItem(x, y);
                if (itemStack == dragging) {
                    dragging = null;
                    return;
                }
                if (itemStack == null) {
                    if (slot >= playerInventory.getSize()) {
                        if (slot == 98) {
                            furnaceInventory.setToSmeltSlot(dragging);
                        } else if (slot == 99) {
                            furnaceInventory.setBurnerSlot(dragging);
                        }
                    } else {
                        playerInventory.setItem(slot, dragging);
                    }
                    if (draggingSlot == 98)
                        furnaceInventory.setToSmeltSlot(null);
                    else if (draggingSlot == 99)
                        furnaceInventory.setBurnerSlot(null);
                    else
                        playerInventory.setItem(draggingSlot, null);
                } else if (itemStack.similar(dragging)) {
                    if (slot>= playerInventory.getSize()) {
                        if (slot == 98) {
                            furnaceInventory.getToSmeltSlot().addAmount(dragging.getAmount());
                        } else if (slot == 99) {
                            furnaceInventory.getBurnerSlot().addAmount(dragging.getAmount());
                        }
                    } else {
                        if (draggingSlot == 98)
                            furnaceInventory.setToSmeltSlot(null);
                        else if (draggingSlot == 99)
                            furnaceInventory.setBurnerSlot(null);
                        itemStack.addAmount(dragging.getAmount());
                    }
                }
                dragging = null;
            }
        }
    }

    private int getSlot(float x, float y) {
        Map.Entry<Integer, AABB> aabbs = positions.entrySet().stream().filter(en -> en.getValue().isOverlap(x, y)).findFirst().orElse(null);
        if (aabbs == null) return -1;
        return aabbs.getKey();
    }

    private ItemStack getItem(float x, float y) {
        int slot = getSlot(x, y);
        if (slot == -1) return null;
        if (slot >= playerInventory.getSize()) {
            switch(slot) {
                case 98:
                    return furnaceInventory.getToSmeltSlot();
                case 99:
                    return furnaceInventory.getBurnerSlot();
                case 100:
                    return furnaceInventory.getProcessedSlot();
            }
        }
        return playerInventory.getItem(slot);
    }

    private boolean isOnExit(float x, float y) {
        return exitButton.distanceSquared(x, y) <= exitButtonRadius * exitButtonRadius;
    }

    @Override
    public void destroy() {
        positions.clear();
        Pixelate.HANDLER.unregisterListener(this);
    }
}
