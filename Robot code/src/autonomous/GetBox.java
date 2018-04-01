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
	private final double speed = 30;
	private final int moveRange = 4;
	private final int doneRadius = 4;
	private final int intakeDist = 26;

	private final Location location;
	private final DriveBase base;
	private final DriveControl driveControl;
	private final ElevatorControl elevatorControl;
	private final Gripper gripper;
	private final Vector2 boxPos;

	private double targetAngle;

	public GetBox(Vector2 boxPos, DriveBase base, DriveControl driveControl, ElevatorControl elevatorControl,
			Gripper gripper) {
		this.boxPos = boxPos;
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

		targetAngle = radToDeg * Math.atan2(boxPos.getX() - robotPos.getX(), boxPos.getY() - robotPos.getY());
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
		
		Robot.nBroadcaster.println(Vector2.distance(location.getLocation(), boxPos));

	}

	@Override
	public void init() {
		Vector2 robotPos = location.getLocation();
		targetAngle = radToDeg * Math.atan2(boxPos.getX() - robotPos.getX(), boxPos.getY() - robotPos.getY());
		gripper.setSpeed(1);
		gripper.open();
		elevatorControl.resetSmoothStart();
		elevatorControl.enablePID();
		
		driveControl.setTurnPIDDisplace();
		driveControl.setMovePIDRate();
		
		driveControl.enableTurnPID();
		driveControl.enableMovePID();
	}

	@Override
	public boolean isDone() {
		Vector2 intakePos = Vector2.rotateCW(new Vector2(0, intakeDist), location.getRot());
		intakePos = Vector2.add(intakePos, location.getLocation());
		return Vector2.distance(intakePos, boxPos) < doneRadius;
	}

	@Override
	public void cleanUp() {
		gripper.close();
		driveControl.disableMovePID();
		driveControl.disableTurnPID();
		base.move(0, 0);
		gripper.setSpeed(0);
	}

}
