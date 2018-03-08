package autonomous;

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
	private double height;

	private long endTime;
	private long openTime;

	public BoxOut(Gripper gripper, Arm arm, Elevator elevator) {
		this.gripper = gripper;
		this.arm = arm;
		this.elevator = elevator;
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
		height = elevator.getPosition();

		elevator.setTarget(height);
		elevator.enablePID();

		gripper.close();
		gripper.setSpeed(-1);

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
	}
}
