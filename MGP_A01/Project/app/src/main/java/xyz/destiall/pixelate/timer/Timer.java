package xyz.destiall.pixelate.timer;

import xyz.destiall.pixelate.graphics.Updateable;

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

    public static long getLastNanoTime() {
        return lastNanoTime;
    }

    public static double getDeltaTime() {
        return deltaTime;
    }

    public static int getFPS() {
        return fps;
    }

    public static boolean isSecond() {
        return isSecond;
    }

    public static double getElapsedTime() {
        return elapsedTime;
    }
}
