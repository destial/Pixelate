package xyz.destiall.pixelate.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import xyz.destiall.pixelate.R;

public class SplashScreen extends Activity {
    protected boolean _active = true;
    protected int _splashTime = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashscreen);
        Thread splashTread = new Thread(() -> {
            try {
                int waited = 0;
                while(_active && (waited < _splashTime)) {
                    Thread.sleep(200);
                    if(_active) {
                        waited += 200;
                    }
                }
            } catch (InterruptedException e) {
                //do nothing
            } finally {
                finish();
                Intent intent = new Intent(SplashScreen.this, MainMenu.class);
                startActivity(intent);
            }
        });
        splashTread.start();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            _active = false;
        }
        return true;
    }
}