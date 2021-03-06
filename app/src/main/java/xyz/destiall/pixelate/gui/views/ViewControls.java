package xyz.destiall.pixelate.gui.views;

import android.graphics.Color;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.List;

import xyz.destiall.utility.java.events.EventHandler;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.environment.WorldManager;
import xyz.destiall.pixelate.events.controls.ControlEvent;
import xyz.destiall.pixelate.events.controls.EventChat;
import xyz.destiall.pixelate.events.controls.EventJoystick;
import xyz.destiall.pixelate.events.controls.EventKeyboard;
import xyz.destiall.pixelate.events.controls.EventLeftHoldButton;
import xyz.destiall.pixelate.events.controls.EventLeftReleaseButton;
import xyz.destiall.pixelate.events.controls.EventLeftTapButton;
import xyz.destiall.pixelate.events.controls.EventOpenInventory;
import xyz.destiall.pixelate.events.controls.EventOpenKeyboard;
import xyz.destiall.pixelate.events.controls.EventRightHoldButton;
import xyz.destiall.pixelate.events.controls.EventRightReleaseButton;
import xyz.destiall.pixelate.events.controls.EventRightTapButton;
import xyz.destiall.pixelate.events.controls.EventTouch;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.gui.buttons.Button;
import xyz.destiall.pixelate.gui.buttons.CircleButton;
import xyz.destiall.pixelate.gui.buttons.ImageButton;
import xyz.destiall.pixelate.gui.buttons.QuadButton;
import xyz.destiall.pixelate.position.Vector2;
import xyz.destiall.pixelate.states.StateGame;
import xyz.destiall.pixelate.status.Gamemode;
import xyz.destiall.pixelate.utils.StringUtils;

/**
 * Written by Rance
 */
public class ViewControls implements View {
    private final int outerCircleRadius;
    private final int innerCircleRadius;
    private final Vector2 outerCircleCenter;
    private final Vector2 innerCircleCenter;
    private final Vector2 actuator;
    private boolean joystick;

    private final EventJoystick eventJoystick;
    private final EventOpenInventory eventOpenInventory = new EventOpenInventory();

    private final EventLeftHoldButton eventLeftHoldButton = new EventLeftHoldButton();
    private final EventLeftReleaseButton eventLeftReleaseButton = new EventLeftReleaseButton();
    private final EventLeftTapButton eventLeftTapButton = new EventLeftTapButton();

    private final EventRightHoldButton eventRightHoldButton = new EventRightHoldButton();
    private final EventRightReleaseButton eventRightReleaseButton = new EventRightReleaseButton();
    private final EventRightTapButton eventRightTapButton = new EventRightTapButton();

    private final EventOpenKeyboard eventOpenKeyboard = new EventOpenKeyboard();

    //private final EventGamePause eventGamePause;

    private final List<Button> buttons;

    public ViewControls() {
        buttons = new ArrayList<>();
        outerCircleCenter = new Vector2(275, Pixelate.HEIGHT - 300);
        innerCircleCenter = outerCircleCenter.clone();
        outerCircleRadius = 120;
        innerCircleRadius = 60;
        actuator = new Vector2();
        actuator.setZero();

        eventJoystick = new EventJoystick(actuator.getX(), actuator.getY(), EventJoystick.Action.DOWN);
        Pixelate.HANDLER.registerListener(this);

        // Button location initialisation
        Button pause = new CircleButton(new Vector2(Pixelate.WIDTH - (Pixelate.WIDTH * 0.9), Pixelate.HEIGHT - (Pixelate.HEIGHT * 0.9)), 25, Color.RED);
        pause.onTap(() -> {
            Pixelate.getHud().setPauseMenu();
            Pixelate.PAUSED = true;
        });
        buttons.add(pause);

        QuadButton chat = new QuadButton(new Vector2(10, Pixelate.HEIGHT- 80), 400, 70, Color.argb(150, 100, 100, 100));
        chat.setText("Chat:");
        chat.onTap(() -> Pixelate.HANDLER.call(eventOpenKeyboard));
        buttons.add(chat);

        Button inv = new CircleButton(new Vector2(Pixelate.WIDTH - 200, Pixelate.HEIGHT - 200), 60, Color.GREEN);
        inv.onTap(() -> Pixelate.HANDLER.call(eventOpenInventory));
        buttons.add(inv);

        Button left = new CircleButton(new Vector2(Pixelate.WIDTH - 340, Pixelate.HEIGHT - 250), 60, Color.YELLOW);
        left.onHold(() -> Pixelate.HANDLER.call(eventLeftHoldButton));
        left.onTap(() -> Pixelate.HANDLER.call(eventLeftTapButton));
        left.onRelease(() -> Pixelate.HANDLER.call(eventLeftReleaseButton));
        buttons.add(left);

        Button right = new CircleButton(new Vector2(Pixelate.WIDTH - 240, Pixelate.HEIGHT - 350), 60, Color.BLUE);
        right.onHold(() -> Pixelate.HANDLER.call(eventRightHoldButton));
        right.onTap(() -> Pixelate.HANDLER.call(eventRightTapButton));
        right.onRelease(() -> Pixelate.HANDLER.call(eventRightReleaseButton));
        buttons.add(right);

        Button world = new CircleButton(new Vector2(Pixelate.WIDTH - (Pixelate.WIDTH * 0.8), Pixelate.HEIGHT - (Pixelate.HEIGHT * 0.9)), 25, Color.MAGENTA);
        world.onTap(() -> {
            StateGame gameState = (StateGame) Pixelate.getGSM().getState(StringUtils.GAME);
            WorldManager wm = gameState.getWorldManager();
            String currentworld = wm.getCurrentWorldName();
            if (currentworld.equals(StringUtils.CAVE))
                Pixelate.setWorld(StringUtils.OVERWORLD);
            else
                Pixelate.setWorld(StringUtils.CAVE);
        });
        buttons.add(world);

        Button shop = new ImageButton(R.drawable.storeicon, new Vector2(Pixelate.WIDTH * 0.9, Pixelate.HEIGHT * 0.1), 0.12f);
        shop.onTap(() -> Pixelate.getHud().setShopMenu());
        buttons.add(shop);
    }

