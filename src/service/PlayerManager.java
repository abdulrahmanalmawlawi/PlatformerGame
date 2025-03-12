package service;

import entity.Player;
import game.Camera;
import assets.GameFileManager;

import java.io.File;

/**
 * PlayerManager handles player data, game progression, and save/load
 * operations.
 * 
 * This class is responsible for:
 * - Tracking the current player's name and progress data
 * - Saving level completion data to the global leaderboard
 * - Managing the save/load functionality for game states
 * - Persisting player data between game sessions
 * 
 * It serves as a bridge between the game state and the data storage system,
 * ensuring player progress is properly tracked and saved.
 */
public class PlayerManager {

    // Player's name and progress data
    public static String name = "";
    public static PlayerProgressTracker data;

    /**
     * Saves the player's level result to the global leaderboard.
     * This method:
     * - Records the level, time, and score in the player's progress tracker
     * - Updates the data.txt file with the new result
     * - Refreshes the leaderboard data to reflect the changes
     * - Sorts the player's results to update max level and score statistics
     * 
     * @param level The level number that was completed
     * @param time  The time (in seconds) taken to complete the level
     * @param score The score achieved in the level
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
     * Saves the current game state to a file for later resumption.
     * The saved data includes:
     * - Current level number
     * - Player score
     * - Player position and power-up state
     * - Elapsed time
     * - Camera position
     * 
     * This allows players to continue their game from where they left off.
     * 
     * @param level The current level number
     * @param p     The player object containing position, health, and power-up
     *              states
     * @param c     The camera object with its current position
     * @param score The current accumulated score
     * @param time  The elapsed time in seconds
     * @param f     The file to save the game state to
     */
    public static void saveGame(int level, Player p, Camera c, int score, int time, File f) {
        GameFileManager.appendLine(f.getPath(), "level;" + level + ";");
        GameFileManager.appendLine(f.getPath(), "score;" + score + ";");
        GameFileManager.appendLine(f.getPath(),
                "player;" + p.getPosition().x + ";" + p.getPosition().y + ";" + p.hasSword + ";");
        GameFileManager.appendLine(f.getPath(), "time;" + time + ";");
        GameFileManager.appendLine(f.getPath(), "camera;" + c.cameraPosition + ";");
    }
}