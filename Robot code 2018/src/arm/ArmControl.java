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

	private boolean armPos;

	public ArmControl(Arm arm, Encoder elevatorEncoder, JoystickController stick) {
		this.arm = arm;
		this.stick = stick;
		this.elevatorEncoder = elevatorEncoder;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		armPos = false;
		arm.moveUp();
	}

	@Override
	public void update(long delta) {
		if (elevatorEncoder.getDistance() > Elevator.deadzone && stick.getButtonDown(2)) {
			armPos = !armPos;
			arm.setPiston(armPos);
		}

		if (Math.abs(elevatorEncoder.getDistance() - Elevator.downHeight) < Elevator.downRadius) {
			arm.moveDown();
		}

	}

	public Arm getArm() {
		return arm;
	}
}
