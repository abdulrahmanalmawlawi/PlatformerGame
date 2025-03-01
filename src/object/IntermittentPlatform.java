package object;

import org.jbox2d.common.Vec2;

import game.Camera;
import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.StaticBody;
import city.cs.engine.World;

/**
 * IntermittentPlatform is a platform that periodically phases in and out of
 * existence.
 * It alternates between a solid platform state and a non-existent (ghost) state
 * at regular intervals, creating a timing challenge for the player.
 */
public class IntermittentPlatform extends InteractiveStaticObject {

	// Platform state constants
	private static final int GHOST_STATE = 0;
	private static final int PLATFORM_STATE = 1;

	// Time (in game steps) between state changes
	public int TIMER = 70;

	// Current timer value
	private int stateTimer = 0;

	// Current platform state
	private int currentState = GHOST_STATE;

	// The game world in which the platform exists
	private World gameWorld;

	// Camera for positioning the platform
	private Camera gameCamera;

	// The physical platform body
	private StaticBody platformBody;

	// Position of the platform in the world
	private Vec2 platformPosition;

	// Physical shape of the intermittent platform
	private static final BoxShape PLATFORM_SHAPE = new BoxShape(0.8f, 0.5f);

	/**
	 * Creates a new intermittent platform that phases in and out
	 * 
	 * @param world    The game world in which the platform exists
	 * @param position The position of the platform in the world
	 * @param camera   The camera for positioning the platform
	 */
	public IntermittentPlatform(World world, Vec2 position, Camera camera) {
		super(world, PLATFORM_SHAPE);
		this.destroy();
		platformPosition = position;
		gameWorld = world;
		gameCamera = camera;
		platformBody = new StaticBody(world);
		makeGhostPlatform();
	}

	/**
	 * Creates a solid platform at the specified position.
	 * The platform becomes visible and can be collided with.
	 */
	public void makeStaticPlatform() {
		currentState = PLATFORM_STATE;
		platformBody.destroy();
		platformBody = new StaticBody(gameWorld, PLATFORM_SHAPE);
		platformBody.addImage(new BodyImage("resources/objects/interactive/platform_disappearing.png", 1f));
		platformBody.setPosition(new Vec2(platformPosition.x - gameCamera.cameraPosition, platformPosition.y));
	}

	/**
	 * Removes the solid platform, making it disappear.
	 * The platform becomes non-existent and cannot be collided with.
	 */
	public void makeGhostPlatform() {
		currentState = GHOST_STATE;
		platformBody.destroy();
	}

	/**
	 * Updates the platform's state on each game step.
	 * Handles the periodic appearance and disappearance of the platform.
	 */
	@Override
	public void eventStep() {
		stateTimer++;

		if (stateTimer >= TIMER) {
			stateTimer = 0;
			if (currentState == GHOST_STATE) {
				makeStaticPlatform();
			} else {
				makeGhostPlatform();
			}
		}
	}
}
