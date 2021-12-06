package xyz.destiall.pixelate;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import xyz.destiall.java.events.EventHandling;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.WorldManager;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.ResourceManager;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.states.GSM;
import xyz.destiall.pixelate.states.StateGame;
import xyz.destiall.pixelate.states.StatePauseMenu;
import xyz.destiall.pixelate.timer.Timer;

public class Pixelate extends Thread {
    public static final EventHandling HANDLER = new EventHandling();
    public static int HEIGHT;
    public static int WIDTH;

    private static GameSurface gameSurface;
    private static SurfaceHolder surfaceHolder;
    private static Canvas canvas;
    private static Bitmap tileMap;
    private static GSM stateManager;
    public static boolean paused = false;
    private final Timer timer;
    private boolean running;

    public Pixelate(GameSurface gameSurface, SurfaceHolder surfaceHolder)  {
        super();
        Pixelate.gameSurface = gameSurface;
        Pixelate.surfaceHolder = surfaceHolder;
        tileMap = ResourceManager.getBitmap(R.drawable.tilemap);
        tileMap = Imageable.scaleImage(tileMap, 1.52f);
        HEIGHT = gameSurface.getHeight();
        WIDTH = gameSurface.getWidth();
        timer = new Timer();
        stateManager = new GSM();
        stateManager.addState("Game", new StateGame(gameSurface));
        stateManager.addState("PauseMenu", new StatePauseMenu(gameSurface));
        stateManager.setState("Game");
    }

    @Override
    public void run() {
        while (running && !paused)  {
            try {
                canvas = surfaceHolder.lockCanvas();
                HEIGHT = canvas.getHeight();
                WIDTH = canvas.getWidth();
                if (!paused) {
                    update();
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
        if (!paused) {
            timer.update();
            stateManager.update();
        }
    }

    private void render(Canvas canvas) {
        if (!paused) {
            canvas.drawRGB(0, 0, 0);
            stateManager.render(canvas);
        }
    }

    public static boolean setWorld(String world) {
        StateGame gameState = ((StateGame) stateManager.getState("Game"));
        WorldManager wm = gameState.getObject(WorldManager.class);
        if (wm.isAWorld(world) && !wm.isWorldActive(world)) {
            // Remove player from current world
            World current = wm.getCurrentWorld();
            current.getEntities().remove(gameState.getPlayer());

            //Set new active world
            wm.setActive(world);

            // Teleport player to new world
            World next = wm.getCurrentWorld();
            System.out.println("Current world: " + wm.getCurrentWorldName());
            next.getEntities().add(gameState.getPlayer());
            gameState.getPlayer().teleport(wm.getCurrentWorld().getNearestEmpty(new Location(0,0, wm.getCurrentWorld())));
            return true;
        }
        return false;
    }

    public static Bitmap getTileMap() {
        return tileMap;
    }

    public static Resources getResources() {
        return getGameSurface().getResources();
    }

    public static GSM getGSM() {
        return stateManager;
    }

    public static GameSurface getGameSurface() {
        return gameSurface;
    }
}
