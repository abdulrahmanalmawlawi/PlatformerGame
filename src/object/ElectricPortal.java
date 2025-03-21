package object;

import city.cs.engine.*;
import entity.Player;
import org.jbox2d.common.Vec2;

/**
 * ElectricPortal is a hazard that periodically switches between active and
 * inactive states.
 * When active, it damages and repels players who come into contact with it.
 * 
 * ElectricPortal serves multiple gameplay purposes:
 * - Creates timing-based challenge sections where players must pass through
 * during inactive phases
 * - Adds dynamic hazards to otherwise static level designs
 * - Punishes player mistakes with health damage and movement disruption
 * - Teaches players to observe patterns and anticipate danger
 * - Creates forced waiting sections that break up the pace of levels
 * 
 * The implementation uses several technical approaches:
 * - State-based design (electric/active vs normal/inactive)
 * - Timer-driven state transitions creating a predictable pattern
 * - Physics-based player repulsion using impulse forces
 * - Visual feedback through different sprites for each state
 * - Transition between solid and ghostly fixtures based on state
 * 
 * ElectricPortals are typically placed in narrow passageways where they cannot
 * be
 * easily bypassed, forcing the player to time their approach correctly.
 */
public class ElectricPortal extends InteractiveStaticObject implements CollisionListener {

    // Time (in game steps) between state changes
    private static final int STATE_CHANGE_INTERVAL = 50;

    // Current timer value
    private int stateTimer;

    // Current portal state (true = electric/active, false = normal/inactive)
    private boolean isElectric;

    // Physical shape of the electric portal
    private static final Shape PORTAL_SHAPE = new BoxShape((float) (14.0 / 20.0), (float) (56.0 / 20.0));

    /**
     * Constructor for creating a new electric portal
     * 
     * Initializes a new electric portal with:
     * - Default inactive state (safe to pass through)
     * - Standard portal shape for collision detection
     * - Collision listener for player interaction
     * - Timer set to 0, beginning the cycle immediately
     * 
     * Electric portals start in the inactive state by default to give
     * players a clear indication of their position before becoming dangerous.
     * 
     * @param world The game world in which the portal exists
     */
    public ElectricPortal(World world) {
        super(world, PORTAL_SHAPE);
        this.addCollisionListener(this);
    }

    /**
     * Activates the electric state of the portal.
     * Makes the portal dangerous to touch and changes its appearance.
     * 
     * This method:
     * 1. Updates the internal state to electric/active
     * 2. Changes the visual appearance to the active (dangerous) sprite
     * 3. Replaces the current fixture with a solid one that blocks movement
     * 4. Enables the damage and repulsion effects on contact
     * 
     * The active state is visually distinctive with electrical effects to
     * provide clear feedback to players about the current danger level.
     */
    public void activateElectric() {
        isElectric = true;
        this.removeAllImages();
        this.addImage(new BodyImage("resources/objects/interactive/portal_electric_active.png", (float) (56.0 / 20.0)));
        this.getFixtureList().get(0).destroy();
        SolidFixture solidFixture = new SolidFixture(this, PORTAL_SHAPE);
    }

    /**
     * Deactivates the electric state of the portal.
     * Makes the portal safe to pass through and changes its appearance.
     * 
     * This method:
     * 1. Updates the internal state to normal/inactive
     * 2. Changes the visual appearance to the inactive (safe) sprite
     * 3. Replaces the current fixture with a ghostly one that allows movement
     * through it
     * 4. Disables the damage and repulsion effects
     * 
     * The inactive state allows players to safely pass through the portal,
     * creating a window of opportunity for progression.
     */
    public void deactivateElectric() {
        isElectric = false;
        this.removeAllImages();
        this.addImage(
                new BodyImage("resources/objects/interactive/portal_electric_inactive.png", (float) (56.0 / 20.0)));
        this.getFixtureList().get(0).destroy();
        GhostlyFixture ghostlyFixture = new GhostlyFixture(this, PORTAL_SHAPE);
    }

    /**
     * Updates the portal's state on each game step.
     * Handles the periodic activation and deactivation of the electric state.
     * 
     * This method is called automatically on each frame and:
     * 1. Increments the state timer tracking elapsed game steps
     * 2. Checks if the state change interval has been reached
     * 3. Toggles between active and inactive states when the interval is reached
     * 4. Resets the timer to restart the cycle
     * 
     * The fixed timing interval creates a predictable pattern that players
     * can learn and anticipate, making the obstacle challenging but fair.
     */
    @Override
    public void eventStep() {
        stateTimer++;
        if (stateTimer >= STATE_CHANGE_INTERVAL) {
            stateTimer = 0;
            if (!isElectric) {
                activateElectric();
            } else {
                deactivateElectric();
            }
        }
    }

    /**
     * Handles collision between the portal and other bodies.
     * When active, damages the player and applies a repulsive force.
     * 
     * This method:
     * 1. Checks if the portal is in its electric/active state
     * 2. Verifies if the colliding object is the player
     * 3. If both conditions are met:
     * - Applies a repulsive force in the opposite direction of player movement
     * - Calls the player's attacked() method to reduce health
     * 
     * The repulsive force serves both as a gameplay mechanic (pushing the player
     * away from danger) and as physics-based feedback that creates a more
     * dynamic feel to the interaction than simply applying damage.
     * 
     * @param collisionEvent The collision event containing contact information
     */
    @Override
    public void collide(CollisionEvent collisionEvent) {
        if (isElectric) {
            // If the portal is active and collides with the player,
            // apply a repulsive force and damage the player
            if (collisionEvent.getOtherBody() instanceof Player) {
                Player player = (Player) collisionEvent.getOtherBody();

                // Apply repulsive force in the opposite direction of player's movement
                if (player.getLinearVelocity().x > 0) {
                    player.applyImpulse(new Vec2(-50, 0));
                } else {
                    player.applyImpulse(new Vec2(50, 0));
                }

                // Damage the player
                player.attacked();
            }
        }
    }
}
