package xyz.destiall.pixelate.events;

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

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }
}
