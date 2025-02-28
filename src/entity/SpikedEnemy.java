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
 */
public class SpikedEnemy extends Enemy {

    // Size of the enemy (for collision shape)
    private static final float ENEMY_SIZE = 0.8f;

    // Physical shape of the enemy
    private static final Shape ENEMY_SHAPE = new BoxShape(ENEMY_SIZE, ENEMY_SIZE);

    /**
     * Creates a new spiked enemy that damages the player from all directions
     * 
     * @param world The game world in which the enemy exists
     */
    public SpikedEnemy(World world) {
        super(world, ENEMY_SHAPE);
        this.addImage(new BodyImage("resources/enemies/enemy_spike.gif", 1.6f));
        this.getImages().get(0).flipHorizontal();
    }

    /**
     * Handles what happens when this enemy is attacked
     * Increases the player's score and destroys the enemy
     */
    @Override
    public void attacked() {
        GameWorld.score += 5;
        this.destroy();
    }

    /**
     * Performs the enemy's action - turning around to change direction
     */
    @Override
    public void action() {
        isMovingRight = !isMovingRight;
        this.getImages().get(0).flipHorizontal();
    }

    /**
     * Overrides the default collision handling for non-attacking players
     * Unlike regular enemies, this enemy damages the player even when jumped on
     * from above due to its spikes
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