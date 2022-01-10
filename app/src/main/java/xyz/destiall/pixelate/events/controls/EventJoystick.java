package xyz.destiall.pixelate.events.controls;

/**
 * Called when the joystick is used
 */
public class EventJoystick extends ControlEvent {
    private double offsetX;
    private double offsetY;

    public EventJoystick(double x, double y, Action action) {
        super(action);
        offsetX = x;
        offsetY = y;
        this.action = action;
    }

    public EventJoystick update(double x, double y, Action action) {
        offsetX = x;
        offsetY = y;
        this.action = action;
        return this;
    }

    /**
     * Get the offset X from center
     * @return The offset X
     */
    public double getOffsetX() {
        return offsetX;
    }

    /**
     * Get the offset Y from center
     * @return The offset Y
     */
    public double getOffsetY() {
        return offsetY;
    }
}
