package entity;

import city.cs.engine.*;
import game.GameWorld;
import org.jbox2d.common.Vec2;
import service.GameAudioManager;

/**
 * ArmoredEnemy is a large, heavily armored enemy that cannot be defeated.
 * It has a reinforced metallic exterior making it completely invulnerable to
 * attacks.
 * While slower than other enemies, its size and invincibility make it a
 * significant threat.
 */
public class ArmoredEnemy extends Enemy {

    // Size of the armored enemy (larger than regular enemies)
    private static final float ENEMY_SIZE = 1.5f;

    // Physical shape of the enemy
    private static final Shape ENEMY_SHAPE = new BoxShape(ENEMY_SIZE, ENEMY_SIZE);

    /**
     * Constructor for creating a new armored enemy
     * 
     * @param world The game world in which the enemy exists
     */
    public ArmoredEnemy(World world) {
        super(world, ENEMY_SHAPE);

        // Reset fixtures and images for custom setup
        this.removeAllImages();
        this.getFixtureList().clear();

        // Create a solid fixture for the enemy
        SolidFixture fixture = new SolidFixture(this, ENEMY_SHAPE);

        // Add the armored enemy image
        this.addImage(new BodyImage("resources/enemies/enemy_metallic.gif", 3f));

        // Set slower movement speed and longer action interval due to heavy armor
        movementSpeed = 2;
        ACTION_INTERVAL = 200;
    }

    /**
     * Handles what happens when this enemy is attacked
     * This enemy is invulnerable and cannot be defeated due to its heavy armor
     */
    @Override
    public void attacked() {
        // Nothing happens because this enemy is invulnerable to attacks
    }

    /**
     * Performs the enemy's action - turning around to change direction
     */
    @Override
    public void action() {
        this.turnAround();
    }
}