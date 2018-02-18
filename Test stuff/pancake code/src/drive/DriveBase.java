package drive;

import org.usfirst.frc.team5199.robot.RobotMap;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Talon;

public class DriveBase {
	private final ADXRS450_Gyro gyro;

	private Talon motorLeft;
	private Talon motorRight;

	public DriveBase() {
		gyro = new ADXRS450_Gyro();
		motorLeft = new Talon(RobotMap.drivemotorLeft);
		motorRight = new Talon(RobotMap.drivemotorRight);

		System.out.println("Calibating gyro...");
		gyro.calibrate();
		System.out.println("done");
		gyro.reset();
	}

	public ADXRS450_Gyro getGyro() {
		return gyro;
	}

	public void move(double left, double right) {
		motorLeft.set(-left);
		motorRight.set(-right);
	}
}
