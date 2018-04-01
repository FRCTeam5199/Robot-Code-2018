package elevator;

import controllers.JoystickController;
import interfaces.LoopModule;
import util.Interpolater;

public class ElevatorControl implements LoopModule {

	private final JoystickController joystick;
	private final Elevator elevator;
	private final int moveDuration = 850;
	private final int maxHeight = 89;
	// private final int maxHeight = 83;

	private double deadzone = .2;
	private double targetPos = 0;
	private long moveEndTime;
	private boolean commitToMove;
	private boolean macroRelease;
	private boolean deadzoneLast;

	private boolean override;

	private Interpolater smoother;

	public ElevatorControl(Elevator elevator, JoystickController joystick) {
		this.elevator = elevator;
		this.joystick = joystick;
		smoother = new Interpolater(.04, 48, .2);
	}

	@Override
	public void init() {

		override = false;
		deadzoneLast = false;
		commitToMove = false;
		macroRelease = true;
		moveEndTime = 0;
		elevator.disablePID();

		elevator.setTarget(elevator.getPosition());

	}

	@Override
	public void update(long delta) {
		if (joystick.getButton(7)) {
			override = true;
		}

		if (override) {
			elevator.disablePID();
			elevator.setMotor(joystick.getSlider() * joystick.getYAxis());
		} else {
			if (commitToMove) {
				commitToMove = System.currentTimeMillis() < moveEndTime;
			} else {
				if (!joystick.getButton(2)) {
					macroRelease = true;
				}

				if (joystick.getButton(2) && elevator.getEncoder().getDistance() < Elevator.deadzone && macroRelease) {
					macroRelease = false;
					elevator.setTarget(Elevator.deadzone + 1);
					elevator.enablePID();

					commitToMove = true;
					moveEndTime = System.currentTimeMillis() + moveDuration;

				} else {
					if (Math.abs(joystick.getYAxis()) < deadzone) {
						if (!deadzoneLast) {
							targetPos = elevator.getPosition();
						}
						elevator.setTarget(targetPos);
						elevator.enablePID();
						deadzoneLast = true;
					} else {
						if (joystick.getYAxis() > 0 && elevator.getPosition() >= maxHeight - 1) {
							// nothing
							elevator.setTarget(maxHeight);
							elevator.enablePID();
						} else {
							elevator.disablePID();
							elevator.setMotor(joystick.getYAxis() * joystick.getSlider());
							// elevator.setMotor(joystick.getYAxis());
							deadzoneLast = false;
						}
					}
				}
			}
		}
	}

	public void setPositionSmooth(double n, long delta) {
		smoother.setTarget(n);
		elevator.setTarget(smoother.getValue(delta));
	}

	public void resetSmoothStart() {
		smoother.setLocation(elevator.getPosition());
	}

	public void setSmoothStart(double n) {
		smoother.setLocation(n);
	}

	public void enablePID() {
		setPIDEnabled(true);
	}

	public void disablePID() {
		setPIDEnabled(false);
	}

	public void setPIDEnabled(boolean b) {
		elevator.setPIDEnabled(b);
	}

}
