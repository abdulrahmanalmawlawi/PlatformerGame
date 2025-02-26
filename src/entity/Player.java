package entity;

import ui.Game;
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
 * Player is the main character controlled by the user.
 * It handles movement, animations, attacks, and interactions with other game
 * objects.
 */
public class Player extends PhysicsActor {

    // Animation timing constants
    private static final int ATTACK_DURATION = 15;
    private static final int INVULNERABILITY_DURATION = 20;

    // Player physical dimensions
    private static final float PLAYER_WIDTH = 0.5f;
    private static final float PLAYER_HEIGHT = 1f;
    private static final Shape PLAYER_SHAPE = new BoxShape(PLAYER_WIDTH, PLAYER_HEIGHT);

    // Animation state constants
    private static final int ANIMATION_STANDING = 0;
    private static final int ANIMATION_RUNNING = 1;
    private static final int ANIMATION_JUMPING = 2;
    private static final int ANIMATION_ATTACKING = 3;

    // Player state variables
    public boolean attacking = false;
    public int health = 3;

    // Timers for attack and invulnerability periods
    private int attackTimer = 0;
    private int invulnerabilityTimer = -1;

    // Animation tracking variables
    private int currentAnimation = 0;
    private int lastMoveAnimation = -1;

    // Power-up states
    public boolean hasSword = false;
    public boolean hasKey = false;

    /**
     * Constructor for creating a new player
     * 
     * @param world The game world in which the player exists
     */
    public Player(World world) {
        super(world, PLAYER_SHAPE);
        this.addImage(new BodyImage("resources/player/player_idle.png", 2f));

        // Initialize movement vectors
        x_right = new Vec2(12 / 2, 0);
        x_left = new Vec2(-12 / 2, 0);
        x_slow_right = new Vec2(2 / 2, 0);
        x_slow_left = new Vec2(-2 / 2, 0);
        impulse_jump = new Vec2(0, 150 / 3);
        max_velocity = 10;
        side = true;
    }

    /**
     * Updates the player's state each game step:
     * - Keeps player upright
     * - Updates animations
     * - Handles attack state
     * - Manages invulnerability period after being hit
     */
    @Override
    public void eventStep() {
        super.eventStep();

        // Keep the player upright
        this.setAngle(0);

        // Update player animation based on state
        updateAnimation();

        // Handle attack state if player is attacking
        if (attacking) {
            updateAttackState();
        }

        // Update invulnerability timer after being hit
        if (invulnerabilityTimer >= 0) {
            invulnerabilityTimer++;
            if (invulnerabilityTimer >= INVULNERABILITY_DURATION) {
                invulnerabilityTimer = -1;
            }
        }
    }

    /**
     * Updates the player's animation based on current state
     * (standing, running, jumping, or attacking)
     */
    public void updateAnimation() {
        if (!attacking) {
            if (!jump) {
                // Player is jumping or falling
                if ((move != lastMoveAnimation) || this.currentAnimation != ANIMATION_JUMPING) {
                    currentAnimation = ANIMATION_JUMPING;
                    this.removeAllImages();

                    if (!hasSword) {
                        this.addImage(new BodyImage("resources/player/player_jump.gif", 2f));
                    } else {
                        this.addImage(new BodyImage("resources/player/player_jump_sword.gif", 2f));
                    }

                    if (!side) {
                        this.getImages().get(0).flipHorizontal();
                    }

                    lastMoveAnimation = move;
                }
            } else {
                if ((move == -1) && this.currentAnimation != ANIMATION_STANDING) {
                    // Player is standing still
                    currentAnimation = ANIMATION_STANDING;
                    this.removeAllImages();

                    if (!hasSword) {
                        this.addImage(new BodyImage("resources/player/player_idle.png", 2f));
                    } else {
                        this.addImage(new BodyImage("resources/player/player_idle_sword.png", 2f));
                    }

                    if (!side) {
                        this.getImages().get(0).flipHorizontal();
                    }

                    lastMoveAnimation = move;
                } else if ((move != -1)
                        && ((move != lastMoveAnimation) || this.currentAnimation != ANIMATION_RUNNING)) {
                    // Player is running
                    currentAnimation = ANIMATION_RUNNING;
                    this.removeAllImages();

                    if (!hasSword) {
                        this.addImage(new BodyImage("resources/player/player_run.gif", 2f));
                    } else {
                        this.addImage(new BodyImage("resources/player/player_run_sword.gif", 2f));
                    }

                    if (!side) {
                        this.getImages().get(0).flipHorizontal();
                    }

                    lastMoveAnimation = move;
                }
            }
        } else if (this.currentAnimation != ANIMATION_ATTACKING) {
            // Player is attacking
            currentAnimation = ANIMATION_ATTACKING;
            this.removeAllImages();
            this.addImage(new BodyImage("resources/player/player_attack.gif", 2f));

            if (!side) {
                this.getImages().get(0).flipHorizontal();
            }

            lastMoveAnimation = move;
        }
    }

    /**
     * Gives the player the sword power-up
     */
    public void upgrade() {
        hasSword = true;
    }

    /**
     * Updates the attack state and timer
     * Ends the attack when timer reaches maximum duration
     */
    public void updateAttackState() {
        attackTimer++;
        if (attackTimer >= ATTACK_DURATION) {
            attackTimer = 0;
            attacking = false;
        }
    }

    /**
     * Initiates a sword attack if the player has the sword power-up
     * Applies a forward impulse in the direction the player is facing
     */
    public void attack() {
        if (hasSword && !attacking) {
            attacking = true;

            // Apply forward impulse based on facing direction
            if (side) {
                this.applyImpulse(new Vec2(50, 0));
            } else {
                this.applyImpulse(new Vec2(-50, 0));
            }

            // Play attack sound
            GameWorld.sound.setFile(GameAudioManager.DAMAGE);
            GameWorld.sound.play();
        }
    }

    /**
     * Handles what happens when the player is attacked by an enemy
     * Reduces health and starts invulnerability period
     */
    public void attacked() {
        if (invulnerabilityTimer < 0) {
            health--;
            invulnerabilityTimer = 0;
        }

        // Play damage sound
        GameWorld.sound.setFile(GameAudioManager.DAMAGE);
        GameWorld.sound.play();
    }

    public void jump() {
        if (jump) {
            this.removeAllImages();
            if (hasSword) {
                this.addImage(new BodyImage("resources/player/player_jump_sword.gif", 2f));
            } else {
                this.addImage(new BodyImage("resources/player/player_jump.gif", 2f));
            }
        }
    }

    public void stand() {
        this.removeAllImages();
        if (hasSword) {
            this.addImage(new BodyImage("resources/player/player_idle_sword.png", 2f));
        } else {
            this.addImage(new BodyImage("resources/player/player_idle.png", 2f));
        }
    }
}