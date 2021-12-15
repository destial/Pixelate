package xyz.destiall.pixelate.events;

import android.view.KeyEvent;

/**
 * Called when a keyboard key is pressed or released
 */
public class EventKeyboard extends ControlEvent {
    private final int keyCode;
    private final KeyEvent event;
    public EventKeyboard(int keyCode, KeyEvent e) {
        super(e.getAction() == KeyEvent.ACTION_UP ? Action.UP : Action.DOWN);
        this.keyCode = keyCode;
        this.event = e;
    }

    /**
     * Get the keycode that was activated
     * @return The keycode
     */
    public int getKeyCode() {
        return keyCode;
    }

    /**
     * Get the raw Android KeyEvent
     * @return The keyEvent
     */
    public KeyEvent getEvent() {
        return event;
    }
}
