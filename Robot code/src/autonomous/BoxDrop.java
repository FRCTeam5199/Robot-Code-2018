package autonomous;

import arm.Arm;
import drive.DriveBase;
import elevator.Elevator;
import gripper.Gripper;
import interfaces.AutFunction;

public class BoxDrop implements AutFunction {
	private final int duration = 250;

	private final Gripper gripper;
	private final Arm arm;
	private final Elevator elevator;
	private double height;

	private long endTime;

	public BoxDrop(Gripper gripper, Arm arm, Elevator elevator) {
		this.gripper = gripper;
		this.arm = arm;
		this.elevator = elevator;
	}

	@Override
	public void update(long deltaTime) {
		if (elevator.getPosition() > Elevator.deadzone && arm.isUp()) {
			arm.moveDown();
		}
	}

	@Override
	public void init() {
		height = elevator.getPosition();

		elevator.setTarget(height);
		elevator.enablePID();

		gripper.open();

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
