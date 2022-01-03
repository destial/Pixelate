package xyz.destiall.pixelate.events;

import xyz.destiall.java.events.Event;

/**
 * An abstract input event
 */
public abstract class ControlEvent extends Event {
    protected Action action;
    public ControlEvent(Action action) {
        //super(true);
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public enum Action {
        UP,
        DOWN,
        MOVE
    }
}
