package arm;

import org.usfirst.frc.team5199.robot.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;

public class Arm {
	private Solenoid piston;

	public Arm() {
		piston = new Solenoid(RobotMap.armPiston);
	}

	public void moveUp() {
		piston.set(false);
	}

	public void moveDown() {
		piston.set(true);
	}

	public void setPiston(boolean b) {
		piston.set(b);
	}

	public boolean isUp() {
		return !piston.get();
	}

	public boolean isDown() {
		return piston.get();
	}
}
