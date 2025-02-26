package entity;

import org.jbox2d.common.Vec2;

import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.DynamicBody;
import city.cs.engine.Shape;
import city.cs.engine.World;

/**
 * PhysicsActor is an abstract class representing a physics-based entity that
 * can move and interact
 * in the game world. It provides common functionality for movement, collision
 * handling, and
 * physics-based behavior for game entities like players and enemies.
 * 
 * This class handles:
 * - Directional movement (left/right)
 * - Jumping mechanics
 * - Physics-based deceleration
 * - Collision detection with surfaces
 */
public abstract class PhysicsActor extends DynamicBody implements CollisionListener {

    // Movement direction constants
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    public static final int UP = 2;
    public static final int NO_DIRECTION = -1;

    // Movement vectors for different directions
    protected Vec2 rightImpulse; // Force applied to move right
    protected Vec2 leftImpulse; // Force applied to move left
    protected Vec2 slowRightImpulse; // Force applied to slow down when moving left
    protected Vec2 slowLeftImpulse; // Force applied to slow down when moving right
    protected Vec2 jumpImpulse; // Force applied to jump

    // Legacy variable names for backward compatibility
    protected Vec2 x_right; // Same as rightImpulse
    protected Vec2 x_left; // Same as leftImpulse
    protected Vec2 x_slow_right; // Same as slowRightImpulse
    protected Vec2 x_slow_left; // Same as slowLeftImpulse
    protected Vec2 impulse_jump; // Same as jumpImpulse

    // Maximum horizontal velocity
    protected int maxVelocity;
    protected int max_velocity; // Legacy variable for backward compatibility

    // Movement state flags
    protected boolean canJump = false; // Whether the object can currently jump
    protected boolean facingRight = false; // Direction the object is facing (true = right, false = left)

    // Legacy variable names for backward compatibility
    protected boolean jump = false; // Same as canJump
    protected boolean side = false; // Same as facingRight

    // Last movement direction (-1 = no movement, 0 = right, 1 = left)
    protected int lastMoveDirection = -1;
    protected int move = -1; // Legacy variable for backward compatibility

    /**
     * Constructor for creating a new physics actor
     * 
     * @param world The game world in which the actor exists
     * @param shape The physical shape of the actor
     */
    public PhysicsActor(World world, Shape shape) {
        super(world, shape);
        this.addCollisionListener(this);
    }

    /**
     * Handles collision events, particularly with the ground.
     * When the actor collides with a surface below it, enables jumping.
     * 
     * @param collisionEvent The collision event containing contact information
     */
    @Override
    public void collide(CollisionEvent collisionEvent) {
        // If collision normal points downward, we're standing on something
        if (collisionEvent.getNormal().y <= -0.4) {
            canJump = true;
            jump = true; // Update legacy variable
        }
    }

    /**
     * Synchronizes the old and new variable names.
     * This ensures that regardless of which set of variables is initialized,
     * both sets will have the same values.
     */
    private void syncMovementVariables() {
        // Sync movement vectors
        if (x_right != null && rightImpulse == null) {
            rightImpulse = x_right;
        } else if (rightImpulse != null && x_right == null) {
            x_right = rightImpulse;
        }

        if (x_left != null && leftImpulse == null) {
            leftImpulse = x_left;
        } else if (leftImpulse != null && x_left == null) {
            x_left = leftImpulse;
        }

        if (x_slow_right != null && slowRightImpulse == null) {
            slowRightImpulse = x_slow_right;
        } else if (slowRightImpulse != null && x_slow_right == null) {
            x_slow_right = slowRightImpulse;
        }

        if (x_slow_left != null && slowLeftImpulse == null) {
            slowLeftImpulse = x_slow_left;
        } else if (slowLeftImpulse != null && x_slow_left == null) {
            x_slow_left = slowLeftImpulse;
        }

        if (impulse_jump != null && jumpImpulse == null) {
            jumpImpulse = impulse_jump;
        } else if (jumpImpulse != null && impulse_jump == null) {
            impulse_jump = jumpImpulse;
        }

        // Sync maximum velocity
        if (max_velocity != 0 && maxVelocity == 0) {
            maxVelocity = max_velocity;
        } else if (maxVelocity != 0 && max_velocity == 0) {
            max_velocity = maxVelocity;
        }
    }

    /**
     * Applies movement forces based on the specified direction.
     * Handles right/left movement, jumping, and deceleration.
     * 
     * @param direction The direction to move (RIGHT, LEFT, UP, or NO_DIRECTION)
     */
    public void move(int direction) {
        // Ensure variables are synchronized
        syncMovementVariables();

        switch (direction) {
            case RIGHT:
                facingRight = true;
                side = true; // Update legacy variable
                lastMoveDirection = 0;
                move = 0; // Update legacy variable

                // Apply right impulse if below maximum velocity
                if (this.getLinearVelocity().x <= maxVelocity) {
                    this.applyImpulse(x_right); // Use legacy variable for consistency
                }
                break;

            case LEFT:
                facingRight = false;
                side = false; // Update legacy variable
                lastMoveDirection = 1;
                move = 1; // Update legacy variable

                // Apply left impulse if below maximum velocity
                if (this.getLinearVelocity().x >= -maxVelocity) {
                    this.applyImpulse(x_left); // Use legacy variable for consistency
                }
                break;

            case UP:
                // Apply jump impulse if able to jump and not already moving upward too fast
                if (canJump && this.getLinearVelocity().y <= 1) {
                    this.applyImpulse(impulse_jump); // Use legacy variable for consistency
                    canJump = false;
                    jump = false; // Update legacy variable
                }
                break;

            case NO_DIRECTION:
                // Apply slowing forces when no direction is pressed
                lastMoveDirection = -1;
                move = -1; // Update legacy variable

                if (this.getLinearVelocity().x >= 1) {
                    this.applyImpulse(x_slow_left); // Use legacy variable for consistency
                } else if (this.getLinearVelocity().x <= -1) {
                    this.applyImpulse(x_slow_right); // Use legacy variable for consistency
                }
                break;
        }
    }

    /**
     * Updates the actor's state on each game step.
     * Disables jumping if the actor is moving vertically (in the air).
     */
    public void eventStep() {
        if (Math.abs(this.getLinearVelocity().y) > 1) {
            canJump = false;
            jump = false; // Update legacy variable
        }
    }
}