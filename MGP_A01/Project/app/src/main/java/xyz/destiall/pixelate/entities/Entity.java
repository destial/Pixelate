package xyz.destiall.pixelate.entities;

import android.graphics.Bitmap;

import java.util.HashMap;

import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.SpriteSheet;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.modular.Modular;
import xyz.destiall.pixelate.modular.Module;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.position.Vector2;
import xyz.destiall.pixelate.timer.Timer;

public abstract class Entity extends Imageable implements Updateable, Renderable, Modular {
    protected final SpriteSheet spriteSheet;
    protected final HashMap<Class<? extends Module>, Module> modules;
    protected Location location;
    protected float scale;
    protected Vector2 velocity;
    protected float currentAnimation;
    protected AABB collision;
    protected Direction facing;
    protected Direction target;

    public Entity(Bitmap image, int rows, int columns) {
        super(image, rows, columns);
        spriteSheet = new SpriteSheet();
        velocity = new Vector2();
        scale = 1;
        location = new Location(0, 0);
        facing = Direction.RIGHT;
        target = facing;
        modules = new HashMap<>();
    }

    public Location getLocation() {
        return location.clone();
    }

    public void teleport(Location location) {
        this.location = location;
    }

    public Vector2 getVelocity() {
        return velocity.clone();
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    @Override
    public void update() {
        // Update sprite animation
        currentAnimation += Timer.getDeltaTime() * Timer.getFPS();
        if (currentAnimation >= columns)  {
            currentAnimation = 0;
        }
        spriteSheet.setCurrentAnimation((int) currentAnimation);

        for (Module m : modules.values()) {
            m.update();
        }
    }

    public AABB getBounds() {
        return collision;
    }

    @Override
    public void render(Screen screen) {
        Bitmap map = spriteSheet.getCurrentAnimation();
        if (scale != 1 && scale > 0) {
            map = Imageable.scaleImage(map, scale);
        }
        Vector2 offset = Screen.convert(location.toVector());
        screen.draw(map, offset.getX(), offset.getY());
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
        return (N) modules.get(clazz);
    }

    @Override
    public <N extends Module> void addModule(N module) {
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
}
