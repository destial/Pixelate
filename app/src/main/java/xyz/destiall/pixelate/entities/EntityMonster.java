package xyz.destiall.pixelate.entities;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.materials.Material;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.SpriteSheet;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.items.inventory.EntityInventory;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.position.Vector2;
import xyz.destiall.pixelate.utils.StringUtils;

/**
 * Written by Rance & Yong Hong
 */
public class EntityMonster extends EntityLiving {
    private final List<Material> drops = new ArrayList<>();
    private Type type = Type.ZOMBIE;

    // private PathFindingModule pathFinding;
    protected EntityMonster() {}

    public EntityMonster(Entity.Type type) {
        this.type = type;
        scale = 0.5f;
        refresh();
        location = new Location((int) (Pixelate.WIDTH * 0.5), (int) (Pixelate.HEIGHT * 0.5));
        health = 20f;
        this.type = type;
        collision = new AABB(location.getX(), location.getY(), location.getX() + Tile.SIZE - 10, location.getY() + Tile.SIZE - 10);
        inventory = new EntityInventory(this, 9);
        updateAABB();
    }

    public void setType(Type type) {
        this.type = type;
        refresh();
    }

    @Override
    public void die() {
        super.die();
        if (health <= 0) {
            World world;
            if ((world = location.getWorld()) == null) return;
            for (Material drop : drops) {
                world.dropItem(new ItemStack(drop, 1), location);
            }
        }
    }

    @Override
    public void refresh() {
        Bitmap image = ResourceManager.getBitmap(type.getDrawable());
        drops.clear();
        spriteSheet = new SpriteSheet();
        spriteSheet.addAnimation(StringUtils.LOOK_RIGHT, Imageable.createAnimation(image, type.getRows(), type.getColumns(), 0, scale));
        spriteSheet.addAnimation(StringUtils.LOOK_LEFT, Imageable.createAnimation(image, type.getRows(), type.getColumns(),1, scale));
        switch (type) {
            case CREEPER:
                spriteSheet.addAnimation(StringUtils.BLOW_RIGHT, Imageable.createAnimation(image, type.getRows(), type.getColumns(),4, scale));
                spriteSheet.addAnimation(StringUtils.BLOW_LEFT, Imageable.createAnimation(image, type.getRows(), type.getColumns(), 5, scale));
                drops.add(Material.GUNPOWDER);
            case ZOMBIE:
                spriteSheet.addAnimation(StringUtils.WALK_RIGHT, Imageable.createAnimation(image, type.getRows(), type.getColumns(),2, scale));
                spriteSheet.addAnimation(StringUtils.WALK_LEFT, Imageable.createAnimation(image, type.getRows(), type.getColumns(),3, scale));
                drops.add(Material.ROTTEN_FLESH);
                break;
            case SKELETON:
                spriteSheet.addAnimation(StringUtils.WALK_RIGHT, Imageable.createAnimation(image, type.getRows(), type.getColumns(),0, scale));
                spriteSheet.addAnimation(StringUtils.WALK_LEFT, Imageable.createAnimation(image, type.getRows(), type.getColumns(),1, scale));
                drops.add(Material.BONE);
                break;
            default: break;
        }
        spriteSheet.setCurrentAnimation(StringUtils.LOOK_RIGHT);
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
