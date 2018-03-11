package arm;

import org.usfirst.frc.team5199.robot.Robot;

import controllers.JoystickController;
import edu.wpi.first.wpilibj.Encoder;
import elevator.Elevator;
import interfaces.LoopModule;

public class ArmControl implements LoopModule {
	private final Arm arm;
	private final Encoder elevatorEncoder;
	private final JoystickController stick;

	private boolean override;

	private boolean armPos;

	public ArmControl(Arm arm, Encoder elevatorEncoder, JoystickController stick) {
		this.arm = arm;
		this.stick = stick;
		this.elevatorEncoder = elevatorEncoder;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		override = false;
		armPos = false;
		arm.moveUp();
	}

	@Override
	public void update(long delta) {
		if (stick.getButton(7)) {
			override = true;
		}

		if ((elevatorEncoder.getDistance() > Elevator.deadzone || override) && stick.getButtonDown(2)) {
			armPos = !armPos;
			arm.setPiston(armPos);
		}

		if (Math.abs(elevatorEncoder.getDistance() - Elevator.downHeight) < Elevator.downRadius && !override) {
			arm.moveDown();
		}

	}

	public Arm getArm() {
		return arm;
	}
}
