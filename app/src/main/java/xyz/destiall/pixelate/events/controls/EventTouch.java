package xyz.destiall.pixelate.events.controls;

import android.view.MotionEvent;

/**
 * Called when the user touches on the screen
 */
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