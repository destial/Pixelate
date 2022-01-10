package xyz.destiall.pixelate.entities;

import android.graphics.Bitmap;
import android.graphics.Color;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.SpriteSheet;
import xyz.destiall.pixelate.items.inventory.EntityInventory;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.position.Vector2;

/**
 * Written by Rance & Yong Hong
 */
public class EntityMonster extends EntityLiving {
    private Type type = Type.ZOMBIE;

    //private PathFindingModule pathFinding;
    protected EntityMonster() {}

    public void setType(Type type) {
        this.type = type;
        refresh();
    }

    @Override
    public void refresh() {
        //setImage(ResourceManager.getBitmap(type.getDrawable()), type.getRows(), type.getColumns());
        Bitmap image = ResourceManager.getBitmap(type.getDrawable());
        spriteSheet = new SpriteSheet();
        spriteSheet.addAnimation("LOOK RIGHT", Imageable.createAnimation(image, type.getRows(), type.getColumns(), 0));
        spriteSheet.addAnimation("LOOK LEFT", Imageable.createAnimation(image, type.getRows(), type.getColumns(),1));
        switch (type) {
            case CREEPER:
                spriteSheet.addAnimation("BLOW RIGHT", Imageable.createAnimation(image, type.getRows(), type.getColumns(),4));
                spriteSheet.addAnimation("BLOW LEFT", Imageable.createAnimation(image, type.getRows(), type.getColumns(), 5));
            case ZOMBIE:
                spriteSheet.addAnimation("WALK RIGHT", Imageable.createAnimation(image, type.getRows(), type.getColumns(),2));
                spriteSheet.addAnimation("WALK LEFT", Imageable.createAnimation(image, type.getRows(), type.getColumns(),3));
                break;
            case SKELETON:
                spriteSheet.addAnimation("WALK RIGHT", Imageable.createAnimation(image, type.getRows(), type.getColumns(),0));
                spriteSheet.addAnimation("WALK LEFT", Imageable.createAnimation(image, type.getRows(), type.getColumns(),1));
                break;
            default: break;
        }
        spriteSheet.setCurrentAnimation("LOOK RIGHT");
    }

    public EntityMonster(Entity.Type type) {
        location = new Location((int) (Pixelate.WIDTH * 0.5), (int) (Pixelate.HEIGHT * 0.5));
        this.type = type;
        refresh();
        scale = 0.5f;
        health = 20f;
        this.type = type;
        collision = new AABB(location.getX(), location.getY(), location.getX() + Tile.SIZE - 10, location.getY() + Tile.SIZE - 10);
        inventory = new EntityInventory(this, 9);
        updateAABB();
    }

    @Override
    public void updateSprite() {
        facing = Direction.RIGHT;
        if (velocity.getX() < 0) facing = Direction.LEFT;
        String anim = (velocity.isZero() ? "LOOK " : "WALK ") + facing.name();
        spriteSheet.setCurrentAnimation(anim);
    }

    public Type getType() {
        return type;
    }

    @Override
    public void update() {
        super.update();
        velocity.setZero();
        World w;
        if ((w = location.getWorld()) == null) return;
        w.getNearestEntities(location, Tile.SIZE * 3).stream().anyMatch(e -> {
            if (e instanceof EntityPlayer) {
                EntityPlayer living = (EntityPlayer) e;
                velocity.set(living.location.getX() - location.getX(), living.location.getY() - location.getY());
                if (velocity.length() < Tile.SIZE) {
                    living.damage(0.5f);
                }
                try {
                    velocity.normalize().multiply(2);
                } catch (Exception ignored) {}
                return true;
            }
            return false;
        });
    }

    @Override
    public void render(Screen screen) {
        super.render(screen);
        Vector2 loc = Screen.convert(location);
        screen.bar(loc.getX(), loc.getY() - 10, Tile.SIZE, 10, Color.RED, Color.GREEN, health / maxHealth);
    }
}
