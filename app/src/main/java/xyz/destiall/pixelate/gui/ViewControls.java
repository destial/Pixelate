package xyz.destiall.pixelate.gui;

import android.graphics.Color;

import xyz.destiall.java.events.EventHandler;
import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.events.EventJoystick;
import xyz.destiall.pixelate.events.EventMining;
import xyz.destiall.pixelate.events.EventOpenInventory;
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
    private final Vector2 invButton;
    private final int invButtonRadius;
    private final int mineButtonRadius;
    private final EventJoystick eventJoystick;
    private boolean joystick;
    private boolean mine;

    public ViewControls() {
        outerCircleCenter = new Vector2(275, 800);
        innerCircleCenter = new Vector2(275, 800);
        outerCircleRadius = 100;
        innerCircleRadius = 50;
        actuator = new Vector2();
        actuator.setZero();
        mineButton = new Vector2(Game.WIDTH - 275, 800);
        mineButtonRadius = 100;
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
                Color.GRAY
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
                Color.GRAY
        );
        screen.circle(
                invButton.getX(),
                invButton.getY(),
                invButtonRadius,
                Color.GRAY
        );
    }

    @Override
    public void update() {
        innerCircleCenter.setX(outerCircleCenter.getX() + actuator.getX() * outerCircleRadius);
        innerCircleCenter.setY(outerCircleCenter.getY() + actuator.getY() * outerCircleRadius);
    }

    @Override
    public void tick() {

    }

    public boolean isOnJoystick(float x, float y) {
        double distance = Math.sqrt(Math.pow(outerCircleCenter.getX() - x, 2) + Math.pow(outerCircleCenter.getY() - y, 2));
        return distance < outerCircleRadius;
    }

    public boolean isOnMineButton(float x, float y) {
        double distance = Math.sqrt(Math.pow(mineButton.getX() - x, 2) + Math.pow(mineButton.getY() - y, 2));
        return distance < mineButtonRadius;
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

    public void setJoystick(boolean joystick) {
        this.joystick = joystick;
    }

    public void setMining(boolean mine) {
        this.mine = mine;
        if (mine) Game.HANDLER.call(new EventMining());
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
                }
                if (isOnMineButton(x, y)) {
                    setMining(true);
                }
                if (isOnInvButton(x, y)) {
                    Game.HANDLER.call(new EventOpenInventory());
                }
                break;
            case MOVE:
                if (isJoystick()) {
                    setActuator(x, y);
                }
                if (isMining()) {
                    if (!isOnMineButton(x, y))
                        setMining(false);
                }
                break;
            case UP:
                if (isOnMineButton(x, y)) {
                    setMining(false);
                }
                if (isJoystick()) {
                    setJoystick(false);
                    setActuator(0, 0);
                    Game.HANDLER.call(eventJoystick.update(0, 0, EventJoystick.Action.UP));
                }
                break;
            default: break;
        }
    }
}
