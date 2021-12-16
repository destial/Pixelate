package xyz.destiall.pixelate.entities;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.List;

import xyz.destiall.java.events.EventHandler;
import xyz.destiall.java.events.Listener;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.environment.Material;
import xyz.destiall.pixelate.environment.sounds.Sound;
import xyz.destiall.pixelate.environment.tiles.EfficiencyType;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.environment.tiles.containers.ContainerTile;
import xyz.destiall.pixelate.environment.tiles.containers.FurnanceTile;
import xyz.destiall.pixelate.events.EventItemPickup;
import xyz.destiall.pixelate.events.EventJoystick;
import xyz.destiall.pixelate.events.EventLeftHoldButton;
import xyz.destiall.pixelate.events.EventLeftReleaseButton;
import xyz.destiall.pixelate.events.EventLeftTapButton;
import xyz.destiall.pixelate.events.EventOpenContainer;
import xyz.destiall.pixelate.events.EventOpenInventory;
import xyz.destiall.pixelate.events.EventRightTapButton;
import xyz.destiall.pixelate.events.EventTileBreak;
import xyz.destiall.pixelate.events.EventTilePlace;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.SpriteSheet;
import xyz.destiall.pixelate.gui.HUD;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.inventory.ChestInventory;
import xyz.destiall.pixelate.items.inventory.FurnaceInventory;
import xyz.destiall.pixelate.items.inventory.PlayerInventory;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.position.Vector2;
import xyz.destiall.pixelate.settings.Settings;
import xyz.destiall.pixelate.timer.Timer;

public class EntityPlayer extends EntityLiving implements Listener {
    private final SpriteSheet slash;
    private final Bitmap crosshair;
    private boolean playSwingAnimation;
    private boolean playPunchAnimation;
    private double swingAnimationTimer;
    private final float originalAnimSpeed;

    public EntityPlayer() {
        super(ResourceManager.getBitmap(R.drawable.player), 6, 3);
        location = new Location((int) (Pixelate.WIDTH * 0.5), (int) (Pixelate.HEIGHT * 0.5));
        spriteSheet.addAnimation("LOOK RIGHT" , createAnimation(0));
        spriteSheet.addAnimation("LOOK LEFT"  , createAnimation(1));
        spriteSheet.addAnimation("WALK RIGHT" , createAnimation(2));
        spriteSheet.addAnimation("WALK LEFT"  , createAnimation(3));
        spriteSheet.addAnimation("PUNCH RIGHT", createAnimation(4));
        spriteSheet.addAnimation("PUNCH LEFT" , createAnimation(5));
        spriteSheet.setCurrentAnimation("LOOK RIGHT");
        scale = 0.5f;
        collision = new AABB(location.getX(), location.getY(), location.getX() + Tile.SIZE - 10, location.getY() + Tile.SIZE - 10);
        inventory = new PlayerInventory(this, 27);
        HUD.INSTANCE.setHotbar(getInventory());
        playSwingAnimation = false;
        playPunchAnimation = false;
        slash = new SpriteSheet();
        Bitmap slashSheet = ResourceManager.getBitmap(R.drawable.slashanimation);
        slash.addAnimation("RIGHT", createAnimation(slashSheet, 4, 4, 0));
        slash.addAnimation("UP"   , createAnimation(slashSheet, 4, 4, 1));
        slash.addAnimation("LEFT" , createAnimation(slashSheet, 4, 4, 2));
        slash.addAnimation("DOWN" , createAnimation(slashSheet, 4, 4, 3));
        crosshair = ResourceManager.getBitmap(R.drawable.crosshair);
        originalAnimSpeed = animationSpeed;
        Pixelate.HANDLER.registerListener(this);
    }

    @Override
    public void remove() {
        if (location.getWorld() != null)
            teleport(location.getWorld().getNearestEmpty(0, 0));
        health = 20.f;
    }

    @Override
    protected void updateSprite() {
        if (velocity.getX() > 0) facing = Direction.RIGHT;
        else if (velocity.getX() < 0) facing = Direction.LEFT;
        //else facing = Direction.RIGHT;

        String anim = (velocity.isZero() ? (playPunchAnimation || swingAnimationTimer != 0 ? "PUNCH " : "LOOK ") : "WALK ") + facing.name();
        spriteSheet.setCurrentAnimation(anim);
        if (playPunchAnimation || swingAnimationTimer != 0) {
            animationSpeed = originalAnimSpeed * 0.2f;
        } else {
            animationSpeed = originalAnimSpeed;
        }
    }

