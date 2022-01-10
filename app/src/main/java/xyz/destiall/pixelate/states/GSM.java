package xyz.destiall.pixelate.states;

import android.graphics.Canvas;

import java.util.HashMap;

import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.modular.Module;

/**
 * Written by Rance & Yong Hong
 */
public class GSM implements Updateable, Module {
    private final HashMap<String, State> states;
    private String currentState;
    public GSM() {
        currentState = null;
        states = new HashMap<>();
    }

    /**
     * Add a state to this GSM
     * @param name The name of this state
     * @param state The state object
     */
    public void addState(String name, State state) {
        states.put(name, state);
    }

    /**
     * Get a state from this GSM
     * @param name The state to get
     * @return The state object, null if not found
     */
    public State getState(String name) {
        return states.get(name);
    }

    /**
     * Set the current active state of this GSM
     * @param name The state name
     */
    public void setState(String name) {
        currentState = name;
    }

    /**
     * Get the current active state of this GSM
     * @return The current active state
     */
    public State getCurrentState() {
        return states.get(currentState);
    }

    @Override
    public void update() {
        if (currentState != null) {
            getCurrentState().update();
        }
    }

    public void render(Canvas canvas) {
        if (currentState != null) {
            getCurrentState().render(canvas);
        }
    }

    @Override
    public void destroy() {
        for (State state : states.values()) {
            state.destroy();
        }
        states.clear();
    }
}
