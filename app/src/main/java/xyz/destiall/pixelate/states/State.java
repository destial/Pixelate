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

    /**
     * Render this state to the canvas
     * @param canvas The canvas
     */
    public abstract void render(Canvas canvas);

    /**
     * Clean up this state
     */
    public abstract void destroy();

    /**
     * Reset this state
     */
    public abstract void reset();

    /**
     * Load this state from a file
     * @param path The simple path (e.g. "game.json")
     * @return true if loaded successfully, otherwise false
     */
    public abstract boolean load(String path);

    /**
     * Save this state to a file
     * @param path The simple path (e.g. "game.json)
     */
    public abstract void save(String path);
}
