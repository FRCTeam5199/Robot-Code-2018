package autonomous;

import org.usfirst.frc.team5199.robot.Robot;

import drive.DriveBase;
import drive.DriveControl;
import interfaces.AutFunction;
import util.Interpolater;

public class MoveInterp implements AutFunction {
	private final double maxSpeed = 100;
	private final double distTolerance = 2;
	private final double rateTolerance = 1;
	private final double dist;
	private final DriveBase base;
	private final DriveControl driveControl;

	private final Interpolater interp;

	private double offset;

	public MoveInterp(double dist, double accel, DriveBase base, DriveControl driveControl) {
		this.dist = dist;
		this.base = base;
		this.driveControl = driveControl;

		interp = new Interpolater(accel, maxSpeed, .5);
	}

	@Override
	public void update(long deltaTime) {
		double target = interp.getValue(deltaTime);
		driveControl.setMovePID(target);
		base.applyPID();
	}

	@Override
	public void init() {
		offset = base.getAvgDist();

		interp.setLocation(offset);
		interp.setTarget(dist + offset);

		base.setHighGear();

		driveControl.setMovePIDDisplace();
		driveControl.setTurnPIDDisplace();
		driveControl.setMovePID(offset);
		driveControl.setTurnPID(base.getGyro().getAngle());
		driveControl.enableMovePID();
		driveControl.enableTurnPID();

	}

	@Override
	public boolean isDone() {
		return Math.abs(base.getAvgRate()) < rateTolerance
				&& Math.abs(base.getAvgDist() - offset - dist) < distTolerance;
	}

	@Override
	public void cleanUp() {
		driveControl.disableMovePID();
		driveControl.disableTurnPID();
		base.move(0, 0);
	}
}
