package xyz.destiall.pixelate.events;

public class EventJoystick extends ControlEvent {
    private final double offsetX;
    private final double offsetY;
    public EventJoystick(double x, double y, Action action) {
        super(action);
        offsetX = x;
        offsetY = y;
        this.action = action;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }
}
