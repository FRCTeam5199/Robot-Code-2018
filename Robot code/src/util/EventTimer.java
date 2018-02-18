package util;

import org.usfirst.frc.team5199.robot.Robot;

public class EventTimer {
	private long msPerUpdate;
	private long lastTime;

	public EventTimer(double frequency) {
		msPerUpdate = (long) (1000 / frequency);
		lastTime = System.currentTimeMillis();
	}

	public void reset() {
		lastTime = System.currentTimeMillis();
	}

	public boolean isTime() {
		long elapsed = System.currentTimeMillis() - lastTime;
		if (elapsed >= msPerUpdate) {
			lastTime = elapsed + lastTime;
			return true;
		}
		return false;
	}

	public long getMsPerUpdate() {
		return msPerUpdate;
	}

	public void setFrequency(double frequency) {
		msPerUpdate = (long) (1000 / frequency);
	}
}
