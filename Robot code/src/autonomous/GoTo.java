package autonomous;

import org.usfirst.frc.team5199.robot.Robot;

import drive.DriveBase;
import drive.DriveControl;
import elevator.ElevatorControl;
import gripper.Gripper;
import interfaces.AutFunction;
import maths.Vector2;
import sensors.Location;

public class GoTo implements AutFunction {

	private final double radToDeg = 180 / Math.PI;
	private final double speed = 50;
	private final int moveRange = 5;
	private final int stopTurnRadius = 3;
	private final int doneRadius = 2;

	private final Location location;
	private final DriveBase base;
	private final DriveControl driveControl;
	private final Vector2 pos;

	private double targetAngle;

	public GoTo(Vector2 pos, DriveBase base, DriveControl driveControl) {
		this.pos = pos;
		this.base = base;
		this.driveControl = driveControl;

		this.location = base.getLocation();

	}

	@Override
	public void update(long deltaTime) {
		Vector2 robotPos = location.getLocation();
		if (Vector2.distance(location.getLocation(), pos) > stopTurnRadius) {
			targetAngle = radToDeg * Math.atan2(pos.getX() - robotPos.getX(), pos.getY() - robotPos.getY());
		}

		driveControl.setTurnPID(targetAngle);

		double error = base.getGyro().getAngle() - targetAngle;

		while (targetAngle < -180) {
			error += 360;
		}
		while (targetAngle > 180) {
			error -= 360;
		}

		if (Math.abs(error) < moveRange) {
			base.setPIDMove(speed);
		} else {
			base.setPIDMove(0);
		}

		base.applyPID();

		Robot.nBroadcaster.println(Vector2.distance(location.getLocation(), pos));

	}

	@Override
	public void init() {
		Vector2 robotPos = location.getLocation();
		targetAngle = radToDeg * Math.atan2(pos.getX() - robotPos.getX(), pos.getY() - robotPos.getY());

		driveControl.enableTurnPID();
		driveControl.enableMovePID();
	}

	@Override
	public boolean isDone() {
		return Vector2.distance(location.getLocation(), pos) < doneRadius;
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
		base.move(0, 0);
	}

}
