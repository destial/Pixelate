package xyz.destiall.pixelate.environment.effects;

import android.graphics.Bitmap;

import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.SpriteSheet;
import xyz.destiall.pixelate.modules.EffectsModule;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.timer.Timer;
import xyz.destiall.pixelate.utils.StringUtils;

/**
 * Written by Rance
 */
public class Effect extends Entity {
    private transient float currentAnimation;
    private EffectType type;

    protected Effect() {}

    public void setType(EffectType type) {
        this.type = type;
        refresh();
    }

    public Effect(EffectType type) {
        this.type = type;
        spriteSheet.addAnimation(StringUtils.EFFECT, Imageable.createAnimation(type.image, 1, type.columns, 0));
        spriteSheet.setCurrentAnimation(StringUtils.EFFECT);
        animationSpeed *= 0.2f;
    }

    public void refresh() {
        spriteSheet = new SpriteSheet();
        spriteSheet.addAnimation(StringUtils.EFFECT, Imageable.createAnimation(type.image, 1, type.columns, 0));
        spriteSheet.setCurrentAnimation(StringUtils.EFFECT);
    }

    public void teleport(Location location) {
        this.location = location.clone();
        this.location.subtract((type.image.getWidth() / (float) type.columns) * 0.5, type.image.getHeight() * 0.5);
    }

    protected void updateSpriteAnimation() {
        currentAnimation += Timer.getDeltaTime() * animationSpeed;
        if (currentAnimation >= spriteSheet.getColumns())  {
            remove();
            return;
        }
        spriteSheet.setCurrentFrame((int) currentAnimation);
    }

    @Override
    public void remove() {
        World w;
        if (isRemoved() || (w = location.getWorld()) == null) return;
        w.getModule(EffectsModule.class).removeEffect(this);
    }

    @Override
    public void render(Screen screen) {
        super.render(screen);
    }

    public EffectType getType() {
        return type;
    }

    public enum EffectType {
        EXPLOSION(R.drawable.explosion, 4);

        private final Bitmap image;
        private final int columns;

        EffectType(int id, int columns) {
            this.columns = columns;
            this.image = ResourceManager.getBitmap(id);
        }
    }
}
