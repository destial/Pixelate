package xyz.destiall.pixelate.activities;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Objects;

import xyz.destiall.java.events.EventHandler;
import xyz.destiall.java.events.Listener;
import xyz.destiall.pixelate.GameSurface;
import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.dialogs.ConfirmationDialogFragment;
import xyz.destiall.pixelate.events.controls.EventDialogueAction;
import xyz.destiall.pixelate.events.controls.EventGamePause;
import xyz.destiall.pixelate.events.controls.EventOpenDialogue;
import xyz.destiall.pixelate.events.controls.EventOpenKeyboard;
import xyz.destiall.pixelate.events.controls.EventPhoneShake;
import xyz.destiall.pixelate.gui.popups.KeyboardFragment;

/**
 * Written by Rance
 */
public class GameActivity extends AppCompatActivity implements Listener {
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(new GameSurface(this));
        Pixelate.HANDLER.registerListener(this);

        mSensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
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
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            if (mAccel > 3) {
                Pixelate.HANDLER.call(new EventPhoneShake());
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @EventHandler
    public void onShakeEvent(EventPhoneShake e)
    {
        System.out.println("Shaked");
    }

}