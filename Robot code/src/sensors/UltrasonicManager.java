package sensors;

import java.util.ArrayList;

import org.usfirst.frc.team5199.robot.Robot;

import edu.wpi.first.wpilibj.Ultrasonic;

public class UltrasonicManager implements Runnable {
	private final ArrayList<Ultrasonic> sensors;
	private final int maxWaitTime = 100;

	private Thread thread;
	private boolean isAlive;

	public UltrasonicManager() {
		sensors = new ArrayList<Ultrasonic>();
	}

	public void add(Ultrasonic ultra) {
		sensors.add(ultra);
	}

	public void start() {
		isAlive = true;
		thread = new Thread(this, "Ultrasonic Manager");
		thread.start();
	}

	@Override
	public void run() {
		while (isAlive) {
			for (Ultrasonic ultra : sensors) {
				if (ultra.isEnabled()) {
					ultra.ping();
					long timeout = System.currentTimeMillis() + maxWaitTime;
					while (!ultra.isRangeValid() && System.currentTimeMillis() <= timeout) {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public void stop() {
		isAlive = false;
	}
}
