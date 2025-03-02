package service;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * PlayerProgressTracker stores and manages a player's game progress and
 * achievements.
 * It tracks:
 * - Completed levels and their statistics
 * - Scores and completion times
 * - Maximum level reached and highest scores
 * 
 * This class provides methods to add new level completion data and sort
 * the player's achievements for display and progression tracking.
 */
public class PlayerProgressTracker {

    // Player's username
    String nameUser;

    // List of level completion data
    List<LevelData> listLevelData = new LinkedList<>();

    // Sorted map of level data for ranking
    SortedMap<Integer, LevelData> sortedData = new TreeMap<>();

    // Highest level the player has unlocked
    public int levelMax = 0;

    // Highest score the player has achieved
    int scoreMax = 0;

    /**
     * Adds level completion data to the player's record
     * 
     * @param num   The level number
     * @param time  The time taken to complete the level (in seconds)
     * @param score The score achieved in the level
     */
    public void appendData(int num, int time, int score) {
        LevelData lv = new LevelData(num, time, score);
        listLevelData.add(lv);
    }

    /**
     * Sorts the player's level data by score and updates max level and score.
     * This method should be called after adding new level data to ensure
     * the player's statistics are up to date.
     */
    public void sort() {
        sortedData.clear();
        levelMax = 0;
        scoreMax = 0;
        int i = 0;

        for (LevelData dt : listLevelData) {
            i++;

            // Update max level reached
            if (dt.num > levelMax) {
                levelMax = dt.num;
            }

            // Calculate and update max score
            int scoreTotal = dt.calculateScoreTotal();
            if (scoreTotal > scoreMax) {
                scoreMax = scoreTotal;
            }

            // Add to sorted map with a unique key
            int l = listLevelData.size() + 1;
            sortedData.put(l * scoreTotal + i, dt);
        }

        // Increment max level to unlock the next level
        levelMax += 1;
    }

    /**
     * LevelData stores completion data for a single level attempt
     */
    static class LevelData {
        int num; // Level number
        int time; // Completion time in seconds
        int score; // Score achieved

        /**
         * Creates a new level data record
         * 
         * @param _num   The level number
         * @param _time  The time taken to complete the level
         * @param _score The score achieved in the level
         */
        public LevelData(int _num, int _time, int _score) {
            num = _num;
            time = _time;
            score = _score;
        }

        /**
         * Calculates the total score based on completion time and points collected.
         * Formula: (60 - time) + score
         * This rewards faster completion times.
         * 
         * @return The total calculated score
         */
        public int calculateScoreTotal() {
            return (60 - time) + score;
        }
    }
}