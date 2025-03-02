package service;

import entity.Player;
import game.Camera;
import assets.GameFileManager;

import java.io.File;

/**
 * PlayerManager handles player data and game save/load operations
 */
public class PlayerManager {

    // Player's name and progress data
    public static String name = "";
    public static PlayerProgressTracker data;

    /**
     * Save a new result of the player
     * 
     * @param level, the level concerned
     * @param time,  the time in which the player passed the level
     * @param score, the score the player has made
     */
    public static void saveResult(int level, int time, int score) {
        data.appendData(level, time, score);
        // we save and load again the results
        GameFileManager.appendLine("data.txt", name + ";" + level + ";" + time + ";" + score + ";");
        ScoreboardManager.loadLeaderBoardData();
        data = ScoreboardManager.playerDataMap.get(name);
        data.sort();
    }

    /**
     * Saves the current game state to a file
     * 
     * @param level The current level number
     * @param p     The player object with position and state
     * @param c     The camera object with position
     * @param time  The elapsed time in seconds
     * @param score The current score
     * @param f     The file to save to
     */
    public static void saveGame(int level, Player p, Camera c, int time, int score, File f) {
        GameFileManager.appendLine(f.getPath(), "level;" + level + ";");
        GameFileManager.appendLine(f.getPath(), "score;" + score + ";");
        GameFileManager.appendLine(f.getPath(),
                "player;" + p.getPosition().x + ";" + p.getPosition().y + ";" + p.hasSword + ";");
        GameFileManager.appendLine(f.getPath(), "time;" + time + ";");
        GameFileManager.appendLine(f.getPath(), "camera;" + c.cameraPosition + ";");
    }
}