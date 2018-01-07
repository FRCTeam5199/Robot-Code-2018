package sensors;

import org.usfirst.frc.team5199.robot.Robot;
import org.usfirst.frc.team5199.robot.RobotMap;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import maths.Vector2;
import util.ClockRegulator;

public class Location implements Runnable {
	private final Thread t;
	private final ClockRegulator regulator;

	private final ADXRS450_Gyro gyro;
	private final Encoder wheelsLeft;
	private final Encoder wheelsRight;

	private final double Gtofmsms;
	private final double degToRad = Math.PI / 180d;
	private final int sampleTime = 10000;

	private double eLeftLast;
	private double eRightLast;
	private double gyroLast;
	private double gyroZero;

	private boolean isAlive;
	private Vector2 location;

	public Location(ADXRS450_Gyro gyro, Encoder wheelsLeft, Encoder wheelsRight) {
		regulator = new ClockRegulator(50);

		this.gyro = gyro;
		this.wheelsLeft = wheelsLeft;
		this.wheelsRight = wheelsRight;

		location = Vector2.ZERO.clone();
		// Gtofmsms = 0.0032174049;
		// constant that converts G's to feet per millisecond^2
		// Gtofmsms = 3.108095e-8;
		// Gtofmsms = 0.00000003108095;

		Gtofmsms = 0.000032152231;

		gyroZero = gyro.getAngle();
		eLeftLast = wheelsLeft.getDistance();
		eRightLast = wheelsRight.getDistance();
		gyroLast = getRot();

		t = new Thread(this, "location");

	}

	private double getRot() {
		return degToRad * (gyro.getAngle() - gyroZero);
	}

	private double deltaLeft() {
		double current = wheelsLeft.getDistance();
		double output = current - eLeftLast;
		eLeftLast = current;
		return output;
	}

	private double deltaRight() {
		double current = wheelsRight.getDistance();
		double output = current - eRightLast;
		eRightLast = current;
		return output;
	}

	private double deltaAngle() {
		double current = getRot();
		double output = current - gyroLast;
		gyroLast = current;
		return output;
	}

	public void start() {
		isAlive = true;
		t.start();
	}

	public void stop() {
		isAlive = false;
	}

	@Override
	public void run() {
		while (isAlive) {
			double avgDistance = (deltaRight() + deltaLeft()) / 2d;
			double dAngle = deltaAngle();
			
			Vector2 delta;

			if (dAngle == 0) {
				delta = new Vector2(0, avgDistance);
			} else {
				double radius = avgDistance / dAngle;
				delta = new Vector2(radius * (1 - Math.cos(dAngle)), radius * Math.sin(dAngle));
				delta = Vector2.rotateCW(delta, getRot());
			}

			location = Vector2.add(location, delta);
			
			regulator.sync();
		}

	}

	public Vector2 getLocation() {
		return location.clone();
	}

	public void reset() {
		location = Vector2.ZERO.clone();
		gyroZero = gyro.getAngle();
	}
}
