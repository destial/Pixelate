package xyz.destiall.pixelate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import xyz.destiall.java.events.EventHandler;
import xyz.destiall.java.events.Listener;
import xyz.destiall.pixelate.GameSurface;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.events.EventGamePause;
import xyz.destiall.pixelate.events.EventOpenKeyboard;
import xyz.destiall.pixelate.gui.KeyboardFragment;

public class GameActivity extends AppCompatActivity implements Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(new GameSurface(this));
        Pixelate.HANDLER.registerListener(this);
    }

    @EventHandler
    public void onTouch(EventGamePause e) {
        Pixelate.PAUSED = true;
        Intent intent = new Intent();
        intent.setClass(this, PauseMenu.class);
        startActivity(intent);
    }

    @EventHandler
    public void onOpenKeyboard(EventOpenKeyboard e) {
        KeyboardFragment keyboard = new KeyboardFragment();
        keyboard.show(getSupportFragmentManager(), "Keyboard");
    }
}