    @Override
    public void update() {
        super.update();
        if (swingAnimationTimer != 0) {
            swingAnimationTimer += Timer.getDeltaTime() * 20;
        }
    }

    @Override
    public void render(Screen screen) {
        super.render(screen);
        Vector2 vector = Screen.convert(location);
        Vector2 dir = target.getVector().multiply(Tile.SIZE);
        dir.add(Tile.SIZE * 0.25, Tile.SIZE * 0.25);
        vector.add(dir);

        if (Settings.ENABLE_BLOCK_TRACE) {
            Tile t = location.clone().add(dir).getTile();
            if (t != null) {
                Vector2 tile = Screen.convert(t.getVector());
                screen.quad(tile.getX(), tile.getY(), Tile.SIZE, Tile.SIZE, Color.argb(60, 0, 0, 255));
            }
        }

        if (Settings.ENABLE_CROSSHAIR)
            screen.draw(crosshair, vector.getX(), vector.getY());

        if (playSwingAnimation) {
            playSwingAnimation = false;
            slash.setCurrentAnimation(target.name());
            swingAnimationTimer += Timer.getDeltaTime();
            slash.setCurrentFrame((int) swingAnimationTimer);
        }

        if (swingAnimationTimer >= 4) {
            slash.setCurrentFrame(0);
            swingAnimationTimer = 0;
        }

        if (swingAnimationTimer != 0) {
            slash.setCurrentFrame((int) swingAnimationTimer);
            Bitmap map = slash.getCurrentSprite();
            map = resizeImage(map, 0.3f);
            Vector2 offset = vector.subtract(Tile.SIZE * 0.5, Tile.SIZE * 0.5);
            screen.draw(map, offset.getX(), offset.getY());
        }
    }

    @Override
    public PlayerInventory getInventory() {
        return (PlayerInventory) inventory;
    }

    /**
     * Get the current item in hand
     * @return The item in hand, or null if none
     */
    public ItemStack getItemInHand() {
        return inventory.getItem(HUD.INSTANCE.getHotbar().getCurrentSlot());
    }

    @EventHandler
    private void onJoystickEvent(EventJoystick e) {
        velocity.setX(e.getOffsetX());
        velocity.setY(e.getOffsetY());
        velocity.multiply((Tile.SIZE - 10) / 5f);
    }

    @EventHandler
    private void onPickUp(EventItemPickup e) {
        if (e.getPicker() != this) return;
        if (location.getWorld() == null) return;
        location.getWorld().playSound(Sound.SoundType.PICK_UP, location, 1.f);
    }

    @EventHandler
    private void onLeftRelease(EventLeftReleaseButton e) {
        playPunchAnimation = false;
    }

    @EventHandler
    private void onLeftTap(EventLeftTapButton e) {
        if (location.getWorld() == null) return;
        playPunchAnimation = true;
        spriteSheet.setCurrentFrame(0);
        List<Tile> currentTiles = location.getWorld().findTiles(collision);
        Location newLoc = location.clone().add(Tile.SIZE * 0.5 + target.getVector().getX() * Tile.SIZE, Tile.SIZE * 0.5 + target.getVector().getY() * Tile.SIZE);
        Tile tile = newLoc.getTile();
        if (!playSwingAnimation && (tile == null || currentTiles.contains(tile) || tile.getTileType() != Tile.TileType.FOREGROUND)) {
            playSwingAnimation = true;
            Location loc = location.clone();
            loc.add(Tile.SIZE * 0.5 + target.getVector().getX() * Tile.SIZE, Tile.SIZE * 0.5 + target.getVector().getY() * Tile.SIZE);
            ItemStack hand = getItemInHand();
            float damage = 1;
            if (hand != null) {
                if (hand.getType().getEfficiencyTier() != EfficiencyType.NONE) {
                    damage = hand.getType().getEfficiencyTier().getMultiplier();
                }
            }
            final float finalDamage = damage;
            location.getWorld().getNearestEntities(loc, Tile.SIZE).stream().filter(en -> en != this).forEach(en -> {
                if (en instanceof EntityPlayer) return;
                if (en instanceof EntityLiving) {
                    EntityLiving living = (EntityLiving) en;
                    living.damage(this, finalDamage);
                }
            });
        }
    }

