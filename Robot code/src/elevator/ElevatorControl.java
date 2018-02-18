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
	private boolean deadzoneLast;
	private double deadzone = .2;
	private double targetPos = 0;

	private Interpolater smoother;
	private EventTimer timer;

	public ElevatorControl(Elevator elevator, JoystickController joystick) {
		this.elevator = elevator;
		this.joystick = joystick;
		timer = new EventTimer(1 / 4d);
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
		elevator.disablePID();
	}

	@Override
	public void update(long delta) {

		if (Math.abs(joystick.getYAxis()) < deadzone) {
			if (!deadzoneLast) {
				targetPos = elevator.getPosition();
			}
			elevator.setTarget(targetPos);
			elevator.enablePID();
			deadzoneLast = true;
		} else {
			elevator.disablePID();
			elevator.setMotor(joystick.getYAxis() * joystick.getSlider());
			deadzoneLast = false;
		}
	}

	public void setPositionSmooth(double n, long delta) {
		smoother.setTarget(n);
		elevator.setTarget(smoother.getValue(delta));
	}
}
