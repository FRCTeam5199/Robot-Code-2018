package autonomous;

import elevator.Elevator;
import elevator.ElevatorControl;
import interfaces.AutFunction;

public class MoveElevator implements AutFunction {
	private final int tolerance = 1;

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
	}

	@Override
	public void init() {
		elevator.enablePID();
	}

	@Override
	public boolean isDone() {
		return Math.abs(elevator.getPosition() - height) < tolerance;
	}

	@Override
	public void cleanUp() {
	}
}
