package object;

import org.jbox2d.common.Vec2;

import game.Camera;
import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.GhostlyFixture;
import city.cs.engine.StaticBody;
import city.cs.engine.World;
import entity.Player;

/**
 * DelayedFallingPlatform is a platform that falls after the player stands on it
 * for a certain time.
 * It can be passed through from below but supports the player from above.
 * The platform has a timer that starts when the player stands on it, and after
 * a specified delay, the platform will fall downward.
 */
public class DelayedFallingPlatform extends InteractiveStaticObject implements CollisionListener {

	// Platform state constants
	private static final int GHOST_STATE = 0;
	private static final int PLATFORM_STATE = 1;

	// Current platform state
	private int currentState = GHOST_STATE;

	// Maximum time (in game steps) the player can stand on the platform before it
	// falls
	public int TIMER_STANDING = 10;

	// Current timer value (-1 means not counting)
	private int standingTimer = -1;

	// Whether the platform is currently falling
	private boolean isFalling = false;

	// Camera for positioning the platform
	private Camera gameCamera;

	// Position of the platform in the world
	private Vec2 platformPosition;

	// The game world in which the platform exists
	private World gameWorld;

	// The physical platform body
	private StaticBody platformBody;

	// Physical shape of the falling platform
	private static final BoxShape PLATFORM_SHAPE = new BoxShape(0.5f, 0.5f);

	// Vector that controls the falling speed and direction
	private final Vec2 FALLING_VECTOR = new Vec2(0, -0.2f);

	// Reference to the player for positioning logic
	private Player player;

	/**
	 * Creates a new delayed falling platform
	 * 
	 * @param world    The game world in which the platform exists
	 * @param position The position of the platform in the world
	 * @param player   The player character for positioning logic
	 * @param camera   The camera for positioning the platform
	 */
	public DelayedFallingPlatform(World world, Vec2 position, Player player, Camera camera) {
		super(world, PLATFORM_SHAPE);
		this.destroy();
		platformPosition = position;
		gameWorld = world;
		this.player = player;
		gameCamera = camera;
		makeGhostPlatform();
	}

	/**
	 * Creates a solid platform that the player can stand on.
	 * The platform becomes visible and can be collided with from above.
	 */
	public void makeStaticPlatform() {
		currentState = PLATFORM_STATE;
		platformBody = new StaticBody(gameWorld, PLATFORM_SHAPE);
		platformBody.addImage(new BodyImage("resources/objects/decorative/prop_log_horizontal.png", 1f));
		platformBody.addCollisionListener(this);
		platformBody.setPosition(new Vec2(platformPosition.x - gameCamera.cameraPosition, platformPosition.y));
	}

	/**
	 * Creates a ghost platform that the player can pass through.
	 * The platform is visible but doesn't block movement.
	 */
	public void makeGhostPlatform() {
		currentState = GHOST_STATE;
		platformBody = new StaticBody(gameWorld);
		GhostlyFixture ghostlyFixture = new GhostlyFixture(platformBody, PLATFORM_SHAPE);
		platformBody.addImage(new BodyImage("resources/objects/decorative/prop_log_horizontal.png", 1f));
		platformBody.setPosition(new Vec2(platformPosition.x - gameCamera.cameraPosition, platformPosition.y));
	}

	/**
	 * Initiates the falling motion of the platform.
	 * Converts the platform to a ghost state and sets it to fall.
	 */
	public void startFalling() {
		makeGhostPlatform();
		isFalling = true;
	}

	/**
	 * Updates the platform's state on each game step.
	 * Handles platform falling, player positioning logic, and timer management.
	 */
	@Override
	public void eventStep() {
		if (isFalling) {
			// Move the platform downward
			platformBody.move(FALLING_VECTOR);

			// Reset the platform when it falls out of view
			if (platformBody.getPosition().y <= -25f) {
				platformBody.destroy();
				makeGhostPlatform();
				isFalling = false;
			}
		} else {
			// If the player is above the platform, make it solid
			// Otherwise, make it ghostly so the player can jump through it
			if (player.getPosition().y - 1.5 > platformPosition.y && currentState != PLATFORM_STATE) {
				platformBody.destroy();
				makeStaticPlatform();
			} else if (player.getPosition().y - 1.5 <= platformPosition.y && currentState != GHOST_STATE) {
				platformBody.destroy();
				makeGhostPlatform();
			}
		}

		// Handle the timer for platform falling
		if (standingTimer >= 0) {
			standingTimer++;
			if (standingTimer >= TIMER_STANDING) {
				standingTimer = -1;
				platformBody.destroy();
				startFalling();
			}
		}
	}

	/**
	 * Handles collision between the platform and other bodies.
	 * Starts the falling timer when the player stands on the platform.
	 * 
	 * @param collisionEvent The collision event containing contact information
	 */
	@Override
	public void collide(CollisionEvent collisionEvent) {
		// Start the falling timer when the player collides with the platform
		if (standingTimer == -1 && collisionEvent.getOtherBody() instanceof Player) {
			standingTimer = 0;
		}
	}
}
