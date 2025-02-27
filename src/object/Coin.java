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
 * A Coin is a pickup item that increases the player's score when
 * collected.
 * Coins can be placed individually or in a series.
 */
public class Coin extends GamePickup {

	// Physical shape of the coin
	private static final Shape COIN_SHAPE = new BoxShape(0.3f, 0.3f);

	/**
	 * Constructor for creating a new coin
	 * 
	 * @param world The game world in which the coin exists
	 */
	public Coin(World world) {
		super(world, COIN_SHAPE);
		this.addImage(new BodyImage("resources/collectibles/collectible_coin.png", 0.6f));
	}

	/**
	 * Action performed when the coin is collected by the player:
	 * - Increases the game score
	 * - Plays a coin sound
	 * - Destroys the coin
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
	 * Utility method to create a coin at a specified position
	 * 
	 * @param position The base position for the coin
	 * @param shape    The shape dimensions used for positioning
	 * @param number   The offset value for positioning
	 * @param world    The game world in which to create the coin
	 */
	public static void makeCoins(Vec2 position, Vec2 shape, int number, World world) {
		Coin coin = new Coin(world);
		coin.setPosition(new Vec2(position.x - 0.3f - number, position.y + shape.y + 0.4f));
	}
}
