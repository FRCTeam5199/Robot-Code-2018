package elevator;

import org.usfirst.frc.team5199.robot.Robot;

import controllers.JoystickController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import interfaces.LoopModule;
import util.EventTimer;
import util.Interpolater;

public class ElevatorControl implements LoopModule {

	private final JoystickController joystick;
	private final Elevator elevator;
	private final int moveDuration = 850;
	private final int maxHeight = 85;

	private double deadzone = .2;
	private double targetPos = 0;
	private long moveEndTime;
	private boolean commitToMove;
	private boolean macroRelease;
	private boolean deadzoneLast;

	private Interpolater smoother;

	public ElevatorControl(Elevator elevator, JoystickController joystick) {
		this.elevator = elevator;
		this.joystick = joystick;
		smoother = new Interpolater(2.1E-5, .2);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

		// SmartDashboard.putNumber("Elevator P", 0);
		// SmartDashboard.putNumber("Elevator I", 0);
		// SmartDashboard.putNumber("Elevator D", 0);
		// SmartDashboard.putNumber("Interpolater accel", 0);

		deadzoneLast = false;
		commitToMove = false;
		macroRelease = true;
		moveEndTime = 0;
		elevator.disablePID();
	}

	@Override
	public void update(long delta) {
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
						deadzoneLast = false;
					}
				}
			}

		}
	}

	public void setPositionSmooth(double n, long delta) {
		smoother.setTarget(n);
		elevator.setTarget(smoother.getValue(delta));
	}
}
