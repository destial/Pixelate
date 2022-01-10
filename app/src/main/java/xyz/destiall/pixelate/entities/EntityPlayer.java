package xyz.destiall.pixelate.entities;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.List;
import java.util.Random;

import xyz.destiall.java.events.EventHandler;
import xyz.destiall.java.events.Listener;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.materials.EfficiencyTier;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.environment.sounds.Sound;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.environment.tiles.containers.ContainerTile;
import xyz.destiall.pixelate.environment.tiles.containers.FurnanceTile;
import xyz.destiall.pixelate.events.EventChat;
import xyz.destiall.pixelate.events.EventIgniteTNT;
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
import xyz.destiall.pixelate.experience.Experience;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.SpriteSheet;
import xyz.destiall.pixelate.gui.HUD;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.LootTable;
import xyz.destiall.pixelate.items.inventory.ChestInventory;
import xyz.destiall.pixelate.items.inventory.FurnaceInventory;
import xyz.destiall.pixelate.items.inventory.PlayerInventory;
import xyz.destiall.pixelate.items.meta.Enchantment;
import xyz.destiall.pixelate.items.meta.ItemMeta;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.position.Vector2;
import xyz.destiall.pixelate.settings.Settings;
import xyz.destiall.pixelate.timer.Timer;

public class EntityPlayer extends EntityLiving implements Listener {
    private transient final SpriteSheet slash;
    private transient final Bitmap crosshair;
    private transient boolean playSwingAnimation;
    private transient boolean playPunchAnimation;
    private transient double swingAnimationTimer;
    private transient final float originalAnimSpeed;

    protected Experience exp;

    public EntityPlayer() {
        Bitmap image = ResourceManager.getBitmap(R.drawable.player);
        location = new Location((int) (Pixelate.WIDTH * 0.5), (int) (Pixelate.HEIGHT * 0.5));
        spriteSheet.addAnimation("LOOK RIGHT" , Imageable.createAnimation(image, 6, 3, 0));
        spriteSheet.addAnimation("LOOK LEFT"  , Imageable.createAnimation(image, 6, 3,1));
        spriteSheet.addAnimation("WALK RIGHT" , Imageable.createAnimation(image, 6, 3,2));
        spriteSheet.addAnimation("WALK LEFT"  , Imageable.createAnimation(image, 6, 3,3));
        spriteSheet.addAnimation("PUNCH RIGHT", Imageable.createAnimation(image, 6, 3,4));
        spriteSheet.addAnimation("PUNCH LEFT" , Imageable.createAnimation(image, 6, 3,5));
        spriteSheet.setCurrentAnimation("LOOK RIGHT");
        scale = 0.5f;
        collision = new AABB(location.getX(), location.getY(), location.getX() + Tile.SIZE - 10, location.getY() + Tile.SIZE - 10);
        inventory = new PlayerInventory(this, 27);
        playSwingAnimation = false;
        playPunchAnimation = false;
        slash = new SpriteSheet();
        Bitmap slashSheet = ResourceManager.getBitmap(R.drawable.slashanimation);
        slash.addAnimation("RIGHT", Imageable.createAnimation(slashSheet, 4, 4, 0));
        slash.addAnimation("UP"   , Imageable.createAnimation(slashSheet, 4, 4, 1));
        slash.addAnimation("LEFT" , Imageable.createAnimation(slashSheet, 4, 4, 2));
        slash.addAnimation("DOWN" , Imageable.createAnimation(slashSheet, 4, 4, 3));
        crosshair = ResourceManager.getBitmap(R.drawable.crosshair);
        originalAnimSpeed = animationSpeed;
        exp = new Experience();
    }

    public void sendMessage(String message) {
        EventChat chat = new EventChat(message);
        Pixelate.HANDLER.call(chat);
    }

