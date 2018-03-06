package gripper;

import controllers.ControlPanel;
import controllers.JoystickController;
import interfaces.LoopModule;

public class GripperControl implements LoopModule {

	private final Gripper gripper;
	private final JoystickController stick;

	private boolean gripperOpen;

	public GripperControl(Gripper gripper, JoystickController stick) {
		this.gripper = gripper;
		this.stick = stick;
		gripperOpen = false;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		gripperOpen = false;
	}

	@Override
	public void update(long delta) {
		if (stick.hatUp()) {
			gripper.setSpeed(-1);
		} else if (stick.hatDown()) {
			gripper.setSpeed(1);
		} else if (stick.hatRight()) {
			gripper.setR(-1);
			gripper.setL(0);
		} else if (stick.hatLeft()) {
			gripper.setL(1);
			gripper.setR(0);
		} else {
			gripper.setSpeed(0);
		}

		if (stick.getButtonDown(1)) {
			gripperOpen = !gripperOpen;
		}

		gripper.setPiston(gripperOpen);

	}

}
