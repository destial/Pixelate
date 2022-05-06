package xyz.destiall.pixelate.gui.views;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import xyz.destiall.utility.java.events.EventHandler;
import xyz.destiall.utility.java.events.Listener;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.dialogs.ConfirmationDialogFragment;
import xyz.destiall.pixelate.events.controls.EventDialogueAction;
import xyz.destiall.pixelate.events.controls.EventTouch;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.gui.buttons.Button;
import xyz.destiall.pixelate.gui.buttons.CircleButton;
import xyz.destiall.pixelate.gui.buttons.ImageButton;
import xyz.destiall.pixelate.items.lootbox.crates.Crate;
import xyz.destiall.pixelate.items.lootbox.crates.SkinCrate;
import xyz.destiall.pixelate.position.Vector2;
import xyz.destiall.pixelate.states.StateGame;

/**
 * Written by Yong Hong
 */
public class ViewShop implements View, Listener {
    private final int scale;

    private static Bitmap bg;
    private final List<Crate> crates;
    private static Bitmap diamond;

    private final List<Button> buttons;

    private Crate selectedCrate;

    public ViewShop() {
        buttons = new ArrayList<>();
        if (bg == null) {
            bg = ResourceManager.getBitmap(R.drawable.pixelatestores);
            bg = Imageable.resizeImage(bg, (float) Pixelate.HEIGHT / bg.getHeight());
        }
        selectedCrate = null;

        crates = new ArrayList<Crate>();
        crates.add(new SkinCrate( new Vector2(Pixelate.WIDTH * 0.5f, Pixelate.HEIGHT * 0.5f)) );

        for(Crate cr : crates)
        {
            CircleButton button = new CircleButton(new Vector2(cr.getScreenOrigin().getX() + Pixelate.WIDTH * 0.1, cr.getScreenOrigin().getY() + Pixelate.HEIGHT * 0.3), 30, Color.GREEN);
            button.onTap(() -> {
                ConfirmationDialogFragment cdf = new ConfirmationDialogFragment();
                cdf.show(Pixelate.getContext().getSupportFragmentManager(), "ConfirmationFragment");
                selectedCrate = cr;
            });
            buttons.add(button);
        }

        ImageButton button = new ImageButton(R.drawable.backarrow, new Vector2(Pixelate.WIDTH * 0.8, Pixelate.HEIGHT * 0.17), 0.1f);
        button.onTap(() -> {
            Pixelate.getHud().returnToGame();
        });
        buttons.add(button);

        diamond = ResourceManager.getBitmap(R.drawable.coal);

        Bitmap image = ResourceManager.getBitmap(R.drawable.hotbar);
        scale = (int) (image.getWidth() * 0.8);
        Pixelate.HANDLER.registerListener(this);
    }

    @EventHandler
    public void onConfirmation(EventDialogueAction e)
    {
        System.out.println("Opened1");
        if(e.getAction() == EventDialogueAction.Dialog_Action.YES)
        {
            System.out.println("Opened");
            selectedCrate.openCrate(((StateGame) Pixelate.getGSM().getState("Game")).getPlayer());
        }
    }

    @Override
    public void render(Screen screen) {
        screen.draw(bg, 0, 0);
        for(Crate cr : crates)
            cr.render(screen);
        for (Button button : buttons) {
            button.render(screen);
        }
    }

    @Override
    public void update() {
        for (Button button : buttons) {
            if (button.isHolding()) {
                button.hold();
            }
        }
        for(Crate cr : crates)
            cr.update();
    }

    @EventHandler
    public void onTouch(EventTouch e) {
        if (!Pixelate.PAUSED) {
            float x = e.getX();
            float y = e.getY();
            switch (e.getAction()) {

                case DOWN:
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


    @Override
    public void destroy() {
        Pixelate.HANDLER.unregisterListener(this);
    }
}