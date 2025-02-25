package game;

import org.jbox2d.common.Vec2;

import city.cs.engine.Body;
import city.cs.engine.DynamicBody;
import city.cs.engine.StaticBody;
import city.cs.engine.World;

/**
 * Camera class that handles the scrolling of the game view.
 * It follows a target body (usually the player) and ensures the view stays
 * within the level boundaries.
 */
public class Camera {

	// Current horizontal position of the camera
	public float cameraPosition = 0f;

	// Desired horizontal position of the target body on screen
	public float targetBodyScreenPosition = 0f;

	// Total length of the level
	public float levelLength = 100f;

	/**
	 * Updates the camera position to center on the target body.
	 * Moves all bodies in the world to create the scrolling effect.
	 * 
	 * @param targetBody The body to center the camera on (usually the player)
	 * @param world      The game world containing all bodies
	 */
	public void updateCameraPosition(Body targetBody, World world) {
		// Get the current x-position of the target body
		float targetBodyX = targetBody.getPosition().x;

		// Calculate how much the camera needs to move
		float cameraShift = targetBodyScreenPosition - targetBodyX;

		// Ensure the camera doesn't show beyond the left boundary of the level
		if (cameraPosition - cameraShift < 0) {
			cameraShift = cameraPosition;
		}

		// Ensure the camera doesn't show beyond the right boundary of the level
		// 786f/20f represents the width of the view in world units
		if (cameraPosition - cameraShift > levelLength - 786f / 20f) {
			cameraShift = cameraPosition - levelLength + 786f / 20f;
		}

		// Update the camera position
		cameraPosition -= cameraShift;

		// Move all bodies in the world to create the scrolling effect
		// Static bodies (platforms, obstacles, etc.)
		for (StaticBody staticBody : world.getStaticBodies()) {
			staticBody.move(new Vec2(cameraShift, 0));
		}

		// Dynamic bodies (players, enemies, etc.)
		for (DynamicBody dynamicBody : world.getDynamicBodies()) {
			dynamicBody.move(new Vec2(cameraShift, 0));
		}
	}
}