    @Override
    public void remove() {
        World w;
        if ((w = location.getWorld()) != null)
            teleport(w.getNearestEmpty(0, 0));
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
        Location newLoc = location.clone().add(Tile.SIZE * 0.5 + target.getVector().getX() * Tile.SIZE, Tile.SIZE * 0.5 + target.getVector().getY() * Tile.SIZE);
        Vector2 vector = Screen.convert(newLoc);
        // Vector2 dir = target.getVector();
        // dir.add(Tile.SIZE * 0.25, Tile.SIZE * 0.25);
        // vector.add(dir);

        if (Settings.ENABLE_BLOCK_TRACE) {
            Tile t = newLoc.getTile();
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
            map = Imageable.resizeImage(map, 0.3f);
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
        return getInventory().getItem(HUD.INSTANCE.getHotbar().getCurrentSlot());
    }

    public void addXP(int xp)
    {
        if(exp.addXP(xp))
        {
            this.getLocation().getWorld().playSound(Sound.SoundType.EXPLOSION, this.getLocation(), 1.0f);
        }
    }

    public void setXPLevel(int level)
    {
        exp.setLevel(level);
    }

    public float getXPProgress()
    {
        return (float)exp.getXP() / Experience.getRequiredXP(exp.getLevel());
    }

    public int getLevel()
    {
        return exp.getLevel();
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
        World w;
        if ((w = location.getWorld()) == null) return;
        w.playSound(Sound.SoundType.PICK_UP, location, 1.f);
    }

    @EventHandler
    private void onLeftRelease(EventLeftReleaseButton e) {
        playPunchAnimation = false;
    }

    @EventHandler
    private void onLeftTap(EventLeftTapButton e) {
        World w;
        if ((w = location.getWorld()) == null) return;
        playPunchAnimation = true;
        spriteSheet.setCurrentFrame(0);
        List<Tile> currentTiles = w.findTiles(collision);
        Location newLoc = location.clone().add(Tile.SIZE * 0.5 + target.getVector().getX() * Tile.SIZE, Tile.SIZE * 0.5 + target.getVector().getY() * Tile.SIZE);
        Tile tile = newLoc.getTile();
        if (!playSwingAnimation && (tile == null || currentTiles.contains(tile) || tile.getTileType() != Tile.TileType.FOREGROUND)) {
            playSwingAnimation = true;
            Location loc = location.clone();
            loc.add(Tile.SIZE * 0.5 + target.getVector().getX() * Tile.SIZE, Tile.SIZE * 0.5 + target.getVector().getY() * Tile.SIZE);
            ItemStack hand = getItemInHand();
            float damage = 1;
            if (hand != null) {
                if (hand.getType().getEfficiencyTier() != EfficiencyTier.NONE) {
                    damage = hand.getType().getEfficiencyTier().getMultiplier();
                }
            }
            final float finalDamage = damage;
            w.getNearestEntities(loc, Tile.SIZE).stream().filter(en -> en != this).forEach(en -> {
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
        World w;
        if ((w = location.getWorld()) == null) return;
        Location newLoc = location.clone().add(Tile.SIZE * 0.5 + target.getVector().getX() * Tile.SIZE, Tile.SIZE * 0.5 + target.getVector().getY() * Tile.SIZE);
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
            ItemStack hand = getItemInHand();
            int luck = 0;
            if (hand.getType().isTool()) {
                int use = 1;
                ItemMeta meta = hand.getItemMeta();
                if (meta.hasEnchantment(Enchantment.DURABILITY)) {
                    use = new Random().nextInt(meta.getEnchantLevel(Enchantment.DURABILITY));
                    if (use > 1 || use == 0) {
                        use = 0;
                    }
                }
                if (meta.hasEnchantment(Enchantment.FORTUNE))
                    luck = meta.getEnchantLevel(Enchantment.FORTUNE);
                meta.setDurability(hand.getItemMeta().getDurability() + use);
                if (meta.getDurability() >= hand.getType().getMaxDurability()) {
                    hand.setAmount(0);
                }
            }
            //XP Drops
            int xpDrop = LootTable.getInstance().getXPDrops(tile.getMaterial(), luck);
            exp.addXP(xpDrop);

            List<ItemStack> drops = w.breakTile(newLoc.getTile(), getItemInHand());
            //if (drops != null) {
                for (double rad = -Math.PI, i = 0; rad <= Math.PI && i < drops.size(); rad += Math.PI / drops.size(), i++) {
                    ItemStack drop = drops.get((int) i);
                    double x = Math.cos(i) * Tile.SIZE * 0.3;
                    double y = Math.sin(i) * Tile.SIZE * 0.3;
                    w.dropItem(drop, tileLoc.add(x, y));
                    tileLoc.subtract(x, y);
                }
            //}
        }
    }

    @EventHandler
    private void onRightTap(EventRightTapButton e) {
        World w;
        if ((w = location.getWorld()) == null) return;
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
            } else if (tile.getMaterial() == Material.CHEST) {
                ChestInventory chestInventory = (ChestInventory) container.getInventory();
                HUD.INSTANCE.setChestDisplay(getInventory(), chestInventory);
            }
        } else if (tile.getMaterial() == Material.TNT) {
            tile.setMaterial(Material.STONE);
            Location location = tile.getLocation();
            EventIgniteTNT ev = new EventIgniteTNT(location);
            Pixelate.HANDLER.call(ev);
            if (ev.isCancelled()) return;
            w.spawnEntity(EntityPrimedTNT.class, location.add(Tile.SIZE * 0.25, Tile.SIZE * 0.25));
        } else if (current != null && current.getType().isBlock()) {
            if (tile.getTileType() != Tile.TileType.FOREGROUND && !w.findTiles(collision).contains(tile)) {
                EventTilePlace ev = new EventTilePlace(this, tile, current.getType());
                Pixelate.HANDLER.call(ev);
                if (ev.isCancelled()) return;
                tile.setMaterial(ev.getReplaced());
                current.removeAmount(1);
            }
        }
    }

    @EventHandler(priority = EventHandler.Priority.HIGHEST)
    private void onOpenInventory(EventOpenInventory e) {
        // HUD.INSTANCE.setCreative(getInventory());
        HUD.INSTANCE.setInventory(getInventory());
    }
}
