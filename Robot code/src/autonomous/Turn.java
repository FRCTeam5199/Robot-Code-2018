package autonomous;

import drive.DriveBase;
import drive.DriveControl;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import interfaces.AutFunction;

public class Turn implements AutFunction {
	private final double angleTolerance = 3;
	private final double rateTolerance = 10;
	private final double angle;
	private final DriveBase base;
	private final DriveControl driveControl;
	private final ADXRS450_Gyro gyro;

	private double offset;

	public Turn(double angle, DriveBase base, DriveControl driveControl) {
		this.angle = angle;
		this.base = base;
		this.driveControl = driveControl;

		gyro = base.getGyro();
	}

	@Override
	public void update(long deltaTime) {
		base.applyTurnPID(0);
	}

	@Override
	public void init() {
		offset = gyro.getAngle();
		
		base.setHighGear();

		driveControl.setTurnPIDDisplace();
		driveControl.setTurnPID(angle + offset);
		driveControl.disableMovePID();
		driveControl.enableTurnPID();
	}

	@Override
	public boolean isDone() {
		return Math.abs(gyro.getRate()) < rateTolerance && Math.abs(gyro.getAngle() - offset - angle) < angleTolerance;
	}

	@Override
	public void cleanUp() {
		driveControl.disableMovePID();
		driveControl.disableTurnPID();
		base.move(0, 0);
	}
}
