package elevator;

import org.usfirst.frc.team5199.robot.RobotMap;
import edu.wpi.first.wpilibj.Victor;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Elevator {
	// 12 tooth 1.432" pitch diameter
	// 15 tooth 1.790" pitch diameter
	// 1024 ppr encoder?
	private final double inchesPerPPR = 1.432d / 1024d;
	//private TalonSRX motor;
	private Victor motor;
	

	public Elevator() {
		//motor = new TalonSRX(RobotMap.elevatorMotor);
		motor = new Victor(RobotMap.elevatorMotor);
	}

	public void setMotor(double d) {
		//motor.set(com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput, d);
		motor.set(d);
	}

//	public double getLocation() {
//		//return motor.getSensorCollection().getQuadraturePosition() * inchesPerPPR;
//	}
//
//	public double getVelocity() {
//		//return motor.getSensorCollection().getQuadratureVelocity() * inchesPerPPR;
//	}
}
