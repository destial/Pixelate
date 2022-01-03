package xyz.destiall.pixelate.graphics;

import android.graphics.Bitmap;

import xyz.destiall.pixelate.R;

public class Glint implements Updateable {
    public static final Glint INSTANCE = new Glint();

    private transient final SpriteSheet spriteSheet;

    private Glint() {
        Bitmap image = ResourceManager.getBitmap(R.drawable.hotbar);
        int width = (int) (image.getWidth() * 0.7);
        int height = (int) (image.getWidth() * 0.7);
        image = Bitmap.createScaledBitmap(image, (int) (image.getWidth() * 0.8), (int) (image.getHeight() * 0.8), false);
        int dWidth = (int) (image.getWidth() * 0.8);
        int dHeight = (int) (image.getWidth() * 0.8);
        spriteSheet = new SpriteSheet();
        spriteSheet.addAnimation("GLINT", Imageable.createAnimation(ResourceManager.getBitmap(R.drawable.glint), 5, 5, 0, dWidth, dHeight));
        spriteSheet.addAnimation("GLINT2", Imageable.createAnimation(ResourceManager.getBitmap(R.drawable.glint), 5, 5, 0, width, height));
        spriteSheet.setCurrentAnimation("GLINT");
        spriteSheet.setSpeed(10);
    }

    @Override
    public void update() {
        spriteSheet.update();
    }

    /**
     * Render in hotbar
     * @param screen The screen to render to
     * @param x Top left x
     * @param y Top left y
     */
    public void render(Screen screen, int x, int y) {
        spriteSheet.setCurrentAnimation("GLINT");
        screen.draw(spriteSheet.getCurrentSprite(), x, y);
    }

    /**
     * Render in inventory
     * @param screen The screen to render to
     * @param x Top left x
     * @param y Top left y
     * @param z 1
     */
    public void render(Screen screen, int x, int y, int z) {
        spriteSheet.setCurrentAnimation("GLINT2");
        screen.draw(spriteSheet.getCurrentSprite(), x, y);
    }
}
