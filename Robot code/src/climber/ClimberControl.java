package climber;

import controllers.ControlPanel;
import controllers.JoystickController;
import interfaces.LoopModule;

public class ClimberControl implements LoopModule {
	private final Climber climber;
	private final JoystickController stick;

	public ClimberControl(Climber climber, JoystickController stick) {
		this.climber = climber;
		this.stick = stick;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		climber.lock();

	}

	@Override
	public void update(long delta) {
		// TODO Auto-generated method stub

		if (stick.getButton(5) || stick.getButton(6)) {
			climber.setMotor(1);
		} else if (stick.getButton(3) || stick.getButton(4)) {
			climber.setMotor(-1);
		} else {
			climber.setMotor(0);
		}

		if (stick.getButton(11)) {
			climber.release();
		} else {
			climber.lock();
		}

	}

}