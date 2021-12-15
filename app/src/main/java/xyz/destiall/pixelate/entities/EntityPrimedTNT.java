package xyz.destiall.pixelate.entities;

import java.util.List;

import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.timer.Timer;

public class EntityPrimedTNT extends Entity {
    private float explosionTimer = 5f;

    public EntityPrimedTNT(double x, double y, World world) {
        super(ResourceManager.getBitmap(R.drawable.primed_tnt), 1, 2);
        location.set(x, y);
        location.setWorld(world);
        scale = 0.8f;
        spriteSheet.addAnimation("TNT", createAnimation(0));
        spriteSheet.setCurrentAnimation("TNT");
        spriteSheet.setCurrentFrame(0);
        animationSpeed = 5;
    }

    @Override
    public void update() {
        super.update();
        explosionTimer -= Timer.getDeltaTime();
        if (explosionTimer <= 0 && !isRemoved()) {
            explode();
        }
    }

    public void explode() {
        if (location.getWorld() == null) return;
        this.remove();
        location.getWorld().getNearestEntities(location, Tile.SIZE * 2).forEach(e -> {
            if (e instanceof EntityLiving) {
                ((EntityLiving) e).damage((float) ((Tile.SIZE * 2 - e.getLocation().distance(location)) / 20f));
            } else {
                e.remove();
            }
        });
        AABB explosionBounds = new AABB(location.getX() - Tile.SIZE, location.getY() - Tile.SIZE, location.getX() + 2 * Tile.SIZE, location.getY() + 2 * Tile.SIZE);
        location.getWorld().findTiles(explosionBounds).forEach(t -> {
            if (t.getTileType() == Tile.TileType.BACKGROUND) return;
            List<ItemStack> drops = location.getWorld().breakTile(t);
            for (ItemStack drop : drops) {
                location.getWorld().dropItem(drop, t.getVector());
            }
        });
    }
}
