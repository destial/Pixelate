package xyz.destiall.pixelate.events;

import android.view.KeyEvent;

public class EventKeyboard extends ControlEvent {
    private final int keyCode;
    private final KeyEvent event;
    public EventKeyboard(int keyCode, KeyEvent e) {
        super(e.getAction() == KeyEvent.ACTION_UP ? Action.UP : Action.DOWN);
        this.keyCode = keyCode;
        this.event = e;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public KeyEvent getEvent() {
        return event;
    }
}
