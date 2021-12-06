package xyz.destiall.pixelate.entities;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.items.Inventory;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Location;

public class EntityMonster extends EntityLiving {
    private final Type type;
    public EntityMonster(Entity.Type type) {
        super(ResourceManager.getBitmap(type.getDrawable()), type.getRows(), type.getColumns());
        location = new Location((int) (Pixelate.WIDTH * 0.5), (int) (Pixelate.HEIGHT * 0.5));
        spriteSheet.addSprite("LOOK RIGHT", createAnimation(0));
        spriteSheet.addSprite("LOOK LEFT", createAnimation(1));
        switch (type) {
            case CREEPER:
                spriteSheet.addSprite("BLOW RIGHT", createAnimation(4));
                spriteSheet.addSprite("BLOW LEFT", createAnimation(5));
            case ZOMBIE:
                spriteSheet.addSprite("WALK RIGHT", createAnimation(2));
                spriteSheet.addSprite("WALK LEFT", createAnimation(3));
                break;
            case SKELETON:
                spriteSheet.addSprite("WALK RIGHT", createAnimation(0));
                spriteSheet.addSprite("WALK LEFT", createAnimation(1));
                break;
            default: break;
        }
        spriteSheet.setCurrentSprite("LOOK RIGHT");
        scale = 0.5f;
        health = 20f;
        this.type = type;
        collision = new AABB(location.getX(), location.getY(), location.getX() + Tile.SIZE - 10, location.getY() + Tile.SIZE - 10);
        inventory = new Inventory(this, 9);
        updateAABB();
    }

    public Type getType() {
        return type;
    }

    @Override
    public void update() {
        super.update();
        location.getWorld().getNearestEntities(location, Tile.SIZE).stream().filter(e -> e != this).forEach(e -> {
            if (e instanceof EntityPlayer) {
                EntityPlayer living = (EntityPlayer) e;
                living.damage(0.5f);
            }
        });
    }
}
