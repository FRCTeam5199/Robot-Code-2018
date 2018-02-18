package autonomous;

import drive.DriveBase;
import interfaces.AutFunction;

public class Stop implements AutFunction {

	private final DriveBase base;
	private boolean done;

	public Stop(DriveBase base) {
		this.base = base;
		done = true;
	}

	@Override
	public void update(long deltaTime) {
		base.move(0, 0);
		done = true;
	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
}
