package xyz.destiall.pixelate.timer;

import xyz.destiall.pixelate.graphics.Updateable;

/**
 * Written by Rance
 */
public class Timer implements Updateable {
    private static long lastNanoTime = -1;
    private static double deltaTime;
    private static int fpsCount;
    private static int fps;
    private static double oneSecond;
    private static boolean isSecond;
    private static double elapsedTime;

    @Override
    public void update() {
        long now = System.nanoTime();
        if (lastNanoTime == -1) {
            lastNanoTime = now;
        }
        deltaTime = (now - lastNanoTime) / 1000000000D;
        oneSecond += deltaTime;
        elapsedTime += deltaTime;
        fpsCount++;
        isSecond = false;
        if (oneSecond >= 1) {
            oneSecond = 0;
            elapsedTime = 0;
            fps = fpsCount;
            fpsCount = 0;
            isSecond = true;
        }
        lastNanoTime = now;
    }

    /**
     * Get the last updated nano time
     * @return The last nano time in nanoseconds
     */
    public static long getLastNanoTime() {
        return lastNanoTime;
    }

    /**
     * Get the current delta time between each update call
     * @return The delta time in milliseconds
     */
    public static double getDeltaTime() {
        return deltaTime;
    }

    /**
     * Get the current FPS
     * @return The FPS
     */
    public static int getFPS() {
        return fps;
    }

    /**
     * Get the elapsed time of this game
     * @return The elapsed time in milliseconds
     */
    public static double getElapsedTime() {
        return elapsedTime;
    }
}