    @EventHandler
    private void onLeftHold(EventLeftHoldButton e) {
        if (location.getWorld() == null) return;
        Location newLoc = getLocation().add(Tile.SIZE * 0.5 + target.getVector().getX() * Tile.SIZE, Tile.SIZE * 0.5 + target.getVector().getY() * Tile.SIZE);
        Tile tile = newLoc.getTile();
        if (tile == null || tile.getTileType() != Tile.TileType.FOREGROUND) {
            playPunchAnimation = false;
            return;
        }
        if (!playPunchAnimation) spriteSheet.setCurrentFrame(0);
        playPunchAnimation = true;
        Vector2 tileLoc = tile.getVector();
        float bbProgress = tile.getBlockBreakProgress(); //Out of 100.0
        float bbDuration = tile.getMaterial().getRequiredMineDuration(getItemInHand());
        float timeRelative = bbProgress / 100.0f * bbDuration;
        timeRelative += Timer.getDeltaTime();
        float bbProgressDiff = timeRelative / bbDuration * 100 - bbProgress;
        tile.addBlockBreakProgression(bbProgressDiff);
        if (tile.getBlockBreakProgress() >= 100) {
            EventTileBreak ev = new EventTileBreak(tile);
            Pixelate.HANDLER.call(ev);
            if (ev.isCancelled()) {
                tile.addBlockBreakProgression(-500);
                return;
            }
            List<ItemStack> drops = location.getWorld().breakTile(newLoc);
            for (double rad = -Math.PI, i = 0; rad <= Math.PI && i < drops.size(); rad += Math.PI / drops.size(), i++) {
                ItemStack drop = drops.get((int) i);
                double x = Math.cos(i) * Tile.SIZE * 0.3;
                double y = Math.sin(i) * Tile.SIZE * 0.3;
                location.getWorld().dropItem(drop, tileLoc.add(x, y));
                tileLoc.subtract(x, y);
            }
        }
    }

    @EventHandler
    private void onRightTap(EventRightTapButton e) {
        if (location.getWorld() == null) return;
        Location newLoc = location.clone().add(Tile.SIZE * 0.5 + target.getVector().getX() * Tile.SIZE, Tile.SIZE * 0.5 + target.getVector().getY() * Tile.SIZE);
        Tile tile = newLoc.getTile();
        ItemStack current = getItemInHand();
        if (tile.getMaterial().isContainer()) {
            ContainerTile container = (ContainerTile) tile;
            EventOpenContainer ev = new EventOpenContainer(container);
            Pixelate.HANDLER.call(ev);
            if (ev.isCancelled()) return;
            if (tile.getMaterial() == Material.FURNACE) {
                FurnaceInventory furnaceInventory = (FurnaceInventory) container.getInventory();
                HUD.INSTANCE.setFurnaceDisplay(getInventory(), (FurnanceTile) container);
                furnaceInventory.setToSmeltSlot(new ItemStack(Material.COAL_ORE, 1));
                furnaceInventory.setBurnerSlot(new ItemStack(Material.COAL, 1));
            } else if (tile.getMaterial() == Material.CHEST) {
                ChestInventory chestInventory = (ChestInventory) container.getInventory();
                HUD.INSTANCE.setChestDisplay(getInventory(), chestInventory);
            }
        } else if (current != null && current.getType().isBlock()) {
            if (tile.getTileType() != Tile.TileType.FOREGROUND && !location.getWorld().findTiles(collision).contains(tile)) {
                EventTilePlace ev = new EventTilePlace(tile, current.getType());
                Pixelate.HANDLER.call(ev);
                if (ev.isCancelled()) return;
                tile.setMaterial(ev.getReplaced());
                current.setAmount(current.getAmount() - 1);
            }
        }
    }

    @EventHandler(priority = EventHandler.Priority.HIGHEST)
    private void onOpenInventory(EventOpenInventory e) {
        HUD.INSTANCE.setInventory(getInventory());
    }

    @EventHandler
    private void onPlace(EventTilePlace e) {
        if (e.getReplaced() == Material.TNT) {
            e.setCancelled(true);
            Location location = e.getTile().getLocation();
            location.getWorld().spawnEntity(EntityPrimedTNT.class, location.add(Tile.SIZE * 0.25, Tile.SIZE * 0.25));
            ItemStack current = getItemInHand();
            current.setAmount(current.getAmount() - 1);
        }
    }
}
