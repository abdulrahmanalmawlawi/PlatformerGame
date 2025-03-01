package object;

import org.jbox2d.common.Vec2;

import city.cs.engine.Body;
import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.Sensor;
import city.cs.engine.SensorEvent;
import city.cs.engine.SensorListener;
import city.cs.engine.World;

/**
 * RectangularPathPlatform is a platform that automatically moves in a
 * rectangular trajectory.
 * It travels horizontally and vertically in a continuous pattern, and can carry
 * objects that stand on it, moving them along with the platform.
 */
public class RectangularPathPlatform extends InteractiveStaticObject implements SensorListener {

	// Movement direction constants
	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;

	// Physical shape of the moving platform
	public static final BoxShape PLATFORM_SHAPE = new BoxShape((float) (63.0 / 40.0), (float) (29.0 / 40.0));

	// Current movement direction (0 = horizontal, 1 = vertical)
	private int currentDirection = 0;

	// Movement speeds
	private float horizontalSpeed = 0.1f;
	private float verticalSpeed = 0.1f;

	// Current distance traveled in the current direction
	private float distanceTraveled = 0f;

	// Maximum travel distances in each direction
	private float maxHorizontalDistance;
	private float maxVerticalDistance;

	// Sensor to detect objects standing on the platform
	private Sensor contactSensor;

	// Body that is currently standing on the platform
	private Body passengerBody;

	/**
	 * Creates a new platform that moves in a rectangular path
	 * 
	 * @param world              The game world in which the platform exists
	 * @param horizontalDistance Maximum horizontal travel distance
	 * @param verticalDistance   Maximum vertical travel distance
	 */
	public RectangularPathPlatform(World world, float horizontalDistance, float verticalDistance) {
		super(world, PLATFORM_SHAPE);
		this.addImage(new BodyImage("resources/objects/interactive/platform_moving.png", (float) (29.0 / 20.0)));
		this.maxHorizontalDistance = horizontalDistance;
		this.maxVerticalDistance = verticalDistance;
		contactSensor = new Sensor(this, PLATFORM_SHAPE);
		contactSensor.addSensorListener(this);
	}

	/**
	 * Updates the platform's position on each game step.
	 * Moves in a rectangular trajectory and carries any object standing on it.
	 */
	@Override
	public void eventStep() {
		Vec2 movement = new Vec2(0, 0);

		if (currentDirection == HORIZONTAL) {
			if (distanceTraveled >= Math.abs(maxHorizontalDistance)) {
				currentDirection = VERTICAL;
				verticalSpeed *= -1;
				distanceTraveled = 0;
			} else {
				movement = new Vec2(horizontalSpeed, 0);
				distanceTraveled += Math.abs(horizontalSpeed);
			}
		}

		if (currentDirection == VERTICAL) {
			if (distanceTraveled >= Math.abs(maxVerticalDistance)) {
				currentDirection = HORIZONTAL;
				horizontalSpeed *= -1;
				distanceTraveled = 0;
			} else {
				movement = new Vec2(0, verticalSpeed);
				distanceTraveled += Math.abs(verticalSpeed);
			}
		}

		// Move the platform
		this.move(movement);

		// Move any passenger body along with the platform
		if (passengerBody != null) {
			passengerBody.move(movement);
		}
	}

	/**
	 * Handles the beginning of contact between the platform and another body.
	 * Sets the contacting body as the passenger to be carried by the platform.
	 * 
	 * @param sensorEvent The sensor event containing contact information
	 */
	@Override
	public void beginContact(SensorEvent sensorEvent) {
		passengerBody = sensorEvent.getContactBody();
	}

	/**
	 * Handles the end of contact between the platform and another body.
	 * Clears the passenger reference when the body leaves the platform.
	 * 
	 * @param sensorEvent The sensor event containing contact information
	 */
	@Override
	public void endContact(SensorEvent sensorEvent) {
		passengerBody = null;
	}
}
