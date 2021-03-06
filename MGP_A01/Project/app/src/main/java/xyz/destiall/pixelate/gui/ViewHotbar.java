package xyz.destiall.pixelate.gui;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.KeyEvent;

import java.util.HashMap;
import java.util.Map;

import xyz.destiall.java.events.EventHandler;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.events.ControlEvent;
import xyz.destiall.pixelate.events.EventKeyboard;
import xyz.destiall.pixelate.events.EventTouch;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.inventory.PlayerInventory;
import xyz.destiall.pixelate.position.AABB;

public class ViewHotbar implements View {
    private final HashMap<Integer, AABB> positions;
    private final HashMap<Material, Bitmap> images;
    private final Bitmap image;
    private final Bitmap currentSlotImage;
    private PlayerInventory playerInventory;
    private int currentSlot;

    public ViewHotbar(PlayerInventory playerInventory) {
        this.playerInventory = playerInventory;
        Bitmap image = ResourceManager.getBitmap(R.drawable.hotbar);
        currentSlotImage = Bitmap.createScaledBitmap(image, (int) (image.getWidth() * 0.85), (int) (image.getHeight() * 0.85), false);
        this.image = Bitmap.createScaledBitmap(image, (int) (image.getWidth() * 0.8), (int) (image.getHeight() * 0.8), false);
        positions = new HashMap<>();
        Pixelate.HANDLER.registerListener(this);
        images = new HashMap<>();
    }

    public void setInventory(PlayerInventory playerInventory) {
        this.playerInventory = playerInventory;
    }

    public PlayerInventory getInventory() {
        return playerInventory;
    }

    public void setCurrentSlot(int currentSlot) {
        this.currentSlot = currentSlot;
    }

    public int getCurrentSlot() {
        return currentSlot;
    }

    @Override
    public void render(Screen screen) {
        int starting = (int) (Pixelate.WIDTH / 2 - image.getWidth() * 4.5);
        for (int i = 0; i < 9; i++) {
            int a = 0;
            Bitmap image = this.image;
            if (i == currentSlot) {
                image = currentSlotImage;
                a = 5;
            }
            int x = starting + (i * this.image.getWidth());
            int y = Pixelate.HEIGHT - 200;
            ItemStack item = playerInventory.getItem(i);
            screen.draw(image, x, y - a);
            if (item != null) {
                Bitmap itemImage;
                if (images.containsKey(item.getType())) {
                    itemImage = images.get(item.getType());
                } else {
                    itemImage = Bitmap.createScaledBitmap(item.getImage(), (int) (this.image.getWidth() * 0.8), (int) (this.image.getWidth() * 0.8), true);
                    images.put(item.getType(), itemImage);
                } screen.draw(itemImage, x + 15, y + 10);
                if (item.getAmount() > 1) {
                    screen.text(
                        "" + item.getAmount(),
                        x + this.image.getWidth() / 2f,
                        y + this.image.getHeight() / 2f,
                        40, Color.WHITE);
                }
            }
            if (!positions.containsKey(i)) {
                positions.put(i, new AABB(x, y, x + image.getWidth(), y + image.getHeight()));
            }
        }
    }

    @Override
    public void update() {}

    @Override
    public void destroy() {
        positions.clear();
        images.clear();
        Pixelate.HANDLER.unregisterListener(this);
    }

    @EventHandler
    private void onKeyboard(EventKeyboard e) {
        if (e.getKeyCode() > KeyEvent.KEYCODE_0 && e.getKeyCode() <= KeyEvent.KEYCODE_9) {
            int slot = e.getKeyCode() - 8;
            setCurrentSlot(slot);
        }
    }

    @EventHandler
    private void onTouch(EventTouch e) {
        if (e.getAction() == ControlEvent.Action.DOWN) {
            float x = e.getX();
            float y= e.getY();
            int slot = getSlot(x, y);
            if (slot == -1) return;
            setCurrentSlot(slot);
        }
    }

    private int getSlot(float x, float y) {
        Map.Entry<Integer, AABB> aabbs = positions.entrySet().stream().filter(en -> en.getValue().isOverlap(x, y)).findFirst().orElse(null);
        if (aabbs == null) return -1;
        return aabbs.getKey();
    }
}
