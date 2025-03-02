package game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import ui.Game;
import service.*;
import service.GameAudioManager;
import service.ScoreboardManager;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

/**
 * Menu manages the game's user interface and state transitions.
 * It handles user interactions, screen navigation, and game state changes
 * such as playing, pausing, level selection, and displaying leaderboards.
 */
public class Menu implements MouseListener {
	// Game state constants
	public static final int STATE_PLAYING = 0;
	public static final int STATE_PAUSED = 1;
	public static final int STATE_MAIN_MENU = 2;
	public static final int STATE_LEADERBOARD = 3;
	public static final int STATE_NAME_ENTRY = 4;
	public static final int STATE_LEVEL_SELECT = 5;

	// Current game state
	public static int currentGameState = STATE_PLAYING;
	public static boolean isGameOver = false;
	public static boolean isLevelCompleted = false;

	// Leaderboard display mode (0 = player-specific, 1 = global)
	public static int leaderboardMode = 0;

	// UI element bounds for hit detection
	private final Rectangle pauseButtonBounds = new Rectangle(786 - 50, 10, 40, 40);
	private final Rectangle menuButtonBounds = new Rectangle(786 - 90, 10, 40, 40);
	private final Rectangle saveButtonBounds = new Rectangle(786 - 240, 16, 120, 30);
	private final Rectangle playButtonBounds = new Rectangle(300, 148, 240, 60);
	private final Rectangle leaderboardButtonBounds = new Rectangle(300, 228, 240, 60);
	private final Rectangle loadGameButtonBounds = new Rectangle(300, 308, 240, 60);
	private final Rectangle okButtonBounds = new Rectangle(358, 292, 129, 60);
	private final Rectangle userTabButtonBounds = new Rectangle(306, 117, 83, 40);
	private final Rectangle globalTabButtonBounds = new Rectangle(399, 112, 83, 40);
	private final Rectangle level1ButtonBounds = new Rectangle(300, 141, 240, 60);
	private final Rectangle level2ButtonBounds = new Rectangle(300, 221, 240, 60);
	private final Rectangle level3ButtonBounds = new Rectangle(300, 301, 240, 60);
	private final Rectangle customLevelButtonBounds = new Rectangle(300, 381, 240, 60);

	// Button pressed states for visual feedback
	public static boolean isOkButtonPressed = false;
	public static boolean isPlayButtonPressed = false;
	public static boolean isLeaderboardButtonPressed = false;
	public static boolean isLevel1ButtonPressed = false;
	public static boolean isLevel2ButtonPressed = false;
	public static boolean isLevel3ButtonPressed = false;
	public static boolean isCustomLevelButtonPressed = false;
	public static boolean isLoadGameButtonPressed = false;

	// Click timer to prevent rapid clicks
	private static int clickCooldownTimer = -1;
	private static final int CLICK_COOLDOWN_DURATION = 3;

	// Reference to the game world
	private static GameWorld gameWorld;

	// Text input for player name
	public static TextInputField nameInputField = new TextInputField("");

	/**
	 * Creates a new menu system connected to the specified game world
	 * 
	 * @param world The game world to control
	 */
	public Menu(GameWorld world) {
		gameWorld = world;
		togglePlayPause();
		currentGameState = STATE_NAME_ENTRY;
	}

	/**
	 * Handles level completion, showing the victory screen
	 */
	public static void handleLevelComplete() {
		togglePlayPause();
		isLevelCompleted = true;
		GameWorld.sound.setFile(GameAudioManager.WIN);
		GameWorld.sound.play();
	}

	/**
	 * Handles player death, showing the game over screen
	 */
	public static void handleGameOver() {
		togglePlayPause();
		isGameOver = true;
		GameWorld.sound.setFile(GameAudioManager.GAME_OVER);
		GameWorld.sound.play();
	}

	/**
	 * Toggles between playing and paused states
	 */
	public static void togglePlayPause() {
		currentGameState = 1 - currentGameState;
		if (currentGameState == STATE_PLAYING) {
			GameWorld.adjustTimerAfterPause();
			GameWorld.sound.playCurrentLoop();
			gameWorld.start();
		} else {
			GameWorld.previousElapsedTimeSeconds = GameWorld.currentElapsedTimeSeconds;
			GameWorld.sound.pauseCurrentLoop();
			gameWorld.stop();
		}
	}

	/**
	 * Navigates to the main menu screen
	 */
	public void navigateToMainMenu() {
		if (currentGameState == STATE_PLAYING) {
			// Pause the game before showing menu
			togglePlayPause();
		}
		currentGameState = STATE_MAIN_MENU;
	}

