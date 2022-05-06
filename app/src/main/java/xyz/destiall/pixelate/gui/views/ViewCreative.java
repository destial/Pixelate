package xyz.destiall.pixelate.gui.views;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

import xyz.destiall.utility.java.events.EventHandler;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.events.controls.ControlEvent;
import xyz.destiall.pixelate.events.controls.EventTouch;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.inventory.CreativeInventory;
import xyz.destiall.pixelate.items.inventory.PlayerInventory;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Vector2;
import xyz.destiall.pixelate.utils.ViewUtils;

/**
 * Written by Rance
 */
public class ViewCreative implements View {
    private final Map<Integer, AABB> positions;
    private final HashMap<Material, Bitmap> images;
    private final CreativeInventory inventory;
    private final PlayerInventory playerInventory;
    private final Bitmap image;
    private final Vector2 exitButton;
    private final int exitButtonRadius;
    private final int scale;

    private ItemStack dragging;
    private int draggingSlot;
    private int draggingX;
    private int draggingY;

    private int page;

    public ViewCreative(PlayerInventory playerInventory) {
        this.inventory = new CreativeInventory();
        this.playerInventory = playerInventory;
        exitButton = new Vector2(Pixelate.WIDTH - 100, 100);
        exitButtonRadius = 40;
        image = ResourceManager.getBitmap(R.drawable.hotbar);
        scale = (int) (image.getWidth() * 0.8);
        positions = new HashMap<>();
        images = new HashMap<>();
        page = 0;
        Pixelate.HANDLER.registerListener(this);
    }

    @Override
    public void render(Screen screen) {
        // Exit button
        screen.circle(exitButton.getX(), exitButton.getY(), exitButtonRadius, Color.RED);

        // Creative inventory (top)
        int starting = (int) (Pixelate.WIDTH / 2 - image.getWidth() * 4.5);
        int i = page * 3 * 9;
        for (int y = 0; y < ((inventory.getSize() - i) / 9); y++) {
            for (int x = 0; x < 9; x++) {
                int posX = starting + (x * image.getWidth());
                int posY = 100 + y * image.getHeight();
                screen.draw(image, posX, posY);
                ItemStack item = inventory.getItem(i);
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

        // Player inventory (bottom)
        i = inventory.getSize();
        for (int y = 0; y < (playerInventory.getSize() / 9); y++) {
            for (int x = 0; x < 9; x++) {
                int posX = starting + (x * image.getWidth());
                int posY = 600 + (y * image.getHeight());
                screen.draw(image, posX, posY);
                ItemStack item = playerInventory.getItem(i - inventory.getSize());
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
    public void update() {}

    @EventHandler
    private void onTouch(EventTouch e) {
        float x = e.getX();
        float y = e.getY();
        if (e.getAction() == ControlEvent.Action.DOWN) {
            if (isOnExit(x, y)) {
                Pixelate.getHud().setCreative(null);
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
                    draggingSlot = inventory.getSlot(dragging);
                    if (draggingSlot == -1) {
                        draggingSlot = playerInventory.getSlot(dragging) + inventory.getSize();
                    }
                }
            }
            return;
        }
        if (e.getAction() == ControlEvent.Action.UP) {
            if (dragging != null) {
                int slot = getSlot(x, y);
                if (slot == -1) {
                    dragging = null;
                    return;
                }
                ItemStack itemStack = getItem(x, y);
                if (itemStack == dragging) {
                    dragging = null;
                    return;
                }
                if (itemStack == null) {
                    if (slot >= inventory.getSize()) {
                        playerInventory.setItem(slot - inventory.getSize(), dragging.cloneItem());
                    } else {
                        inventory.setItem(slot, dragging);
                    }
                    if (draggingSlot >= inventory.getSize()) {
                        playerInventory.setItem(draggingSlot - inventory.getSize(), null);
                    }
                } else if (itemStack.similar(dragging)) {
                    if (draggingSlot >= inventory.getSize()) {
                        playerInventory.setItem(draggingSlot - inventory.getSize(), null);
                    }
                    itemStack.addAmount(dragging.getAmount());
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
        if (slot >= inventory.getSize()) {
            return playerInventory.getItem(slot - inventory.getSize());
        }
        return inventory.getItem(slot);
    }

    private boolean isOnExit(float x, float y) {
        return exitButton.distanceSquared(x, y) <= exitButtonRadius * exitButtonRadius;
    }

    @Override
    public void destroy() {
        positions.clear();
        images.clear();
        Pixelate.HANDLER.unregisterListener(this);
    }
}
