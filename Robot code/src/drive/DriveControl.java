package drive;

import org.usfirst.frc.team5199.robot.Robot;

import controllers.XBoxController;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import interfaces.LoopModule;

public class DriveControl implements LoopModule {
	private final DriveBase base;

	private DriveBaseMovePID baseMovePID;
	private DriveBaseTurnPID baseTurnPID;

	private PIDController turnPID;
	private PIDController movePID;

	private final double speed = 1;
	private final double rSpeed = 200;
	private final double radToDegrees = 180 / Math.PI;
	private final double deadzone = .075;

	// Comp robot
	private final double[] turnPIDDisplace = { 0.06, 0.0002, 0.2 };
	private final double[] turnPIDRate = { 0.001, 0, 0.006 };
	private final double[] movePIDDisplace = { 0.1, 0, 0.5 };
	private final double[] movePIDRate = { 0.005, 0, 0.025 };

	// // Test robot
	// private final double[] turnPIDDisplace = { 0.03, 0.0003, 0.1 };
	// private final double[] turnPIDRate = { 0.001, 0, 0.006 };
	// private final double[] movePIDDisplace = { 0.1, 0, 0.39 };
	// private final double[] movePIDRate = { 0.002, .1, 0.01 };

	private final XBoxController controller;

	private double pointControlTargetAngle = 0;
	private double pointControlOffset = 0;

	private DriveMode driveMode;

	public DriveControl(DriveBase base, XBoxController controller) {
		this.base = base;
		this.controller = controller;
		driveMode = DriveMode.ARCADE_ASSISTED;

		baseMovePID = new DriveBaseMovePID(base);
		baseTurnPID = new DriveBaseTurnPID(base);

		turnPID = new PIDController(0, 0, 0, baseTurnPID, baseTurnPID);
		movePID = new PIDController(0, 0, 0, baseMovePID, baseMovePID);

	}

	@Override
	public void init() {
		turnPID.enable();
		movePID.disable();
		// initAdjustMovePID();
	}

	@Override
	public void update(long delta) {
		base.setGearbox(controller.getButton(6));
		// adjustMovePID();

		String ultraInfo = "Front\t" + base.getUltraFront() + "\nRear\t" + base.getUltraRear() + "\nRight\t"
				+ base.getUltraRight() + "\nLeft\t" + base.getUltraLeft() + "\n";
		// Robot.nBroadcaster.println(ultraInfo);

		selectDriveMode();

		switch (driveMode) {
		case POINT:
			pointControl(delta);
			break;
		case TANK_ASSISTED:
			tankControlAssisted();
			break;
		case ARCADE_ASSISTED:
			arcadeControlAssisted();
			break;
		case TANK:
			tankControl();
			break;
		}

		SmartDashboard.putBoolean("Box in", (base.getUltraFront() < 6.75));

	}

	public void selectDriveMode() {
		if (controller.getButton(1)) {
			pointControlTargetAngle = 0;
			turnPID.reset();
			enableTurnPID();
			disableMovePID();
			driveMode = DriveMode.POINT;

		} else if (controller.getButton(2)) {
			driveMode = DriveMode.TANK_ASSISTED;
			enableTurnPID();
			disableMovePID();

		} else if (controller.getButton(3)) {
			driveMode = DriveMode.ARCADE_ASSISTED;
			enableTurnPID();
			enableMovePID();
			// disableMovePID();
		} else if (controller.getButton(4)) {
			disableTurnPID();
			disableMovePID();
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

		base.move(right, left);
		// Robot.nBroadcaster.println(gyro.getRate());
	}

	public void tankControlAssisted() {
		double speedMultiplier = speed;
		double right = controller.getStickRY();
		double left = controller.getStickLY();

		baseTurnPID.setPIDSourceType(PIDSourceType.kRate);

		// Right trigger boost
		speedMultiplier += (1 - speed) * controller.getRTrigger();
		right *= speedMultiplier;
		left *= speedMultiplier;

		// Assists
		double avg = (right + left) / 2;
		double targetRotSpeed = (left - right) * 300;

		setTurnPID(targetRotSpeed);

		base.applyTurnPID(avg);

		// Robot.nBroadcaster.println(left - turnSpeed + "\t" + right +
		// turnSpeed);
		// Robot.nBroadcaster.println(gyro.getRate());
	}

	public void arcadeControlAssisted() {
		// enableMovePID();
		baseTurnPID.setPIDSourceType(PIDSourceType.kRate);
		// baseMovePID.setPIDSourceType(PIDSourceType.kRate);

		double targetTurnSpeed;
		if (Math.abs(controller.getStickRX()) > deadzone) {
			targetTurnSpeed = controller.getStickRX() * rSpeed;
		} else {
			targetTurnSpeed = 0;
		}
		double movespeed = controller.getStickLY() * 80;
		setTurnPID(targetTurnSpeed);
		// setMovePID(movespeed);
		// base.applyPID();
		base.applyTurnPID(controller.getStickLY());
	}

	public void pointControl(long delta) {
		double deadzone = 0.5;

		baseTurnPID.setPIDSourceType(PIDSourceType.kDisplacement);

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
			pointControlOffset = baseTurnPID.pidGet();
		}

		double outputRotation = pointControlTargetAngle + pointControlOffset;

		setTurnPID(outputRotation);
		base.applyTurnPID(controller.getStickLY());

	}

