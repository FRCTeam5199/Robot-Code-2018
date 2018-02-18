package gripper;

import org.usfirst.frc.team5199.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Solenoid;

public class Gripper {
	private VictorSPX motorL;
	private VictorSPX motorR;
	private Solenoid piston;

	public Gripper() {
		motorL = new VictorSPX(RobotMap.gripperMotorL);
		motorR = new VictorSPX(RobotMap.gripperMotorR);
		piston = new Solenoid(RobotMap.gripperPiston);
	}

	public void setSpeed(double n) {
		motorL.set(ControlMode.PercentOutput, n);
		motorR.set(ControlMode.PercentOutput, -n);
	}
	
	public void setL(double n){
		motorL.set(ControlMode.PercentOutput, n);
	}
	
	public void setR(double n){
		motorR.set(ControlMode.PercentOutput, n);
	}
	
	public void rotate(double n){
		motorL.set(ControlMode.PercentOutput, n);
		motorR.set(ControlMode.PercentOutput, n);
	}

	public void setPiston(boolean b) {
		piston.set(b);
	}

	public void close() {
		piston.set(false);
	}

	public void open() {
		piston.set(true);
	}
}
