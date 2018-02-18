package drive;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

import org.usfirst.frc.team5199.robot.Robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDOutput;

public class DriveBaseMovePID implements PIDSource, PIDOutput {
	private final DriveBase base;
	private final Encoder left;
	private final Encoder right;

	private PIDSourceType pidSourceType;

	public DriveBaseMovePID(DriveBase base) {
		this.base = base;
		left = Robot.sensors.getLeftWheelEncoder();
		right = Robot.sensors.getRightWheelEncoder();

		pidSourceType = PIDSourceType.kRate;

	}

	@Override
	public void pidWrite(double output) {
		base.setPIDMove(output);
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
			return (left.getDistance() + right.getDistance()) / 2;
		} else if (pidSourceType == PIDSourceType.kRate) {
			return (left.getRate() + right.getRate()) / 2;
		} else {
			return 0;
		}
	}

}
