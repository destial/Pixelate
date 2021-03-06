package xyz.destiall.pixelate.entities;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.Collection;
import java.util.HashMap;

import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.SpriteSheet;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.modular.Component;
import xyz.destiall.pixelate.modular.Modular;
import xyz.destiall.pixelate.modular.Module;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.position.Vector2;
import xyz.destiall.pixelate.settings.Settings;

/**
 * Written by Rance & Yong Hong
 */
public abstract class Entity implements Updateable, Renderable, Modular {
    private final HashMap<Class<? extends Module>, Module> modules;
    private transient boolean removed;

    protected transient SpriteSheet spriteSheet;

    protected Location location;
    protected Vector2 velocity;
    protected AABB collision;
    protected Direction facing;
    protected Direction target;

    protected float animationSpeed;
    protected float scale;

    protected Entity() {
        spriteSheet = new SpriteSheet();
        velocity = new Vector2();
        scale = 1;
        location = new Location(0, 0);
        facing = Direction.RIGHT;
        target = facing;
        modules = new HashMap<>();
        removed = false;
        animationSpeed = 60;
    }

    public void refresh() {
        for (Module m : modules.values()) {
            if (m instanceof Component) {
                Component<Entity> component = (Component<Entity>) m;
                component.setParent(this);
            }
        }
    }

    /**
     * Get this entity's location
     * @return An immutable location
     */
    public Location getLocation() {
        return getLocation(true);
    }

    /**
     * Get this entity's location
     * @param mutable Get a mutable reference or an immutable clone
     * @return The location
     */
    public Location getLocation(boolean mutable) {
        return mutable ? location : location.clone();
    }

    /**
     * Teleport this entity to the requested location
     * @param location The location to teleport to
     */
    public void teleport(Location location) {
        this.location = location.clone();
    }

    /**
     * Get this entity's velocity
     * @return An immutable velocity
     */
    public Vector2 getVelocity() {
        return velocity;
    }

    /**
     * Set this entity's velocity
     * @param velocity The velocity to set
     */
    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity.clone();
    }

    /**
     * Get this entity's facing direction
     * @return The facing direction
     */
    public Direction getFacing() {
        return facing;
    }

    /**
     * Get this entity's target direction
     * @return The target direction
     */
    public Direction getTarget() {
        return target;
    }

    @Override
    public void update() {
        updateSpriteAnimation();
        updateModules();
    }

    protected void updateModules() {
        for (Module m : modules.values()) {
            m.update();
        }
    }

    protected void updateSpriteAnimation() {
        // Update sprite animation
        spriteSheet.setSpeed(animationSpeed);
        spriteSheet.update();
    }

    /**
     * Get the collision bounds of this entity
     * @return The bounds
     */
    public AABB getBounds() {
        return collision;
    }

    /**
     * Remove this entity from the world
     */
    @SuppressWarnings("all")
    public void remove() {
        World w;
        if (isRemoved() || (w = location.getWorld()) == null) return;
        w.removeEntity(this);
        removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }

    @Override
    public void render(Screen screen) {
        Bitmap map = spriteSheet.getCurrentSprite();
        Vector2 offset = Screen.convert(location.toVector());
        screen.draw(map, offset.getX(), offset.getY());

        if (Settings.HITBOXES)
            screen.quadRing(offset.getX(), offset.getY(), map.getWidth(), map.getHeight(), 5f, Color.WHITE);
    }

    public enum Type {
        ZOMBIE(R.drawable.zombie, 4, 3),
        SKELETON(R.drawable.skeleton, 2, 3),
        CREEPER(R.drawable.creeper, 6, 3);

        private final int drawable;
        private final int rows;
        private final int columns;
        Type(int drawable, int rows, int columns) {
            this.drawable = drawable;
            this.rows = rows;
            this.columns = columns;
        }

        public int getRows() {
            return rows;
        }

        public int getColumns() {
            return columns;
        }

        public int getDrawable() {
            return drawable;
        }
    }

    public enum Direction {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        private final Vector2 vector;
        Direction(int x, int y) {
            vector = new Vector2(x, y);
        }

        public Vector2 getVector() {
            return vector.clone();
        }
    }

    @Override
    public <N extends Module> N getModule(Class<N> clazz) {
        if (!hasModule(clazz)) return null;
        return clazz.cast(modules.get(clazz));
    }

    @Override
    public void addModule(Module module) {
        modules.putIfAbsent(module.getClass(), module);
    }

    @Override
    public <N extends Module> boolean hasModule(Class<N> clazz) {
        return modules.containsKey(clazz);
    }

    @Override
    public <N extends Module> N removeModule(Class<N> clazz) {
        N module = getModule(clazz);
        if (module != null) {
            module.destroy();
            modules.remove(clazz);
        }
        return module;
    }

    @Override
    public Collection<Module> getModules() {
        return modules.values();
    }
}
