package xyz.destiall.pixelate.gui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.KeyEvent;

import xyz.destiall.java.events.EventHandler;
import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.events.EventKeyboard;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.items.Inventory;
import xyz.destiall.pixelate.items.ItemStack;

public class ViewHotbar implements View {
    private final Bitmap image;
    private final Bitmap currentSlotImage;
    private Inventory inventory;
    private int currentSlot;
    public ViewHotbar(Inventory inventory) {
        this.inventory = inventory;
        Bitmap image = Bitmap.createBitmap(BitmapFactory.decodeResource(Game.getResources(), R.drawable.hotbar));
        currentSlotImage = Bitmap.createScaledBitmap(image, (int) (image.getWidth() * 0.85), (int) (image.getHeight() * 0.85), false);
        this.image = Bitmap.createScaledBitmap(image, (int) (image.getWidth() * 0.8), (int) (image.getHeight() * 0.8), false);
        Game.HANDLER.registerListener(this);
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setCurrentSlot(int currentSlot) {
        this.currentSlot = currentSlot;
    }

    public int getCurrentSlot() {
        return currentSlot;
    }

    @Override
    public void render(Screen screen) {
        int starting = (int) (Game.WIDTH / 2 - image.getWidth() * 4.5);
        for (int i = 0; i < 9; i++) {
            int a = 0;
            Bitmap image = this.image;
            if (i == currentSlot) {
                image = currentSlotImage;
                a = 5;
            }
            int x = starting + (i * this.image.getWidth());
            int y = Game.HEIGHT - 200;
            ItemStack item = inventory.getItem(i);
            if (item != null) {
                Bitmap itemImage = Bitmap.createScaledBitmap(item.getImage(), (int) (this.image.getWidth() * 0.8), (int) (this.image.getWidth() * 0.8), true);
                screen.getCanvas().drawBitmap(
                        itemImage,
                        x + 15,
                        y + 10,
                        null);
            }
            screen.getCanvas().drawBitmap(
                    image,
                    x,
                    y - a,
                    null);
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void destroy() {
        Game.HANDLER.unregisterListener(this);
    }

    @EventHandler
    public void onKeyboard(EventKeyboard e) {
        if (e.getKeyCode() > KeyEvent.KEYCODE_0 && e.getKeyCode() <= KeyEvent.KEYCODE_9) {
            int slot = e.getKeyCode() - 8;
            setCurrentSlot(slot);
        }
    }
}
