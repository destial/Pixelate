package xyz.destiall.pixelate.events.controls;

import xyz.destiall.utility.java.events.Event;

/**
 * Called when a message was entered into the chat
 */
public class EventDialogueAction extends Event {
    public enum Dialog_Action{
        YES,
        NO,
    }
    private Dialog_Action action;
    public EventDialogueAction(Dialog_Action action) {
        this.action = action;
    }

    /**
     * Get the action
     * @return The action
     */
    public Dialog_Action getAction() {
        return action;
    }

    /**
     * Set the action
     * @param action The new action
     */
    public void setAction(Dialog_Action action) {
        this.action = action;
    }
}
