package xyz.destiall.pixelate.modules;

import java.util.LinkedList;
import java.util.List;

import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.effects.Effect;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.modular.Component;
import xyz.destiall.pixelate.position.Location;

/**
 * Written by Rance
 */
public class EffectsModule implements Renderable, Component<World> {
    private transient World world;
    private transient final List<Effect> effects;

    public EffectsModule() {
        effects = new LinkedList<>();
    }

    public EffectsModule setWorld(World world) {
        this.world = world;
        return this;
    }

    /**
     * Play a particle effect at the requested location
     * @param type The particle type
     * @param location The requested location
     */
    public Effect spawnEffect(Effect.EffectType type, Location location) {
        Effect effect = new Effect(type);
        effect.teleport(location.clone().setWorld(world));
        effects.add(effect);
        return effect;
    }

    /**
     * Remove this effect from this module
     * @param effect The effect to remove
     */
    public void removeEffect(Effect effect) {
        effects.remove(effect);
    }

    @Override
    public void update() {
        for (int i = 0; i < effects.size(); i++) {
            effects.get(i).update();
        }
    }

    @Override
    public void render(Screen screen) {
        for (int i = 0; i < effects.size(); i++) {
            effects.get(i).render(screen);
        }
    }

    @Override
    public void destroy() {
        effects.clear();
    }

    @Override
    public World getParent() {
        return world;
    }

    @Override
    public void setParent(World world) {
        this.world = world;
    }
}
