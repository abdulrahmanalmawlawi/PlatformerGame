package object;

import city.cs.engine.*;
import entity.Player;
import game.GameWorld;
import service.GameAudioManager;

/**
 * A Key is a pickup item that allows the player to open doors.
 * When collected, it updates the player's state to indicate they have a key.
 */
public class Key extends GamePickup {

	// Physical shape of the key
	private static final BoxShape KEY_SHAPE = new BoxShape((float) (43 / 40.0), (float) (41 / 40.0));

	/**
	 * Constructor for creating a new key
	 * 
	 * @param world The game world in which the key exists
	 */
	public Key(World world) {
		super(world, KEY_SHAPE);
		this.addImage(new BodyImage("resources/collectibles/collectible_key.png", (float) (83.0 / 20.0)));
	}

	/**
	 * Action performed when the key is collected by the player:
	 * - Updates player's state to indicate they have a key
	 * - Plays a collection sound
	 * - Destroys the key
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
