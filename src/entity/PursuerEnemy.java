package entity;

import city.cs.engine.*;
import game.GameWorld;
import org.jbox2d.common.Vec2;
import service.GameAudioManager;

/**
 * PursuerEnemy is an enemy that actively pursues the player when they are
 * within detection range.
 * It tracks the player's position and adjusts its movement direction to follow
 * them.
 * When the player is out of range, it reverts to a standard patrol pattern.
 */
public class PursuerEnemy extends Enemy {

    // Physical shape of the pursuer enemy
    private static final Shape PURSUER_SHAPE = new BoxShape(0.8f, 0.8f);

    // Reference to the player for tracking
    private Player targetPlayer;

    // Detection range for pursuing player (horizontal)
    private static final float HORIZONTAL_DETECTION_RANGE = 6.0f;

    // Detection range for pursuing player (vertical)
    private static final float VERTICAL_DETECTION_RANGE = 5.0f;

    /**
     * Constructor for creating a new pursuer enemy
     * 
     * @param world  The game world in which the enemy exists
     * @param player The player that this enemy will pursue
     */
    public PursuerEnemy(World world, Player player) {
        super(world, PURSUER_SHAPE);
        this.addImage(new BodyImage("resources/enemies/enemy_follower.gif", 1.6f));
        targetPlayer = player;
        this.getImages().get(0).flipHorizontal();
        ACTION_INTERVAL = 140;
    }

    /**
     * Updates the enemy's state each game step:
     * - Keeps the enemy upright
     * - Checks if player is within detection range
     * - Pursues player if within range, otherwise moves in patrol pattern
     */
    @Override
    public void eventStep() {
        // Keep the enemy upright
        this.setAngle(0);

        // Check if player is within detection range
        float distanceX = Math.abs(this.getPosition().x - targetPlayer.getPosition().x);
        float distanceY = Math.abs(this.getPosition().y - targetPlayer.getPosition().y);

        if (distanceX <= HORIZONTAL_DETECTION_RANGE && distanceY <= VERTICAL_DETECTION_RANGE) {
            // Player is within range - pursue them
            if (this.getPosition().x > targetPlayer.getPosition().x) {
                // Player is to the left
                if (!isMovingRight) {
                    action();
                }
            } else {
                // Player is to the right
                if (isMovingRight) {
                    action();
                }
            }
        } else {
            // Player is out of range - normal patrol pattern
            actionTimer++;
            if (actionTimer >= ACTION_INTERVAL) {
                actionTimer = 0;
                action();
            }
        }

        // Set velocity based on movement direction
        if (!isMovingRight) {
            this.setLinearVelocity(new Vec2(movementSpeed, 0));
        } else {
            this.setLinearVelocity(new Vec2(-movementSpeed, 0));
        }
    }

    /**
     * Performs the enemy's action - turning around to change direction
     */
    @Override
    public void action() {
        this.turnAround();
    }

    /**
     * Handles what happens when this enemy is attacked
     * Destroys the enemy
     */
    @Override
    public void attacked() {
        this.destroy();
    }
}