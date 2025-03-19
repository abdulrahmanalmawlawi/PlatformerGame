package object;

import city.cs.engine.GhostlyFixture;
import city.cs.engine.Sensor;
import city.cs.engine.SensorEvent;
import city.cs.engine.SensorListener;
import city.cs.engine.Shape;
import city.cs.engine.StaticBody;
import city.cs.engine.World;
import entity.Player;

/**
 * GamePickup is an abstract class representing items that can be picked up by
 * the player.
 * It provides a common framework for handling player contact and triggering
 * specific actions when the item is collected.
 * 
 * Features:
 * - Uses a ghostly fixture to allow other objects to pass through it
 * - Uses a sensor to detect when the player makes contact with it
 * - Automatically triggers the appropriate action when collected
 * - Subclasses define specific behaviors for different types of pickups
 * 
 * The pickup system is designed to be extensible, with different types of
 * pickups
 * implemented as subclasses, including:
 * - Coins for score
 * - Keys for opening doors
 * - Power-ups for player abilities
 * - Health items for restoration
 * - Special items for quest objectives
 * 
 * The sensor-based detection system ensures that pickups are automatically
 * collected when the player touches them, creating a responsive and intuitive
 * gameplay experience without requiring explicit interaction commands.
 * 
 * Using the ghostly fixture approach allows pickups to be placed in locations
 * where they won't impede player movement or create physics conflicts with
 * other game objects.
 */
public abstract class GamePickup extends StaticBody implements SensorListener {

    /**
     * Constructor for creating a new pickup item
     * 
     * @param world The game world in which the pickup exists
     * @param shape The physical shape of the pickup
     */
    public GamePickup(World world, Shape shape) {
        super(world);
        // Add a ghostly fixture to allow objects to pass through
        GhostlyFixture ghostlyFixture = new GhostlyFixture(this, shape);
        // Add a sensor to detect contact with other bodies
        Sensor contactSensor = new Sensor(this, shape);
        contactSensor.addSensorListener(this);
    }

    /**
     * Handles the beginning of contact between the pickup and another body.
     * If the contact is with a player, triggers the pickup's action.
     * 
     * This method is automatically called by the physics engine when any
     * object enters the pickup's sensor area. It filters the contacts to
     * only respond to the player, ignoring other entities like enemies
     * or projectiles.
     * 
     * @param sensorEvent The sensor event containing contact information
     */
    @Override
    public void beginContact(SensorEvent sensorEvent) {
        if (sensorEvent.getContactBody() instanceof Player) {
            action((Player) sensorEvent.getContactBody());
        }
    }

    /**
     * Handles the end of contact between the pickup and another body.
     * Default implementation does nothing.
     * 
     * Subclasses can override this method if they need to implement
     * special behavior when the player leaves the pickup's sensor area
     * without collecting it.
     * 
     * @param sensorEvent The sensor event containing contact information
     */
    @Override
    public void endContact(SensorEvent sensorEvent) {
        // No action needed when contact ends
    }

    /**
     * Defines the action to perform when the player collects this item.
     * Must be implemented by subclasses to provide specific behavior.
     * 
     * This method is the core of the pickup system, defining what happens
     * when a player collects the item. Common actions include:
     * - Incrementing player statistics (score, health, etc.)
     * - Granting temporary or permanent abilities
     * - Changing player state (e.g., setting flags like hasKey)
     * - Playing sound effects for feedback
     * - Removing the pickup from the game world
     * 
     * @param player The player that collected the item
     */
    public abstract void action(Player player);
}