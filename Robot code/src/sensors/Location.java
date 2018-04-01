package sensors;

import org.usfirst.frc.team5199.robot.Robot;
import org.usfirst.frc.team5199.robot.RobotMap;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import maths.Vector2;
import util.ClockRegulator;

public class Location implements Runnable {
	private final double wheelOffset = 12.625; // inches

	private final Thread t;
	private final ClockRegulator regulator;

	private final ADXRS450_Gyro gyro;
	private final Encoder wheelsLeft;
	private final Encoder wheelsRight;

	private final double degToRad = Math.PI / 180d;

	private double eLeftLast;
	private double eRightLast;
	private double gyroLast;
	private double gyroZero;

	private boolean isAlive;
	private Vector2 location;

	public Location(ADXRS450_Gyro gyro, Encoder wheelsLeft, Encoder wheelsRight) {
		regulator = new ClockRegulator(100);

		this.gyro = gyro;
		this.wheelsLeft = wheelsLeft;
		this.wheelsRight = wheelsRight;

		location = Vector2.ZERO.clone();

		gyroZero = gyro.getAngle();
		eLeftLast = wheelsLeft.getDistance();
		eRightLast = wheelsRight.getDistance();
		gyroLast = getRot();

		t = new Thread(this, "location");

	}

	public double getRot() {
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
			// two encoder
			// ------------------------------------------------------------------

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

			// ------------------------------------------------------------------

			// // single encoder
			// //
			// ------------------------------------------------------------------
			// Vector2 delta;
			//
			// double lDist = deltaLeft();
			// double dAngle = deltaAngle();
			//
			// if (dAngle == 0) {
			// delta = new Vector2(0, lDist);
			// } else {
			// double radius = (lDist - dAngle * wheelOffset) / dAngle;
			// delta = new Vector2(radius * (1 - Math.cos(dAngle)), radius *
			// Math.sin(dAngle));
			// delta = Vector2.rotateCW(delta, getRot());
			// }
			//
			// location = Vector2.add(location, delta);
			// //
			// ------------------------------------------------------------------

			regulator.sync();
			if (Robot.toolInterface != null) {
				Robot.toolInterface.sendPos();
			}

			SmartDashboard.putNumber("Encoder L", wheelsLeft.getDistance());
			SmartDashboard.putNumber("Encoder R", wheelsRight.getDistance());
		}
	}

	public void set(Vector2 newPos) {
		reset();
		location = newPos;
		//flush out delta stuff
		deltaLeft();
		deltaRight();
		deltaAngle();
		deltaLeft();
		deltaRight();
		deltaAngle();
	}

	public Vector2 getLocation() {
		return location.clone();
	}

	public double getBaseVelocity() {
		return (wheelsLeft.getRate() + wheelsRight.getRate()) / 2;
	}

	public void reset() {
		location = Vector2.ZERO.clone();
		gyroZero = gyro.getAngle();
	}
}
