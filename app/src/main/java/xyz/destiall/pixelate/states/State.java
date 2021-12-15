package xyz.destiall.pixelate.states;

import android.graphics.Canvas;

import xyz.destiall.pixelate.GameSurface;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.modular.Module;

public abstract class State implements Updateable, Module {
    protected GameSurface surface;
    public State(GameSurface surface) {
        this.surface = surface;
    }

    public abstract void render(Canvas canvas);
    public abstract void destroy();
}
