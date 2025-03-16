package object;

import game.GameWorld;
import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.Shape;
import city.cs.engine.World;
import entity.Player;
import org.jbox2d.common.Vec2;
import service.GameAudioManager;

/**
 * A Coin is a pickup item that increases the player's score when collected.
 * Coins can be placed individually or in a series.
 * 
 * Coins are a fundamental part of the game's reward system:
 * - They provide immediate feedback to the player through score increases and
 * sounds
 * - They encourage exploration of the level by rewarding players for reaching
 * platforms
 * - They guide the player through the intended path of the level
 * - They serve as a measure of level completion and player skill
 * 
 * The coin system is designed to be simple yet satisfying, with consistent
 * collection mechanics and visual/audio feedback to reinforce positive player
 * actions.
 */
public class Coin extends GamePickup {

	// Physical shape of the coin
	// Small square collision box that is easy to interact with
	private static final Shape COIN_SHAPE = new BoxShape(0.3f, 0.3f);

	/**
	 * Constructor for creating a new coin in the game world.
	 * 
	 * The coin is configured with:
	 * - A square collision box slightly smaller than its visual size
	 * - A static golden coin image that stands out against level backgrounds
	 * - Automatic registration with the physics system for collision detection
	 * 
	 * @param world The game world in which the coin exists
	 */
	public Coin(World world) {
		super(world, COIN_SHAPE);
		this.addImage(new BodyImage("resources/collectibles/collectible_coin.png", 0.6f));
	}

	/**
	 * Action performed when the coin is collected by the player.
	 * 
	 * This method implements the gameplay effects when a player collides with a
	 * coin:
	 * - Increments the global game score by 1 point
	 * - Destroys the coin to prevent multiple collections
	 * - Plays a satisfying "ding" sound effect for audio feedback
	 * - Implicitly removes the coin from the physics world
	 * 
	 * The coin collection is intentionally kept simple to maintain game flow,
	 * avoiding interruptions to the platforming experience.
	 * 
	 * @param player The player that collected the coin
	 */
	@Override
	public void action(Player player) {
		GameWorld.score++;
		this.destroy();
		GameWorld.sound.setFile(GameAudioManager.COIN);
		GameWorld.sound.play();
	}

	/**
	 * Utility method to create a coin at a specified position relative to a
	 * platform.
	 * 
	 * This method is used throughout level design to place coins consistently:
	 * - Above platforms at a fixed height
	 * - In horizontal patterns determined by the number parameter
	 * - Calculating position based on the platform's center and dimensions
	 * 
	 * The positioning formula places coins slightly above platforms in
	 * horizontal arrangements, with the number parameter determining
	 * the offset from center (0 = center, negative = left, positive = right).
	 * 
	 * @param position The base position (center) of the platform
	 * @param shape    The shape dimensions of the platform for height calculation
	 * @param number   The horizontal offset value (-2 to 2 typically used for
	 *                 patterns)
	 * @param world    The game world in which to create the coin
	 */
	public static void makeCoins(Vec2 position, Vec2 shape, int number, World world) {
		Coin coin = new Coin(world);
		coin.setPosition(new Vec2(position.x - 0.3f - number, position.y + shape.y + 0.4f));
	}
}