	public void adjustTurnPID() {
		if (baseMovePID.getPIDSourceType() == PIDSourceType.kDisplacement) {
			turnPIDDisplace[0] = SmartDashboard.getNumber("Turn P", 0);
			turnPIDDisplace[1] = SmartDashboard.getNumber("Turn I", 0);
			turnPIDDisplace[2] = SmartDashboard.getNumber("Turn D", 0);
		} else if (baseMovePID.getPIDSourceType() == PIDSourceType.kRate) {
			turnPIDRate[0] = SmartDashboard.getNumber("Turn P", 0);
			turnPIDRate[1] = SmartDashboard.getNumber("Turn I", 0);
			turnPIDRate[2] = SmartDashboard.getNumber("Turn D", 0);
		}
	}

	public void initAdjustTurnPID() {
		SmartDashboard.putNumber("Turn P", turnPID.getP());
		SmartDashboard.putNumber("Turn I", turnPID.getI());
		SmartDashboard.putNumber("Turn D", turnPID.getD());
	}

	public void setTurnPIDDisplace() {
		baseTurnPID.setPIDSourceType(PIDSourceType.kDisplacement);
	}

	public void setTurnPIDRate() {
		baseTurnPID.setPIDSourceType(PIDSourceType.kRate);
	}

	public void setTurnPIDType(PIDSourceType type) {
		baseTurnPID.setPIDSourceType(type);
	}

	public void setTurnPID(double target) {

		if (baseTurnPID.getPIDSourceType() == PIDSourceType.kRate) {
			turnPID.setPID(turnPIDRate[0], turnPIDRate[1], turnPIDRate[2]);
			turnPID.setSetpoint(target);
			return;
		} else if (baseTurnPID.getPIDSourceType() == PIDSourceType.kDisplacement) {
			turnPID.setPID(turnPIDDisplace[0], turnPIDDisplace[1], turnPIDDisplace[2]);

			double currentAngle = baseTurnPID.pidGet();
			// double currentAngleLocal = currentAngle - (int) (currentAngle /
			// 360) * 360;

			double currentAngleLocal = currentAngle;

			while (currentAngleLocal > 360) {
				currentAngleLocal -= 360;
			}

			while (currentAngleLocal < 0) {
				currentAngleLocal += 360;
			}

			double diff = currentAngleLocal - target;

			if (diff > 180) {
				diff -= 360;

			} else if (diff < -180) {
				diff += 360;
			}

			turnPID.setSetpoint(currentAngle - diff);

			// Robot.nBroadcaster.println(currentAngle + "\t" +
			// currentAngleLocal + "\t" + diff + "\t" + target);
			return;
		}

	}

	public boolean turnPIDIsEnabled() {
		return turnPID.isEnabled();
	}

	public void enableTurnPID() {
		turnPID.enable();
		Robot.nBroadcaster.println("Enable " + turnPID.getP() + "\t" + baseTurnPID.pidGet());
	}

	public void disableTurnPID() {
		turnPID.disable();
	}

	public void setTurnPIDEnabled(boolean b) {
		turnPID.setEnabled(b);
	}

	public void resetTurnPID() {
		turnPID.reset();
	}

	public void adjustMovePID() {
		if (baseTurnPID.getPIDSourceType() == PIDSourceType.kDisplacement) {
			movePIDDisplace[0] = SmartDashboard.getNumber("Move P", 0);
			movePIDDisplace[1] = SmartDashboard.getNumber("Move I", 0);
			movePIDDisplace[2] = SmartDashboard.getNumber("Move D", 0);
		} else if (baseTurnPID.getPIDSourceType() == PIDSourceType.kRate) {
			movePIDRate[0] = SmartDashboard.getNumber("Move P", 0);
			movePIDRate[1] = SmartDashboard.getNumber("Move I", 0);
			movePIDRate[2] = SmartDashboard.getNumber("Move D", 0);
		}
	}

	public void initAdjustMovePID() {
		SmartDashboard.putNumber("Move P", movePID.getP());
		SmartDashboard.putNumber("Move I", movePID.getI());
		SmartDashboard.putNumber("Move D", movePID.getD());
	}

	public void setMovePIDDisplace() {
		baseMovePID.setPIDSourceType(PIDSourceType.kDisplacement);
	}

	public void setMovePIDRate() {
		baseMovePID.setPIDSourceType(PIDSourceType.kRate);
	}

	public void setMovePIDType(PIDSourceType type) {
		baseMovePID.setPIDSourceType(type);
	}

	public void setMovePID(double d) {
		if (baseMovePID.getPIDSourceType() == PIDSourceType.kRate) {
			movePID.setPID(movePIDRate[0], movePIDRate[1], movePIDRate[2]);
		} else if (baseMovePID.getPIDSourceType() == PIDSourceType.kDisplacement) {
			movePID.setPID(movePIDDisplace[0], movePIDDisplace[1], movePIDDisplace[2]);
		}
		movePID.setSetpoint(d);
	}

	public void enableMovePID() {
		movePID.enable();
	}

	public void disableMovePID() {
		movePID.disable();
	}

	public void setMovePIDEnabled(boolean b) {
		movePID.setEnabled(b);
	}

	public void resetMovePID() {
		movePID.reset();
	}

	public enum DriveMode {
		TANK, TANK_ASSISTED, ARCADE_ASSISTED, POINT;
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub

	}
}
