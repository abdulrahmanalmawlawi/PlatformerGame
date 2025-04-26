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
 * 
 * Gameplay purposes of IntermittentPlatform:
 * - Creates precision timing challenges for players to test their reflexes
 * - Forces players to observe patterns before attempting jumps
 * - Introduces an element of risk/reward for collectibles placed near them
 * - Breaks up the rhythm of standard platforming sections
 * - Creates areas that can only be accessed during specific time windows
 * 
 * Implementation details:
 * - Uses a timer-based state system alternating between solid and non-existent
 * - Adjusts platform position based on camera movement to maintain level
 * coherence
 * - Provides visual feedback when platforms are present
 * - Complete removal of physics body when in ghost state for performance
 * 
 * These platforms are typically arranged in sequences that require the player
 * to time a series of jumps correctly, creating escalating difficulty patterns
 * that test both observation skills and execution timing.
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
	 * This constructor initializes a platform that:
	 * 1. Starts in the ghost (non-existent) state for predictable cycling
	 * 2. Tracks its fixed world position independent of physics body
	 * creation/destruction
	 * 3. Maintains references to the game world and camera for positioning
	 * 4. Creates an initial empty physics body as a placeholder
	 * 
	 * The platform begins in its ghost state to establish a clear pattern
	 * for the player to observe before attempting to traverse it.
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
	 * 
	 * This method:
	 * 1. Updates the current state to PLATFORM_STATE
	 * 2. Destroys any existing physics body
	 * 3. Creates a new solid physics body with proper collision
	 * 4. Adds the visual platform image to provide player feedback
	 * 5. Positions the platform correctly in screen space based on camera offset
	 * 
	 * The platform is positioned relative to the camera's position, ensuring
	 * it appears in the correct location on screen regardless of level scrolling.
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
	 * 
	 * This method:
	 * 1. Updates the current state to GHOST_STATE
	 * 2. Completely destroys the physics body to eliminate collision detection
	 * 
	 * Unlike other approaches that might use a ghostly fixture, this implementation
	 * fully removes the physics body when the platform disappears. This provides
	 * completely unobstructed movement through the platform's space and improves
	 * performance by removing unnecessary collision objects from the physics
	 * engine.
	 */
	public void makeGhostPlatform() {
		currentState = GHOST_STATE;
		platformBody.destroy();
	}

	/**
	 * Updates the platform's state on each game step.
	 * Handles the periodic appearance and disappearance of the platform.
	 * 
	 * This method is called automatically on each frame and:
	 * 1. Increments the state timer tracking elapsed game steps
	 * 2. Checks if the cycle interval has been reached
	 * 3. Toggles between solid and ghost states when the interval is reached
	 * 4. Resets the timer to restart the cycle
	 * 
	 * The fixed timing interval creates a predictable pattern that observant
	 * players can learn and time their jumps accordingly. The default cycle
	 * of 70 game steps is calibrated to give players enough time to react
	 * while still requiring precise timing for successful traversal.
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
