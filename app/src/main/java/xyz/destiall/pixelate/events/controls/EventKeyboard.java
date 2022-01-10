package xyz.destiall.pixelate.events.controls;

import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Called when a keyboard key is pressed or released
 */
public class EventKeyboard extends ControlEvent {
    private final int keyCode;
    private final KeyEvent event;
    private static final List<Integer> keysPressed = new ArrayList<>();

    public EventKeyboard(int keyCode, KeyEvent e) {
        super(convert(e.getAction()));
        this.keyCode = keyCode;
        this.event = e;
        if (e.getAction() == KeyEvent.ACTION_UP) {
            if (keysPressed.contains(keyCode)) {
                keysPressed.remove((Object) keyCode);
            }
        } else {
            if (!keysPressed.contains(keyCode)) {
                keysPressed.add(keyCode);
            }
        }
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

    private static Action convert(int action) {
        if (action == KeyEvent.ACTION_UP) {
            return Action.UP;
        }
        return Action.DOWN;
    }

    public static boolean isKeyPressed(int keyCode) {
        return keysPressed.contains(keyCode);
    }
}
