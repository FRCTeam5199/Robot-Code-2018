package climber;

import org.usfirst.frc.team5199.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Solenoid;

public class Climber {
	private Solenoid piston;
	private VictorSPX motor;

	public Climber() {
		piston = new Solenoid(RobotMap.climberPiston);
		motor = new VictorSPX(RobotMap.climberMotor);
	}

	public void setMotor(double d) {
		motor.set(ControlMode.PercentOutput, d);
	}

	public void setPiston(boolean b) {
		piston.set(b);
	}

	public void release() {
		piston.set(true);
	}

	public void lock() {
		piston.set(false);
	}
	
	public void setBrakeOn(){
		motor.setNeutralMode(NeutralMode.Brake);
	}
	
	public void setBrakeOff(){
		motor.setNeutralMode(NeutralMode.Coast);
	}
}
