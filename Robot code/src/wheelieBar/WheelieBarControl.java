package wheelieBar;

import controllers.JoystickController;
import interfaces.LoopModule;

public class WheelieBarControl implements LoopModule {
	private final int deployDuration = 500;

	private final WheelieBar wheelieBars;
	private final JoystickController joy;
	private long endTime;

	public WheelieBarControl(WheelieBar wheelieBars, JoystickController joy) {
		this.wheelieBars = wheelieBars;
		this.joy = joy;
	}

	@Override
	public void init() {
		endTime = System.currentTimeMillis() + deployDuration;
	}

	@Override
	public void update(long delta) {
		wheelieBars.set(joy.getButton(8) || System.currentTimeMillis() < endTime);
	}
}
