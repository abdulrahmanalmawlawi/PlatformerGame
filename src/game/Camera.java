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
 * 
 * This implementation uses a unique approach to camera control:
 * Instead of moving a viewport within a world, it keeps the viewport fixed
 * and moves all objects in the world in the opposite direction to create
 * the illusion of camera movement. This approach simplifies integration with
 * the physics engine and user interface components.
 * 
 * Key features:
 * - Smooth tracking of the player character
 * - Boundary detection to prevent viewing areas outside the level
 * - Efficient movement of all game objects to create the scrolling effect
 * - Compatible with the CityEngine physics system
 */
public class Camera {

	// Current horizontal position of the camera in world coordinates
	// This represents how far the camera has moved from its starting position
	public float cameraPosition = 0f;

	// Desired horizontal position of the target body on screen
	// This determines where the player will appear in the view (default is center)
	public float targetBodyScreenPosition = 0f;

	// Total length of the level in world units
	// Used to prevent the camera from showing empty space beyond level boundaries
	public float levelLength = 100f;

	/**
	 * Updates the camera position to center on the target body.
	 * Moves all bodies in the world to create the scrolling effect.
	 * 
	 * This method is called every frame/step to:
	 * 1. Calculate how much the camera needs to move
	 * 2. Apply boundary constraints
	 * 3. Move all world objects to create the scrolling effect
	 * 
	 * The scrolling illusion is created by moving all objects in the
	 * world in the opposite direction of the "camera movement".
	 * 
	 * @param targetBody The body to center the camera on (usually the player)
	 * @param world      The game world containing all bodies
	 */
	public void updateCameraPosition(Body targetBody, World world) {
		// Get the current x-position of the target body
		float targetBodyX = targetBody.getPosition().x;

		// Calculate how much the camera needs to move to keep the target body
		// at the desired position on screen (typically center)
		float cameraShift = targetBodyScreenPosition - targetBodyX;

		// Ensure the camera doesn't show beyond the left boundary of the level (0)
		// If the camera would show negative coordinates, limit the shift
		if (cameraPosition - cameraShift < 0) {
			cameraShift = cameraPosition;
		}

		// Ensure the camera doesn't show beyond the right boundary of the level
		// 786f/20f represents the width of the view in world units (39.3 units)
		// If the camera would show beyond level length, limit the shift
		if (cameraPosition - cameraShift > levelLength - 786f / 20f) {
			cameraShift = cameraPosition - levelLength + 786f / 20f;
		}

		// Update the camera position tracking variable
		cameraPosition -= cameraShift;

		// Move all bodies in the world to create the scrolling effect
		// Instead of moving the "camera", we move everything in the world
		// in the opposite direction to create the illusion of camera movement

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
