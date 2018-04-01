package autonomous;

import org.usfirst.frc.team5199.robot.Robot;

import arm.Arm;
import drive.DriveBase;
import elevator.Elevator;
import gripper.Gripper;
import interfaces.AutFunction;

public class BoxOut implements AutFunction {
	private final int closedDuration = 250;
	private final int duration = 750;

	private final Gripper gripper;
	private final Arm arm;
	private final Elevator elevator;
	private final double power;

	private long endTime;
	private long openTime;

	public BoxOut(double power, Gripper gripper, Arm arm, Elevator elevator) {
		this.gripper = gripper;
		this.arm = arm;
		this.elevator = elevator;
		this.power = power;
	}

	public BoxOut(Gripper gripper, Arm arm, Elevator elevator) {
		this(1, gripper, arm, elevator);
	}

	@Override
	public void update(long deltaTime) {

		if (elevator.getPosition() > Elevator.deadzone && arm.isUp()) {
			arm.moveDown();
		}

		if (System.currentTimeMillis() > openTime) {
			gripper.open();
		}
	}

	@Override
	public void init() {
		elevator.setTarget(elevator.getPosition());
		elevator.enablePID();

		gripper.close();
		gripper.setSpeed(-power);

		openTime = System.currentTimeMillis() + closedDuration;
		endTime = System.currentTimeMillis() + duration;
	}

	@Override
	public boolean isDone() {
		return System.currentTimeMillis() > endTime;
	}

	@Override
	public void cleanUp() {
		gripper.setSpeed(0);
		gripper.open();
	}
}
