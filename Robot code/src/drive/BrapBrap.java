package drive;

import org.usfirst.frc.team5199.robot.Robot;
import org.usfirst.frc.team5199.robot.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import util.ClockRegulator;

public class BrapBrap implements Runnable {
	private int idleRate = 10;
	private int maxRate = 25;
	private double kRate = 15;
	private ClockRegulator cl;
	private Solenoid valve;
	private long period;
	private boolean isAlive;

	public BrapBrap() {
		valve = new Solenoid(RobotMap.gearboxPiston);
		cl = new ClockRegulator(50);
		isAlive = false;
	}

	public void start() {
		if (!isAlive) {
			isAlive = true;
			Robot.nBroadcaster.println("Brap Start");
			Thread t = new Thread(this, "BrapBrap");
			t.start();
		}
	}

	public void updateMotorPower(double l, double r) {
		double newRate = kRate * (Math.abs(l) + Math.abs(r));
		if (newRate < idleRate) {
			newRate = idleRate;
		} else if (newRate > maxRate) {
			newRate = maxRate;
		}
		setRate(newRate);
	}

	private void setRate(double rate) {
		period = (long) (1000 / rate);
	}

	@Override
	public void run() {
		// Robot.nBroadcaster.println("Running");
		long lastBap = System.currentTimeMillis();
		while (isAlive) {
			if (System.currentTimeMillis() >= lastBap + period) {
				lastBap += period;
				// Robot.nBroadcaster.println("Brap!");
				valve.set(!valve.get());
			}
			cl.sync();
		}
		Robot.nBroadcaster.println("exit");
	}

	public void stop() {
		isAlive = false;
	}
}
