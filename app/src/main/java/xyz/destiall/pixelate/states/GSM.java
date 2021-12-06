package xyz.destiall.pixelate.states;

import android.graphics.Canvas;

import java.util.HashMap;

import xyz.destiall.pixelate.graphics.Updateable;

public class GSM implements Updateable {
    private final HashMap<String, State> states;
    private String currentState;
    public GSM() {
        currentState = null;
        states = new HashMap<>();
    }

    public void addState(String name, State state) {
        states.put(name, state);
    }

    public State getState(String name) { return states.get(name); }

    public void setState(String name) {
        currentState = name;
    }

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
}
