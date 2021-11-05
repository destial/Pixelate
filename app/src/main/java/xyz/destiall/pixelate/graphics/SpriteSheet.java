package xyz.destiall.pixelate.graphics;

import android.graphics.Bitmap;

import java.util.HashMap;

public class SpriteSheet {
    private final HashMap<String, Bitmap[]> sprites;
    private String current;
    private int animationFrame;
    public SpriteSheet() {
        sprites = new HashMap<>();
        animationFrame = 0;
    }

    public HashMap<String, Bitmap[]> getSprites() {
        return sprites;
    }

    public void setCurrentAnimation(int value) {
        animationFrame = value;
    }

    public void addSprite(String name, Bitmap[] images) {
        sprites.put(name, images);
    }

    public void setCurrentSprite(String name) {
        current = name;
    }

    public Bitmap[] getCurrentSprites() {
        if (current != null) {
            return sprites.get(current);
        }
        return sprites.values().stream().findFirst().orElse(null);
    }

    public Bitmap getCurrentAnimation() {
        return getCurrentSprites()[animationFrame];
    }
}
