package xyz.destiall.pixelate.gui.views;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

import xyz.destiall.utility.java.events.EventHandler;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.environment.tiles.containers.EnchantTableTile;
import xyz.destiall.pixelate.events.controls.ControlEvent;
import xyz.destiall.pixelate.events.controls.EventTouch;
import xyz.destiall.pixelate.graphics.Glint;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.gui.buttons.Button;
import xyz.destiall.pixelate.gui.buttons.QuadButton;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.inventory.PlayerInventory;
import xyz.destiall.pixelate.items.meta.Enchantment;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Vector2;
import xyz.destiall.pixelate.utils.MathematicUtils;
import xyz.destiall.pixelate.utils.ViewUtils;

/**
 * Written by Rance
 */
public class ViewEnchantingTable implements View {
    private final EnchantTableTile tile;
    private final Map<Integer, AABB> positions;
    private final HashMap<Material, Bitmap> images;
    private final PlayerInventory playerInventory;
    private final Map<Enchantment, QuadButton> possibleEnchants;
    private final Bitmap image;
    private final Vector2 exitButton;
    private final int exitButtonRadius;
    private final int scale;

    private ItemStack dragging;
    private int draggingSlot;
    private int draggingX;
    private int draggingY;

    public ViewEnchantingTable(EnchantTableTile tile, PlayerInventory playerInventory) {
        this.tile = tile;
        this.playerInventory = playerInventory;
        this.possibleEnchants = new HashMap<>();
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

        // Render enchant table
        int cOutX = Pixelate.WIDTH / 3 - image.getWidth() + (6 * image.getWidth());
        int cOutY = (int)(Pixelate.HEIGHT * 0.1f) + (image.getWidth() / 2);
        if (!positions.containsKey(100)) {
            positions.put(100, new AABB(cOutX, cOutY, cOutX + image.getWidth(), cOutY + image.getHeight()));
        }
        screen.draw(image, cOutX, cOutY);
        ItemStack lapisSlot = tile.getInventory().getLapisSlot();
        if (lapisSlot != null) {
            Bitmap image;
            if (images.containsKey(lapisSlot.getType())) {
                image = images.get(lapisSlot.getType());
            } else {
                image = Bitmap.createScaledBitmap(lapisSlot.getImage(), scale, scale, true);
                images.put(lapisSlot.getType(), image);
            }
            int drawX, drawY;
            if (lapisSlot != dragging) {
                drawX = cOutX + 15;
                drawY = cOutY + 15;
            } else {
                drawX = (int) (draggingX - image.getWidth() / 2f);
                drawY = (int) (draggingY - image.getHeight() / 2f);
                ViewUtils.displayItemDescription(screen, this.image, drawX, drawY, lapisSlot);
            }
            screen.draw(image, drawX, drawY);
            //if (lapisSlot.getAmount() > 1) {
                screen.text(""+lapisSlot.getAmount(),
                        drawX + this.image.getWidth() / 2f,
                        drawY + this.image.getHeight() / 2f,
                        40, Color.WHITE);
            //}
            ItemStack.renderInventory(screen, lapisSlot, drawX, drawY);
        }

        if (!positions.containsKey(101)) {
            positions.put(101, new AABB(cOutX + image.getWidth(), cOutY, cOutX + image.getWidth() + image.getWidth(), cOutY + image.getHeight()));
        }

        screen.draw(image, cOutX + image.getWidth(), cOutY);
        ItemStack enchantSlot = tile.getInventory().getEnchantSlot();
        if (enchantSlot != null) {
            Bitmap image;
            if (images.containsKey(enchantSlot.getType())) {
                image = images.get(enchantSlot.getType());
            } else {
                image = Bitmap.createScaledBitmap(enchantSlot.getImage(), scale, scale, true);
                images.put(enchantSlot.getType(), image);
            }
            int drawX, drawY;
            if (enchantSlot != dragging) {
                drawX = cOutX + this.image.getWidth() + 15;
                drawY = cOutY + 15;
            } else {
                drawX = (int) (draggingX - image.getWidth() / 2f);
                drawY = (int) (draggingY - image.getHeight() / 2f);
                ViewUtils.displayItemDescription(screen, this.image, drawX, drawY, enchantSlot);
            }
            screen.draw(image, drawX, drawY);
            if (enchantSlot.getAmount() > 1) {
                screen.text(""+enchantSlot.getAmount(),
                        drawX + this.image.getWidth() / 2f,
                        drawY + this.image.getHeight() / 2f,
                        40, Color.WHITE);
            }
            ItemStack.renderInventory(screen, enchantSlot, drawX, drawY);
        }

        screen.quadRing(positions.get(100), 10, Color.BLUE);

        for (QuadButton button : possibleEnchants.values()) {
            button.render(screen);
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
            if (isOnExit(x, y)) {
                Pixelate.getHud().setEnchantingTable(tile, null);
                return;
            }
            for (Button button : possibleEnchants.values()) {
                if (button.isOn(x, y)) {
                    button.tap();
                }
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
                        if (dragging == tile.getInventory().getEnchantSlot()) {
                            draggingSlot = 101;
                        } else if (dragging == tile.getInventory().getLapisSlot()){
                            draggingSlot = 100;
                        }
                    }
                }
            }
            return;
        }
        if (e.getAction() == ControlEvent.Action.UP) {
            if (dragging != null) {
                int slot = getSlot(x, y);
                // not valid slot or same slot
                if (slot == -1 || slot == draggingSlot) {
                    dragging = null;
                    return;
                }
                ItemStack itemStack = getItem(x, y);
                if (itemStack == null) {
                    // valid but empty slot
                    if (slot == 100) {
                        // lapis slot
                        if (dragging.getType() != Material.LAPIS) {
                            dragging = null;
                            return;
                        }
                    }

                    if (draggingSlot == 100) {
                        tile.getInventory().setLapisSlot(null);
                        generateEnchants();
                    } else if (draggingSlot == 101) {
                        tile.getInventory().setEnchantSlot(null);
                        generateEnchants();
                    } else {
                        playerInventory.setItem(draggingSlot, null);
                    }

                    if (slot == 100) {
                        tile.getInventory().setLapisSlot(dragging);
                        generateEnchants();
                    } else if (slot == 101) {
                        // enchant slot
                        tile.getInventory().setEnchantSlot(dragging);
                        generateEnchants();
                    } else {
                        // player slot
                        playerInventory.setItem(slot, dragging);
                    }

                } else if (itemStack.equals(dragging)) {
                    if (slot == 100) {
                        // lapis slot
                        if (dragging.getType() != Material.LAPIS) {
                            dragging = null;
                            return;
                        }
                    }
                    itemStack.addAmount(dragging.getAmount());
                    if (draggingSlot == 100) {
                        tile.getInventory().setLapisSlot(null);
                        generateEnchants();
                    } else if (draggingSlot == 101) {
                        tile.getInventory().setEnchantSlot(null);
                        generateEnchants();
                    } else {
                        playerInventory.setItem(draggingSlot, null);
                    }
                }
                dragging = null;
            }
        }
    }

    private void generateEnchants() {
        possibleEnchants.clear();
        EntityPlayer player = (EntityPlayer)playerInventory.getHolder();
        if (player.getXPLevel() < 1) return;
        int x = (int) (Pixelate.WIDTH / 2 - image.getWidth() * 4.5);
        int i = 0;
        int y = (int) (Pixelate.HEIGHT * 0.15);

        Map<Enchantment, Integer> enchants = tile.getInventory().generateEnchantments(tile);
        if (enchants == null) return;
        for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
            QuadButton button = new QuadButton(new Vector2(x, y + (i * (image.getHeight() + 10 - 50))), (int) (Pixelate.WIDTH * 0.2f), image.getHeight() - 50, Color.GRAY);
            button.setText(entry.getKey().getEnchantName() + " " + MathematicUtils.toRoman(entry.getValue()));
            button.onTap(() -> {
                tile.getInventory().enchant(entry);
                player.setXPLevel(player.getXPLevel() - 1);
                generateEnchants();
            });
            possibleEnchants.put(entry.getKey(), button);
            i++;
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
        if (slot == 100) return tile.getInventory().getLapisSlot();
        if (slot == 101) return tile.getInventory().getEnchantSlot();
        return playerInventory.getItem(slot);
    }

    private boolean isOnExit(float x, float y) {
        return exitButton.distanceSquared(x, y) <= exitButtonRadius * exitButtonRadius;
    }

    @Override
    public void destroy() {
        positions.clear();
        images.clear();
        possibleEnchants.clear();
        Pixelate.HANDLER.unregisterListener(this);
    }
}
