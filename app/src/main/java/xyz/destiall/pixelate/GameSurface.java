package xyz.destiall.pixelate;

import android.content.Context;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import xyz.destiall.java.events.Listener;
import xyz.destiall.pixelate.events.EventKeyboard;
import xyz.destiall.pixelate.events.EventTouch;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback, Listener {
    private Pixelate gameThread;

    public GameSurface(Context context)  {
        super(context);
        setFocusable(true);
        getHolder().addCallback(this);
    }

    @Override
    public void draw(Canvas canvas)  {
        super.draw(canvas);
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
        while (retry) {
            try {
                gameThread.setRunning(false);
                gameThread.join();
                retry = false;
            } catch(InterruptedException e)  {
                e.printStackTrace();
                retry = true;
            }
        }
        Pixelate.HANDLER.unregisterListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Pixelate.HANDLER.call(new EventTouch(event));
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Pixelate.HANDLER.call(new EventKeyboard(keyCode, event));
        System.out.println("KeyDown");
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Pixelate.HANDLER.call(new EventKeyboard(keyCode, event));
        System.out.println("KeyUp");
        return true;
    }
}
