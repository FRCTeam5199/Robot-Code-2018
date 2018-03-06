package wheelieBar;

import org.usfirst.frc.team5199.robot.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;

public class WheelieBar {
	private final Solenoid piston;

	public WheelieBar() {
		piston = new Solenoid(RobotMap.wheelieBarPiston);
	}

	public void release() {
		piston.set(true);
	}

	public void lock() {
		piston.set(false);
	}

	public void set(boolean b) {
		piston.set(b);
	}
}
