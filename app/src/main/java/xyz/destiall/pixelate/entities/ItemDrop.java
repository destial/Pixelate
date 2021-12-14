package xyz.destiall.pixelate.entities;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.events.EventItemPickup;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.timer.Timer;

public class ItemDrop extends Entity {
    private final ItemStack drop;
    private float upAndDownTimer;
    private boolean down;
    public ItemDrop(ItemStack itemStack) {
        super(itemStack.getImage(), 1, 1);
        scale = 0.5f;
        this.drop = itemStack;
        spriteSheet.addSprite("DROP", createAnimation(0));
        spriteSheet.setCurrentSprite("DROP");
        spriteSheet.setCurrentAnimation(0);
        upAndDownTimer = 0f;
        down = false;
    }

    @Override
    public void update() {
        upAndDownTimer += (down ? -1 : 1) * Timer.getDeltaTime() * 20;
        if (Math.abs(upAndDownTimer) >= 10) {
            upAndDownTimer = (down ? -1 : 1) * 10;
            down = !down;
        }
        if (location.getWorld() == null) return;
        location.getWorld().getNearestEntities(location, Tile.SIZE * 0.5).stream().anyMatch(e -> {
            if (e instanceof EntityPlayer) {
                EventItemPickup ev = new EventItemPickup(this);
                Pixelate.HANDLER.call(ev);
                if (ev.isCancelled()) return false;
                if (((EntityPlayer) e).getInventory().addItem(drop)) this.remove();
                return true;
            }
            return false;
        });
    }

    @Override
    public void render(Screen screen) {
        location.add(0, upAndDownTimer);
        super.render(screen);
        location.subtract(0, upAndDownTimer);
    }

    public ItemStack getDrop() {
        return drop;
    }
}
