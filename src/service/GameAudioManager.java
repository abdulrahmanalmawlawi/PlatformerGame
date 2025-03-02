package service;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

/**
 * Class responsible for loading, playing, and managing all game audio including
 * background music and sound effects.
 */
public class GameAudioManager {

    // Sound effect constants
    public static final int COIN = 3;
    public static final int DAMAGE = 4;
    public static final int WIN = 5;
    public static final int SWORD = 6;
    public static final int BOUNCE = 7;
    public static final int GAME_OVER = 8;

    Clip clip;
    Clip activeClip;
    URL[] urlSound = new URL[30];
    long cliptime = 0;

    /**
     * Constructor method, loads all the audio files used in the game
     */
    public GameAudioManager() {
        urlSound[0] = getClass().getResource("/music_level1_theme.wav");
        urlSound[1] = getClass().getResource("/music_level2_theme.wav");
        urlSound[2] = getClass().getResource("/music_level3_theme.wav");
        urlSound[3] = getClass().getResource("/sfx_pickup_coin.wav");
        urlSound[4] = getClass().getResource("/sfx_player_damage.wav");
        urlSound[5] = getClass().getResource("/sfx_game_victory.wav");
        urlSound[6] = getClass().getResource("/sfx_player_powerup.wav");
        urlSound[7] = getClass().getResource("/sfx_player_bounce.wav");
        urlSound[8] = getClass().getResource("/sfx_game_over.wav");
    }

    /**
     * Set the clip to the wanted audio file
     * 
     * @param i, the index of the audio file in the preloaded array
     */
    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(urlSound[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            // Silent exception handling - could be improved with logging
        }
    }

    /**
     * Play the current audio clip once
     */
    public void play() {
        clip.start();
    }

    /**
     * Loop the current audio clip continuously
     */
    public void loop() {
        try {
            // Stop the previous background music if any
            if (activeClip != null) {
                activeClip.stop();
            }
        } catch (Exception e) {
            // Silent exception handling
        }

        clip.loop(Clip.LOOP_CONTINUOUSLY);
        activeClip = clip;
    }

    /**
     * Stop the current audio clip
     */
    public void stop() {
        clip.stop();
    }

    /**
     * Switch the background music loop to a different track
     * 
     * @param num The index of the audio track to switch to
     */
    public void switchloop(int num) {
        setFile(num);
        play();
        loop();
    }

    /**
     * Get the currently playing background music clip
     * 
     * @return The active background music Clip
     */
    public Clip getCurrentLoop() {
        return activeClip;
    }

    /**
     * Pause the currently playing background music
     */
    public void pauseCurrentLoop() {
        cliptime = activeClip.getMicrosecondPosition();
        activeClip.stop();
    }

    /**
     * Resume playing the previously paused background music
     */
    public void playCurrentLoop() {
        activeClip.setMicrosecondPosition(cliptime);
        activeClip.start();
    }
}