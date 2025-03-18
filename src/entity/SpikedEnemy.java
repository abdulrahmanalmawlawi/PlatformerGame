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
 * SpikedEnemy is a hazardous enemy covered in spikes that damage the player
 * from all directions.
 * Unlike regular enemies, it cannot be safely jumped on from above,
 * making it more dangerous and requiring the player to avoid it completely or
 * attack it.
 *
 * This enemy type serves several important roles in the game's progression:
 * - Introduces the concept that not all enemies follow the same rules
 * - Forces players to use attack mechanics rather than simple jumping
 * - Creates no-go areas that must be navigated around or approached carefully
 * - Teaches awareness of different enemy types through distinct visual design
 * 
 * The SpikedEnemy is a significant step up in difficulty from the
 * BasicPatrolEnemy,
 * requiring players to have mastered the sword attack mechanic or develop
 * advanced
 * avoidance strategies to progress through areas containing these enemies.
 */
public class SpikedEnemy extends Enemy {

    // Size of the enemy (for collision shape)
    // Standard size matches other basic enemies for consistent gameplay
    private static final float ENEMY_SIZE = 0.8f;

    // Physical shape of the enemy
    // Square collision box clearly defines the hazardous area
    private static final Shape ENEMY_SHAPE = new BoxShape(ENEMY_SIZE, ENEMY_SIZE);

    /**
     * Creates a new spiked enemy that damages the player from all directions.
     * 
     * The enemy is initialized with:
     * - The standard enemy collision shape for predictable interactions
     * - A distinctive spiked appearance to signal danger to the player
     * - Default patrol movement behavior inherited from the Enemy base class
     * - Custom collision handling that makes it dangerous from all directions
     * 
     * The sprite is intentionally flipped horizontally at creation to ensure
     * the initial movement direction matches the sprite orientation.
     * 
     * @param world The game world in which the enemy exists
     */
    public SpikedEnemy(World world) {
        super(world, ENEMY_SHAPE);
        this.addImage(new BodyImage("resources/enemies/enemy_spike.gif", 1.6f));
        this.getImages().get(0).flipHorizontal();
    }

    /**
     * Handles what happens when this enemy is attacked by the player.
     * 
     * Despite its dangerous spikes, this enemy can still be defeated
     * when the player uses the sword attack:
     * - Increases the player's score by 5 points (same as basic enemy)
     * - Destroys the enemy, removing it from the game world
     * 
     * This creates a risk-reward balance: the enemy is more dangerous than
     * basic types but provides the same score reward, encouraging players
     * to avoid rather than confront when possible.
     */
    @Override
    public void attacked() {
        GameWorld.score += 5;
        this.destroy();
    }

    /**
     * Performs the enemy's periodic action - turning around to change direction.
     * 
     * Unlike BasicPatrolEnemy, this method directly modifies the movement direction
     * and sprite orientation rather than calling the parent class's turnAround()
     * method.
     * This is functionally equivalent but implemented differently as an example of
     * alternative implementation patterns.
     */
    @Override
    public void action() {
        isMovingRight = !isMovingRight;
        this.getImages().get(0).flipHorizontal();
    }

    /**
     * Overrides the default collision handling for non-attacking players.
     * 
     * This is the key difference between the SpikedEnemy and other enemies:
     * - When jumped on from above, the player still takes damage (not safe to
     * stomp)
     * - Side collisions behave similarly to other enemies (player takes damage)
     * - All collisions result in a bounce effect to push the player away
     * 
     * This method significantly changes the gameplay dynamic by removing the
     * safe approach option (jumping on from above) that works for basic enemies,
     * forcing the player to either use the sword attack or avoid the enemy
     * entirely.
     * 
     * @param player             The player that collided with this enemy
     * @param collisionDirection -1 if collision from right, 1 if from left, 0 if
     *                           vertical collision
     */
    @Override
    public void handleCollisionWithNonAttackingPlayer(Player player, int collisionDirection) {
        if (collisionDirection == 0) {
            // Player jumped on enemy from above - still takes damage due to spikes
            player.applyImpulse(new Vec2(0, 150 / 8));
            player.attacked();
        } else {
            // Player collided with enemy from the side
            float playerVelocityX = player.getLinearVelocity().x;
            this.applyImpulse(new Vec2(-playerVelocityX * 5, 0));
            player.applyImpulse(new Vec2(collisionDirection * 150 / 8, 100 / 8));
            player.attacked();
        }
    }
}