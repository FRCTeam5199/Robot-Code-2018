package autonomous;

import org.usfirst.frc.team5199.robot.Robot;

import drive.DriveBase;
import drive.DriveControl;
import elevator.ElevatorControl;
import gripper.Gripper;
import interfaces.AutFunction;
import maths.Vector2;
import sensors.Location;

public class GetBox implements AutFunction {

	private final double radToDeg = 180 / Math.PI;
	private final double speed = .5;
	private final int moveRange = 4;
	private final int stopTurnRadius = 8;
	private final int doneRadius = 6;

	private final Location location;
	private final DriveBase base;
	private final DriveControl driveControl;
	private final ElevatorControl elevatorControl;
	private final Gripper gripper;
	private final Vector2 pos;

	private double targetAngle;

	public GetBox(Vector2 pos, DriveBase base, DriveControl driveControl, ElevatorControl elevatorControl,
			Gripper gripper) {
		this.pos = pos;
		this.base = base;
		this.driveControl = driveControl;
		this.elevatorControl = elevatorControl;
		this.gripper = gripper;

		this.location = base.getLocation();

	}

	@Override
	public void update(long deltaTime) {
		Vector2 robotPos = location.getLocation();
		elevatorControl.setPositionSmooth(1, deltaTime);
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
			base.applyTurnPID(speed);
		} else {
			base.applyTurnPID(0);
		}

		Robot.nBroadcaster.println(Vector2.distance(location.getLocation(), pos));

	}

	@Override
	public void init() {
		Vector2 robotPos = location.getLocation();
		targetAngle = radToDeg * Math.atan2(pos.getX() - robotPos.getX(), pos.getY() - robotPos.getY());
		gripper.setSpeed(1);
		gripper.open();
		elevatorControl.enablePID();
		driveControl.enableTurnPID();
	}

	@Override
	public boolean isDone() {
		return Vector2.distance(location.getLocation(), pos) < doneRadius;
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
		gripper.close();
		base.move(0, 0);
		gripper.setSpeed(0);
	}

}
