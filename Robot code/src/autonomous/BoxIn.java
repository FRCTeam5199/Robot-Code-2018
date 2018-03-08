package autonomous;

import arm.Arm;
import drive.DriveBase;
import elevator.Elevator;
import gripper.Gripper;
import interfaces.AutFunction;

public class BoxIn implements AutFunction {
	private final int duration = 12000;

	private final Gripper gripper;
	private final Arm arm;
	private final Elevator elevator;
	private final DriveBase base;
	private double height;

	private long endTime;

	public BoxIn(Gripper gripper, Arm arm, Elevator elevator, DriveBase base) {
		this.gripper = gripper;
		this.arm = arm;
		this.elevator = elevator;
		this.base = base;
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
		
		base.setHighGear();
		base.moveArcade(0.3, 0);

		elevator.setTarget(height);
		elevator.enablePID();

		gripper.open();
		gripper.setSpeed(1);
		endTime = System.currentTimeMillis() + duration;
	}

	@Override
	public boolean isDone() {
		return System.currentTimeMillis() > endTime;
	}

	@Override
	public void cleanUp() {
		gripper.close();
		gripper.setSpeed(0);
		base.move(0, 0);
	}
}
