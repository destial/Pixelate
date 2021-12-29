package xyz.destiall.pixelate.environment.sounds;

import android.media.MediaPlayer;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.entities.EntityPlayer;
import xyz.destiall.pixelate.environment.tiles.Tile;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.position.Location;
import xyz.destiall.pixelate.position.Vector2;
import xyz.destiall.pixelate.states.StateGame;

public class Sound implements Updateable {
    private final EntityPlayer entityPlayer;
    private static final float DISTANCE = 10f;
    private Location location;
    private MediaPlayer player;
    private boolean released;
    private float volume;

    public void setType(SoundType effect) {
        player = MediaPlayer.create(Pixelate.getGameSurface().getContext(), effect.getId());
        released = false;
    }

    public Sound(SoundType effect) {
        setType(effect);
        entityPlayer = ((StateGame) Pixelate.getGSM().getState("Game")).getPlayer();
    }

    public void play(float volume) {
        this.location = entityPlayer.getLocation(true);
        this.volume = volume;
        player.setVolume(volume, volume);
        player.seekTo(0);
        player.start();
    }

    // TODO: Adjust the volume based on given location, since we can define pan volume
    public void play(Location location, float volume) {
        this.location = location.clone();
        this.volume = volume;
        player.setVolume(volume, volume);
        player.seekTo(0);
        player.start();
    }

    public void resume() {
        player.start();
    }

    public void pause() {
        player.pause();
    }

    public void stop() {
        player.stop();
        player.reset();
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    @Override
    public void update() {
        if (!player.isPlaying()) {
            stop();
            if (!released) {
                player.release();
                released = true;
            }
            // return;
        }

        // TODO: Math sound pls i need help how to math
        /**
        if (location == null || location == entityPlayer.getLocation(true)) return;
        try {
            Vector2 normalized = location.toVector().subtract(entityPlayer.getLocation().toVector()).divide(Tile.SIZE);
            float right = volume;
            float left = volume;

            if (normalized.isZero()) {
                player.setVolume(left, right);
                return;
            }

            if (normalized.length() >= DISTANCE) {
                player.setVolume(0, 0);
                return;
            }

            left *= (float) Math.sin(1 / normalized.getX()) * (float) Math.cos(1 / normalized.getY());
            right *= (float) Math.cos(1 / normalized.getX()) * (float) Math.sin(1 / normalized.getY());
            player.setVolume(Math.max(Math.min(left, 1), 0), Math.max(Math.min(right, 1), 0));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public boolean isReleased() {
        return released;
    }

    public enum SoundType {
        EXPLOSION(R.raw.explosion),
        SIZZLE(R.raw.sizzle),
        PICK_UP(R.raw.pickup)

        ;

        private final int id;

        SoundType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
