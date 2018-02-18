package util;

import org.usfirst.frc.team5199.robot.Robot;

public class Interpolater {
	private double lockMargin;

	private double acceleration;
	private double velocity;
	private double location;

	private double target;

	public Interpolater(double acceleration, double lockMargin) {
		this.acceleration = acceleration;
		velocity = 0;
		location = 0;
		target = 0;

		this.lockMargin = lockMargin;
	}

	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	public void setTarget(double d) {
		target = d;
	}

	public double getValue(long delta) {
		if (Math.abs(target - location) < lockMargin) {
			return target;
		}

		if (target > location) {
			double startingSpeed = startingSpeed(0, target - location, -acceleration);
			if (velocity < startingSpeed) {
				velocity += acceleration * delta;
			} else {
				velocity = startingSpeed;
			}
		} else if (target < location) {
			double startingSpeed = startingSpeed(0, location - target, -acceleration);
			if (velocity > startingSpeed) {
				velocity += -acceleration * delta;
			} else {
				velocity = -startingSpeed;
			}
		}

		location += velocity * delta;

		return location;
	}

	public double startingSpeed(double vf, double d, double maxAccel) {
		if ((vf * vf - (2 * maxAccel * d)) < 0) {
			Robot.nBroadcaster.println(vf + "\t" + d + "\t" + maxAccel);
			System.exit(-1);
		}
		return Math.sqrt(vf * vf - (2 * maxAccel * d));
	}
}
