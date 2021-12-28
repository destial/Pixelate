package xyz.destiall.pixelate.gui;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import xyz.destiall.java.events.EventHandler;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.environment.WorldManager;
import xyz.destiall.pixelate.events.ControlEvent;
import xyz.destiall.pixelate.events.EventJoystick;
import xyz.destiall.pixelate.events.EventLeftHoldButton;
import xyz.destiall.pixelate.events.EventLeftReleaseButton;
import xyz.destiall.pixelate.events.EventLeftTapButton;
import xyz.destiall.pixelate.events.EventOpenInventory;
import xyz.destiall.pixelate.events.EventRightHoldButton;
import xyz.destiall.pixelate.events.EventRightReleaseButton;
import xyz.destiall.pixelate.events.EventRightTapButton;
import xyz.destiall.pixelate.events.EventTouch;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.gui.buttons.Button;
import xyz.destiall.pixelate.gui.buttons.CircleButton;
import xyz.destiall.pixelate.position.Vector2;
import xyz.destiall.pixelate.states.StateGame;

public class ViewControls implements View {
    private final int outerCircleRadius;
    private final int innerCircleRadius;
    private final Vector2 outerCircleCenter;
    private final Vector2 innerCircleCenter;
    private final Vector2 actuator;
    private boolean joystick;

    private final EventJoystick eventJoystick;
    private final EventOpenInventory eventOpenInventory;

    private final EventLeftHoldButton eventLeftHoldButton;
    private final EventLeftReleaseButton eventLeftReleaseButton;
    private final EventLeftTapButton eventLeftTapButton;

    private final EventRightHoldButton eventRightHoldButton;
    private final EventRightReleaseButton eventRightReleaseButton;
    private final EventRightTapButton eventRightTapButton;

    //private final EventGamePause eventGamePause;

    private final List<Button> buttons;

    public ViewControls() {
        buttons = new ArrayList<>();
        outerCircleCenter = new Vector2(275, Pixelate.HEIGHT - 200);
        innerCircleCenter = new Vector2(275, Pixelate.HEIGHT - 200);
        outerCircleRadius = 100;
        innerCircleRadius = 50;
        actuator = new Vector2();
        actuator.setZero();

        eventJoystick = new EventJoystick(actuator.getX(), actuator.getY(), EventJoystick.Action.DOWN);
        eventLeftHoldButton = new EventLeftHoldButton();
        eventLeftReleaseButton = new EventLeftReleaseButton();
        eventLeftTapButton = new EventLeftTapButton();
        eventRightHoldButton = new EventRightHoldButton();
        eventRightReleaseButton = new EventRightReleaseButton();
        eventRightTapButton = new EventRightTapButton();
        eventOpenInventory = new EventOpenInventory();
        //eventGamePause = new EventGamePause();
        Pixelate.HANDLER.registerListener(this);

        // Button location initialisation
        Button pause = new CircleButton(new Vector2(Pixelate.WIDTH - (Pixelate.WIDTH * 0.9), Pixelate.HEIGHT - (Pixelate.HEIGHT * 0.9)), 25, Color.RED);
        pause.onTap(() -> {
            HUD.INSTANCE.setPauseMenu();
            Pixelate.PAUSED = true;
        });
        buttons.add(pause);

        Button inv = new CircleButton(new Vector2(Pixelate.WIDTH - 150, Pixelate.HEIGHT - 100), 50, Color.GREEN);
        inv.onTap(() -> Pixelate.HANDLER.call(eventOpenInventory));
        buttons.add(inv);

        Button left = new CircleButton(new Vector2(Pixelate.WIDTH - 300, Pixelate.HEIGHT - 150), 50, Color.YELLOW);
        left.onHold(() -> Pixelate.HANDLER.call(eventLeftHoldButton));
        left.onTap(() -> Pixelate.HANDLER.call(eventLeftTapButton));
        left.onRelease(() -> Pixelate.HANDLER.call(eventLeftReleaseButton));
        buttons.add(left);

        Button right = new CircleButton(new Vector2(Pixelate.WIDTH - 200, Pixelate.HEIGHT - 250), 50, Color.BLUE);
        right.onHold(() -> Pixelate.HANDLER.call(eventRightHoldButton));
        right.onTap(() -> Pixelate.HANDLER.call(eventRightTapButton));
        right.onRelease(() -> Pixelate.HANDLER.call(eventRightReleaseButton));
        buttons.add(right);

        Button world = new CircleButton(new Vector2(Pixelate.WIDTH - (Pixelate.WIDTH * 0.1), Pixelate.HEIGHT - (Pixelate.HEIGHT * 0.9)), 25, Color.MAGENTA);
        world.onTap(() -> {
            StateGame gameState = (StateGame) Pixelate.getGSM().getState("Game");
            WorldManager wm = gameState.getWorldManager();
            String currentworld = wm.getCurrentWorldName();
            if (currentworld.equals("Cave"))
                Pixelate.setWorld("Overworld");
            else
                Pixelate.setWorld("Cave");
        });
        buttons.add(world);
    }

    @Override
    public void render(Screen screen) {
        for (Button button : buttons) {
            button.render(screen);
        }
        screen.ring(outerCircleCenter.getX(), outerCircleCenter.getY(), outerCircleRadius, 20, Color.RED);
        screen.circle(innerCircleCenter.getX(), innerCircleCenter.getY(), innerCircleRadius, Color.BLUE);

        EntityPlayer player = ((StateGame) Pixelate.getGSM().getCurrentState()).getPlayer();
        screen.bar(Pixelate.WIDTH * 0.25, Pixelate.HEIGHT * 0.75, Pixelate.WIDTH * 0.2, 50, Color.RED, Color.GREEN, player.getHealth() / player.getMaxHealth());
    }

    @Override
    public void update() {
        for (Button button : buttons) {
            if (button.isHolding()) {
                button.hold();
            }
        }
        innerCircleCenter.setX(outerCircleCenter.getX() + actuator.getX() * outerCircleRadius);
        innerCircleCenter.setY(outerCircleCenter.getY() + actuator.getY() * outerCircleRadius);
    }

    private boolean isOnJoystick(float x, float y) {
        return outerCircleCenter.distanceSquared(x, y) <= outerCircleRadius * outerCircleRadius;
    }

    public boolean isJoystick() {
        return joystick;
    }

    public void setJoystick(boolean joystick) {
        this.joystick = joystick;
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
        buttons.clear();
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
                    }
                    for (Button button : buttons) {
                        if (button.isOn(x, y)) {
                            if (!button.isHolding()) {
                                button.setHold(true);
                                button.tap();
                            }
                        }
                    }
                    break;
                case MOVE:
                    if (isJoystick()) {
                        setActuator(x, y);
                    }
                    for (Button button : buttons) {
                        if (button.isOn(x, y)) {
                            if (!button.isHolding()) {
                                button.setHold(true);
                                button.tap();
                            }
                        } else if (button.isHolding()) {
                            button.setHold(false);
                            button.release();
                        }
                    }
                    break;
                case UP:
                    if (isJoystick()) {
                        setJoystick(false);
                        setActuator(0, 0);
                        Pixelate.HANDLER.call(eventJoystick.update(0, 0, ControlEvent.Action.UP));
                    }
                    for (Button button : buttons) {
                        if (button.isHolding()) {
                            button.setHold(false);
                            button.release();
                        }
                    }
                    break;
            }
        }
    }
}
