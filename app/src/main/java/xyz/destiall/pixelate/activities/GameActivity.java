package xyz.destiall.pixelate.activities;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import xyz.destiall.pixelate.GameSurface;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.events.controls.EventGamePause;
import xyz.destiall.pixelate.events.controls.EventOpenKeyboard;
import xyz.destiall.pixelate.events.controls.EventPhoneShake;
import xyz.destiall.pixelate.gui.popups.KeyboardFragment;
import xyz.destiall.utility.java.events.EventHandler;
import xyz.destiall.utility.java.events.Listener;

/**
 * Written by Rance
 */
public class GameActivity extends AppCompatActivity implements Listener {
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(new GameSurface(this));
        Pixelate.HANDLER.registerListener(this);

        SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
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

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        private final EventPhoneShake phoneShake = new EventPhoneShake();
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            if (mAccel > 3) {
                Pixelate.HANDLER.call(phoneShake);
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @EventHandler
    public void onShakeEvent(EventPhoneShake e) {
        System.out.println("Shaked");
    }

}