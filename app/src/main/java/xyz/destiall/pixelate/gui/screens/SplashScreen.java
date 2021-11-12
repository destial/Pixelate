package xyz.destiall.pixelate.gui.screens;
import xyz.destiall.pixelate.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity {
    protected boolean _active = true;  // Boolean use to check for whether the page is active and running
    protected int _splashTime = 5000;  // meant for wait so after something the splashpage will auto transit to the main menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashscreen);   // Create the view from splashpage.xml

        //thread for displaying the Splash Screen
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
                //Create new activity based on and intent with CurrentActivity
                Intent intent = new Intent(SplashScreen.this, MainMenu.class);
                startActivity(intent);
            }
        });
        splashTread.start();

    }

    // Below is the code for not waiting for the transit to happen,
    // user can touch the screen to move to the next screem
    // Method here called onTouchEvent
    // and action down = detect a touch on the screen
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            _active = false;  // boolean active = false = dun want this view anymore = go to the next screen
        }
        return true;
    }
}