package xyz.destiall.pixelate.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;
import java.util.Map;

import xyz.destiall.pixelate.Pixelate;

/**
 * Written by Ms Tan
 */
public class ResourceManager {
    private static final Map<Integer, Bitmap> RESOURCES = new HashMap<>();

    /**
     * Get the image from this drawable id
     * @param id The drawable id
     * @return The image
     */
    public static Bitmap getBitmap(int id) {
        if (RESOURCES.containsKey(id)) return RESOURCES.get(id);
        Bitmap bitmap = BitmapFactory.decodeResource(Pixelate.getResources(), id);
        RESOURCES.put(id, bitmap);
        return bitmap;
    }
}
