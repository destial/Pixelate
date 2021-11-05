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

    public void setState(String name) {
        currentState = name;
    }

    @Override
    public void update() {
        if (currentState != null) {
            states.get(currentState).update();
        }
    }

    @Override
    public void tick() {
        if (currentState != null) {
            states.get(currentState).tick();
        }
    }

    public void render(Canvas canvas) {
        if (currentState != null) {
            states.get(currentState).render(canvas);
        }
    }
}
