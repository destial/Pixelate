package xyz.destiall.pixelate.gui;

import android.graphics.Color;

import xyz.destiall.java.events.EventHandler;
import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.events.EventJoystick;
import xyz.destiall.pixelate.events.EventMining;
import xyz.destiall.pixelate.events.EventOpenInventory;
import xyz.destiall.pixelate.events.EventPlace;
import xyz.destiall.pixelate.events.EventTouch;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.position.Vector2;

public class ViewControls implements View {
    private final int outerCircleRadius;
    private final int innerCircleRadius;
    private final Vector2 outerCircleCenter;
    private final Vector2 innerCircleCenter;
    private final Vector2 actuator;
    private final Vector2 mineButton;
    private final Vector2 placeButton;
    private final Vector2 invButton;
    private final int invButtonRadius;
    private final int mineButtonRadius;
    private final int placeButtonRadius;
    private final EventJoystick eventJoystick;
    private boolean joystick;
    private boolean mine;
    private boolean place;

    public ViewControls() {
        outerCircleCenter = new Vector2(275, 800);
        innerCircleCenter = new Vector2(275, 800);
        outerCircleRadius = 100;
        innerCircleRadius = 50;
        actuator = new Vector2();
        actuator.setZero();
        mineButton = new Vector2(Game.WIDTH - 300, 850);
        placeButton = new Vector2(Game.WIDTH - 200, 750);
        placeButtonRadius = 50;
        mineButtonRadius = 50;
        invButton = new Vector2(Game.WIDTH - 150, 900);
        invButtonRadius = 50;
        eventJoystick = new EventJoystick(actuator.getX(), actuator.getY(), EventJoystick.Action.DOWN);
        Game.HANDLER.registerListener(this);
    }

    @Override
    public void render(Screen screen) {
        screen.ring(
                outerCircleCenter.getX(),
                outerCircleCenter.getY(),
                outerCircleRadius,
                20,
                Color.RED
        );
        screen.circle(
                innerCircleCenter.getX(),
                innerCircleCenter.getY(),
                innerCircleRadius,
                Color.BLUE
        );
        screen.circle(
                mineButton.getX(),
                mineButton.getY(),
                mineButtonRadius * (mine ? 0.8f : 1),
                Color.YELLOW
        );
        screen.circle(
                placeButton.getX(),
                placeButton.getY(),
                placeButtonRadius * (place ? 0.8f: 1),
                Color.YELLOW
        );
        screen.circle(
                invButton.getX(),
                invButton.getY(),
                invButtonRadius,
                Color.GREEN
        );
    }

    @Override
    public void update() {
        if (actuator.isZero()) {
            innerCircleCenter.set(outerCircleCenter);
        } else {
            innerCircleCenter.setX(outerCircleCenter.getX() + actuator.getX() * outerCircleRadius);
            innerCircleCenter.setY(outerCircleCenter.getY() + actuator.getY() * outerCircleRadius);
        }
    }

    @Override
    public void tick() {}

    public boolean isOnJoystick(float x, float y) {
        double distance = Math.sqrt(Math.pow(outerCircleCenter.getX() - x, 2) + Math.pow(outerCircleCenter.getY() - y, 2));
        return distance < outerCircleRadius;
    }

    public boolean isOnMineButton(float x, float y) {
        double distance = Math.sqrt(Math.pow(mineButton.getX() - x, 2) + Math.pow(mineButton.getY() - y, 2));
        return distance < mineButtonRadius;
    }

    public boolean isOnPlaceButton(float x, float y) {
        double distance = Math.sqrt(Math.pow(placeButton.getX() - x, 2) + Math.pow(placeButton.getY() - y, 2));
        return distance < placeButtonRadius;
    }

    public boolean isOnInvButton(float x, float y) {
        double distance = Math.sqrt(Math.pow(invButton.getX() - x, 2) + Math.pow(invButton.getY() - y, 2));
        return distance < invButtonRadius;
    }

    public boolean isJoystick() {
        return joystick;
    }

    public boolean isMining() {
        return mine;
    }

    public boolean isPlacing() {
        return place;
    }

    public void setJoystick(boolean joystick) {
        this.joystick = joystick;
    }

    public void setMining(boolean mine) {
        this.mine = mine;
        if (mine) Game.HANDLER.call(new EventMining());
    }

    public void setPlacing(boolean place) {
        this.place = place;
        if (place) Game.HANDLER.call(new EventPlace());
    }

    public void setActuator(float x, float y) {
        if (x == 0 && y == 0) {
            actuator.setZero();
            return;
        }
        double deltaX = x - outerCircleCenter.getX();
        double deltaY = y - outerCircleCenter.getY();
        double distance = Vector2.lengthOf(deltaX, deltaY);
        if (distance < outerCircleRadius) {
            actuator.setX(deltaX / outerCircleRadius);
            actuator.setY(deltaY / outerCircleRadius);
        } else {
            actuator.setX(deltaX / distance);
            actuator.setY(deltaY / distance);
        }
        Game.HANDLER.call(eventJoystick.update(actuator.getX(), actuator.getY(), EventJoystick.Action.DOWN));
    }

    @Override
    public void destroy() {
        Game.HANDLER.unregisterListener(this);
    }

    @EventHandler
    public void onTouch(EventTouch e) {
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
            case DOWN:
                if (isOnJoystick(x, y)) {
                    setJoystick(true);
                } else if (isOnMineButton(x, y)) {
                    setMining(true);
                } else if (isOnInvButton(x, y)) {
                    Game.HANDLER.call(new EventOpenInventory());
                } else if (isOnPlaceButton(x, y)) {
                    setPlacing(true);
                }
                break;
            case MOVE:
                if (isJoystick()) {
                    setActuator(x, y);
                } else if (isMining()) {
                    if (!isOnMineButton(x, y))
                        setMining(false);
                } else if (isPlacing()) {
                    if (!isOnPlaceButton(x, y))
                        setPlacing(false);
                }
                break;
            case UP:
                if (isOnMineButton(x, y)) {
                    setMining(false);
                } else if (isJoystick()) {
                    setJoystick(false);
                    setActuator(0, 0);
                    Game.HANDLER.call(eventJoystick.update(0, 0, EventJoystick.Action.UP));
                } else if (isOnPlaceButton(x, y)) {
                    setPlacing(false);
                }
                break;
            default: break;
        }
    }
}
