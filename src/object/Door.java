package object;

import city.cs.engine.*;
import entity.Player;
import org.jbox2d.common.Vec2;

/**
 * Door is a static object that requires a key to be opened.
 * When opened, it allows the player to pass through it.
 */
public class Door extends InteractiveStaticObject implements CollisionListener {

	// The game world in which the door exists
	private World gameWorld;

	// The physical door body
	private StaticBody doorBody;

	// Physical shape of the door
	private static final Shape DOOR_SHAPE = new BoxShape((float) (14.0 / 40.0), (float) (56.0 / 40.0));

	// Door state (true = open, false = closed)
	private boolean isOpen = false;

	/**
	 * Constructor for creating a new door
	 * 
	 * @param world    The game world in which the door exists
	 * @param position The position of the door in the world
	 */
	public Door(World world, Vec2 position) {
		super(world, DOOR_SHAPE);
		this.destroy();
		gameWorld = world;
		doorBody = new StaticBody(world, DOOR_SHAPE);
		doorBody.setPosition(position);
		close();
	}

	/**
	 * Opens the door, allowing the player to pass through it.
	 * Creates a ghostly fixture that doesn't block movement.
	 */
	public void open() {
		Vec2 position = doorBody.getPosition();
		doorBody.destroy();
		doorBody = new StaticBody(gameWorld);
		GhostlyFixture ghostlyFixture = new GhostlyFixture(doorBody, DOOR_SHAPE);
		doorBody.addImage(new BodyImage("resources/objects/interactive/door_open.png", (float) (57.0 / 20.0)));
		doorBody.setPosition(position);
		isOpen = true;
	}

	/**
	 * Closes the door, blocking the player's path.
	 * Creates a solid door that prevents movement through it.
	 */
	public void close() {
		Vec2 position = doorBody.getPosition();
		doorBody.destroy();
		doorBody = new StaticBody(gameWorld, DOOR_SHAPE);
		doorBody.addImage(new BodyImage("resources/objects/interactive/door_closed.png", (float) (56.0 / 20.0)));
		doorBody.setPosition(position);
		doorBody.addCollisionListener(this);
		isOpen = false;
	}

	/**
	 * Event that happens on each game step.
	 * Currently not used for doors.
	 */
	@Override
	public void eventStep() {
		// No action needed on each step for doors
	}

	/**
	 * Handles collision between the door and other bodies.
	 * If the player has a key, the door opens and the key is consumed.
	 * 
	 * @param collisionEvent The collision event containing contact information
	 */
	@Override
	public void collide(CollisionEvent collisionEvent) {
		if (collisionEvent.getOtherBody() instanceof Player && !isOpen) {
			Player player = (Player) collisionEvent.getOtherBody();
			if (player.hasKey) {
				open();
				player.hasKey = false;
			}
		}
	}
}
