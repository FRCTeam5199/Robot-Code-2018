package led;

import org.usfirst.frc.team5199.robot.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;

public class LED {
	private final Solenoid red;
	private final Solenoid green;
	private final Solenoid blue;

	public LED() {
		red = new Solenoid(RobotMap.ledR);
		green = new Solenoid(RobotMap.ledG);
		blue = new Solenoid(RobotMap.ledB);
	}

	public void setRed(boolean b) {
		red.set(b);
	}

	public void setGreen(boolean b) {
		green.set(b);
	}

	public void setBlue(boolean b) {
		blue.set(b);
	}

}
