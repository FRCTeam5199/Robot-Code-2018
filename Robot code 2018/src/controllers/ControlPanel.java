package controllers;

import edu.wpi.first.wpilibj.Joystick;

public class ControlPanel {
	private final Joystick panel;

	public ControlPanel(int n) {
		panel = new Joystick(n);
	}

	public boolean foreArmUp() {
		return panel.getRawButton(1);
	}

	public boolean foreArmDown() {
		return panel.getRawButton(2);
	}

	public boolean winchUp() {
		return panel.getRawButton(3);
	}

	public boolean winchDown() {
		return panel.getRawButton(4);
	}

	public boolean ballBoxIn() {
		return panel.getRawButton(7);
	}

	public boolean ballBoxOut() {
		return panel.getRawButton(5);
	}

	public boolean ballBoxToggle() {
		return panel.getRawButtonPressed(6);
	}

	public boolean upperArmUp() {
		return panel.getRawButton(8);
	}

	public boolean upperArmDown() {
		return panel.getRawButton(9);
	}

	public boolean getButton(int n) {
		return panel.getRawButton(n);
	}
}
