package climber;

import controllers.JoystickController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import interfaces.LoopModule;

public class ClimberControl implements LoopModule {
	private final Climber climber;
	private final JoystickController stick;

	private boolean climberRelease;

	public ClimberControl(Climber climber, JoystickController stick) {
		this.climber = climber;
		this.stick = stick;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		climberRelease = false;
		climber.lock();
	}

	@Override
	public void update(long delta) {
		// TODO Auto-generated method stub

		if (stick.getButton(8)) {
			climber.setMotor(1);
		} else if (stick.getButton(7) || stick.getButton(9)) {
			climber.setMotor(-1);
		} else {
			climber.setMotor(0);
		}

		if (stick.getButtonDown(11)||stick.getButtonDown(12)) {
			climberRelease = !climberRelease;
			climber.setPiston(climberRelease);
		}

		SmartDashboard.putBoolean("Climber release", climberRelease);
		
	}

}
