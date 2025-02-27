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
 */
public class SwordPowerup extends StaticBody implements CollisionListener {

	// Physical shape of the sword power-up
	private static final BoxShape SWORD_SHAPE = new BoxShape((float) (43 / 40.0), (float) (41 / 40.0));

	/**
	 * Creates a new sword power-up in the game world
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
	 * When the player collides with the power-up:
	 * - Upgrades the player's abilities
	 * - Plays a collection sound
	 * - Removes the power-up from the game world
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
