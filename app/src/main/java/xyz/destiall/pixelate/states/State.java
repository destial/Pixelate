package xyz.destiall.pixelate.states;

import android.graphics.Canvas;

import xyz.destiall.pixelate.GameSurface;
import xyz.destiall.pixelate.graphics.Updateable;

public abstract class State implements Updateable {
    protected GameSurface surface;
    public State(GameSurface surface) {
        this.surface = surface;
    }
    public abstract void render(Canvas canvas);
}
