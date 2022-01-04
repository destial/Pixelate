package xyz.destiall.pixelate.entities;

import android.graphics.Color;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.items.inventory.PlayerInventory;
import xyz.destiall.pixelate.pathfinding.PathFindingAI;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.position.Vector2;

public class EntityMonster extends EntityLiving {
    private final Type type;
    private PathFindingAI pathFinding;
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
        playerInventory = new PlayerInventory(this, 9);
        updateAABB();
    }

    @Override
    public void updateSprite() {
        facing = Direction.RIGHT;
        if (velocity.getX() < 0) facing = Direction.LEFT;
        String anim = (velocity.isZero() ? "LOOK " : "WALK ") + facing.name();
        spriteSheet.setCurrentSprite(anim);
    }

    public Type getType() {
        return type;
    }

    @Override
    public void update() {
        super.update();
        velocity.setZero();
        location.getWorld().getNearestEntities(location, Tile.SIZE * 3).stream().filter(e -> e != this).forEach(e -> {
            if (e instanceof EntityPlayer) {
                EntityPlayer living = (EntityPlayer) e;
                velocity.set(living.location.getX() - location.getX(), living.location.getY() - location.getY());
                if (velocity.length() < Tile.SIZE) {
                    living.damage(0.5f);
                }
                try {
                    velocity.normalize().multiply(2);
                } catch (Exception ignored) {}
            }
        });
    }

    @Override
    public void render(Screen screen) {
        super.render(screen);
        Vector2 loc = Screen.convert(location);
        screen.bar(loc.getX(), loc.getY() - 10, Tile.SIZE, 2, Color.RED, Color.GREEN, health / maxHealth);
    }
}
