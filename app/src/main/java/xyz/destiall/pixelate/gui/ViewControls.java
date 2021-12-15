package xyz.destiall.pixelate.gui;

import android.graphics.Color;
import android.view.KeyEvent;

import xyz.destiall.java.events.EventHandler;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.environment.WorldManager;
import xyz.destiall.pixelate.events.ControlEvent;
import xyz.destiall.pixelate.events.EventJoystick;
import xyz.destiall.pixelate.events.EventKeyboard;
import xyz.destiall.pixelate.events.EventLeftHoldButton;
import xyz.destiall.pixelate.events.EventLeftReleaseButton;
import xyz.destiall.pixelate.events.EventLeftTapButton;
import xyz.destiall.pixelate.events.EventOpenInventory;
import xyz.destiall.pixelate.events.EventRightReleaseButton;
import xyz.destiall.pixelate.events.EventRightTapButton;
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
    private final Vector2 switchWorldButton;
    private final int invButtonRadius;
    private final int mineButtonRadius;
    private final int placeButtonRadius;
    private final int pauseButtonRadius;
    private final int switchWorldButtonRadius;
    private final EventJoystick eventJoystick;
    private boolean joystick;
    private boolean swing;
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
        switchWorldButton = new Vector2(Pixelate.WIDTH - (Pixelate.WIDTH * 0.1), Pixelate.HEIGHT - (Pixelate.HEIGHT * 0.9));

        // Button radius
        pauseButtonRadius = 25;
        invButtonRadius = 50;
        placeButtonRadius = 50;
        mineButtonRadius = 50;
        switchWorldButtonRadius = 25;

        eventJoystick = new EventJoystick(actuator.getX(), actuator.getY(), EventJoystick.Action.DOWN);
        Pixelate.HANDLER.registerListener(this);
    }

    @Override
    public void render(Screen screen) {
        screen.ring(outerCircleCenter.getX(), outerCircleCenter.getY(), outerCircleRadius, 20, Color.RED);
        screen.circle(innerCircleCenter.getX(), innerCircleCenter.getY(), innerCircleRadius, Color.BLUE);
        screen.circle(mineButton.getX(), mineButton.getY(), mineButtonRadius * (swing ? 0.8f : 1), Color.YELLOW);
        screen.circle(placeButton.getX(), placeButton.getY(), placeButtonRadius * (place ? 0.8f : 1), Color.YELLOW);
        screen.circle(invButton.getX(), invButton.getY(), invButtonRadius, Color.GREEN);
        screen.circle(pauseButton.getX(), pauseButton.getY(), pauseButtonRadius, Color.RED);
        screen.circle(switchWorldButton.getX(), switchWorldButton.getY(), switchWorldButtonRadius, Color.BLUE);

        EntityPlayer player = ((StateGame) Pixelate.getGSM().getCurrentState()).getPlayer();
        screen.bar(Pixelate.WIDTH * 0.25, Pixelate.HEIGHT * 0.75, Pixelate.WIDTH * 0.2, 10, Color.RED, Color.GREEN, player.getHealth() / player.getMaxHealth());
    }

    @Override
    public void update() {
        if (actuator.isZero()) {
            innerCircleCenter.set(outerCircleCenter);
        } else {
            innerCircleCenter.setX(outerCircleCenter.getX() + actuator.getX() * outerCircleRadius);
            innerCircleCenter.setY(outerCircleCenter.getY() + actuator.getY() * outerCircleRadius);
        }
        if (isSwinging())
            Pixelate.HANDLER.call(new EventLeftHoldButton());
    }

    private boolean isOnJoystick(float x, float y) {
        return isOnButton(outerCircleCenter, x, y, outerCircleRadius);
    }

    private boolean isOnSwitchWorldButton(float x, float y)
    {
        return isOnButton(switchWorldButton, x, y, switchWorldButtonRadius);
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
        return vect.distanceSquared(x, y) <= radius * radius;
    }

    public boolean isJoystick() {
        return joystick;
    }

    public boolean isSwinging() {
        return swing;
    }

    public boolean isPlacing() {
        return place;
    }

    public void setJoystick(boolean joystick) {
        this.joystick = joystick;
    }

    public void setSwinging(boolean swing) {
        this.swing = swing;
        if (swing)
            Pixelate.HANDLER.call(new EventLeftTapButton());
    }

    public void setPlacing(boolean place) {
        this.place = place;
        if (place)
            Pixelate.HANDLER.call(new EventRightTapButton());
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
        Pixelate.HANDLER.call(eventJoystick.update(actuator.getX(), actuator.getY(), ControlEvent.Action.DOWN));
    }

    @Override
    public void destroy() {
        Pixelate.HANDLER.unregisterListener(this);
    }

    @EventHandler
    public void onTouch(EventTouch e) {
        if (!Pixelate.PAUSED) {
            float x = e.getX();
            float y = e.getY();
            switch (e.getAction()) {
                case DOWN:
                    if (isOnJoystick(x, y)) {
                        setJoystick(true);
                    } else if (isOnMineButton(x, y)) {
                        setSwinging(true);
                    } else if (isOnInvButton(x, y)) {
                        Pixelate.HANDLER.call(new EventOpenInventory());
                    } else if (isOnPlaceButton(x, y)) {
                        setPlacing(true);
                    } else if (isOnPauseButton(x, y)) {
                        HUD.INSTANCE.setPauseMenu();
                        Pixelate.PAUSED = true;

                        //Pixelate.HANDLER.call(new EventGamePause());
                    } else if (isOnSwitchWorldButton(x,y)) {
                        StateGame gameState = ((StateGame) Pixelate.getGSM().getState("Game"));
                        WorldManager wm = gameState.getObject(WorldManager.class);
                        String currentworld = wm.getCurrentWorldName();
                        if (currentworld.equals("Cave"))
                            Pixelate.setWorld("Overworld");
                        else
                            Pixelate.setWorld("Cave");


                    }
                    break;
                case MOVE:
                    if (isJoystick()) {
                        setActuator(x, y);
                    } else if (isSwinging()) {
                        if (!isOnMineButton(x, y))
                            setSwinging(false);
                    } else if (isPlacing()) {
                        if (!isOnPlaceButton(x, y))
                            setPlacing(false);
                    }
                    break;
                case UP:
                    if (isOnMineButton(x, y)) {
                        setSwinging(false);
                        Pixelate.HANDLER.call(new EventLeftReleaseButton());
                    } else if (isJoystick()) {
                        setJoystick(false);
                        setActuator(0, 0);
                        Pixelate.HANDLER.call(eventJoystick.update(0, 0, EventJoystick.Action.UP));
                    } else if (isOnPlaceButton(x, y)) {
                        setPlacing(false);
                        Pixelate.HANDLER.call(new EventRightReleaseButton());
                    }
            }
        }
    }

    // TODO: Add keyboard support
    @EventHandler
    public void onKeyboard(EventKeyboard e) {
        float x = 0; float y = 0;
        if (e.getAction() == ControlEvent.Action.DOWN) {
            if (e.getKeyCode() == KeyEvent.KEYCODE_W) {
                y = -5;
            } else if (e.getKeyCode() == KeyEvent.KEYCODE_S) {
                y = 5;
            }

            if (e.getKeyCode() == KeyEvent.KEYCODE_A) {
                x = -5;
            } else if (e.getKeyCode() == KeyEvent.KEYCODE_D) {
                x = 5;
            }
        }
        if (x == 0 && y == 0) {
            setActuator(0, 0);
            return;
        }
        setActuator((float) outerCircleCenter.getX() + x, (float) outerCircleCenter.getY() + y);
    }
}
