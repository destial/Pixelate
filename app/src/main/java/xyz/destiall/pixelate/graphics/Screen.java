package xyz.destiall.pixelate.graphics;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.position.Vector2;

public class Screen {
    private Canvas canvas;
    private Vector2 offset;
    private Location gameCenter;
    private final Vector2 displayCenter;

    public Screen(Canvas canvas, Entity center, int width, int height) {
        this.canvas = canvas;
        gameCenter = center.getLocation();
        displayCenter = new Vector2(width / 2f, height / 2f);
        offset = displayCenter.subtract(center.getBounds().getWidth() / 2, center.getBounds().getHeight() / 2).subtract(gameCenter.getRawX(), gameCenter.getRawY());
        constraint();
    }

    public void update(Canvas canvas, Entity center, int width, int height) {
        this.canvas = canvas;
        gameCenter = center.getLocation();
        displayCenter.set(width / 2f, height / 2f);
        offset = displayCenter.subtract(center.getBounds().getWidth() / 2, center.getBounds().getHeight() / 2).subtract(gameCenter.getRawX(), gameCenter.getRawY());
        constraint();
    }

    public Vector2 convert(@NonNull Vector2 worldSpace) {
        return worldSpace.clone().add(offset);
    }

    public Vector2 convert(@NonNull Location location) {
        return convert(location.toVector());
    }

    public Canvas getCanvas() {
        return canvas;
    }

    private void constraint() {
        if (offset.getX() > Game.WIDTH) {
            offset.setX(Game.WIDTH);
        }
        if (offset.getY() > Game.HEIGHT) {
            offset.setY(Game.HEIGHT);
        }
        if (offset.getX() < -Game.WIDTH / 2f) {
            offset.setX(-Game.WIDTH / 2f);
        }
        if (offset.getY() < -Game.HEIGHT / 2f) {
            offset.setY(-Game.HEIGHT / 2f);
        }
    }
}
