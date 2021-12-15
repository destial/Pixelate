package xyz.destiall.pixelate.environment.effects;

import java.util.LinkedList;
import java.util.List;

import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.graphics.Renderable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.modular.Module;
import xyz.destiall.pixelate.position.Location;

public class EffectsModule implements Renderable, Module {
    private final World world;
    private final List<ParticleEffect> effects;

    public EffectsModule(World world) {
        effects = new LinkedList<>();
        this.world = world;
    }

    public ParticleEffect spawnEffect(ParticleEffect.ParticleType type, Location location) {
        ParticleEffect effect = new ParticleEffect(type);
        effect.teleport(location.setWorld(world));
        effects.add(effect);
        return effect;
    }

    public void removeEffect(ParticleEffect e) {
        effects.remove(e);
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
}
