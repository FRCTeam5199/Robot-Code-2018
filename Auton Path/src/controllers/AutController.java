package controllers;

import edu.wpi.first.wpilibj.Joystick;

public class AutController {
	Joystick joystick;

	public AutController(int n) {
		joystick = new Joystick(n);
	}
	public int getAutMode() {
		if(joystick.getRawButton(1)) {
			return 1;
		} else if(joystick.getRawButton(2)) {
			return 2;
		} else if(joystick.getRawButton(3)) {
			return 3;
		} else if (joystick.getRawButton(4)) {
			return 4;
		} else if(joystick.getRawButton(5)) {
			return 5;
		}
		return 6;
	}
	
}
