package xyz.destiall.pixelate.gui.views;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

import xyz.destiall.java.events.EventHandler;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.events.controls.ControlEvent;
import xyz.destiall.pixelate.events.controls.EventTouch;
import xyz.destiall.pixelate.graphics.Glint;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.gui.HUD;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.crafting.Recipe;
import xyz.destiall.pixelate.items.inventory.PlayerInventory;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Vector2;
import xyz.destiall.pixelate.utils.ViewUtils;

/**
 * Written by Rance & Yong Hong
 */
public class ViewInventory implements View {
    private final Map<Integer, AABB> positions;
    private final HashMap<Material, Bitmap> images;
    private final PlayerInventory playerInventory;
    private final Bitmap image;
    private final Vector2 exitButton;
    private final int exitButtonRadius;
    private final int scale;

    private ItemStack dragging;
    private int draggingSlot;
    private int draggingX;
    private int draggingY;

    public ViewInventory(PlayerInventory playerInventory) {
        this.playerInventory = playerInventory;
        exitButton = new Vector2(Pixelate.WIDTH - 100, 100);
        exitButtonRadius = 40;
        image = ResourceManager.getBitmap(R.drawable.hotbar);
        scale = (int) (image.getWidth() * 0.7);
        positions = new HashMap<>();
        images = new HashMap<>();
        Pixelate.HANDLER.registerListener(this);
    }

    @Override
    public void render(Screen screen) {
        screen.circle(exitButton.getX(), exitButton.getY(), exitButtonRadius, Color.RED);
        int startingCrafting = Pixelate.WIDTH / 3 - image.getWidth();
        int a = 0;
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                int posX = startingCrafting + (x * image.getWidth());
                int posY = (int)(Pixelate.HEIGHT * 0.1f) + (y * image.getWidth());
                screen.draw(image, posX, posY);
                ItemStack item = playerInventory.getCraftingItem(a);
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
                if (!positions.containsKey(playerInventory.getSize() + a)) {
                    positions.put(playerInventory.getSize() + a, new AABB(posX, posY, posX + image.getWidth(), posY + image.getHeight()));
                }
                a++;
            }
        }
        int cOutX = startingCrafting + (6 * image.getWidth());
        int cOutY = (int)(Pixelate.HEIGHT * 0.1f) + (image.getWidth() / 2);
        if (!positions.containsKey(100)) {
            positions.put(100, new AABB(cOutX, cOutY, cOutX + image.getWidth(), cOutY + image.getHeight()));
        }
        screen.draw(image, cOutX, cOutY);
        for (Recipe recipe : Pixelate.getRecipeManager().getRecipes()) {
            if (recipe.isFulfilled(playerInventory.getCrafting())) {
                ItemStack item = recipe.getItem();
                Bitmap image;
                if (images.containsKey(item.getType())) {
                    image = images.get(item.getType());
                } else {
                    image = Bitmap.createScaledBitmap(item.getImage(), scale, scale, true);
                    images.put(item.getType(), image);
                }
                screen.draw(image, cOutX + 15, cOutY + 15);
                ItemStack.renderInventory(screen, item, cOutX + 15, cOutY + 15);
                break;
            }
        }
        int starting = (int) (Pixelate.WIDTH / 2 - image.getWidth() * 4.5);
        int i = 0;
        for (int y = 0; y < (playerInventory.getSize() / 9); y++) {
            for (int x = 0; x < 9; x++) {
                int posX = starting + (x * image.getWidth());
                int posY = (int)(Pixelate.HEIGHT * 0.50f) + (y * image.getHeight());
                screen.draw(image, posX, posY);
                ItemStack item = playerInventory.getItem(i);
                if (item != null) {
                    Bitmap image;
                    if (images.containsKey(item.getType())) {
                        image = images.get(item.getType());
                    } else {
                        image = Bitmap.createScaledBitmap(item.getImage(), scale, scale, true);
                        images.put(item.getType(), image);
                    }
                    int drawX, drawY;
                    if (item != dragging) {
                        drawX = posX + 15;
                        drawY = posY + 15;
                    } else {
                        drawX = (int) (draggingX - image.getWidth() / 2f);
                        drawY = (int) (draggingY - image.getHeight() / 2f);
                        ViewUtils.displayItemDescription(screen, this.image, drawX, drawY, item);
                    }
                    screen.draw(image, drawX, drawY);
                    if (item.getAmount() > 1) {
                        screen.text(""+item.getAmount(),
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
                for (Recipe recipe : Pixelate.getRecipeManager().getRecipes()) {
                    if (recipe.isFulfilled(playerInventory.getCrafting())) {
                        //playerInventory.setItem(draggingSlot, null);
                        if (playerInventory.addItem(recipe.getItem())) {
                            for (int i = 0; i < 4; i++) {
                                ItemStack item = playerInventory.getCraftingItem(i);
                                if (item == null) continue;
                                item.removeAmount(1);
                            }
                        }
                        break;
                    }
                }
                return;
            }
            if (isOnExit(x, y)) {
                for (ItemStack crafting : playerInventory.getCrafting()) {
                    if (crafting == null) continue;
                    playerInventory.addItem(crafting);
                }
                playerInventory.clearCrafting();
                HUD.INSTANCE.setInventory(null);
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
                    draggingSlot = playerInventory.getSlot(dragging);
                    if (draggingSlot == -1) {
                        draggingSlot = playerInventory.getCraftingSlot(dragging) + playerInventory.getSize();
                    }
                }
            }
            return;
        }
        if (e.getAction() == ControlEvent.Action.UP) {
            if (dragging != null) {
                int slot = getSlot(x, y);
                if (slot == -1 || slot == 100 || slot == draggingSlot) {
                    dragging = null;
                    return;
                }
                ItemStack itemStack = getItem(x, y);
                if (slot >= playerInventory.getSize()) {
                    if (draggingSlot >= playerInventory.getSize()) {
                        playerInventory.setCrafting(draggingSlot - playerInventory.getSize(), null);
                    } else {
                        playerInventory.setItem(draggingSlot, null);
                    }
                    playerInventory.setCrafting(slot - playerInventory.getSize(), dragging);
                } else if (itemStack == null) {
                    if (draggingSlot >= playerInventory.getSize()) {
                        playerInventory.setCrafting(draggingSlot - playerInventory.getSize(), null);
                    } else {
                        playerInventory.setItem(draggingSlot, null);
                    }
                    playerInventory.setItem(slot, dragging);
                } else if (itemStack.similar(dragging)) {
                    if (draggingSlot >= playerInventory.getSize()) {
                        playerInventory.setCrafting(draggingSlot - playerInventory.getSize(), null);
                    } else {
                        playerInventory.setItem(draggingSlot, null);
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
        if (slot >= playerInventory.getSize() && slot != 100) {
            return playerInventory.getCraftingItem(slot - playerInventory.getSize());
        }
        return playerInventory.getItem(slot);
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
