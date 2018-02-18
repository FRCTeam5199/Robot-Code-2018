package util;

import org.usfirst.frc.team5199.robot.Robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class TalonSRXWatchdog implements Runnable {

	private final TalonSRX motor;
	private final Thread t;

	private final double maxCurrent;
	private final long timeout;
	private final long maxOvercurrentTime;

	private long overCurrentEnd = 0;
	private long timeoutEnd = 0;

	private boolean isAlive;

	public TalonSRXWatchdog(TalonSRX motor, double maxCurrent, long maxOvercurrentTime, long timeout) {
		this.motor = motor;
		this.maxCurrent = maxCurrent;
		this.maxOvercurrentTime = maxOvercurrentTime;
		this.timeout = timeout;

		t = new Thread(this, "TalonSRX Watchdog");
	}

	public void start() {
		isAlive = true;
		t.start();
	}

	public void stop() {
		isAlive = false;
	}

	public boolean isOk() {
		return timeoutEnd < System.currentTimeMillis();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ClockRegulator clockRegulator = new ClockRegulator(10);

		boolean lastState = false;

		while (isAlive) {
			if (overCurrent() && !lastState) {
				// Overcurrent start
				overCurrentEnd = System.currentTimeMillis() + maxOvercurrentTime;
				//Robot.nBroadcaster.println("Overcurrent start...");
			}

			if (overCurrent() && System.currentTimeMillis() > overCurrentEnd) {
				// isOvercurrent and maxOvercurrentTime has passed
				timeoutEnd = System.currentTimeMillis() + timeout;
				//Robot.nBroadcaster.println("Timeout for " + timeout + " ms");
			}

			lastState = overCurrent();

			clockRegulator.sync();
		}
	}

	private boolean overCurrent() {
		return motor.getOutputCurrent() > maxCurrent;
	}
}
