package autonomous;

import drive.DriveBase;
import interfaces.AutFunction;

public class ReleaseWheelieBar implements AutFunction {
	private final int duration = 250;
	private final DriveBase driveBase;

	private long endTime;

	public ReleaseWheelieBar(DriveBase driveBase) {
		this.driveBase = driveBase;
	}

	@Override
	public void update(long deltaTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		endTime = System.currentTimeMillis() + duration;
		driveBase.setLowGear();
	}

	@Override
	public boolean isDone() {
		return System.currentTimeMillis() > endTime;
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
		driveBase.setHighGear();
	}
}
