package xyz.destiall.pixelate.events.controls;

/**
 * Called when the user touches on the screen
 */
public class EventTouch extends ControlEvent {
    private int id;
    private float x;
    private float y;

    public EventTouch() {
        super(Action.UP);
    }

    public EventTouch(int id, Action action, float x, float y) {
        super(action);
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
