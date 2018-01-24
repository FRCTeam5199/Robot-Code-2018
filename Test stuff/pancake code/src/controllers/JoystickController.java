package controllers;

import edu.wpi.first.wpilibj.Joystick;

public class JoystickController {

	private final Joystick meme;

	public JoystickController(int n) {
		meme = new Joystick(n);
	}

	public double GetXAxis() {
		return meme.getRawAxis(0);
	}

	public double GetYAxis() {
		return meme.getRawAxis(1);
	}

	public double GetZAxis() {
		return meme.getRawAxis(2);
	}

	public double GetSlider() {
		return (1 - meme.getRawAxis(3) / 2);
		// return -(meme.getRawAxis(3));
	}

	public boolean GetButton(int n) {
		return meme.getRawButton(n);
	}

}
