package entity;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.Shape;
import city.cs.engine.World;
import org.jbox2d.common.Vec2;

/**
 * TurretEnemy is a stationary enemy that fires projectiles at regular
 * intervals.
 * It remains fixed in position and periodically shoots bullets in a horizontal
 * direction.
 * This enemy is immune to player attacks and serves as a ranged hazard.
 */
public class TurretEnemy extends Enemy {

    // Define the physical shape of the turret
    private static final Shape TURRET_SHAPE = new BoxShape((float) (37.0 / 40.0), (float) (38.0 / 40.0));

    /**
     * Constructor for creating a new turret enemy
     * 
     * @param world The game world in which the turret exists
     */
    public TurretEnemy(World world) {
        super(world, TURRET_SHAPE);
        // High gravity scale ensures the turret stays firmly in place
        this.setGravityScale(1000);
        this.addImage(new BodyImage("resources/enemies/enemy_shooter.png", 2f));
        // Zero movement speed as turrets are stationary
        movementSpeed = 0;
    }

    /**
     * Handles what happens when the turret is attacked
     * The turret is immune to player attacks
     */
    @Override
    public void attacked() {
        // Turret is immune to attacks - no implementation needed
    }

    /**
     * Performs the turret's action - firing a bullet
     * Creates a new bullet that travels horizontally to the right
     */
    @Override
    public void action() {
        // Create bullet slightly to the right of the turret
        Vec2 bulletStartPosition = new Vec2(this.getPosition().x + 1, this.getPosition().y);
        boolean movingRight = true;
        Bullet bullet = new Bullet(this.getWorld(), bulletStartPosition, movingRight);
    }
}