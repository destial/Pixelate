package xyz.destiall.pixelate.environment.effects;

import android.graphics.Bitmap;

import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.timer.Timer;

public class ParticleEffect extends Entity {
    private final ParticleType type;
    public ParticleEffect(ParticleType type) {
        super(type.image, 1, type.columns);
        this.type = type;
        spriteSheet.addAnimation("EFFECT", createAnimation(0));
        spriteSheet.setCurrentAnimation("EFFECT");
        animationSpeed *= 0.2f;
    }

    public void teleport(Location location) {
        this.location = location.clone();
        this.location.subtract((type.image.getWidth() / (float) type.columns) * 0.5, type.image.getHeight() * 0.5);
    }

    protected void updateSpriteAnimation() {
        currentAnimation += Timer.getDeltaTime() * animationSpeed;
        if (currentAnimation >= columns)  {
            remove();
            return;
        }
        spriteSheet.setCurrentFrame((int) currentAnimation);
    }

    @Override
    public void remove() {
        if (isRemoved() || location.getWorld() == null) return;
        location.getWorld().getModule(EffectsModule.class).removeEffect((ParticleEffect) this);
    }

    @Override
    public void render(Screen screen) {
        super.render(screen);
    }

    public ParticleType getType() {
        return type;
    }

    public enum ParticleType {
        EXPLOSION(R.drawable.explosion, 4);

        private final Bitmap image;
        private final int columns;

        ParticleType(int id, int columns) {
            this.columns = columns;
            this.image = ResourceManager.getBitmap(id);
        }
    }
}