    @Override
    public void render(Screen screen) {
        for (Button button : buttons) {
            button.render(screen);
        }
        screen.ring(outerCircleCenter.getX(), outerCircleCenter.getY(), outerCircleRadius, 10, Color.argb(235, 54, 54, 54));
        screen.circle(innerCircleCenter.getX(), innerCircleCenter.getY(), innerCircleRadius, Color.argb(220, 128, 128 ,128));

        EntityPlayer player = ((StateGame) Pixelate.getGSM().getState(StringUtils.GAME)).getPlayer();
        if (player.getGamemode() != Gamemode.CREATIVE) {
            screen.bar(Pixelate.WIDTH * 0.325, Pixelate.HEIGHT * 0.82, Pixelate.WIDTH * 0.35, 30, Color.DKGRAY, Color.GREEN, player.getXPProgress());
            screen.text(String.valueOf(player.getXPLevel()), Pixelate.WIDTH * 0.5f, Pixelate.HEIGHT * 0.814, 60, Color.GREEN);

            screen.bar(Pixelate.WIDTH * 0.252, Pixelate.HEIGHT * 0.777, Pixelate.WIDTH * 0.2, 30, Color.DKGRAY, Color.DKGRAY, player.getHealth() / player.getMaxHealth());
            screen.bar(Pixelate.WIDTH * 0.25, Pixelate.HEIGHT * 0.77, Pixelate.WIDTH * 0.2, 30, Color.GRAY, Color.RED, player.getHealth() / player.getMaxHealth());
        }
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

    @EventHandler(priority = EventHandler.Priority.HIGHEST)
    public void onChat(EventChat e) {
        String message = e.getMessage();
        QuadButton chat = (QuadButton) buttons.stream().filter(b -> b instanceof QuadButton).findFirst().orElse(null);
        if (chat == null) return;
        if (message.startsWith("/")) {
            if (!Pixelate.getCommands().executeCommand(message.substring(1))) {
                chat.setText("Unable to execute command!");
            }
            return;
        }
        chat.setText("Chat: " + message);
    }

    @EventHandler
    public void onKeyboard(EventKeyboard e) {
        if (e.getEvent().getAction() == KeyEvent.ACTION_DOWN) {
            if (e.getKeyCode() == KeyEvent.KEYCODE_SLASH) {
                Pixelate.HANDLER.call(eventOpenKeyboard);
            }
        }
    }

    private int joystickPointerId;

    @EventHandler
    public void onTouch(EventTouch e) {
        if (!Pixelate.PAUSED) {
            float x = e.getX();
            float y = e.getY();
            switch (e.getAction()) {
                case DOWN:
                    if (isOnJoystick(x, y)) {
                        setJoystick(true);
                        joystickPointerId = e.getId();
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
                    if (isJoystick() && joystickPointerId == e.getId()) {
                        setActuator(x, y);
                    }
                    for (Button button : buttons) {
                        if (button.isOn(x, y)) {
                            if (!button.isHolding()) {
                                button.setHold(true);
                                //button.tap();
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
                        joystickPointerId = -1;
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
