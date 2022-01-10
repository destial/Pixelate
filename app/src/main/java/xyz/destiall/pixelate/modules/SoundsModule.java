package xyz.destiall.pixelate.modules;

import java.util.ArrayList;
import java.util.List;

import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.environment.sounds.Sound;
import xyz.destiall.pixelate.modular.Module;
import xyz.destiall.pixelate.position.Location;

/**
 * Written by Rance
 */
public class SoundsModule implements Module {
    private transient World world;
    private transient final List<Sound> soundsPlaying;

    // TODO: Maybe use another thread to play sound, idk if it's already doing that
    public SoundsModule() {
        soundsPlaying = new ArrayList<>();
    }

    public SoundsModule setWorld(World world) {
        this.world = world;
        return this;
    }

    /**
     * Play a sound at the requested location
     * @param effect The sound to play
     * @param location The requested location
     * @param volume The volume of the sound
     */
    public void playSound(Sound.SoundType effect, Location location, float volume) {
        Sound sound = new Sound(effect);
        sound.play(location.clone().setWorld(world), volume);
        soundsPlaying.add(sound);
    }

    @Override
    public void update() {
        for (int i = 0; i < soundsPlaying.size(); i++) {
            soundsPlaying.get(i).update();
            if (soundsPlaying.get(i).isReleased()) {
                soundsPlaying.remove(i--);
            }
        }
    }

    @Override
    public void destroy() {
        for (Sound sound : soundsPlaying) {
            sound.stop();
            if (!sound.isReleased()) sound.getPlayer().release();
        }
        soundsPlaying.clear();
    }
}
