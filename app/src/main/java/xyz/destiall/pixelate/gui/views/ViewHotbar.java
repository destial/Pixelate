package xyz.destiall.pixelate.gui.views;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.KeyEvent;

import java.util.HashMap;
import java.util.Map;

import xyz.destiall.java.events.EventHandler;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.events.controls.ControlEvent;
import xyz.destiall.pixelate.events.controls.EventKeyboard;
import xyz.destiall.pixelate.events.controls.EventTouch;
import xyz.destiall.pixelate.graphics.Glint;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.inventory.PlayerInventory;
import xyz.destiall.pixelate.items.meta.ItemFlag;
import xyz.destiall.pixelate.items.meta.ItemMeta;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.timer.Timer;

/**
 * Written by Rance
 */
public class ViewHotbar implements View {
    private final HashMap<Integer, AABB> positions;
    private final HashMap<Material, Bitmap> images;
    private final Bitmap image;
    private final Bitmap currentSlotImage;
    private PlayerInventory playerInventory;
    private int droppingSlot;
    private float dropTimer;
    private int currentSlot;

    public ViewHotbar(PlayerInventory playerInventory) {
        this.playerInventory = playerInventory;
        Bitmap glint = ResourceManager.getBitmap(R.drawable.glint);
        Bitmap image = ResourceManager.getBitmap(R.drawable.hotbar);
        currentSlotImage = Bitmap.createScaledBitmap(image, (int) (image.getWidth() * 0.85), (int) (image.getHeight() * 0.85), false);
        this.image = Bitmap.createScaledBitmap(image, (int) (image.getWidth() * 0.8), (int) (image.getHeight() * 0.8), false);
        positions = new HashMap<>();
        Pixelate.HANDLER.registerListener(this);
        images = new HashMap<>();
        droppingSlot = -1;
        dropTimer = 0f;
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
        int y = Pixelate.HEIGHT - (int)(Pixelate.HEIGHT * 0.14);
        for (int i = 0; i < 9; i++) {
            int a = 0;
            Bitmap image = this.image;
            if (i == currentSlot) {
                image = currentSlotImage;
                a = 5;
            }
            int x = starting + (i * this.image.getWidth());
            ItemStack item = playerInventory.getItem(i);
            screen.draw(image, x, y - a);
            if (item != null) {
                Bitmap itemImage;
                if (images.containsKey(item.getType())) {
                    itemImage = images.get(item.getType());
                } else {
                    itemImage = Bitmap.createScaledBitmap(item.getImage(), (int) (this.image.getWidth() * 0.8), (int) (this.image.getWidth() * 0.8), true);
                    images.put(item.getType(), itemImage);
                }
                screen.draw(itemImage, x + 10, y + 10);
                if (item.getAmount() > 1) {
                    screen.text(
                        "" + item.getAmount(),
                        x + this.image.getWidth() / 2f,
                        y + this.image.getHeight() / 2f,
                        40, Color.WHITE);
                }
                ItemMeta meta = item.getItemMeta();
                if (item.getType().isTool()) {
                    screen.bar(x + 20, y + (int) (this.image.getWidth() * 0.8), this.image.getWidth() - 40, 10, Color.GREEN, Color.RED, meta.getDurability() / (float) item.getType().getMaxDurability());
                }
                if (meta.isEnchanted() && !meta.hasItemFlag(ItemFlag.HIDE_ENCHANT)) {
                    Glint.INSTANCE.renderHotbar(screen, x + 10, y + 10);
                }
            }
            if (!positions.containsKey(i)) {
                positions.put(i, new AABB(x, y, x + image.getWidth(), y + image.getHeight()));
            }
        }
        if (droppingSlot != -1) {
            AABB aabb = positions.get(droppingSlot);
            if (aabb == null) return;
            screen.bar(aabb.getMin().getX(), aabb.getMin().getY(), aabb.getWidth(), aabb.getHeight(), Color.alpha(Color.WHITE), Color.argb(100, 255, 255, 255), dropTimer);
        }

        ItemStack current = playerInventory.getItem(currentSlot);
        if (current != null) {
            ItemMeta meta = current.getItemMeta();
            String name = meta.hasDisplayName() ? meta.getDisplayName() : current.getType().getName();
            screen.text(name, Pixelate.WIDTH * 0.5f - (name.length() * 10), y - 160, 40, Color.WHITE);
        }
    }

    @Override
    public void update() {
        if (droppingSlot != -1) {
            dropTimer += Timer.getDeltaTime();
        }

        if (dropTimer >= 1 && droppingSlot != -1) {
            ItemStack item = playerInventory.getItem(droppingSlot);
            if (item == null) return;
            EntityPlayer player = (EntityPlayer) playerInventory.getHolder();
            Location location = player.getLocation();
            World w;
            if ((w = location.getWorld()) == null) return;
            location.add(player.getTarget().getVector().multiply(Tile.SIZE));
            playerInventory.removeItem(item);
            w.dropItem(item, location);
            droppingSlot = -1;
            dropTimer = 0f;
        }

        Glint.INSTANCE.update();
    }

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
            int slot = getSlot(e.getX(), e.getY());
            if (slot == -1) return;
            setCurrentSlot(slot);
            if (droppingSlot == -1) {
                droppingSlot = slot;
                dropTimer = 0f;
            } else if (droppingSlot != slot) {
                droppingSlot = slot;
                dropTimer = 0f;
            }
        } else if (e.getAction() == ControlEvent.Action.MOVE) {
            int slot = getSlot(e.getX(), e.getY());
            if (slot == -1 || droppingSlot != -1) {
                droppingSlot = -1;
                dropTimer = 0f;
            }
        } else if (e.getAction() == ControlEvent.Action.UP) {
            int slot = getSlot(e.getX(), e.getY());
            if (slot == -1 || droppingSlot == slot) {
                droppingSlot = -1;
                dropTimer = 0f;
            }
        }
    }

    private int getSlot(float x, float y) {
        Map.Entry<Integer, AABB> aabbs = positions.entrySet().stream().filter(en -> en.getValue().isOverlap(x, y)).findFirst().orElse(null);
        if (aabbs == null) return -1;
        return aabbs.getKey();
    }
}
