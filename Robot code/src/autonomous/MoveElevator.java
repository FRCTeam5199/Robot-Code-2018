package autonomous;

import org.usfirst.frc.team5199.robot.Robot;

import elevator.Elevator;
import elevator.ElevatorControl;
import interfaces.AutFunction;

public class MoveElevator implements AutFunction {
	private final int tolerance = 2;

	private final ElevatorControl elevatorControl;
	private final Elevator elevator;
	private final double height;

	public MoveElevator(double height, Elevator elevator, ElevatorControl elevatorControl) {
		this.height = height;
		this.elevator = elevator;
		this.elevatorControl = elevatorControl;
	}

	@Override
	public void update(long deltaTime) {
		elevatorControl.setPositionSmooth(height, deltaTime);
		Robot.nBroadcaster.println(elevator.getPosition());
	}

	@Override
	public void init() {
		elevator.enablePID();
		elevatorControl.setSmoothStart(elevator.getPosition());
	}

	@Override
	public boolean isDone() {
		return Math.abs(elevator.getPosition() - height) < tolerance;
	}

	@Override
	public void cleanUp() {
	}
}
