package xyz.destiall.pixelate.environment.sounds;

import java.util.ArrayList;
import java.util.List;

import xyz.destiall.pixelate.environment.World;
import xyz.destiall.pixelate.modular.Module;
import xyz.destiall.pixelate.position.Location;

public class SoundsModule implements Module {
    private final World world;
    private final List<Sound> soundsPlaying;

    public SoundsModule(World world) {
        soundsPlaying = new ArrayList<>();
        this.world = world;
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
