package drive;

import org.usfirst.frc.team5199.robot.Robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class DriveBaseTurnPID implements PIDSource, PIDOutput {
	private final DriveBase base;
	private final ADXRS450_Gyro gyro;

	private PIDSourceType pidSourceType;

	public DriveBaseTurnPID(DriveBase base) {
		this.base = base;
		gyro = Robot.sensors.getGyro();
		pidSourceType = PIDSourceType.kDisplacement;
	}

	@Override
	public void pidWrite(double output) {
		base.setPIDTurn(output);
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		pidSourceType = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return pidSourceType;
	}

	@Override
	public double pidGet() {
		if (pidSourceType == PIDSourceType.kDisplacement) {
			return gyro.getAngle();
		} else if (pidSourceType == PIDSourceType.kRate) {
			return gyro.getRate();
		} else {
			return 0;
		}
	}
}
