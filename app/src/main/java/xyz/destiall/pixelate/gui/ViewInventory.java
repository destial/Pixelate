package xyz.destiall.pixelate.gui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.HashMap;
import java.util.Map;

import xyz.destiall.java.events.EventHandler;
import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.events.ControlEvent;
import xyz.destiall.pixelate.events.EventTouch;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.items.Inventory;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Vector2;

public class ViewInventory implements View {
    private final Inventory inventory;
    private final Bitmap image;
    private final Paint exitPaint;
    private final Paint amountPaint;
    private final Vector2 exitButton;
    private final int exitButtonRadius;
    private ItemStack dragging;
    private int draggingSlot;
    private int draggingX;
    private int draggingY;
    private final Map<Integer, AABB> positions;
    private final HashMap<Material, Bitmap> images;

    public ViewInventory(Inventory inventory) {
        this.inventory = inventory;
        exitPaint = new Paint();
        exitPaint.setColor(Color.RED);
        exitButton = new Vector2(Game.WIDTH - 100, 100);
        exitButtonRadius = 40;
        amountPaint = new Paint();
        amountPaint.setTextSize(55);
        amountPaint.setColor(Color.WHITE);
        amountPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        image = Bitmap.createBitmap(BitmapFactory.decodeResource(Game.getResources(), R.drawable.hotbar));
        positions = new HashMap<>();
        images = new HashMap<>();
        Game.HANDLER.registerListener(this);
    }

    @Override
    public void render(Screen screen) {
        screen.getCanvas().drawCircle(
            (int) exitButton.getX(),
            (int) exitButton.getY(),
            exitButtonRadius,
            exitPaint
        );
        int startingCrafting = Game.WIDTH / 2 - image.getWidth();
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                screen.getCanvas().drawBitmap(image,
                        startingCrafting + (x * image.getWidth()),
                    100 + (y * image.getWidth()),
                    null);
            }
        }
        int starting = (int) (Game.WIDTH / 2 - image.getWidth() * 4.5);
        int i = 0;
        for (int y = 0; y < (inventory.getSize() / 9); y++) {
            for (int x = 0; x < 9; x++) {
                int posX = starting + (x * image.getWidth());
                int posY = 400 + (y * image.getHeight());
                screen.getCanvas().drawBitmap(image,
                    posX,
                    posY,
                    null);
                ItemStack item = inventory.getItem(i);
                if (item != null) {
                    Bitmap image;
                    if (images.containsKey(item.getMaterial())) {
                        image = images.get(item.getMaterial());
                    } else {
                        image = Bitmap.createScaledBitmap(item.getImage(), (int) (this.image.getWidth() * 0.8), (int) (this.image.getWidth() * 0.8), true);
                        images.put(item.getMaterial(), image);
                    }
                    if (item != dragging) {
                        screen.getCanvas().drawBitmap(
                            image,
                            posX + 15,
                            posY + 5,
                            null);
                        if (item.getAmount() > 1) {
                            screen.getCanvas().drawText(""+item.getAmount(),
                                posX + this.image.getWidth() / 2f,
                                posY + this.image.getHeight() / 2f,
                                amountPaint);
                        }
                    } else {
                        screen.getCanvas().drawBitmap(
                            image,
                            draggingX - image.getWidth() / 2f,
                            draggingY - image.getHeight() / 2f,
                            null);
                        if (item.getAmount() > 1) {
                            screen.getCanvas().drawText(""+(item.getAmount() - 1),
                                posX + this.image.getWidth(),
                                posY + this.image.getHeight(),
                                amountPaint);
                        }
                    }
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

    }

    @Override
    public void tick() {

    }

    @EventHandler
    private void onTouch(EventTouch e) {
        float x = e.getX();
        float y = e.getY();
        if (e.getAction() == ControlEvent.Action.DOWN) {
            if (isOnExit(x, y)) {
                HUD.INSTANCE.setInventory(null);
            }
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
                }
            }
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
                }
                if (itemStack == null) {
                    inventory.setItem(slot, dragging);
                    inventory.setItem(draggingSlot, null);
                    dragging = null;
                }
            }
        }
    }

    private int getSlot(float x, float y) {
        Map.Entry<Integer, AABB> aabbs = positions.entrySet().stream().filter(en -> en.getValue().isAABB(x, y)).findFirst().orElse(null);
        if (aabbs == null) return -1;
        return aabbs.getKey();
    }

    private ItemStack getItem(float x, float y) {
        int slot = getSlot(x, y);
        if (slot == -1) return null;
        return inventory.getItem(slot);
    }

    private boolean isOnExit(float x, float y) {
        double distance = Math.sqrt(Math.pow(exitButton.getX() - x, 2) + Math.pow(exitButton.getY() - y, 2));
        return distance < exitButtonRadius;
    }

    @Override
    public void destroy() {
        positions.clear();
        Game.HANDLER.unregisterListener(this);
    }
}
