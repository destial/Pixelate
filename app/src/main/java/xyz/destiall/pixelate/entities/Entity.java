package xyz.destiall.pixelate.entities;

import android.graphics.Bitmap;

import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.SpriteSheet;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.position.AABB;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.position.Vector2;

public abstract class Entity extends Imageable implements Updateable, Renderable {
    protected Location location;
    protected float scale;
    protected Vector2 velocity;
    protected int currentAnimation;
    protected final SpriteSheet spriteSheet;
    protected AABB collision;
    protected Direction facing;

    public Entity(Bitmap image, int rows, int columns) {
        super(image, rows, columns);
        spriteSheet = new SpriteSheet();
        velocity = new Vector2();
        scale = 1;
        location = new Location(0, 0);
        facing = Direction.RIGHT;
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
        currentAnimation++;
        if (currentAnimation >= columns)  {
            currentAnimation = 0;
        }
        spriteSheet.setCurrentAnimation(currentAnimation);
    }

    public AABB getBounds() {
        return collision;
    }

    @Override
    public void render(Screen screen) {
        Bitmap map = spriteSheet.getCurrentAnimation();
        if (scale != 1 && scale > 0) {
            map = Bitmap.createScaledBitmap(map, (int)(map.getWidth() * scale), (int)(map.getHeight() * scale), false);
        }
        Vector2 offset = screen.convert(location.toVector());
        screen.getCanvas().drawBitmap(map, (int) offset.getX(), (int) offset.getY(), null);
    }

    @Override
    public void tick() {}

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
}
