package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import object.InteractiveStaticObject;
import service.LevelFileParser;
import service.GameAudioManager;
import service.PlayerManager;
import city.cs.engine.DynamicBody;
import city.cs.engine.StaticBody;
import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import city.cs.engine.World;
import entity.PhysicsActor;
import entity.Player;

/**
 * GameWorld manages the game's physics, entities, and core gameplay logic.
 * It handles player input, updates game state, manages level loading,
 * and coordinates interactions between game objects.
 */
public class GameWorld extends World implements KeyListener, StepListener {
	// Input state tracking
	private static boolean isLeftKeyPressed = false;
	private static boolean isRightKeyPressed = false;
	private static boolean isJumpKeyPressed = false;
	private static boolean isAttackKeyPressed = false;

	// Time tracking
	public static long levelStartTimeMillis;
	public static int previousElapsedTimeSeconds;
	public static int currentElapsedTimeSeconds;

	// Score tracking
	public static int score;

	// Current level
	public int currentLevelNumber = 1;

	// Audio system
	public static GameAudioManager sound = new GameAudioManager();

	// Game entities
	public Player player;
	public Camera camera;

	// Entity collections
	public List<InteractiveStaticObject> staticObjects = new LinkedList<>();
	public List<PhysicsActor> movableObjects = new LinkedList<>();

	/**
	 * Creates a new game world with default settings
	 */
	public GameWorld() {
		super();

		// Initialize camera
		this.camera = new Camera();

		// Configure physics
		this.setGravity(75f);
		this.addStepListener(this);

		// Initialize audio
		sound.setFile(0);
		sound.play();
		sound.loop();

		// Load initial level
		loadLevel(currentLevelNumber);
	}

	/**
	 * Loads a numbered level from the built-in level designs
	 * 
	 * @param levelNumber The level number to load
	 */
	public void loadLevel(int levelNumber) {
		// Reset game state
		score = 0;
		staticObjects.clear();
		movableObjects.clear();

		// Set level boundaries
		camera.levelLength = Level.LENGTH_LEVEL[levelNumber];

		// Clear existing physics bodies
		for (StaticBody staticBody : this.getStaticBodies()) {
			staticBody.destroy();
		}
		for (DynamicBody dynamicBody : this.getDynamicBodies()) {
			dynamicBody.destroy();
		}

		// Create player and camera
		player = new Player(this);
		player.setPosition(Level.STARTING_POSITION[0]);
		this.camera = new Camera();

		// Load level content and add player to world
		Level.loadLevel(this, levelNumber);
		movableObjects.add(player);

		// Initialize time tracking
		levelStartTimeMillis = (new Date()).getTime();
		previousElapsedTimeSeconds = 0;
		currentElapsedTimeSeconds = 0;

		// Start level music
		GameWorld.sound.switchloop(levelNumber);
	}

	/**
	 * Loads a custom level from a file
	 * 
	 * @param filePath Path to the level file
	 */
	public void loadLevel(String filePath) {
		// Reset game state
		score = 0;
		staticObjects.clear();
		movableObjects.clear();

		// Initialize camera
		this.camera = new Camera();

		// Clear existing physics bodies
		for (StaticBody staticBody : this.getStaticBodies()) {
			staticBody.destroy();
		}
		for (DynamicBody dynamicBody : this.getDynamicBodies()) {
			dynamicBody.destroy();
		}

		// Create player and load level
		player = new Player(this);
		LevelFileParser.loadLevel(this, filePath);
		player.setPosition(Level.startingPositionOpenLevel);
		camera.levelLength = Level.lengthLevelOpenLevel;
		movableObjects.add(player);

		// Initialize time tracking
		levelStartTimeMillis = (new Date()).getTime();

		// Start level music
		GameWorld.sound.switchloop(0);
	}

	/**
	 * Loads a saved game state from a file
	 * 
	 * @param filePath Path to the saved game file
	 */
	public void loadSavedGame(String filePath) {
		// Reset game state
		score = 0;
		staticObjects.clear();
		movableObjects.clear();

		// Initialize camera
		this.camera = new Camera();

		// Clear existing physics bodies
		for (StaticBody staticBody : this.getStaticBodies()) {
			staticBody.destroy();
		}
		for (DynamicBody dynamicBody : this.getDynamicBodies()) {
			dynamicBody.destroy();
		}

		// Create player and load saved game
		player = new Player(this);
		LevelFileParser.loadLevel(this, filePath);
	}

	/**
	 * Checks if the player has died or fallen out of bounds
	 */
	public void checkGameOver() {
		if (player.getPosition().y < -20f || player.health <= 0 && !Menu.isGameOver) {
			player.health = 0;
			Menu.handleGameOver();
		}
	}

