package xyz.destiall.pixelate.entities;

import java.util.List;

import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.effects.Effect;
import xyz.destiall.pixelate.environment.sounds.Sound;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.timer.Timer;

public class EntityPrimedTNT extends Entity {
    private float explosionTimer = 5f;
    private boolean sizzled;
    protected EntityPrimedTNT() {}

    public EntityPrimedTNT(double x, double y, World world) {
        location.set(x, y);
        location.setWorld(world);
        scale = 0.8f;
        spriteSheet.addAnimation("TNT", Imageable.createAnimation(ResourceManager.getBitmap(R.drawable.primed_tnt), 1, 2,0));
        spriteSheet.setCurrentAnimation("TNT");
        spriteSheet.setCurrentFrame(0);
        animationSpeed = 5;
        sizzled = false;
    }

    @Override
    public void update() {
        super.update();
        if (explosionTimer == 5.f && !sizzled) {
            sizzle();
            sizzled = true;
        }
        explosionTimer -= Timer.getDeltaTime();
        if (explosionTimer <= 0 && !isRemoved()) {
            explode();
        }
    }

    public void sizzle() {
        World w;
        if ((w = location.getWorld()) == null) return;
        w.playSound(Sound.SoundType.SIZZLE, location, 1.f);
    }

    public void explode() {
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
        AABB explosionBounds = new AABB(location.getX() - Tile.SIZE, location.getY() - Tile.SIZE, location.getX() + 2 * Tile.SIZE, location.getY() + 2 * Tile.SIZE);
        w.findTiles(explosionBounds).forEach(t -> {
            if (t.getTileType() == Tile.TileType.BACKGROUND) return;
            List<ItemStack> drops = w.breakTile(t);
            for (ItemStack drop : drops) {
                w.dropItem(drop, t.getVector());
            }
        });
        w.playEffect(Effect.EffectType.EXPLOSION, location);
        w.playSound(Sound.SoundType.EXPLOSION, location, 1.f);
    }
}
