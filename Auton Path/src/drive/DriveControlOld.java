package drive;

import org.usfirst.frc.team5199.robot.Robot;

import controllers.XBoxController;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import interfaces.LoopModule;
import maths.Vector2;

public class DriveControlOld implements LoopModule {
	private final DriveBase base;
	private final ADXRS450_Gyro gyro;
	// private final JoystickController joystick;
	private final double speed = 1;
	private final double rSpeed = 400;
	private final double radToDegrees = 180 / Math.PI;
	private final XBoxController controller;

	private double integral = 0;

	private double pointControlTargetAngle = 0;

	private DriveMode driveMode;

	public DriveControlOld(DriveBase base, XBoxController controller) {
		this.base = base;
		this.gyro = Robot.sensors.getGyro();
		this.controller = controller;
		driveMode = DriveMode.POINT;
	}

	@Override
	public void init() {
	}

	@Override
	public void update(long delta) {
		selectDriveMode();
		if (controller.getButton(8)) {
			//
		} else {
			switch (driveMode) {
			case POINT:
				pointControl(delta);
				// Robot.nBroadcaster.println("Point Control");
				break;
			case TANK_ASSISTED:
				tankControlAssisted();
				// Robot.nBroadcaster.println("Tank Assisted Control");
				break;
			case ARCADE_ASSISTED:
				arcadeControlAssisted();
				// Robot.nBroadcaster.println("Arcade Control Assisted
				// Control");
				break;
			case TANK:
				tankControl();
				// Robot.nBroadcaster.println("Tank Control");
				break;
			}
		}

	}

	public void selectDriveMode() {
		if (controller.getButton(1)) {
			integral = 0;
			gyro.reset();
			pointControlTargetAngle = 0;
			driveMode = DriveMode.POINT;
		} else if (controller.getButton(2)) {
			driveMode = DriveMode.TANK_ASSISTED;
		} else if (controller.getButton(3)) {
			driveMode = DriveMode.ARCADE_ASSISTED;
		} else if (controller.getButton(4)) {
			driveMode = DriveMode.TANK;
		}
	}

	public void tankControl() {
		double speedMultiplier = speed;
		double right = controller.getStickRY();
		double left = controller.getStickLY();

		// Right trigger boost
		speedMultiplier += (1 - speed) * controller.getRTrigger();
		right *= speedMultiplier;
		left *= speedMultiplier;

		// Left trigger straighten
		double avg = (right + left) / 2;
		double lTrigger = controller.getLTrigger();
		double notLTrigger = 1 - lTrigger;
		right = notLTrigger * right + avg * lTrigger;
		left = notLTrigger * left + avg * lTrigger;

		base.move(right, left);
		// Robot.nBroadcaster.println(gyro.getRate());
	}

	public void tankControlAssisted() {
		double speedMultiplier = speed;
		double right = controller.getStickRY();
		double left = controller.getStickLY();

		// Right trigger boost
		speedMultiplier += (1 - speed) * controller.getRTrigger();
		right *= speedMultiplier;
		left *= speedMultiplier;

		// Assists
		double avg = (right + left) / 2;
		double targetRotSpeed = (left - right) * 300;

		double error = targetRotSpeed - gyro.getRate();
		double turnSpeed = error * .01;

		base.move(avg - turnSpeed, avg + turnSpeed);

		// Robot.nBroadcaster.println(left - turnSpeed + "\t" + right +
		// turnSpeed);
		// Robot.nBroadcaster.println(gyro.getRate());
	}

	public void arcadeControlAssisted() {
		double targetSpeed = controller.getStickRX() * rSpeed;
		double currentSpeed = gyro.getRate();
		double turnSpeed = (targetSpeed - currentSpeed) * .01;
		// Robot.nBroadcaster.println(gyro.getRate());
		base.move(controller.getStickLY() - turnSpeed, controller.getStickLY() + turnSpeed);
	}

	public void pointControl(long delta) {
		double deadzone = 0.5;

		if (Math.sqrt(controller.getStickRX() * controller.getStickRX()
				+ controller.getStickRY() * controller.getStickRY()) > deadzone) {
			// forwards is 0 degrees, right is 90 degrees
			pointControlTargetAngle = Math.atan2(controller.getStickRX(), controller.getStickRY()) * radToDegrees;
		}

		if (pointControlTargetAngle < 0) {
			pointControlTargetAngle += 360;
		}

		if (controller.getButton(6)) {
			pointControlTargetAngle = 0;
			gyro.reset();
		}

		double turnSpeed = turnToCalc(pointControlTargetAngle, delta);

		base.move(controller.getStickLY() - turnSpeed, controller.getStickLY() + turnSpeed);

	}

	public double turnToCalc(double targetAngle, long delta) {

		double p = 0.04;
		double i = 0;
		double d = 0.005;

		double angle = gyro.getAngle();

		// normalize angle
		angle = angle - (int) (angle / 360) * 360;

		double error = targetAngle - angle;

		// check if there is a faster way to get to the target by crossing the 0
		// - 360 degrees jump thing
		if (Math.abs(error - 360) < Math.abs(error)) {
			error -= 360;
		} else if (Math.abs(error + 360) < Math.abs(error)) {
			error += 360;
		}

		integral += error * delta;

		return error * p - gyro.getRate() * d + integral * i;
	}

	public DriveBase getBase() {
		return base;
	}

	public enum DriveMode {
		TANK, TANK_ASSISTED, ARCADE, ARCADE_ASSISTED, POINT;
	}
}
