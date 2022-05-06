package xyz.destiall.pixelate.entities;

import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.effects.Effect;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.environment.sounds.Sound;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.timer.Timer;

/**
 * Written by Rance
 */
public class EntityPrimedTNT extends Entity {
    private float explosionTimer = 5f;
    private boolean sizzled;
    protected EntityPrimedTNT() {}

    public EntityPrimedTNT(double x, double y, World world) {
        scale = 0.8f;
        spriteSheet.addAnimation(Material.TNT.name(), Imageable.createAnimation(ResourceManager.getBitmap(R.drawable.primed_tnt), 1, 2,0, scale));
        spriteSheet.setCurrentAnimation(Material.TNT.name());
        spriteSheet.setCurrentFrame(0);
        location.set(x, y);
        location.setWorld(world);
        animationSpeed = 5;
        sizzled = false;
    }

    @Override
    public void update() {
        super.update();
        if (explosionTimer == 5.f && !sizzled) {
            sizzle();
        }
        explosionTimer -= Timer.getDeltaTime();
        if (explosionTimer <= 0 && !isRemoved()) {
            explode();
        }
    }

    private void sizzle() {
        sizzled = true;
        World w;
        if ((w = location.getWorld()) == null) return;
        w.playSound(Sound.SoundType.SIZZLE, location, 1.f);
    }

    private void explode() {
        World w;
        if ((w = location.getWorld()) == null) return;
        this.remove();
        w.getNearestEntities(location, Tile.SIZE * 2).forEach(e -> {
            if (e instanceof EntityLiving) {
                ((EntityLiving) e).damage(this, (float) ((Tile.SIZE * 2 - e.getLocation().distance(location)) / 20f));
            } else {
                e.remove();
            }
        });
        AABB explosionBounds = new AABB(location.getX() - Tile.SIZE * 2, location.getY() - Tile.SIZE * 2, location.getX() + 2 * Tile.SIZE, location.getY() + 2 * Tile.SIZE);
        w.findTiles(explosionBounds).forEach(t -> {
            if (t.getTileType() == Tile.TileType.BACKGROUND) return;
            double distance = t.getVector().distance(location.toVector()) / Tile.SIZE;
            if (Math.random() * distance < 1.5) w.breakTile(t);
            else t.addBlockBreakProgression((float) ((1f / distance) * 100));
        });
        w.playEffect(Effect.EffectType.EXPLOSION, location);
        w.playSound(Sound.SoundType.EXPLOSION, location, 1.f);
    }
}
