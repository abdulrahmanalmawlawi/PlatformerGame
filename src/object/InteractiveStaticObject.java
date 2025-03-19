package object;

import city.cs.engine.Shape;
import city.cs.engine.StaticBody;
import city.cs.engine.World;

/**
 * InteractiveStaticObject is a StaticBody that can perform actions on each game
 * step.
 * This abstract class serves as a base for all static objects in the game world
 * that need to update their state or perform actions during gameplay, despite
 * being physically static (non-moving) in the physics engine.
 * 
 * Interactive static objects include:
 * - Doors that can be opened with keys
 * - Platforms that appear/disappear
 * - Switches or levers that activate game mechanics
 * - Checkpoints that save player progress
 * - Special terrain elements that change behavior
 * 
 * All interactive static objects share these common characteristics:
 * - They remain in fixed positions in the game world
 * - They can change state or appearance
 * - They respond to player interaction
 * - They update their state on each game step
 * 
 * This class provides a foundation for implementing diverse interactive
 * elements
 * that create engaging gameplay experiences while maintaining the performance
 * benefits of static physics bodies.
 */
public abstract class InteractiveStaticObject extends StaticBody {

    /**
     * Constructor for creating a new interactive static object
     * 
     * @param world The game world in which the object exists
     * @param shape The physical shape of the object
     */
    public InteractiveStaticObject(World world, Shape shape) {
        super(world, shape);
    }

    /**
     * An event that happens on each game step (frame)
     * Must be implemented by subclasses to define object-specific behavior.
     * 
     * This method is called by the game loop to allow the object to:
     * - Update its visual state
     * - Check for conditions that trigger state changes
     * - Perform time-based actions
     * - Interact with the game world
     */
    public abstract void eventStep();
}
