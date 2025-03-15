package service;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

/**
 * Class responsible for loading, playing, and managing all game audio including
 * background music and sound effects.
 * 
 * This manager handles:
 * - Loading and caching sound effects and music tracks
 * - Playing one-time sound effects
 * - Looping background music tracks
 * - Pausing and resuming audio playback
 * - Switching between different music tracks
 * - Fallback loading from alternate locations if primary resource loading fails
 */
public class GameAudioManager {

    // Sound effect constants for easy reference throughout the game
    /** Constant for coin pickup sound effect */
    public static final int COIN = 3;
    /** Constant for player damage sound effect */
    public static final int DAMAGE = 4;
    /** Constant for level victory sound effect */
    public static final int WIN = 5;
    /** Constant for sword powerup sound effect */
    public static final int SWORD = 6;
    /** Constant for player bounce sound effect */
    public static final int BOUNCE = 7;
    /** Constant for game over sound effect */
    public static final int GAME_OVER = 8;

    /** Current audio clip being prepared for playback */
    Clip clip;
    /** Currently active background music clip */
    Clip activeClip;
    /** Array of URLs to audio files, indexed by their constant values */
    URL[] urlSound = new URL[30];
    /** Stores the timestamp when audio is paused for resuming later */
    long cliptime = 0;

    /**
     * Constructor method that initializes the audio manager by loading all game
     * audio files.
     * Attempts to load from resources first, then falls back to the sound directory
     * if resource loading fails for any file.
     */
    public GameAudioManager() {
        try {
            // Try to load from resources first
            urlSound[0] = getClass().getResource("/music_level1_theme.wav");
            urlSound[1] = getClass().getResource("/music_level2_theme.wav");
            urlSound[2] = getClass().getResource("/music_level3_theme.wav");
            urlSound[3] = getClass().getResource("/sfx_pickup_coin.wav");
            urlSound[4] = getClass().getResource("/sfx_player_damage.wav");
            urlSound[5] = getClass().getResource("/sfx_game_victory.wav");
            urlSound[6] = getClass().getResource("/sfx_player_powerup.wav");
            urlSound[7] = getClass().getResource("/sfx_player_bounce.wav");
            urlSound[8] = getClass().getResource("/sfx_game_over.wav");

            // If any of the resources are null, try loading from the sound directory
            for (int i = 0; i < 9; i++) {
                if (urlSound[i] == null) {
                    String fileName = "";
                    switch (i) {
                        case 0:
                            fileName = "music_level1_theme.wav";
                            break;
                        case 1:
                            fileName = "music_level2_theme.wav";
                            break;
                        case 2:
                            fileName = "music_level3_theme.wav";
                            break;
                        case 3:
                            fileName = "sfx_pickup_coin.wav";
                            break;
                        case 4:
                            fileName = "sfx_player_damage.wav";
                            break;
                        case 5:
                            fileName = "sfx_game_victory.wav";
                            break;
                        case 6:
                            fileName = "sfx_player_powerup.wav";
                            break;
                        case 7:
                            fileName = "sfx_player_bounce.wav";
                            break;
                        case 8:
                            fileName = "sfx_game_over.wav";
                            break;
                    }
                    urlSound[i] = new java.io.File("sound/" + fileName).toURI().toURL();
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading audio files: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Prepares an audio file for playback by loading it into the clip.
     * 
     * @param i The index of the audio file in the preloaded array to set as current
     *          clip
     */
    public void setFile(int i) {
        try {
            if (urlSound[i] == null) {
                System.err.println("Error: Audio file at index " + i + " is not available");
                return;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(urlSound[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            System.err.println("Error setting audio file at index " + i + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Plays the current audio clip once from beginning to end.
     * Typically used for sound effects that should play once on events.
     */
    public void play() {
        if (clip != null) {
            clip.start();
        } else {
            System.err.println("Warning: Attempted to play null audio clip");
        }
    }

    /**
     * Loops the current audio clip continuously.
     * Typically used for background music that should play until stopped.
     * Stops any previously looping clip first.
     */
    public void loop() {
        try {
            // Stop the previous background music if any
            if (activeClip != null) {
                activeClip.stop();
            }
        } catch (Exception e) {
            System.err.println("Error stopping previous audio: " + e.getMessage());
        }

        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            activeClip = clip;
        } else {
            System.err.println("Warning: Attempted to loop null audio clip");
        }
    }

    /**
     * Stops playback of the current audio clip.
     */
    public void stop() {
        clip.stop();
    }

    /**
     * Convenience method to switch background music to a different track.
     * Sets the file, plays it, and puts it in a continuous loop.
     * 
     * @param num The index of the audio track to switch to (0-2 for music tracks)
     */
    public void switchloop(int num) {
        setFile(num);
        play();
        loop();
    }

    /**
     * Returns the currently playing background music clip.
     * Useful for checking the active music state.
     * 
     * @return The active background music Clip
     */
    public Clip getCurrentLoop() {
        return activeClip;
    }

    /**
     * Pauses the currently playing background music.
     * Stores the current position so it can be resumed later.
     */
    public void pauseCurrentLoop() {
        cliptime = activeClip.getMicrosecondPosition();
        activeClip.stop();
    }

    /**
     * Resumes playing the previously paused background music.
     * Continues from the position where it was paused.
     */
    public void playCurrentLoop() {
        activeClip.setMicrosecondPosition(cliptime);
        activeClip.start();
    }
}