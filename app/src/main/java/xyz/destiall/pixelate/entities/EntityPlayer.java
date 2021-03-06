package xyz.destiall.pixelate.entities;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import xyz.destiall.utility.java.events.EventDispatcher;
import xyz.destiall.utility.java.events.Listener;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.materials.EfficiencyTier;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.environment.sounds.Sound;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.environment.tiles.containers.AnvilTile;
import xyz.destiall.pixelate.environment.tiles.containers.ContainerTile;
import xyz.destiall.pixelate.environment.tiles.containers.EnchantTableTile;
import xyz.destiall.pixelate.environment.tiles.containers.FurnanceTile;
import xyz.destiall.pixelate.events.controls.EventChat;
import xyz.destiall.pixelate.events.controls.EventJoystick;
import xyz.destiall.pixelate.events.controls.EventLeftHoldButton;
import xyz.destiall.pixelate.events.controls.EventLeftReleaseButton;
import xyz.destiall.pixelate.events.controls.EventLeftTapButton;
import xyz.destiall.pixelate.events.controls.EventOpenInventory;
import xyz.destiall.pixelate.events.controls.EventRightTapButton;
import xyz.destiall.pixelate.events.entity.EventItemPickup;
import xyz.destiall.pixelate.events.tile.EventIgniteTNT;
import xyz.destiall.pixelate.events.tile.EventOpenContainer;
import xyz.destiall.pixelate.events.tile.EventTilePlace;
import xyz.destiall.pixelate.experience.Experience;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.SpriteSheet;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.LootTable;
import xyz.destiall.pixelate.items.inventory.AnvilInventory;
import xyz.destiall.pixelate.items.inventory.ChestInventory;
import xyz.destiall.pixelate.items.inventory.FurnaceInventory;
import xyz.destiall.pixelate.items.inventory.PlayerInventory;
import xyz.destiall.pixelate.items.meta.Enchantment;
import xyz.destiall.pixelate.items.meta.ItemMeta;
import xyz.destiall.pixelate.modular.Module;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.position.Vector2;
import xyz.destiall.pixelate.score.Score;
import xyz.destiall.pixelate.score.ScoreType;
import xyz.destiall.pixelate.score.Scoreboard;
import xyz.destiall.pixelate.settings.Settings;
import xyz.destiall.pixelate.status.Gamemode;
import xyz.destiall.pixelate.timer.Timer;
import xyz.destiall.utility.java.events.EventHandler;

/**
 * Written by Rance & Yong Hong
 */
public class EntityPlayer extends EntityLiving implements Listener {
    private transient final SpriteSheet slash;
    private transient final Bitmap crosshair;
    private transient boolean playSwingAnimation;
    private transient boolean playPunchAnimation;
    private transient double swingAnimationTimer;
    private transient final float originalAnimSpeed;
    private final Experience exp;
    private final Score score;
    private Gamemode gamemode;

    public EntityPlayer() {
        Bitmap image = ResourceManager.getBitmap(R.drawable.player);
        scale = 0.5f;
        spriteSheet.addAnimation("LOOK RIGHT" , Imageable.createAnimation(image, 6, 3, 0, scale));
        spriteSheet.addAnimation("LOOK LEFT"  , Imageable.createAnimation(image, 6, 3,1, scale));
        spriteSheet.addAnimation("WALK RIGHT" , Imageable.createAnimation(image, 6, 3,2, scale));
        spriteSheet.addAnimation("WALK LEFT"  , Imageable.createAnimation(image, 6, 3,3, scale));
        spriteSheet.addAnimation("PUNCH RIGHT", Imageable.createAnimation(image, 6, 3,4, scale));
        spriteSheet.addAnimation("PUNCH LEFT" , Imageable.createAnimation(image, 6, 3,5, scale));
        spriteSheet.setCurrentAnimation("LOOK RIGHT");
        location = new Location((int) (Pixelate.WIDTH * 0.5), (int) (Pixelate.HEIGHT * 0.5));
        collision = new AABB(location.getX(), location.getY(), location.getX() + Tile.SIZE - 10, location.getY() + Tile.SIZE - 10);
        inventory = new PlayerInventory(this, 27);
        playSwingAnimation = false;
        playPunchAnimation = false;
        slash = new SpriteSheet();
        Bitmap slashSheet = ResourceManager.getBitmap(R.drawable.slashanimation);
        slash.addAnimation("RIGHT", Imageable.createAnimation(slashSheet, 4, 4, 0, 0.3f));
        slash.addAnimation("UP"   , Imageable.createAnimation(slashSheet, 4, 4, 1, 0.3f));
        slash.addAnimation("LEFT" , Imageable.createAnimation(slashSheet, 4, 4, 2, 0.3f));
        slash.addAnimation("DOWN" , Imageable.createAnimation(slashSheet, 4, 4, 3, 0.3f));
        crosshair = ResourceManager.getBitmap(R.drawable.crosshair);
        originalAnimSpeed = animationSpeed;
        exp = new Experience();
        score = new Score();
        gamemode = Gamemode.SURVIVAL;
    }

