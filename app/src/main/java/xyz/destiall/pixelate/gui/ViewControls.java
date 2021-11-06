package xyz.destiall.pixelate.gui;

import android.graphics.Color;
import android.graphics.Paint;

import xyz.destiall.java.events.EventHandler;
import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.events.EventJoystick;
import xyz.destiall.pixelate.events.EventMining;
import xyz.destiall.pixelate.events.EventOpenInventory;
import xyz.destiall.pixelate.events.EventTouch;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.position.Vector2;

public class ViewControls implements View {
    private final Paint outerCirclePaint;
    private final Paint innerCirclePaint;
    private final Paint minePaint;
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
        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(Color.GRAY);
        outerCirclePaint.setStyle(Paint.Style.STROKE);
        outerCirclePaint.setStrokeWidth(5);
        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(Color.BLUE);
        innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        actuator = new Vector2();
        actuator.setZero();
        minePaint = new Paint();
        minePaint.setColor(Color.GRAY);
        minePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mineButton = new Vector2(Game.WIDTH - 275, 800);
        mineButtonRadius = 100;
        invButton = new Vector2(Game.WIDTH - 150, 900);
        invButtonRadius = 50;
        eventJoystick = new EventJoystick(actuator.getX(), actuator.getY(), EventJoystick.Action.DOWN);
        Game.HANDLER.registerListener(this);
    }

    @Override
    public void render(Screen screen) {
        screen.getCanvas().drawCircle(
                (int) outerCircleCenter.getX(),
                (int) outerCircleCenter.getY(),
                outerCircleRadius,
                outerCirclePaint
        );
        screen.getCanvas().drawCircle(
                (int) innerCircleCenter.getX(),
                (int) innerCircleCenter.getY(),
                innerCircleRadius,
                innerCirclePaint
        );
        screen.getCanvas().drawCircle(
                (int) mineButton.getX(),
                (int) mineButton.getY(),
                mineButtonRadius * (mine ? 0.8f : 1),
                minePaint
        );
        screen.getCanvas().drawCircle(
                (int) invButton.getX(),
                (int) invButton.getY(),
                invButtonRadius,
                minePaint
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
