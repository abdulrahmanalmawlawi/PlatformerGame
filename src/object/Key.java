package object;

import city.cs.engine.*;
import entity.Player;
import game.GameWorld;
import service.GameAudioManager;

/**
 * A Key is a pickup item that allows the player to open doors.
 * When collected, it updates the player's state to indicate they have a key.
 * 
 * Keys are an essential part of the game's progression system:
 * - They act as collectible items that gate progress through levels
 * - Create exploration-focused gameplay by requiring players to search areas
 * - Form part of environmental puzzles (reaching keys in difficult locations)
 * - Establish a "lock and key" mechanic with the Door class
 * 
 * Key design and implementation:
 * - Uses the GamePickup base class for standard collection behavior
 * - Provides clear visual indication to the player (recognizable key sprite)
 * - Plays audio feedback when collected
 * - Updates player state through the hasKey boolean flag
 * - Single-use design (opening a door consumes the key)
 * 
 * In more complex levels, players may need to decide which doors to open
 * with limited keys, creating strategic decision points in gameplay.
 */
public class Key extends GamePickup {

	// Physical shape of the key
	private static final BoxShape KEY_SHAPE = new BoxShape((float) (43 / 40.0), (float) (41 / 40.0));

	/**
	 * Constructor for creating a new key
	 * 
	 * Initializes the key with:
	 * 1. The standard key collision shape
	 * 2. A recognizable key sprite for visual identification
	 * 3. Ghostly physics (inherited from GamePickup) so players can walk through it
	 * 4. Sensor-based collision detection (inherited from GamePickup)
	 * 
	 * Keys are typically placed in strategic locations that require some
	 * platforming
	 * skill to reach, creating micro-challenges within the level.
	 * 
	 * @param world The game world in which the key exists
	 */
	public Key(World world) {
		super(world, KEY_SHAPE);
		this.addImage(new BodyImage("resources/collectibles/collectible_key.png", (float) (83.0 / 20.0)));
	}

	/**
	 * Action performed when the key is collected by the player.
	 * 
	 * This method is automatically called when the player touches the key and:
	 * 1. Sets the player's hasKey flag to true, enabling door interactions
	 * 2. Removes the key from the game world (single-use item)
	 * 3. Plays a collection sound for audio feedback
	 * 
	 * The player can only hold one key at a time (binary state - has key or
	 * doesn't),
	 * making key management straightforward. If the player already has a key and
	 * collects another, the old key is effectively replaced.
	 * 
	 * Once the player has a key, any contact with a closed door will automatically
	 * open it and consume the key.
	 * 
	 * @param player The player that collected the key
	 */
	@Override
	public void action(Player player) {
		player.hasKey = true;
		this.destroy();
		GameWorld.sound.setFile(GameAudioManager.COIN);
		GameWorld.sound.play();
	}
}
