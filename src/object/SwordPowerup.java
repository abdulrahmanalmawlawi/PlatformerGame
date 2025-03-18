package object;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.StaticBody;
import city.cs.engine.World;
import entity.Player;
import game.GameWorld;
import service.GameAudioManager;

/**
 * SwordPowerup is a collectible weapon power-up that enhances the player's
 * combat abilities.
 * When collected, it gives the player the ability to attack enemies and break
 * certain obstacles.
 * The power-up is automatically activated upon collision with the player.
 * 
 * The sword is a key gameplay-changing item that serves multiple purposes:
 * - Acts as a progression gate, unlocking new areas and enemy-defeating
 * abilities
 * - Transforms the player's approach to combat from avoidance/jumping to direct
 * engagement
 * - Provides new strategic options for dealing with otherwise dangerous enemies
 * - Serves as a visual indicator of the player's increased capabilities
 * 
 * Finding and collecting the sword is often a major milestone in level
 * progression,
 * as it fundamentally changes how the player interacts with the game world and
 * encourages more offensive rather than defensive play patterns.
 */
public class SwordPowerup extends StaticBody implements CollisionListener {

	// Physical shape of the sword power-up
	// Sized to be visually prominent and easily noticed by players
	private static final BoxShape SWORD_SHAPE = new BoxShape((float) (43 / 40.0), (float) (41 / 40.0));

	/**
	 * Creates a new sword power-up in the game world.
	 * 
	 * The power-up is initialized with:
	 * - A distinctive visual appearance that stands out in the environment
	 * - A slightly larger collision box than standard pickups for easier collection
	 * - Automatic collision detection to trigger when the player makes contact
	 * - Static body type ensuring it doesn't move due to physics interactions
	 * 
	 * The power-up is typically placed in strategic locations that require
	 * some exploration or overcoming challenges to reach, making its
	 * acquisition feel rewarding to the player.
	 * 
	 * @param world The game world in which the power-up exists
	 */
	public SwordPowerup(World world) {
		super(world, SWORD_SHAPE);
		this.addImage(new BodyImage("resources/objects/interactive/item_sword_power.png", (float) (83.0 / 20.0)));
		this.addCollisionListener(this);
	}

	/**
	 * Handles collision between the sword power-up and other bodies.
	 * 
	 * When the player collides with the power-up, it triggers several effects:
	 * - Calls the player's upgrade() method, enabling sword attack capabilities
	 * - Changes player sprite to show the sword visually in animations
	 * - Plays a distinctive power-up sound for audio feedback
	 * - Removes itself from the world to prevent multiple collections
	 * 
	 * The upgrade is permanent for the duration of the level and affects
	 * all player animations and interactions going forward. After collecting
	 * the sword, the player can press the attack key to perform sword attacks,
	 * which can defeat all types of enemies including those that can't be
	 * defeated by jumping on them (like SpikedEnemy).
	 * 
	 * @param collisionEvent The collision event containing contact information
	 */
	@Override
	public void collide(CollisionEvent collisionEvent) {
		if (collisionEvent.getOtherBody() instanceof Player) {
			Player player = (Player) collisionEvent.getOtherBody();
			player.upgrade();
			this.destroy();
			GameWorld.sound.setFile(GameAudioManager.COIN);
			GameWorld.sound.play();
		}
	}
}
