package xyz.destiall.pixelate;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import xyz.destiall.pixelate.events.controls.ControlEvent;
import xyz.destiall.pixelate.events.controls.EventKeyboard;
import xyz.destiall.pixelate.events.controls.EventTouch;
import xyz.destiall.utility.java.events.Listener;

/**
 * Written by Rance
 */
public class GameSurface extends SurfaceView implements SurfaceHolder.Callback, Listener {
    private Pixelate gameThread;

    public GameSurface(Context context)  {
        super(context);
        setFocusable(true);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameThread = new Pixelate(this, holder);
        Pixelate.HANDLER.registerListener(this);
        gameThread.setRunning(true);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        gameThread.setRunning(false);
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch(InterruptedException e)  {
                e.printStackTrace();
                retry = true;
            }
        }
        Pixelate.HANDLER.unregisterListener(this);
        getHolder().removeCallback(this);
    }

    private final EventTouch eventTouch = new EventTouch();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int maskedAction = event.getActionMasked();
        ControlEvent.Action action = ControlEvent.Action.UP;
        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                action = ControlEvent.Action.DOWN;
                break;
            }
            case MotionEvent.ACTION_MOVE: { // a pointer was moved
                action = ControlEvent.Action.MOVE;
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                action = ControlEvent.Action.UP;
                break;
            }
        }
        eventTouch.setId(event.getPointerId(pointerIndex));
        eventTouch.setAction(action);
        eventTouch.setX(event.getX(pointerIndex));
        eventTouch.setY(event.getY(pointerIndex));
        Pixelate.HANDLER.call(eventTouch);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Pixelate.HANDLER.call(new EventKeyboard(keyCode, event));
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Pixelate.HANDLER.call(new EventKeyboard(keyCode, event));
        return true;
    }
}
