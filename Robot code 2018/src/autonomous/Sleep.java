package autonomous;

import interfaces.AutFunction;

public class Sleep implements AutFunction {

	private final long duration;
	private long stopTime;

	public Sleep(long duration) {
		this.duration = duration;
	}

	@Override
	public void update(long deltaTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		stopTime = System.currentTimeMillis() + duration;
	}

	@Override
	public boolean isDone() {
		return System.currentTimeMillis() > stopTime;
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub

	}

}
