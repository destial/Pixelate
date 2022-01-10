package xyz.destiall.pixelate.environment.tiles;

import android.graphics.Bitmap;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.graphics.Imageable;

/**
 * Written by Yong Hong
 */
public class TileBreakProgression extends Imageable {
    private static TileBreakProgression instance = null;

    Bitmap[] animationTiles;

    public static TileBreakProgression getInstance() {
        if (instance == null)
            instance = new TileBreakProgression(Pixelate.getBlockBreakAnimationMap(), 1, 10);
        return instance;
    }

    private TileBreakProgression(Bitmap image, int rows, int columns) {
        super(image, rows, columns);

        animationTiles = new Bitmap[columns];
        for (int i = 0; i < columns; ++i) {
            animationTiles[i] = Bitmap.createBitmap(image, i * (int) width, 0, (int)width, (int)height);
        }
    }

    public Bitmap getTileBreakProgression(int stage)
    {
        if (stage < 0) stage = 0;
        else if (stage >= this.getColumns()) stage = this.getColumns() - 1;
        return animationTiles[stage];
    }


}
