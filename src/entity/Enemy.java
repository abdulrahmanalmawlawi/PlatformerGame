package entity;

import game.GameWorld;
import org.jbox2d.common.Vec2;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.DynamicBody;
import city.cs.engine.Shape;
import city.cs.engine.World;
import service.GameAudioManager;

/**
 * Enemy is an abstract class from which all the enemies in the game inherit.
 * It provides common functionality for enemy movement, collision handling,
 * and interaction with the player.
 */
public abstract class Enemy extends PhysicsActor implements CollisionListener {

    // Time interval between enemy actions (in game steps)
    protected int ACTION_INTERVAL = 70;

    // Timer to track when to perform actions
    protected int actionTimer = 0;

    // Movement speed of the enemy
    protected int movementSpeed = 5;

    // Direction of enemy movement (true = right, false = left)
    protected boolean isMovingRight = false;

    /**
     * Constructor for creating a new enemy
     * 
     * @param world The game world in which the enemy exists
     * @param shape The physical shape of the enemy
     */
    public Enemy(World world, Shape shape) {
        super(world, shape);
        this.addCollisionListener(this);
    }

    /**
     * Defines how the enemy reacts when attacked by the player
     * Must be implemented by subclasses
     */
    public abstract void attacked();

    /**
     * Defines special actions the enemy can perform (e.g., jumping, shooting)
     * Must be implemented by subclasses
     */
    public abstract void action();

    /**
     * Updates the enemy's state each game step:
     * 1. Increments the action timer
     * 2. Performs the action when timer reaches the interval
     * 3. Maintains constant horizontal movement speed
     */
    @Override
    public void eventStep() {
        actionTimer++;
        if (actionTimer >= ACTION_INTERVAL) {
            actionTimer = 0;
            action();
        }

        // Set horizontal velocity based on movement direction
        if (!isMovingRight) {
            this.setLinearVelocity(new Vec2(movementSpeed, this.getLinearVelocity().y));
        } else {
            this.setLinearVelocity(new Vec2(-movementSpeed, this.getLinearVelocity().y));
        }
    }

    /**
     * Handles collision between player and enemy when player is not attacking
     * 
     * @param player             The player that collided with this enemy
     * @param collisionDirection -1 if collision from right, 1 if from left, 0 if
     *                           vertical collision
     */
    public void handleCollisionWithNonAttackingPlayer(Player player, int collisionDirection) {
        if (collisionDirection == 0) {
            // Player jumped on enemy from above
            this.attacked();
            player.applyImpulse(new Vec2(0, 150 / 8));
            GameWorld.sound.setFile(GameAudioManager.BOUNCE);
            GameWorld.sound.play();
        } else {
            // Player collided with enemy from the side
            float playerVelocityX = player.getLinearVelocity().x;
            this.applyImpulse(new Vec2(-playerVelocityX * 5, 0));
            player.applyImpulse(new Vec2(collisionDirection * 150 / 8, 100 / 8));
            player.attacked();
        }
    }

    /**
     * Handles collision between player and enemy when player is attacking
     */
    public void handleCollisionWithAttackingPlayer() {
        this.attacked();
    }

    /**
     * Changes the enemy's movement direction and flips its image horizontally
     */
    public void turnAround() {
        isMovingRight = !isMovingRight;
        this.getImages().get(0).flipHorizontal();
    }

    /**
     * Handles collision events between this enemy and other bodies
     * 
     * @param collisionEvent The collision event data
     */
    @Override
    public void collide(CollisionEvent collisionEvent) {
        if (collisionEvent.getOtherBody().getClass().getName().equals(Player.class.getName())) {
            Player player = (Player) collisionEvent.getOtherBody();

            // Determine collision direction and handle accordingly
            if (collisionEvent.getNormal().x == -1) {
                // Collision from right side
                if (!player.attacking) {
                    handleCollisionWithNonAttackingPlayer(player, -1);
                } else {
                    handleCollisionWithAttackingPlayer();
                }
            } else if (collisionEvent.getNormal().x == 1) {
                // Collision from left side
                if (!player.attacking) {
                    handleCollisionWithNonAttackingPlayer(player, 1);
                } else {
                    handleCollisionWithAttackingPlayer();
                }
            } else if (collisionEvent.getNormal().y >= 0.8) {
                // Collision from above (player jumping on enemy)
                if (!player.attacking) {
                    handleCollisionWithNonAttackingPlayer(player, 0);
                } else {
                    handleCollisionWithAttackingPlayer();
                }
            } else {
                // Other collision angles
                if (!player.attacking) {
                    player.applyImpulse(new Vec2(-player.getLinearVelocity().x, 150 / 8));
                    player.attacked();
                } else {
                    handleCollisionWithAttackingPlayer();
                }
            }
        }
    }
}