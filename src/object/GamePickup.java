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
 * specific actions
 * when the item is collected.
 * 
 * Features:
 * - Uses a ghostly fixture to allow other objects to pass through it
 * - Uses a sensor to detect when the player makes contact with it
 * - Automatically triggers the appropriate action when collected
 * - Subclasses define specific behaviors for different types of pickups
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
     * @param player The player that collected the item
     */
    public abstract void action(Player player);
}