	/**
	 * Checks if the player has reached the end of the level
	 */
	public void checkLevelCompletion() {
		if (camera.cameraPosition >= camera.levelLength - 786f / 20f && player.getPosition().x >= 786f / 40f) {
			// Award completion bonus
			score += 100;

			if (currentLevelNumber < Level.numberLv && currentLevelNumber != -1) {
				advanceToNextLevel();
			} else {
				// Player has completed the final level
				Menu.handleLevelComplete();
			}
		}
	}

	/**
	 * Advances to the next level
	 */
	public void advanceToNextLevel() {
		currentLevelNumber++;
		loadLevel(currentLevelNumber);
	}

	/**
	 * Adjusts the level start time to account for pauses
	 * Ensures the timer doesn't count time spent in pause menu
	 */
	public static void adjustTimerAfterPause() {
		int timeBeforePause = currentElapsedTimeSeconds;
		int timeAfterPause = (int) ((new Date()).getTime() - levelStartTimeMillis) / 1000;
		int pauseDuration = timeAfterPause - timeBeforePause;
		levelStartTimeMillis += pauseDuration * 1000;
	}

	/**
	 * Updates the elapsed time counter
	 */
	public void updateElapsedTime() {
		currentElapsedTimeSeconds = (int) ((new Date()).getTime() - levelStartTimeMillis) / 1000;
	}

	/**
	 * Handles text input events, particularly for name entry
	 * 
	 * @param event The key event
	 */
	@Override
	public void keyTyped(KeyEvent event) {
		if (event.getKeyChar() == 'f') {
			isAttackKeyPressed = true;
		}

		if (Menu.currentGameState == Menu.STATE_NAME_ENTRY) {
			// Handle name input
			if ((int) event.getKeyChar() == 8) {
				try {
					// Handle backspace - remove last character
					String currentText = Menu.nameInputField.getValue();
					if (currentText.length() > 0) {
						Menu.nameInputField.setValue(currentText.substring(0, currentText.length() - 1));
					}
				} catch (Exception ex) {
					// Ignore exceptions from empty strings
				}
			} else {
				// Add typed character to name
				Menu.nameInputField.setValue(Menu.nameInputField.getValue() + event.getKeyChar());
			}
		}
	}

	/**
	 * Handles key press events for movement and actions
	 * 
	 * @param event The key event
	 */
	@Override
	public void keyPressed(KeyEvent event) {
		int keyCode = event.getKeyCode();

		if (keyCode == KeyEvent.VK_LEFT) {
			isLeftKeyPressed = true;
		}
		if (event.getKeyChar() == ' ') {
			isJumpKeyPressed = true;
		}
		if (keyCode == KeyEvent.VK_RIGHT) {
			isRightKeyPressed = true;
		}
	}

	/**
	 * Handles key release events for movement and actions
	 * 
	 * @param event The key event
	 */
	@Override
	public void keyReleased(KeyEvent event) {
		int keyCode = event.getKeyCode();

		if (keyCode == KeyEvent.VK_LEFT) {
			isLeftKeyPressed = false;
		}
		if (event.getKeyChar() == ' ') {
			isJumpKeyPressed = false;
		}
		if (keyCode == KeyEvent.VK_RIGHT) {
			isRightKeyPressed = false;
		}
	}

	/**
	 * Main game loop - executes after each physics step
	 * Updates all game entities and handles player input
	 * 
	 * @param event The step event
	 */
	@Override
	public void postStep(StepEvent event) {
		// Process player movement based on input
		if (isLeftKeyPressed) {
			player.move(Player.LEFT);
		}
		if (isRightKeyPressed) {
			player.move(Player.RIGHT);
		}
		if (isJumpKeyPressed) {
			player.move(Player.UP);
		}
		if (!isLeftKeyPressed && !isRightKeyPressed && !player.attacking) {
			player.move(Player.NO_DIRECTION);
		}
		if (isAttackKeyPressed) {
			player.attack();
			isAttackKeyPressed = false;
		}

		// Update camera position
		camera.updateCameraPosition(player, this);

		// Update all game objects
		for (InteractiveStaticObject staticObject : this.staticObjects) {
			staticObject.eventStep();
		}
		for (PhysicsActor movableObject : this.movableObjects) {
			movableObject.eventStep();
		}

		// Check game state conditions
		checkLevelCompletion();
		checkGameOver();

		// Update timer
		updateElapsedTime();
	}

	/**
	 * Executes before each physics step
	 * Currently not used but required by StepListener interface
	 * 
	 * @param event The step event
	 */
	@Override
	public void preStep(StepEvent event) {
		// Not currently used
	}
}