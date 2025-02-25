package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.GameWorld;
import game.Level;
import game.Menu;
import service.ScoreboardManager;
import service.PlayerManager;
import city.cs.engine.UserView;
import assets.GameAssetManager;

/**
 * GameView handles the rendering of all game elements on the screen.
 * It is responsible for drawing the background, foreground, UI elements,
 * and different game states (menu, gameplay, leaderboard, etc.).
 */
public class GameView extends UserView {

    private static final long serialVersionUID = 1L;

    // Reference to the game world
    private GameWorld gameWorld;

    // Font used for text rendering throughout the game
    public static Font GAME_FONT = new Font("", Font.BOLD, 20);

    /**
     * Constructor for creating a new game view
     * 
     * @param world  The game world to be rendered
     * @param width  The width of the view window
     * @param height The height of the view window
     */
    public GameView(GameWorld world, int width, int height) {
        super(world, width, height);
        gameWorld = world;
    }

    /**
     * Draws the background elements based on the current game state
     * This includes parallax backgrounds and level backgrounds
     * 
     * @param g The Graphics2D context used for drawing
     */
    @Override
    public void paintBackground(Graphics2D g) {
        // Only draw game backgrounds when in play or pause state
        if (Menu.currentGameState == Menu.STATE_PLAYING || Menu.currentGameState == Menu.STATE_PAUSED) {
            if (gameWorld.currentLevelNumber != -1) {
                // Draw parallax background layers with different scroll speeds
                g.drawImage(GameAssetManager.background1[gameWorld.currentLevelNumber], 0, 0, null);
                g.drawImage(GameAssetManager.background2[gameWorld.currentLevelNumber],
                        -(int) (gameWorld.camera.cameraPosition), 0, null);
                g.drawImage(GameAssetManager.background3[gameWorld.currentLevelNumber],
                        -(int) (gameWorld.camera.cameraPosition * 2), 0, null);
                g.drawImage(GameAssetManager.background4[gameWorld.currentLevelNumber],
                        -(int) (gameWorld.camera.cameraPosition * 3), 0, null);

                // Draw the level-specific background
                g.drawImage(Level.backgroundLevels[gameWorld.currentLevelNumber],
                        -(int) (gameWorld.camera.cameraPosition * 20) - 2, 1, null);
            } else {
                // Default background for non-level screens (level -1)
                g.drawImage(GameAssetManager.background1[0], 0, 0, null);
                g.drawImage(GameAssetManager.background2[0], -(int) (gameWorld.camera.cameraPosition), 0, null);
                g.drawImage(GameAssetManager.background3[0], -(int) (gameWorld.camera.cameraPosition * 2), 0, null);
                g.drawImage(GameAssetManager.background4[0], -(int) (gameWorld.camera.cameraPosition * 3), 0, null);
            }
        }
    }

    /**
     * Draws the foreground elements and UI based on the current game state
     * This includes player HUD, menus, and overlays
     * 
     * @param g The Graphics2D context used for drawing
     */
    @Override
    public void paintForeground(Graphics2D g) {
        if (Menu.currentGameState == Menu.STATE_PLAYING || Menu.currentGameState == Menu.STATE_PAUSED) {
            // Draw gameplay UI elements
            renderGameplayUI(g);
        } else if (Menu.currentGameState == Menu.STATE_MAIN_MENU) {
            renderMainMenu(g);
        } else if (Menu.currentGameState == Menu.STATE_NAME_ENTRY) {
            renderNameEntryScreen(g);
        } else if (Menu.currentGameState == Menu.STATE_LEADERBOARD) {
            renderLeaderboard(g);
        } else {
            renderLevelSelectionScreen(g);
        }
    }

