package autonomous;

import drive.DriveBase;
import drive.DriveControl;
import interfaces.AutFunction;

public class Move implements AutFunction {
	private final double distTolerance = 2;
	private final double rateTolerance = 1;
	private final double dist;
	private final DriveBase base;
	private final DriveControl driveControl;

	private double offset;

	public Move(double dist, DriveBase base, DriveControl driveControl) {
		this.dist = dist;
		this.base = base;
		this.driveControl = driveControl;
	}

	@Override
	public void update(long deltaTime) {
		base.applyPID();
	}

	@Override
	public void init() {
		offset = base.getAvgDist();

		driveControl.setMovePIDDisplace();
		driveControl.setTurnPIDDisplace();
		driveControl.setMovePID(dist + offset);
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