	/**
	 * Starts a new game with the specified level number
	 * 
	 * @param levelNumber The level number to load
	 */
	public void startGame(int levelNumber) {
		gameWorld.currentLevelNumber = levelNumber;
		gameWorld.loadLevel(levelNumber);
		currentGameState = STATE_PAUSED;
		togglePlayPause();
	}

	/**
	 * Loads a custom level from a file
	 * 
	 * @param filePath Path to the level file
	 */
	public void loadCustomLevel(String filePath) {
		gameWorld.currentLevelNumber = -1;
		gameWorld.loadLevel(filePath);
		currentGameState = STATE_PAUSED;
		togglePlayPause();
	}

	/**
	 * Loads a saved game state from a file
	 * 
	 * @param filePath Path to the saved game file
	 */
	public void loadSavedGame(String filePath) {
		gameWorld.loadSavedGame(filePath);
		currentGameState = STATE_PAUSED;
		togglePlayPause();
	}

	/**
	 * Navigates to the level selection screen
	 */
	public void navigateToLevelSelect() {
		currentGameState = STATE_LEVEL_SELECT;
	}

	/**
	 * Navigates to the leaderboard screen
	 */
	public void navigateToLeaderboard() {
		ScoreboardManager.loadLeaderBoardData();
		currentGameState = STATE_LEADERBOARD;
	}

	/**
	 * Handles mouse click events for all menu screens
	 * 
	 * @param event The mouse event data
	 */
	@Override
	public void mouseClicked(MouseEvent event) {
		// Adjust for window title bar offset
		Point clickPosition = new Point(event.getPoint().x, event.getPoint().y - 20);

		// Handle clicks based on current game state
		if (currentGameState == STATE_PLAYING || currentGameState == STATE_PAUSED) {
			handleGameplayScreenClick(clickPosition);
		} else if (currentGameState == STATE_MAIN_MENU) {
			handleMainMenuClick(clickPosition);
		} else if (currentGameState == STATE_NAME_ENTRY) {
			handleNameEntryClick(clickPosition);
		} else if (currentGameState == STATE_LEADERBOARD) {
			handleLeaderboardClick(clickPosition);
		} else if (currentGameState == STATE_LEVEL_SELECT) {
			handleLevelSelectClick(clickPosition);
		}

		// Reset game state flags
		isGameOver = false;
		isLevelCompleted = false;
	}

	/**
	 * Handles clicks on the gameplay screen (playing or paused)
	 * 
	 * @param clickPosition The position of the mouse click
	 */
	private void handleGameplayScreenClick(Point clickPosition) {
		if (pauseButtonBounds.contains(clickPosition) && !isGameOver && !isLevelCompleted) {
			togglePlayPause();
		}

		if (menuButtonBounds.contains(clickPosition)) {
			if (gameWorld.currentLevelNumber != -1) {
				PlayerManager.saveResult(gameWorld.currentLevelNumber, gameWorld.currentElapsedTimeSeconds,
						gameWorld.score);
			}
			navigateToMainMenu();
		} else if (saveButtonBounds.contains(clickPosition)) {
			showSaveGameDialog();
		} else {
			if (isGameOver) {
				if (gameWorld.currentLevelNumber != -1) {
					PlayerManager.saveResult(gameWorld.currentLevelNumber, gameWorld.currentElapsedTimeSeconds,
							gameWorld.score);
					startGame(gameWorld.currentLevelNumber);
				} else {
					navigateToMainMenu();
				}
			}
			if (isLevelCompleted) {
				if (gameWorld.currentLevelNumber != -1) {
					navigateToMainMenu();
				}
			}
		}
	}