    /**
     * Renders the UI elements during gameplay (play or pause state)
     * 
     * @param g The Graphics2D context used for drawing
     */
    private void renderGameplayUI(Graphics2D g) {
        // Draw level-specific foreground and gradient overlay
        if (gameWorld.currentLevelNumber != -1) {
            g.drawImage(Level.foregroundLevels[gameWorld.currentLevelNumber],
                    -(int) (gameWorld.camera.cameraPosition * 20) - 2, 1, null);
            g.drawImage(GameAssetManager.gradient[gameWorld.currentLevelNumber], 0, 0, null);
        }

        // Draw save button
        g.drawImage(GameAssetManager.savebutton, 786 - 240, 16, 120, 30, null);

        if (Menu.currentGameState == Menu.STATE_PLAYING) {
            // Draw pause button during gameplay
            g.drawImage(GameAssetManager.pauseIcon, 786 - 50, 10, 40, 40, null);
        } else {
            // Draw pause overlay and play button when paused
            if (!Menu.isGameOver && !Menu.isLevelCompleted) {
                // Standard pause overlay
                g.drawImage(GameAssetManager.darkforeground, 0, 0, 786, 600, null);
            } else if (Menu.isGameOver) {
                // Game over screen
                g.drawImage(GameAssetManager.gameOver, 0, 0, 786, 600, null);
            } else {
                // Victory screen
                g.drawImage(GameAssetManager.congratulations, 0, 0, 786, 600, null);
            }
            g.drawImage(GameAssetManager.playIcon, 786 - 50, 10, 40, 40, null);
        }

        // Draw player health hearts
        for (int i = 0; i < gameWorld.player.health; i++) {
            g.drawImage(GameAssetManager.heart, 10 + 35 * i, 10, 20, 20, null);
        }

        // Draw score and coin icon
        g.drawImage(GameAssetManager.coin, 10, 40, 20, 20, null);
        g.setColor(Color.black);
        g.setFont(GAME_FONT);
        g.drawString(String.valueOf(gameWorld.score * 5), 40, 57);

        // Draw menu button
        g.drawImage(GameAssetManager.menuIcon, 786 - 90, 10, 40, 40, null);

        // Draw power-up indicators
        if (gameWorld.player.hasSword) {
            g.drawImage(GameAssetManager.sword, 110, 6, 30, 30, null);
        }
        if (gameWorld.player.hasKey) {
            g.drawImage(GameAssetManager.key, 140, 6, 30, 30, null);
        }

        // Draw timer
        g.drawString(String.valueOf(GameWorld.currentElapsedTimeSeconds) + "s", 786 - 130, 38);
    }

    /**
     * Renders the main menu screen
     * 
     * @param g The Graphics2D context used for drawing
     */
    public void renderMainMenu(Graphics2D g) {
        // Draw background layers
        g.drawImage(GameAssetManager.background1[0], 0, 0, null);
        g.drawImage(GameAssetManager.background2[0], 0, 0, null);
        g.drawImage(GameAssetManager.background3[0], 0, 0, null);
        g.drawImage(GameAssetManager.background4[0], 0, 0, null);
        g.drawImage(GameAssetManager.backgroundmenu, 0, 0, null);
        g.drawImage(GameAssetManager.gradient[0], 0, 0, null);

        // Draw menu buttons with pressed/unpressed states
        g.drawImage(!Menu.isPlayButtonPressed ? GameAssetManager.playButton : GameAssetManager.playButton2, 300, 148,
                240, 60, null);
        g.drawImage(
                !Menu.isLeaderboardButtonPressed ? GameAssetManager.leaderBoardButton
                        : GameAssetManager.leaderBoardButton2,
                300,
                228, 240, 60, null);
        g.drawImage(
                !Menu.isLoadGameButtonPressed ? GameAssetManager.loadPreviousGame : GameAssetManager.loadPreviousGame2,
                300, 308,
                240, 60, null);
    }