    /**
     * Set the skin of the player
     * @param image The sprite image to use
     */
    public void setSkin(Bitmap image) {
        spriteSheet = new SpriteSheet();
        spriteSheet.addAnimation("LOOK RIGHT" , Imageable.createAnimation(image, 6, 3, 0, scale));
        spriteSheet.addAnimation("LOOK LEFT"  , Imageable.createAnimation(image, 6, 3,1, scale));
        spriteSheet.addAnimation("WALK RIGHT" , Imageable.createAnimation(image, 6, 3,2, scale));
        spriteSheet.addAnimation("WALK LEFT"  , Imageable.createAnimation(image, 6, 3,3, scale));
        spriteSheet.addAnimation("PUNCH RIGHT", Imageable.createAnimation(image, 6, 3,4, scale));
        spriteSheet.addAnimation("PUNCH LEFT" , Imageable.createAnimation(image, 6, 3,5, scale));
        spriteSheet.setCurrentAnimation("LOOK RIGHT");
    }

    @Override
    public void damage(float damage) {
        if (gamemode == Gamemode.CREATIVE) return;
        super.damage(damage);
    }

    @Override
    public void damage(Entity damager, float damage) {
        if (gamemode == Gamemode.CREATIVE) return;
        super.damage(damager, damage);
    }

    @Override
    public void die() {
        Pixelate.getHud().setRespawnMenu();
        List<ItemStack> toDrop = new ArrayList<>(Arrays.asList(inventory.getItems().clone()));
        inventory.clear();
        World w;
        if ((w = location.getWorld()) != null) w.dropItems(toDrop, location);
        exp.setXP(0);
        exp.setLevel(0);
        Scoreboard.getInstance().addToLeaderboard("LOCALUSER", (float)score.getScore(), System.currentTimeMillis());
        score.clearScore();
    }

    public void respawn() {
        World w;
        if ((w = location.getWorld()) != null) teleport(w.getNearestEmpty(0, 0));
        health = 20.f;
    }

