package xyz.destiall.pixelate.events.controls;

import xyz.destiall.utility.java.events.Event;

/**
 * Called when a message was entered into the chat
 */
public class EventChat extends Event {
    private String message;
    public EventChat(String message) {
        this.message = message;
    }

    /**
     * Get the message
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message
     * @param message The new message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
