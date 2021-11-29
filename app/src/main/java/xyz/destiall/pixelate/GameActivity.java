package xyz.destiall.pixelate;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import xyz.destiall.java.events.EventHandler;
import xyz.destiall.java.events.Listener;
import xyz.destiall.pixelate.events.EventGamePause;
import xyz.destiall.pixelate.gui.screens.PauseMenu;

public class GameActivity extends AppCompatActivity implements Listener {
    private Button pause_resume, pause_quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(new GameSurface(this));
        Game.HANDLER.registerListener(this);
    }

    @EventHandler
    public void onTouch(EventGamePause e) {
        Game.paused = true;
        Intent intent = new Intent();
        intent.setClass(this, PauseMenu.class);
        startActivity(intent);

    }



}