    @Override
    protected void updateSprite() {
        if (velocity.getX() > 0) facing = Direction.RIGHT;
        else if (velocity.getX() < 0) facing = Direction.LEFT;

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

        if (Settings.BLOCKTRACE) {
            Tile t = newLoc.getTile();
            if (t != null) {
                Vector2 tile = Screen.convert(t.getVector());
                screen.quadRing(tile.getX(), tile.getY(), Tile.SIZE, Tile.SIZE, 2, Color.WHITE);
            }
        }

        if (Settings.CROSSHAIR)
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
            Vector2 offset = vector.subtract(Tile.SIZE * 0.5, Tile.SIZE * 0.5);
            screen.draw(map, offset.getX(), offset.getY());
        }
    }

    @Override
    public PlayerInventory getInventory() {
        return (PlayerInventory) inventory;
    }

    /**
     * Send a message to this player
     * @param message The message to send
     */
    public void sendMessage(String message) {
        EventChat chat = new EventChat(message);
        Pixelate.HANDLER.call(chat);
    }

    /**
     * Set the gamemode of this player
     * @param gamemode The gamemode to set
     */
    public void setGamemode(Gamemode gamemode) {
        this.gamemode = gamemode;
    }

    /**
     * Get the gamemode of this player
     * @return The player's gamemode
     */
    public Gamemode getGamemode() {
        return gamemode;
    }

    /**
     * Get the current item in hand
     * @return The item in hand, or null if none
     */
    public ItemStack getItemInHand() {
        return getInventory().getItem(Pixelate.getHud().getHotbar().getCurrentSlot());
    }

    /**
     * Add experience points to this player
     * @param xp The experience to add
     */
    public void addXP(int xp) {
        if (exp.addXP(xp)) {
            World w;
            if ((w = location.getWorld()) == null) return;
            w.playSound(Sound.SoundType.ENTITY_ORBPICKUP, this.getLocation(), 1.0f);
        }
    }

    /**
     * Set the experience level of this player
     * @param level The new level
     */
    public void setXPLevel(int level) {
        exp.setLevel(level);
    }

    /**
     * Get the experience progress of this player
     * @return The experience progress
     */
    public float getXPProgress() {
        return (float) exp.getXP() / Experience.getRequiredXP(exp.getLevel());
    }

    /**
     * Get the current score of this player
     * @return The score
     */
    public int getScore() {
        return score.getScore();
    }

    /**
     * Get the experience level of this player
     * @return The experience level
     */
    public int getXPLevel() {
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
        if  (health <= 0) {
            e.setCancelled(true);
            return;
        }
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
        Material mat = tile.getMaterial();
        if (!playPunchAnimation) spriteSheet.setCurrentFrame(0);
        playPunchAnimation = true;
        float bbProgress = tile.getBlockBreakProgress(); //Out of 100.0
        float bbDuration = tile.getMaterial().getRequiredMineDuration(getItemInHand());
        float timeRelative = bbProgress / 100.0f * bbDuration;
        timeRelative += Timer.getDeltaTime();
        float bbProgressDiff = timeRelative / bbDuration * 100 - bbProgress;
        if (gamemode == Gamemode.CREATIVE) {
            bbProgressDiff = 500;
        }
        if (tile.addBlockBreakProgression(bbProgressDiff)) {
            if (gamemode != Gamemode.CREATIVE) {
                int luck = 0;
                ItemStack hand = getItemInHand();
                if (hand.getType().isTool()) {
                    int use = 1;
                    ItemMeta meta = hand.getItemMeta();
                    if (meta.hasEnchantment(Enchantment.DURABILITY)) {
                        use = new Random().nextInt(meta.getEnchantLevel(Enchantment.DURABILITY));
                        if (use > 1) {
                            use = 0;
                        }
                    }
                    if (meta.hasEnchantment(Enchantment.FORTUNE)) {
                        luck = meta.getEnchantLevel(Enchantment.FORTUNE);
                    }
                    meta.setDurability(hand.getItemMeta().getDurability() + use);
                    if (meta.getDurability() >= hand.getType().getMaxDurability()) {
                        hand.setAmount(0);
                    }
                    meta.setDurability(hand.getItemMeta().getDurability() + use);
                    if (meta.getDurability() >= hand.getType().getMaxDurability()) {
                        hand.setAmount(0);
                    }
                }
                //XP Drops
                int xpDrop = LootTable.getInstance().getXPDrops(mat, luck);
                if (exp.addXP(xpDrop))
                    w.playSound(Sound.SoundType.ENTITY_LEVELUP, this.getLocation(), 1.0f);
                score.addScore(ScoreType.GATHER_XP, xpDrop);
                if (xpDrop > 0)
                    w.playSound(Sound.SoundType.ENTITY_ORBPICKUP, this.getLocation(), 0.5f);
                //Add to score
                score.addScore(ScoreType.BREAK_ORE, 1);
            }
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
                Pixelate.getHud().setFurnaceDisplay(getInventory(), (FurnanceTile) container);
                FurnaceInventory furnaceInventory = (FurnaceInventory) container.getInventory();
                Pixelate.getHud().setFurnaceDisplay(getInventory(), (FurnanceTile) container);
            }else if (tile.getMaterial() == Material.ANVIL)
            {
                AnvilInventory anvilInventory = (AnvilInventory) container.getInventory();
                Pixelate.getHud().setAnvilDisplay(getInventory(), (AnvilTile) container);
            } else if (tile.getMaterial() == Material.CHEST) {
                Pixelate.getHud().setChestDisplay(getInventory(), (ChestInventory) container.getInventory());
            } else if (tile.getMaterial() == Material.ENCHANT_TABLE) {
                Pixelate.getHud().setEnchantingTable((EnchantTableTile) tile, getInventory());
            }
        } else if (tile.getMaterial() == Material.TNT) {
            tile.setMaterial(Material.STONE);
            Location location = tile.getLocation();
            EventIgniteTNT ev = new EventIgniteTNT(location);
            Pixelate.HANDLER.call(ev);
            if (ev.isCancelled()) return;
            w.spawnEntity(EntityPrimedTNT.class, location.add(Tile.SIZE * 0.25, Tile.SIZE * 0.25));
        } else if (tile.getMaterial() == Material.WORKBENCH) {
            Pixelate.getHud().setCraftingTable(getInventory());
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
        if (gamemode == Gamemode.CREATIVE) {
            Pixelate.getHud().setCreative(getInventory());
        } else {
            Pixelate.getHud().setInventory(getInventory());
        }
    }
}
