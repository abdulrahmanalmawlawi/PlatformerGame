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
 */
public class BasicPatrolEnemy extends Enemy {

    // Size of the enemy (for collision shape)
    private static final float ENEMY_SIZE = 0.8f;

    // Physical shape of the enemy
    private static final Shape ENEMY_SHAPE = new BoxShape(ENEMY_SIZE, ENEMY_SIZE);

    /**
     * Creates a new basic patrolling enemy
     * 
     * @param world The game world in which the enemy exists
     */
    public BasicPatrolEnemy(World world) {
        super(world, ENEMY_SHAPE);
        this.addImage(new BodyImage("resources/enemies/enemy_basic.gif", 1.6f));
        this.addCollisionListener(this);
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
        this.turnAround();
    }
}