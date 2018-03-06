package drive;

import org.usfirst.frc.team5199.robot.RobotMap;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;

public class DriveBase {
	private final ADXRS450_Gyro gyro;
	private final Encoder encoderLeft;

	private final Spark motorLeft;
	private final Spark motorRight;

	public DriveBase() {
		gyro = new ADXRS450_Gyro();
		encoderLeft = new Encoder(RobotMap.leftEncoderA, RobotMap.leftEncoderB);

		motorLeft = new Spark(RobotMap.drivemotorLeft);
		motorRight = new Spark(RobotMap.drivemotorRight);

		System.out.println("Calibating gyro...");
		gyro.calibrate();
		System.out.println("done");
		gyro.reset();
	}

	public ADXRS450_Gyro getGyro() {
		return gyro;
	}

	public Encoder getEncoderL() {
		return encoderLeft;
	}

	public void move(double left, double right) {
		motorLeft.set(-left);
		motorRight.set(right);
		// left is reversed
	}
}
