package elevator;

import util.PIDController;
import controllers.JoystickController;
import interfaces.LoopModule;

public class ElevatorControl implements LoopModule {

	private final JoystickController joystick;
	private final Elevator elevator;
	// private final PIDController elevatorPID;

	public ElevatorControl(Elevator elevator, JoystickController joystick) {
		this.elevator = elevator;
		this.joystick = joystick;
		// elevatorPID = new PIDController(.1, 0, .05);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(long delta) {
		// TODO Auto-generated method stub
		// elevator.setMotor(elevatorPID.update(elevator.getLocation()));

		elevator.setMotor(joystick.GetYAxis() * joystick.GetSlider());
		elevator.getPosition();
		//System.out.println(elevator.getPosition());
	}

	public void setPosition() {

	}
}
