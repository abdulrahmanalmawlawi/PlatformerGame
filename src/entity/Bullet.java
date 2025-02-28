package entity;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

/**
 * A Bullet is a dynamic object that travels in a specific direction (left or
 * right).
 * Collision with the player causes damage and applies an impulse force.
 * The bullet is destroyed upon collision with any object except the turret
 * that fired it.
 */
public class Bullet extends DynamicBody implements CollisionListener {

    // Define the physical shape of the bullet
    private static final Shape BULLET_SHAPE = new BoxShape(0.2f, 0.2f);

    // Bullet movement speed
    private static final float BULLET_VELOCITY = 15.0f;

    // Force applied to player when hit
    private static final float IMPACT_FORCE = 20.0f;

    /**
     * Constructor for creating a new bullet
     * 
     * @param world         The game world in which the bullet exists
     * @param startPosition The initial position of the bullet
     * @param movingRight   If true, the bullet moves right; if false, it moves left
     */
    public Bullet(World world, Vec2 startPosition, boolean movingRight) {
        super(world, BULLET_SHAPE);
        this.setPosition(startPosition);
        this.addImage(new BodyImage("resources/projectiles/shooter_bullet.png", 0.4f));
        this.addCollisionListener(this);

        // Set bullet velocity based on direction
        if (movingRight) {
            this.setLinearVelocity(new Vec2(BULLET_VELOCITY, 0));
        } else {
            this.setLinearVelocity(new Vec2(-BULLET_VELOCITY, 0));
        }

        // Disable gravity effect on bullet
        this.setGravityScale(0);
    }

    /**
     * Handles collision events between the bullet and other bodies
     * 
     * @param collisionEvent The collision event data
     */
    @Override
    public void collide(CollisionEvent collisionEvent) {
        if (collisionEvent.getOtherBody() instanceof Player) {
            // When bullet hits player:
            // 1. Player takes damage
            // 2. Apply force to player in bullet's direction
            // 3. Destroy the bullet
            Player hitPlayer = (Player) collisionEvent.getOtherBody();
            hitPlayer.attacked();

            // Apply impulse in the direction the bullet was traveling
            if (this.getLinearVelocity().x > 0) {
                hitPlayer.applyImpulse(new Vec2(IMPACT_FORCE, 0));
            } else {
                hitPlayer.applyImpulse(new Vec2(-IMPACT_FORCE, 0));
            }

            this.destroy();
        } else if (!(collisionEvent.getOtherBody() instanceof TurretEnemy)) {
            // Destroy bullet on collision with any object except the turret
            this.destroy();
        }
    }
}