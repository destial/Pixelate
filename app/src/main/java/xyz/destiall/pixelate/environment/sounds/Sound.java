package xyz.destiall.pixelate.environment.sounds;

import android.media.MediaPlayer;

import xyz.destiall.pixelate.Pixelate;
import xyz.destiall.pixelate.R;
import xyz.destiall.pixelate.graphics.Updateable;
import xyz.destiall.pixelate.position.Location;

public class Sound implements Updateable {
    private MediaPlayer player;
    private boolean released;

    public Sound() {

    }

    public void setType(SoundType effect) {
        player = MediaPlayer.create(Pixelate.getGameSurface().getContext(), effect.getId());
        released = false;
    }

    public Sound(SoundType effect) {
        setType(effect);
    }

    public void play() {
        player.seekTo(0);
        player.start();
    }

    // TODO: Adjust the volume based on given location, since we can define pan volume
    public void play(Location location, float volume) {
        player.setVolume(volume, volume);
        play();
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
        if (!released && !player.isPlaying()) {
            stop();
            if (!released) {
                player.release();
                released = true;
            }
        }
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
