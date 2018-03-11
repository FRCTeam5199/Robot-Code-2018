package elevator;

import org.usfirst.frc.team5199.robot.Robot;
import org.usfirst.frc.team5199.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import util.VictorSPXPID;

public class Elevator {
	// 1-10 ratio
	// 18 tooth 1.432 pitch diameter
	// 1024 ppr encoder
	
	public static final int deadzone = 7;
	public static final int downHeight = 42;
	public static final int downRadius = 13;

	private final double inchesPerPulse = (Math.PI * 1.432d * 2) / (1024d * 9d);
	private final VictorSPXPID motor;
	private final Encoder encoder;
	private PIDController elevatorPID;

	public Elevator() {
		motor = new VictorSPXPID(RobotMap.liftMotor);
		encoder = new Encoder(RobotMap.liftEncoderA, RobotMap.liftEncoderB);
		encoder.setDistancePerPulse(inchesPerPulse);
		elevatorPID = new PIDController(0.4, 0, 0.3, encoder, motor);

	}

	public void setPIDEnabled(boolean b) {
		elevatorPID.setEnabled(b);
	}

	public void enablePID() {
		elevatorPID.enable();
	}

	public void disablePID() {
		elevatorPID.disable();
	}

	public void adjustPID() {
		elevatorPID.setP(SmartDashboard.getNumber("Elevator P", 0));
		elevatorPID.setI(SmartDashboard.getNumber("Elevator I", 0));
		elevatorPID.setD(SmartDashboard.getNumber("Elevator D", 0));
	}

	public void setTarget(double d) {
		elevatorPID.setSetpoint(d);
	}

	public void setMotor(double d) {
		// motor.set(com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput,
		// d);
		motor.set(ControlMode.PercentOutput, d);
	}

	public double getPosition() {
		return encoder.getDistance();
	}

	public double getVelocity() {
		return encoder.getRate();
	}

	public Encoder getEncoder() {
		return encoder;
	}
}
