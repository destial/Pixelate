package xyz.destiall.pixelate.gui.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import xyz.destiall.pixelate.GameActivity;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;

public class PauseMenu extends Activity implements View.OnClickListener {
    private Button btn_start, btn_quit;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        //Hide Title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Hide TopBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.pausemenu);

        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);

        btn_quit = findViewById(R.id.btn_quit);
        btn_quit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //Intent = action to be performed
        //Intent is an object that provides runtime binding

        Intent intent = new Intent();
        if (v == btn_start) {
            intent.setClass(this, GameActivity.class);
            Pixelate.PAUSED = false;
            Pixelate.getGSM().setState("Game");
        } else if (v == btn_quit) {
            intent.setClass(this, MainMenu.class);
        }

        startActivity(intent);
    }
}
