package xyz.destiall.pixelate.states;

import android.graphics.Canvas;

import xyz.destiall.pixelate.GameSurface;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.modular.Module;

public abstract class State implements Updateable, Module {
    protected transient GameSurface surface;
    public State() {}

    public void setSurface(GameSurface surface) {
        this.surface = surface;
    }

    public abstract void render(Canvas canvas);
    public abstract void reset();
    public abstract boolean load(String path);
    public abstract void save(String path);
    public abstract void destroy();
}
