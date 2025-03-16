package entity;

import game.GameWorld;
import org.jbox2d.common.Vec2;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.CollisionEvent;
import city.cs.engine.Shape;
import city.cs.engine.World;
import service.GameAudioManager;

/**
 * BasicPatrolEnemy is a simple enemy type that patrols back and forth.
 * It moves in a straight line, turning around when it encounters obstacles,
 * and can be defeated by the player's attacks.
 * 
 * This enemy serves as the most common and basic challenge in the game:
 * - Moves at a constant speed in a horizontal pattern
 * - Changes direction periodically or when hitting obstacles
 * - Can be defeated by jumping on top or attacking with the sword
 * - Awards points to the player upon defeat
 * 
 * The BasicPatrolEnemy is designed to introduce players to the core combat
 * mechanics of the game without being too challenging. It teaches players
 * about timing jumps and using attack moves effectively.
 */
public class BasicPatrolEnemy extends Enemy {

    // Size of the enemy (for collision shape)
    // This determines both visual size and hitbox dimensions
    private static final float ENEMY_SIZE = 0.8f;

    // Physical shape of the enemy
    // Using a box shape for simple and predictable collisions
    private static final Shape ENEMY_SHAPE = new BoxShape(ENEMY_SIZE, ENEMY_SIZE);

    /**
     * Creates a new basic patrolling enemy with default behavior and appearance.
     * 
     * The enemy is initialized with:
     * - A square collision box for consistent physics interactions
     * - Default movement speed inherited from the Enemy base class
     * - A looping animation that reflects its continuous movement
     * - Collision handling for interactions with the player and environment
     * 
     * @param world The game world in which the enemy exists
     */
    public BasicPatrolEnemy(World world) {
        super(world, ENEMY_SHAPE);
        this.addImage(new BodyImage("resources/enemies/enemy_basic.gif", 1.6f));
        this.addCollisionListener(this);
    }

    /**
     * Handles what happens when this enemy is attacked by the player.
     * 
     * When defeated:
     * - Increases the player's score by 5 points
     * - Destroys the enemy, removing it from the game world
     * - Implicitly triggers any physics updates from the removal
     * 
     * This method is called either when the player jumps on the enemy or
     * attacks it with the sword.
     */
    @Override
    public void attacked() {
        GameWorld.score += 5;
        this.destroy();
    }

    /**
     * Performs the enemy's periodic action - turning around to change direction.
     * 
     * This is triggered at regular intervals defined by ACTION_INTERVAL in the
     * Enemy base class. It creates a predictable patrol pattern that players
     * can learn and anticipate. The enemy will:
     * 1. Reverse its movement direction
     * 2. Flip its sprite horizontally to match the new direction
     * 
     * This method is automatically called by the eventStep() method in the
     * Enemy base class when the action timer reaches its threshold.
     */
    @Override
    public void action() {
        this.turnAround();
    }
}