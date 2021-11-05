package xyz.destiall.pixelate.graphics;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import xyz.destiall.pixelate.Game;
import xyz.destiall.pixelate.entities.Entity;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.position.Vector2;

public class Screen {
    private final Canvas canvas;
    private final Vector2 offset;

    public Screen(Canvas canvas, Entity center, int width, int height) {
        this.canvas = canvas;
        Vector2 gameCenter = center.getLocation().toVector();
        Vector2 displayCenter = new Vector2(width / 2f, height / 2f);
        offset = displayCenter.subtract(center.getBounds().getWidth() / 2, center.getBounds().getHeight() / 2).subtract(gameCenter);
        constraint();
    }

    public Vector2 convert(@NonNull Vector2 worldSpace) {
        return worldSpace.clone().add(offset);
    }

    public Vector2 convert(@NonNull Location location) {
        return location.toVector().add(offset);
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
        if (offset.getX() < -Game.WIDTH) {
            offset.setX(-Game.WIDTH);
        }
        if (offset.getY() < -Game.HEIGHT) {
            offset.setY(-Game.HEIGHT);
        }
    }

    /*public boolean isOutOfBounds(Location location) {
        Vector2 offset = convert(location);
        return offset.getX() < 0 || offset.getX() > Game.WIDTH || offset.getY() < 0 || offset.getY() > Game.HEIGHT;
    }

    public boolean isOutOfBounds(Vector2 location) {
        Vector2 offset = convert(location);
        return offset.getX() < 0 || offset.getX() > Game.WIDTH || offset.getY() < 0 || offset.getY() > Game.HEIGHT;
    }*/
}
