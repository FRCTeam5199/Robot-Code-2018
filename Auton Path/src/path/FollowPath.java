package path;

import org.usfirst.frc.team5199.robot.Robot;

import controllers.XBoxController;
import drive.DriveBase;
import drive.DriveControl;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.PIDSourceType;
import interfaces.AutFunction;
import interfaces.LoopModule;
import maths.Vector2;
import sensors.Location;

public class FollowPath implements AutFunction, LoopModule {

	private final double radToDeg = 180d / Math.PI;
	private final double checkpointDistance = 6;

	private final XBoxController controller;
	private final DriveControl driveControl;
	private final DriveBase base;
	private final Location loc;
	private final Path path;

	private double[] speed;
	private PathNode currentCheckpoint;
	private boolean isDone;

	public FollowPath(DriveControl driveControl, DriveBase base, Path path, XBoxController controller) {
		this.controller = controller;
		this.driveControl = driveControl;
		this.base = base;
		this.path = path;

		isDone = false;
		loc = Robot.sensors.getLocation();
	}

	@Override
	public void update(long deltaTime) {
		double angle = angle(loc.getLocation(), currentCheckpoint.getPos()) * radToDeg;

		if (controller.getButton(1)) {
			driveControl.setTurnPID(angle);
			driveControl.setMovePID(currentCheckpoint.getSpeed());
			base.applyPID();
			Robot.nBroadcaster.println(currentCheckpoint.getSpeed() + "\t"
					+ (Robot.sensors.getLeftWheelEncoder().getRate() + Robot.sensors.getRightWheelEncoder().getRate())
							/ 2);
		} else {
			base.move(0, 0);
		}
		// Robot.nBroadcaster.println("on: " + currentCheckpoint + " pos: " +
		// loc.getLocation() + " angle: " + angle);

		while (Vector2.distance(loc.getLocation(), currentCheckpoint.getPos()) < checkpointDistance) {
			PathNode newCheckpoint = path.getCheckpoint();
			if (newCheckpoint == null) {
				base.move(0, 0);
				isDone = true;
			} else {
				currentCheckpoint = newCheckpoint;
			}
		}
	}

	private double angle(Vector2 from, Vector2 to) {
		return Math.atan2(to.getX() - from.getX(), to.getY() - from.getY());
	}

	@Override
	public void init() {
		driveControl.setTurnPIDDisplace();
		driveControl.setMovePIDRate();
		driveControl.enableTurnPID();
		driveControl.enableMovePID();
		currentCheckpoint = path.getCheckpoint();
		path.reset();
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

}
