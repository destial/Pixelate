package xyz.destiall.pixelate;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import xyz.destiall.java.events.EventHandling;
import xyz.destiall.pixelate.states.GSM;
import xyz.destiall.pixelate.states.StateGame;
import xyz.destiall.pixelate.timer.Timer;

public class Game extends Thread {
    public static final EventHandling HANDLER = new EventHandling();
    public static int HEIGHT;
    public static int WIDTH;

    private static GameSurface gameSurface;
    private static SurfaceHolder surfaceHolder;
    private static Canvas canvas;
    private static Bitmap tileMap;
    private final Timer timer;
    private final GSM manager;
    private boolean running;

    public Game(GameSurface gameSurface, SurfaceHolder surfaceHolder)  {
        super();
        Game.gameSurface = gameSurface;
        Game.surfaceHolder = surfaceHolder;
        Game.tileMap = Bitmap.createBitmap(BitmapFactory.decodeResource(gameSurface.getResources(), R.drawable.tilemap));
        Game.tileMap = Bitmap.createScaledBitmap(tileMap, (int) (tileMap.getWidth() * 1.52), (int) (tileMap.getHeight() * 1.52), false);
        HEIGHT = gameSurface.getHeight();
        WIDTH = gameSurface.getWidth();
        timer = new Timer();
        manager = new GSM();
        manager.addState("Game", new StateGame(gameSurface));
        manager.setState("Game");
    }

    @Override
    public void run() {
        long nsPerTick = 1000000000 / 60;
        long lastTime = System.nanoTime();
        long unprocessed = 0;
        while (running)  {
            try {
                canvas = surfaceHolder.lockCanvas();
                HEIGHT = canvas.getHeight();
                WIDTH = canvas.getWidth();
                update();
                unprocessed += (Timer.getLastNanoTime() - lastTime) / nsPerTick;
                lastTime = Timer.getLastNanoTime();
                while (unprocessed >= 1) {
                    tick();
                    unprocessed -= 1;
                }
                render(canvas);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null)  {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                    canvas = null;
                }
            }
        }
    }

    public void setRunning(boolean running)  {
        this.running = running;
    }

    private void update() {
        timer.update();
        manager.update();
    }

    private void render(Canvas canvas) {
        gameSurface.draw(canvas);
        manager.render(canvas);
    }

    private void tick() {
        manager.tick();
        timer.tick();
    }

    public static Bitmap getTileMap() {
        return tileMap;
    }

    public static Resources getResources() {
        return gameSurface.getResources();
    }
}
