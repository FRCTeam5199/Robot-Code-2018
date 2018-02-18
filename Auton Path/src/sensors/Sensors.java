package sensors;

import org.usfirst.frc.team5199.robot.Robot;
import org.usfirst.frc.team5199.robot.RobotMap;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;
import maths.Vector2;

public class Sensors {
	private final ADXRS450_Gyro gyro;
	private final BuiltInAccelerometer accelerometer;
	private static Encoder flywheelEncoder;
	private final Encoder wheelsLeft;
	private final Encoder wheelsRight;
	private final Location location;

	private static UltrasonicData ultraData;

	public static CircularAverageBuffer flywheelAVG;
	public static CircularAverageBuffer shooterXAVG;

	public Sensors() {
		// init accelerometer
		accelerometer = new BuiltInAccelerometer(Range.k8G);

		// init flywheel encoder
		flywheelEncoder = new Encoder(RobotMap.encoderShooterDIOA, RobotMap.encoderShooterDIOB, false,
				Encoder.EncodingType.k4X);
		flywheelEncoder.reset();
		flywheelEncoder.setDistancePerPulse(RobotMap.rotationsPerStepShooter);

		// init wheel encoders
		wheelsRight = new Encoder(RobotMap.encoderRightDIOA, RobotMap.encoderRightDIOB, false,
				Encoder.EncodingType.k4X);
		wheelsRight.reset();
		wheelsRight.setDistancePerPulse(-RobotMap.inchesPerPulse);

		wheelsLeft = new Encoder(RobotMap.encoderLeftDIOA, RobotMap.encoderLeftDIOB, false, Encoder.EncodingType.k4X);
		wheelsLeft.reset();
		wheelsLeft.setDistancePerPulse(RobotMap.inchesPerPulse);

		// init gyro
		gyro = new ADXRS450_Gyro();
		Robot.nBroadcaster.println("Calibrating gyro...");
		gyro.calibrate();
		Robot.nBroadcaster.println("Done calibrating gyro");

		// init ultrasonic
		ultraData = new UltrasonicData(RobotMap.ultraRightEcho, RobotMap.ultraRightPing, RobotMap.ultraLeftEcho,
				RobotMap.ultraLeftPing);

		// ?????????????
		flywheelAVG = new CircularAverageBuffer(75);
		shooterXAVG = new CircularAverageBuffer(10);

		location = new Location(gyro, wheelsLeft, wheelsRight);
		location.start();
	}

	public ADXRS450_Gyro getGyro() {
		return gyro;
	}

	public BuiltInAccelerometer getAccelerometer() {
		Robot.nBroadcaster.println("getAccelerometer");
		return accelerometer;
	}

	public Encoder getFlywheelEncoder() {
		return flywheelEncoder;
	}

	public Encoder getLeftWheelEncoder() {
		return wheelsLeft;
	}

	public Encoder getRightWheelEncoder() {
		return wheelsRight;
	}

	public Location getLocation() {
		return location;
	}

	public static double ultraDistanceRight() {
		return ultraData.distanceRight();
	}

	public static double ultraDistanceLeft() {
		return ultraData.distanceLeft();
	}

	public static double shooterRPM() {
		return flywheelAVG.DataAverage(flywheelEncoder.getRate());
	}


}
