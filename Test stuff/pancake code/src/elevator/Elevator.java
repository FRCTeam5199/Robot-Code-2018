package elevator;

import org.usfirst.frc.team5199.robot.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.DigitalInput;

public class Elevator {
	// 1-10 ratio
	// 18 tooth 1.432 pitch diameter
	// 1024 ppr encoder
	private final double inchesPerPulse = (1.432d * 2) / (1024d * 10d);
	private Spark motor;
	private Encoder encoder;

	private DigitalInput a;
	private DigitalInput b;
	private DigitalInput x;

	public Elevator() {
		// motor = new TalonSRX(RobotMap.elevatorMotor);
		motor = new Spark(RobotMap.elevatorMotor);
		// encoder = new Encoder(RobotMap.liftEncoderA, RobotMap.liftEncoderB,
		// RobotMap.liftEncoderIndex);
		encoder = new Encoder(RobotMap.liftEncoderA, RobotMap.liftEncoderB);
		encoder.setDistancePerPulse(inchesPerPulse);
		encoder.setDistancePerPulse(1);
		//
		// a = new DigitalInput(RobotMap.liftEncoderA);
		// b = new DigitalInput(RobotMap.liftEncoderB);
		// x = new DigitalInput(RobotMap.liftEncoderIndex);
	}

	public void setMotor(double d) {
		// motor.set(com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput,
		// d);
		motor.set(d);
	}

	public void getPosition() {
		// return encoder.getDistance();
		// System.out.println(a.get() + " \t" + b.get() + " \t" + x.get());
		System.out.println(encoder.getDistance());
	}

	public double getVelocity() {
		return encoder.getRate();
	}
}