	/**
	 * Shows a file dialog to save the current game state
	 */
	private void showSaveGameDialog() {
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showSaveDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			PlayerManager.saveGame(gameWorld.currentLevelNumber, gameWorld.player, gameWorld.camera,
					GameWorld.score, GameWorld.currentElapsedTimeSeconds, selectedFile);
		}
	}

	/**
	 * Handles clicks on the main menu screen
	 * 
	 * @param clickPosition The position of the mouse click
	 */
	private void handleMainMenuClick(Point clickPosition) {
		if (playButtonBounds.contains(clickPosition)) {
			navigateToLevelSelect();
		}

		if (leaderboardButtonBounds.contains(clickPosition)) {
			navigateToLeaderboard();
		}

		if (loadGameButtonBounds.contains(clickPosition)) {
			showLoadGameDialog();
		}
	}

	/**
	 * Shows a file dialog to load a saved game
	 */
	private void showLoadGameDialog() {
		JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile() != null) {
			String filePath = fileChooser.getSelectedFile().getAbsolutePath();
			loadSavedGame(filePath);
		}
	}

	/**
	 * Handles clicks on the name entry screen
	 * 
	 * @param clickPosition The position of the mouse click
	 */
	private void handleNameEntryClick(Point clickPosition) {
		if (okButtonBounds.contains(clickPosition)) {
			// Save player name and load their data
			PlayerManager.name = nameInputField.getValue();
			ScoreboardManager.loadLeaderBoardData();

			if (ScoreboardManager.playerDataMap.containsKey(PlayerManager.name)) {
				PlayerManager.data = ScoreboardManager.playerDataMap.get(PlayerManager.name);
			} else {
				PlayerManager.data = new PlayerProgressTracker();
			}

			navigateToMainMenu();
		}
	}

	/**
	 * Handles clicks on the leaderboard screen
	 * 
	 * @param clickPosition The position of the mouse click
	 */
	private void handleLeaderboardClick(Point clickPosition) {
		if (userTabButtonBounds.contains(clickPosition)) {
			// Switch to player-specific leaderboard
			Menu.leaderboardMode = 0;
		}

		if (globalTabButtonBounds.contains(clickPosition)) {
			// Switch to global leaderboard
			Menu.leaderboardMode = 1;
		}

		if (menuButtonBounds.contains(clickPosition)) {
			navigateToMainMenu();
		}
	}

	/**
	 * Handles clicks on the level selection screen
	 * 
	 * @param clickPosition The position of the mouse click
	 */
	private void handleLevelSelectClick(Point clickPosition) {
		if (level1ButtonBounds.contains(clickPosition)) {
			startGame(0);
		}

		if (level2ButtonBounds.contains(clickPosition) && PlayerManager.data.levelMax >= 1) {
			startGame(1);
		}

		if (level3ButtonBounds.contains(clickPosition) && PlayerManager.data.levelMax >= 2) {
			startGame(2);
		}

		if (customLevelButtonBounds.contains(clickPosition)) {
			showLoadLevelDialog();
		}
	}

	/**
	 * Shows a file dialog to load a custom level
	 */
	private void showLoadLevelDialog() {
		JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile() != null) {
			String filePath = fileChooser.getSelectedFile().getAbsolutePath();
			loadCustomLevel(filePath);
		}
	}

	/**
	 * Handles mouse press events for button visual feedback
	 * 
	 * @param event The mouse event data
	 */
	@Override
	public void mousePressed(MouseEvent event) {
		Point clickPosition = new Point(event.getPoint().x, event.getPoint().y - 20);

		if (currentGameState == STATE_NAME_ENTRY) {
			if (okButtonBounds.contains(clickPosition)) {
				isOkButtonPressed = true;
			}
		}

		if (currentGameState == STATE_MAIN_MENU) {
			if (playButtonBounds.contains(clickPosition)) {
				isPlayButtonPressed = true;
			}
			if (leaderboardButtonBounds.contains(clickPosition)) {
				isLeaderboardButtonPressed = true;
			}
			if (loadGameButtonBounds.contains(clickPosition)) {
				isLoadGameButtonPressed = true;
			}
		}

		if (currentGameState == STATE_LEVEL_SELECT) {
			if (level1ButtonBounds.contains(clickPosition)) {
				isLevel1ButtonPressed = true;
			}
			if (level2ButtonBounds.contains(clickPosition) && PlayerManager.data.levelMax >= 1) {
				isLevel2ButtonPressed = true;
			}
			if (level3ButtonBounds.contains(clickPosition) && PlayerManager.data.levelMax >= 2) {
				isLevel3ButtonPressed = true;
			}
		}
	}

	/**
	 * Handles mouse release events to reset button visual states
	 * 
	 * @param event The mouse event data
	 */
	@Override
	public void mouseReleased(MouseEvent event) {
		// Reset all button pressed states
		isOkButtonPressed = false;
		isPlayButtonPressed = false;
		isLeaderboardButtonPressed = false;
		isLevel1ButtonPressed = false;
		isLevel2ButtonPressed = false;
		isLevel3ButtonPressed = false;
		isLoadGameButtonPressed = false;
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		// Not used
	}

	@Override
	public void mouseExited(MouseEvent event) {
		// Not used
	}
}
