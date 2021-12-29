package xyz.destiall.pixelate.entities;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.events.EventItemPickup;
import xyz.destiall.pixelate.graphics.Imageable;
import xyz.destiall.pixelate.graphics.Screen;
import xyz.destiall.pixelate.graphics.SpriteSheet;
import xyz.destiall.pixelate.items.ItemStack;
import xyz.destiall.pixelate.timer.Timer;

public class EntityItem extends Entity {
    private ItemStack drop;
    private transient float upAndDownTimer;
    private transient boolean down;

    protected EntityItem() {
        scale = 0.5f;
        upAndDownTimer = 0f;
        down = false;
    }

    public EntityItem(ItemStack itemStack) {
        scale = 0.5f;
        this.drop = itemStack;
        spriteSheet.addAnimation("DROP", Imageable.createAnimation(itemStack.getImage(), 1, 1, 0));
        spriteSheet.setCurrentAnimation("DROP");
        spriteSheet.setCurrentFrame(0);
        upAndDownTimer = 0f;
        down = false;
    }

    @Override
    public void refresh() {
        //setImage(drop.getImage(), 1, 1);
        spriteSheet = new SpriteSheet();
        spriteSheet.addAnimation("DROP", Imageable.createAnimation(drop.getImage(), 1, 1, 0));
        spriteSheet.setCurrentAnimation("DROP");
        spriteSheet.setCurrentFrame(0);
    }

    @Override
    public void update() {
        upAndDownTimer += (down ? -1 : 1) * Timer.getDeltaTime() * 20;
        if (Math.abs(upAndDownTimer) >= 10) {
            upAndDownTimer = (down ? -1 : 1) * 10;
            down = !down;
        }
        World w;
        if ((w = location.getWorld()) == null) return;
        w.getNearestEntities(location, Tile.SIZE * 0.5).stream().anyMatch(e -> {
            if (e instanceof EntityPlayer) {
                EventItemPickup ev = new EventItemPickup(e, this);
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

    /**
     * Set the item that this drop is representing
     * @param drop The item to set
     */
    public void setDrop(ItemStack drop) {
        this.drop = drop;
        refresh();
    }

    /**
     * Get the item that this drop is representing
     * @return The dropped item
     */
    public ItemStack getDrop() {
        return drop;
    }
}