    /**
     * Renders the name entry screen
     * 
     * @param g The Graphics2D context used for drawing
     */
    public void renderNameEntryScreen(Graphics2D g) {
        // Draw background layers
        g.drawImage(GameAssetManager.background1[0], 0, 0, null);
        g.drawImage(GameAssetManager.background2[0], 0, 0, null);
        g.drawImage(GameAssetManager.background3[0], 0, 0, null);
        g.drawImage(GameAssetManager.background4[0], 0, 0, null);
        g.drawImage(GameAssetManager.namebackground, 0, 0, null);
        g.drawImage(GameAssetManager.gradient[0], 0, 0, null);

        // Draw name entry box and OK button
        g.drawImage(GameAssetManager.nameBox, 283, 217, 240, 60, null);
        g.drawImage(!Menu.isOkButtonPressed ? GameAssetManager.okbutton : GameAssetManager.okbutton2, 283, 287, 240, 60,
                null);

        // Draw the text entered by the player
        Menu.nameInputField.draw(g);
    }

    /**
     * Renders the leaderboard screen
     * 
     * @param g The Graphics2D context used for drawing
     */
    public void renderLeaderboard(Graphics2D g) {
        // Draw background layers
        g.drawImage(GameAssetManager.background1[0], 0, 0, null);
        g.drawImage(GameAssetManager.background2[0], 0, 0, null);
        g.drawImage(GameAssetManager.background3[0], 0, 0, null);
        g.drawImage(GameAssetManager.background4[0], 0, 0, null);
        g.drawImage(GameAssetManager.backgroundleaderboard, 0, 0, null);
        g.drawImage(GameAssetManager.gradient[0], 0, 0, null);

        // Draw scoreboard content based on current mode
        if (Menu.leaderboardMode == 1) {
            // Global scoreboard
            ScoreboardManager.drawLeaderBoardMode1(g);
        } else if (Menu.leaderboardMode == 0) {
            // Player-specific scoreboard
            ScoreboardManager.drawLeaderBoardMode2(g, PlayerManager.name);
        }

        // Draw back button
        g.drawImage(GameAssetManager.menuIcon, 786 - 90, 10, 40, 40, null);
    }

    /**
     * Renders the level selection screen
     * 
     * @param g The Graphics2D context used for drawing
     */
    public void renderLevelSelectionScreen(Graphics2D g) {
        // Draw background layers
        g.drawImage(GameAssetManager.background1[0], 0, 0, null);
        g.drawImage(GameAssetManager.background2[0], 0, 0, null);
        g.drawImage(GameAssetManager.background3[0], 0, 0, null);
        g.drawImage(GameAssetManager.background4[0], 0, 0, null);
        g.drawImage(GameAssetManager.backgroundmenu, 0, 0, null);
        g.drawImage(GameAssetManager.gradient[0], 0, 0, null);

        // Draw level selection buttons
        // Level 1 is always available
        g.drawImage(!Menu.isLevel1ButtonPressed ? GameAssetManager.level1button : GameAssetManager.level1button2, 300,
                141, 240, 60,
                null);

        // Level 2 is available only if player has completed level 1
        if (PlayerManager.data.levelMax >= 1) {
            g.drawImage(!Menu.isLevel2ButtonPressed ? GameAssetManager.level2button : GameAssetManager.level2button2,
                    300, 221, 240,
                    60, null);
        } else {
            g.drawImage(GameAssetManager.level2buttonlocked, 300, 221, 240, 60, null);
        }

        // Level 3 is available only if player has completed level 2
        if (PlayerManager.data.levelMax >= 2) {
            g.drawImage(!Menu.isLevel3ButtonPressed ? GameAssetManager.level3button : GameAssetManager.level3button2,
                    300, 301, 240,
                    60, null);
        } else {
            g.drawImage(GameAssetManager.level3buttonlocked, 300, 301, 240, 60, null);
        }

        // Custom level from file button
        g.drawImage(GameAssetManager.buttonFile, 300, 381, 240, 60, null);
    }
}