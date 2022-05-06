package xyz.destiall.pixelate.events.controls;

import xyz.destiall.utility.java.events.Event;

/**
 * An abstract input event
 */
public abstract class ControlEvent extends Event {
    protected Action action;
    public ControlEvent(Action action) {
        super(true);
        this.action = action;
    }

    public void setAction(Action action) {
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
