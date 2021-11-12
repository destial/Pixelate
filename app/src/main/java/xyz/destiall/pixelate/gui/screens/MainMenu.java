package xyz.destiall.pixelate.gui.screens;
import xyz.destiall.pixelate.GameActivity;
import xyz.destiall.pixelate.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;



public class MainMenu extends Activity implements View.OnClickListener {

    private Button btn_start, btn_back;

    @Override
    protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);

        //Hide Title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Hide TopBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.mainmenu);

        btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);

        btn_back = (Button)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        //Intent = action to be performed
        //Intent is an object that provides runtime binding

        Intent intent = new Intent();
        if(v == btn_start)
        {
            intent.setClass(this, GameActivity.class);
        }else if (v == btn_back)
        {
            intent.setClass(this, MainMenu.class);
        }
        startActivity(intent);

    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
