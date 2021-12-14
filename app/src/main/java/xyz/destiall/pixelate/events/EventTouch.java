package xyz.destiall.pixelate.events;

import android.view.MotionEvent;

public class EventTouch extends ControlEvent {
    private final MotionEvent motionEvent;
    public EventTouch(MotionEvent e) {
        super(e.getAction() == MotionEvent.ACTION_UP ? Action.UP : e.getAction() == MotionEvent.ACTION_MOVE ? Action.MOVE : Action.DOWN);
        motionEvent = e;
    }

    public float getX() {
        return motionEvent.getX();
    }

    public float getY() {
        return motionEvent.getY();
    }
}
