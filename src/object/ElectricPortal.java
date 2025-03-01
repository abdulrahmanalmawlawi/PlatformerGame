package object;

import city.cs.engine.*;
import entity.Player;
import org.jbox2d.common.Vec2;

/**
 * ElectricPortal is a hazard that periodically switches between active and
 * inactive states.
 * When active, it damages and repels players who come into contact with it.
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
     * @param world The game world in which the portal exists
     */
    public ElectricPortal(World world) {
        super(world, PORTAL_SHAPE);
        this.addCollisionListener(this);
    }

    /**
     * Activates the electric state of the portal.
     * Makes the portal dangerous to touch and changes its appearance.
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
