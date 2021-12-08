package xyz.destiall.pixelate.gui;

import android.graphics.Color;

import xyz.destiall.java.events.EventHandler;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.events.EventJoystick;
import xyz.destiall.pixelate.events.EventMining;
import xyz.destiall.pixelate.events.EventOpenInventory;
import xyz.destiall.pixelate.events.EventPlace;
import xyz.destiall.pixelate.events.EventPlayerMineAnimation;
import xyz.destiall.pixelate.events.EventTouch;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.position.Vector2;
import xyz.destiall.pixelate.states.StateGame;

public class ViewControls implements View {
    private final int outerCircleRadius;
    private final int innerCircleRadius;
    private final Vector2 outerCircleCenter;
    private final Vector2 innerCircleCenter;
    private final Vector2 actuator;
    private final Vector2 mineButton;
    private final Vector2 placeButton;
    private final Vector2 invButton;
    private final Vector2 pauseButton;
    private final int invButtonRadius;
    private final int mineButtonRadius;
    private final int placeButtonRadius;
    private final int pauseButtonRadius;
    private final EventJoystick eventJoystick;
    private boolean joystick;
    private boolean mine;
    private boolean place;

    public ViewControls() {
        outerCircleCenter = new Vector2(275, Pixelate.HEIGHT - 200);
        innerCircleCenter = new Vector2(275, Pixelate.HEIGHT - 200);
        outerCircleRadius = 100;
        innerCircleRadius = 50;
        actuator = new Vector2();
        actuator.setZero();

        // Button location initialisation
        pauseButton = new Vector2(Pixelate.WIDTH - (Pixelate.WIDTH * 0.9), Pixelate.HEIGHT - (Pixelate.HEIGHT * 0.9));
        invButton = new Vector2(Pixelate.WIDTH - 150, Pixelate.HEIGHT - 100);
        mineButton = new Vector2(Pixelate.WIDTH - 300, Pixelate.HEIGHT - 150);
        placeButton = new Vector2(Pixelate.WIDTH - 200, Pixelate.HEIGHT - 250);

        // Button radius
        pauseButtonRadius = 25;
        invButtonRadius = 50;
        placeButtonRadius = 50;
        mineButtonRadius = 50;

        eventJoystick = new EventJoystick(actuator.getX(), actuator.getY(), EventJoystick.Action.DOWN);
        Pixelate.HANDLER.registerListener(this);
    }

    @Override
    public void render(Screen screen) {
        screen.ring(outerCircleCenter.getX(), outerCircleCenter.getY(), outerCircleRadius, 20, Color.RED);
        screen.circle(innerCircleCenter.getX(), innerCircleCenter.getY(), innerCircleRadius, Color.BLUE);
        screen.circle(mineButton.getX(), mineButton.getY(), mineButtonRadius * (mine ? 0.8f : 1), Color.YELLOW);
        screen.circle(placeButton.getX(), placeButton.getY(), placeButtonRadius * (place ? 0.8f : 1), Color.YELLOW);
        screen.circle(invButton.getX(), invButton.getY(), invButtonRadius, Color.GREEN);
        screen.circle(pauseButton.getX(), pauseButton.getY(), pauseButtonRadius, Color.RED);

        screen.bar(Pixelate.WIDTH * 0.25, Pixelate.HEIGHT * 0.75, Pixelate.WIDTH * 0.2, 10, Color.RED, Color.GREEN,
                ((StateGame) Pixelate.getGSM().getCurrentState()).getPlayer().getHealth() / 20f);
    }

    @Override
    public void update() {
        if (actuator.isZero()) {
            innerCircleCenter.set(outerCircleCenter);
        } else {
            innerCircleCenter.setX(outerCircleCenter.getX() + actuator.getX() * outerCircleRadius);
            innerCircleCenter.setY(outerCircleCenter.getY() + actuator.getY() * outerCircleRadius);
        }
        if(isMining())
            Pixelate.HANDLER.call(new EventPlayerMineAnimation());
    }

    private boolean isOnJoystick(float x, float y) {
        double distance = Math
                .sqrt(Math.pow(outerCircleCenter.getX() - x, 2) + Math.pow(outerCircleCenter.getY() - y, 2));
        return distance < outerCircleRadius;
    }

    private boolean isOnPauseButton(float x, float y) {
        return isOnButton(pauseButton, x, y, pauseButtonRadius);
    }

    private boolean isOnMineButton(float x, float y) {
        return isOnButton(mineButton, x, y, mineButtonRadius);
    }

    private boolean isOnPlaceButton(float x, float y) {
        return isOnButton(placeButton, x, y, placeButtonRadius);
    }

    private boolean isOnInvButton(float x, float y) {
        return isOnButton(invButton, x, y, invButtonRadius);
    }

    private boolean isOnButton(Vector2 vect, float x, float y, int radius) {
        return vect.distance(x, y) < radius;
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
        if (mine)
            Pixelate.HANDLER.call(new EventMining());
    }

    public void setPlacing(boolean place) {
        this.place = place;
        if (place)
            Pixelate.HANDLER.call(new EventPlace());
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
        Pixelate.HANDLER.call(eventJoystick.update(actuator.getX(), actuator.getY(), EventJoystick.Action.DOWN));
    }

    @Override
    public void destroy() {
        Pixelate.HANDLER.unregisterListener(this);
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
                    Pixelate.HANDLER.call(new EventOpenInventory());
                } else if (isOnPlaceButton(x, y)) {
                    setPlacing(true);
                } else if (isOnPauseButton(x, y) && !Pixelate.paused) {

                    Pixelate.setWorld("Cave");
                    // Game.HANDLER.call(new EventGamePause());
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
                    Pixelate.HANDLER.call(eventJoystick.update(0, 0, EventJoystick.Action.UP));
                } else if (isOnPlaceButton(x, y)) {
                    setPlacing(false);
                }
                break;
            default:
                break;
        }
    }
}
