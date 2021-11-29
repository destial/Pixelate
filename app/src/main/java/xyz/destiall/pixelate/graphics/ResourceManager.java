package xyz.destiall.pixelate.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;
import java.util.Map;

import xyz.destiall.pixelate.Game;

public class ResourceManager {
    private static final Map<Integer, Bitmap> RESOURCES = new HashMap<>();

    public static Bitmap getBitmap(int id) {
        if (RESOURCES.containsKey(id)) return RESOURCES.get(id);
        Bitmap bitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(Game.getResources(), id));
        RESOURCES.put(id, bitmap);
        return bitmap;
    }
}
