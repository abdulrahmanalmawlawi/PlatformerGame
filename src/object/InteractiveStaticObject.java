package object;

import city.cs.engine.Shape;
import city.cs.engine.StaticBody;
import city.cs.engine.World;

/**
 * InteractiveStaticObject is a StaticBody that can perform actions on each game
 * step.
 * This abstract class serves as a base for all static objects in the game world
 * that need to update their state or perform actions during gameplay, despite
 * being
 * physically static (non-moving) in the physics engine.
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
     * Must be implemented by subclasses to define object-specific behavior
     */
    public abstract void eventStep();
}
