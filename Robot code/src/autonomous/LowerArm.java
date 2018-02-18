package autonomous;

import org.usfirst.frc.team5199.robot.Robot;

import arm.ArmControl;
import elevator.Elevator;
import elevator.ElevatorControl;
import interfaces.AutFunction;

public class LowerArm implements AutFunction {
	private final Elevator elevator;
	private final ArmControl armControl;
	private long waitTime = 750;

	private long stopTime = -1;
	private double startPos;
	private boolean moved;
	private boolean isDone;

	public LowerArm(Elevator elevator, ArmControl armControl) {
		this.elevator = elevator;
		this.armControl = armControl;
	}

	@Override
	public void update(long deltaTime) {
		if (elevator.getPosition() >= Elevator.deadzone) {
			armControl.getArm().moveDown();
			if (stopTime == -1) {
				stopTime = System.currentTimeMillis() + waitTime;
			}
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		isDone = false;
		startPos = elevator.getPosition();
		if(startPos<Elevator.deadzone){
			elevator.setTarget(Elevator.deadzone + 2);
			moved = true;
		}else{
			moved = false;
		}
		
		elevator.enablePID();
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		if (stopTime == -1) {
			return false;
		} else {
			return System.currentTimeMillis() > stopTime;
		}
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
		if (moved) {
			elevator.setTarget(startPos);
		}
	}
}
