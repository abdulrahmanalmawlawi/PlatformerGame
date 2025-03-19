package object;

import city.cs.engine.*;
import entity.Player;
import org.jbox2d.common.Vec2;

/**
 * Door is a static object that requires a key to be opened.
 * When opened, it allows the player to pass through it.
 * 
 * Doors serve as progression gates in the game, blocking access to new areas
 * until the player finds and collects the corresponding key. This creates:
 * - Exploration-based puzzles where players must find keys
 * - Sequential progression through level sections
 * - Controlled pacing of gameplay challenges
 * - Backtracking opportunities for experienced players
 * 
 * Door implementation features:
 * - State-based design (open/closed states with different physics)
 * - Automatic interaction with player when they have a key
 * - Visual feedback through different sprites for each state
 * - Collision detection to trigger interaction
 * 
 * The door uses a ghostly fixture when open to allow the player to pass through
 * while still maintaining a visual presence in the game world.
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
	 * This constructor initializes the door in a closed state. It:
	 * 1. Creates the door with the standard door shape
	 * 2. Destroys the initial physics body created by the parent class
	 * 3. Creates a new static body for the door
	 * 4. Positions the door at the specified location
	 * 5. Calls close() to set up the appropriate physics and visuals
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
	 * 
	 * This method performs several important steps:
	 * 1. Preserves the door's current position
	 * 2. Destroys the solid physics body
	 * 3. Creates a new body with a ghostly fixture that doesn't block movement
	 * 4. Applies the open door visual appearance
	 * 5. Restores the door to its original position
	 * 6. Updates the door's internal state to "open"
	 * 
	 * The ghostly fixture allows other objects to pass through the door
	 * while still maintaining its visual presence in the game.
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
	 * 
	 * This method performs several important steps:
	 * 1. Preserves the door's current position
	 * 2. Destroys any existing physics body
	 * 3. Creates a new solid static body that blocks movement
	 * 4. Applies the closed door visual appearance
	 * 5. Restores the door to its original position
	 * 6. Adds a collision listener to detect player contact
	 * 7. Updates the door's internal state to "closed"
	 * 
	 * The solid physics body prevents the player from passing through
	 * and triggers collision events when the player contacts it.
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
	 * 
	 * Unlike other interactive objects such as platforms that might change state
	 * over time, doors only change state when the player interacts with them
	 * while having a key. Therefore, no action is needed on each step.
	 */
	@Override
	public void eventStep() {
		// No action needed on each step for doors
	}

	/**
	 * Handles collision between the door and other bodies.
	 * 
	 * This method is automatically called by the physics engine when any object
	 * collides with the door. It performs these checks:
	 * 1. Verifies if the colliding object is the player
	 * 2. Checks if the door is currently closed
	 * 3. Tests if the player has a key
	 * 
	 * If all conditions are met:
	 * - The door opens (changing both physics and visuals)
	 * - The player's key is consumed (hasKey set to false)
	 * 
	 * This creates an automatic, intuitive interaction where simply touching
	 * the door with a key in inventory opens it without requiring explicit
	 * player commands.
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
