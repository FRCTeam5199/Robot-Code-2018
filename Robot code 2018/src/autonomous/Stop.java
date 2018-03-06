package autonomous;

import arm.Arm;
import climber.Climber;
import drive.DriveBase;
import elevator.Elevator;
import gripper.Gripper;
import interfaces.AutFunction;

public class Stop implements AutFunction {
	private final Arm arm;
	private final DriveBase base;
	private final Climber climber;
	private final Elevator elevator;
	private final Gripper gripper;
	private boolean done;

	public Stop(Arm arm, DriveBase base, Climber climber, Elevator elevator, Gripper gripper) {
		this.arm = arm;
		this.base = base;
		this.climber = climber;
		this.elevator = elevator;
		this.gripper = gripper;
		done = true;
	}

	@Override
	public void update(long deltaTime) {
		base.move(0, 0);
		climber.setMotor(0);
		elevator.setMotor(0);
		elevator.disablePID();
		gripper.setSpeed(0);
		done = true;
	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub

	}
}
