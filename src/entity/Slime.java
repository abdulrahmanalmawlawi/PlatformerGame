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
 * Slime is an enemy that appears in level 2 of the game.
 * It moves by turning around and jumping periodically.
 */
public class Slime extends Enemy {

    // Physical dimensions of the slime
    private static final float SLIME_SIZE = 0.6f;

    // Physical shape of the slime
    private static final Shape SLIME_SHAPE = new BoxShape(SLIME_SIZE, SLIME_SIZE);

    /**
     * Constructor for creating a new slime enemy
     * 
     * @param world The game world in which the slime exists
     */
    public Slime(World world) {
        super(world, SLIME_SHAPE);
        this.addImage(new BodyImage("resources/enemies/enemy_slime.gif", 1.2f));
        this.addCollisionListener(this);
    }

    /**
     * Handles what happens when the slime is attacked by the player.
     * Increases the player's score and destroys the slime.
     */
    @Override
    public void attacked() {
        GameWorld.score += 5;
        this.destroy();
    }

    /**
     * Performs the slime's action - turning around and jumping.
     * Called periodically based on the action timer.
     */
    @Override
    public void action() {
        this.setAngle(0); // Keep the slime upright
        this.turnAround(); // Change direction
        this.applyImpulse(new Vec2(0, 20f)); // Jump upward
    }
}