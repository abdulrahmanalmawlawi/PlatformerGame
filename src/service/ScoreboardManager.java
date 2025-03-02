package service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.*;
import java.util.Map.Entry;

import ui.GameView;

import service.PlayerProgressTracker.LevelData;
import assets.GameFileManager;

/**
 * ScoreboardManager handles loading, processing, and displaying player scores
 * and rankings.
 * It maintains a database of player achievements and provides visualization in
 * different formats:
 * - Global scoreboard showing top players across all levels
 * - Player-specific scoreboard showing individual achievements and progress
 */
public class ScoreboardManager {
    // Maps player names to their progress data
    public static Map<String, PlayerProgressTracker> playerDataMap = new HashMap<String, PlayerProgressTracker>();

    // Sorted map of players by score for ranking
    private static SortedMap<Integer, PlayerProgressTracker> rankedPlayerData = new TreeMap<Integer, PlayerProgressTracker>();

    // Rendering constants
    private static final int MAX_DISPLAYED_ENTRIES = 5;
    private static final int COLUMN_X_NAME = 210;
    private static final int COLUMN_X_SCORE = 310;
    private static final int COLUMN_X_TIME = 410;
    private static final int COLUMN_X_LEVEL = 510;
    private static final int GLOBAL_SCOREBOARD_Y = 230;
    private static final int PLAYER_SCOREBOARD_Y = 260;
    private static final int ROW_HEIGHT = 30;

    /**
     * Loads player score data from the data file
     * Parses player names, levels, times, and scores
     */
    public static void loadScoreboardData() {
        // Clear existing data
        playerDataMap.clear();
        rankedPlayerData.clear();

        // Read data from file
        List<String> dataLines = GameFileManager.ReadTextLevel("data.txt");

        // Parse each line of data
        for (String dataLine : dataLines) {
            try {
                List<String> dataFields = GameFileManager.AnalyseLine(dataLine);
                String playerName = dataFields.get(0);
                int levelNumber = Integer.valueOf(dataFields.get(1));
                int completionTime = Integer.valueOf(dataFields.get(2));
                int playerScore = Integer.valueOf(dataFields.get(3));

                if (playerDataMap.containsKey(playerName)) {
                    // Add data to existing player record
                    playerDataMap.get(playerName).appendData(levelNumber, completionTime, playerScore);
                } else {
                    // Create new player record
                    PlayerProgressTracker playerData = new PlayerProgressTracker();
                    playerData.nameUser = playerName;
                    playerData.appendData(levelNumber, completionTime, playerScore);
                    playerDataMap.put(playerName, playerData);
                }
            } catch (Exception e) {
                // Skip invalid data entries
            }
        }

        // Sort and rank all players
        int multiplier = playerDataMap.size() + 1;
        int index = 0;

        for (Entry<String, PlayerProgressTracker> playerEntry : playerDataMap.entrySet()) {
            index++;
            PlayerProgressTracker playerData = playerEntry.getValue();
            playerData.sort();

            // Create a unique ranking key based on score and index
            int rankingKey = multiplier * playerData.scoreMax + index;
            rankedPlayerData.put(rankingKey, playerData);
        }
    }

    /**
     * Renders the global scoreboard showing top players across all levels
     * 
     * @param graphics The Graphics2D context used for drawing
     */
    public static void drawGlobalScoreboard(Graphics2D graphics) {
        int rowIndex = 0;

        // Convert sorted map to list for easier iteration
        List<PlayerProgressTracker> rankedPlayers = new LinkedList<>();
        for (Entry<Integer, PlayerProgressTracker> entry : rankedPlayerData.entrySet()) {
            rankedPlayers.add(entry.getValue());
        }

        // Display top players in descending order of score
        for (int i = rankedPlayers.size() - 1; i >= 0; i--) {
            PlayerProgressTracker playerData = rankedPlayers.get(i);
            rowIndex++;

            if (rowIndex <= MAX_DISPLAYED_ENTRIES) {
                // Get the player's best level data
                LevelData bestLevelData = playerData.sortedData.get(playerData.sortedData.lastKey());
                int yPosition = GLOBAL_SCOREBOARD_Y + rowIndex * ROW_HEIGHT;

                // Render player data row
                graphics.setColor(Color.WHITE);
                graphics.setFont(GameView.GAME_FONT);
                graphics.drawString(playerData.nameUser, COLUMN_X_NAME, yPosition);
                graphics.drawString(String.valueOf(bestLevelData.score), COLUMN_X_SCORE, yPosition);
                graphics.drawString(String.valueOf(bestLevelData.time), COLUMN_X_TIME, yPosition);
                graphics.drawString(String.valueOf(bestLevelData.num + 1), COLUMN_X_LEVEL, yPosition);
            }
        }
    }

    /**
     * Renders a player-specific scoreboard showing their achievements across
     * levels
     * 
     * @param graphics   The Graphics2D context used for drawing
     * @param playerName The name of the player whose data to display
     */
    public static void drawPlayerScoreboard(Graphics2D graphics, String playerName) {
        int rowIndex = 0;

        if (playerDataMap.containsKey(playerName)) {
            // Get the player's data
            PlayerProgressTracker playerData = playerDataMap.get(playerName);

            // Convert level data to list for easier iteration
            List<LevelData> playerLevels = new LinkedList<>();
            for (Entry<Integer, LevelData> entry : playerData.sortedData.entrySet()) {
                playerLevels.add(entry.getValue());
            }

            // Display player's levels in descending order
            for (int i = playerLevels.size() - 1; i >= 0; i--) {
                if (rowIndex < MAX_DISPLAYED_ENTRIES) {
                    LevelData levelData = playerLevels.get(i);
                    int yPosition = PLAYER_SCOREBOARD_Y + rowIndex * ROW_HEIGHT;

                    // Render level data row
                    graphics.setColor(Color.WHITE);
                    graphics.setFont(GameView.GAME_FONT);
                    graphics.drawString(playerName, COLUMN_X_NAME, yPosition);
                    graphics.drawString(String.valueOf(levelData.score), COLUMN_X_SCORE, yPosition);
                    graphics.drawString(String.valueOf(levelData.time), COLUMN_X_TIME, yPosition);
                    graphics.drawString(String.valueOf(levelData.num + 1), COLUMN_X_LEVEL, yPosition);
                }
                rowIndex++;
            }
        }
    }

    // For backward compatibility
    public static void loadLeaderBoardData() {
        loadScoreboardData();
    }

    // For backward compatibility
    public static void drawLeaderBoardMode1(Graphics2D graphics) {
        drawGlobalScoreboard(graphics);
    }

    // For backward compatibility
    public static void drawLeaderBoardMode2(Graphics2D graphics, String playerName) {
        drawPlayerScoreboard(graphics, playerName);
    }
}