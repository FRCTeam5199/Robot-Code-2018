package led;

import org.usfirst.frc.team5199.robot.Robot;

public class LEDControl {
	private final LED led;

	public LEDControl(LED led) {
		this.led = led;
	}

	public void setColor(Color color) {
		setColor(color.ordinal());
	}

	public void setColor(int color) {
		boolean red = (color & 0b1) != 0;
		boolean green = (color & 0b10) != 0;
		boolean blue = (color & 0b100) != 0;

		Robot.nBroadcaster.println("LED set to: " + red + "\t" + green + "\t" + blue);

		led.setRed(red);
		led.setGreen(green);
		led.setBlue(blue);
	}

	public enum Color {
		BLACK, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE;
	}
}
