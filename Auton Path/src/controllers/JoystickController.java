package controllers;

import edu.wpi.first.wpilibj.Joystick;

public class JoystickController {
	private Joystick stick;

	public JoystickController(int n) {
		stick = new Joystick(n);
	}

	public double getX() {
		return stick.getRawAxis(0);
	}

	public double getY() {
		return stick.getRawAxis(1);
	}

	public double getZ() {
		return stick.getRawAxis(2);
	}

	public double getSlider() {
		return stick.getRawAxis(3);
	}

	public double getScaledSlider() {
		return (1 - getSlider()) / 2;
	}

	public boolean getTrigger() {
		return stick.getRawButton(1);
	}

	public boolean getButton(int n) {
		return stick.getRawButton(n);
	}

}
