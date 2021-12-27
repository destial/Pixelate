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

    /**
     * Get all the sprites of this animation
     * @return The sprites (raw pointer)
     */
    public HashMap<String, Bitmap[]> getSprites() {
        return sprites;
    }

    /**
     * Set the current animation frame
     * @param value The frame
     */
    public void setCurrentFrame(int value) {
        animationFrame = value;
    }

    /**
     * Add an animation to this sheet
     * @param name The name of this animation
     * @param images The animation array
     */
    public void addAnimation(String name, Bitmap[] images) {
        sprites.put(name, images);
    }

    /**
     * Set the current animation to play
     * @param name The requested animation name
     */
    public void setCurrentAnimation(String name) {
        current = name;
    }

    /**
     * Check if this sheet has this animation
     * @param name The requested animation name
     * @return true if exists, otherwise false
     */
    public boolean hasAnimation(String name) {
        return sprites.containsKey(name);
    }

    /**
     * Get the current animation array that is playing
     * @return The current animation array
     */
    public Bitmap[] getCurrentAnimation() {
        if (current != null) {
            return sprites.get(current);
        }
        return null;
    }

    /**
     * Get the current animation frame that is playing
     * @return The current animation frame
     */
    public int getAnimationFrame() {
        return animationFrame;
    }

    /**
     * Get the current sprite from this animation frame
     * @return The current sprite
     */
    public Bitmap getCurrentSprite() {
        return getCurrentAnimation()[animationFrame];
    }
}
