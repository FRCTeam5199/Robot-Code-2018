package util;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.PIDOutput;

public class VictorSPXPID extends VictorSPX implements PIDOutput {

	public VictorSPXPID(int deviceNumber) {
		super(deviceNumber);
	}

	public void pidWrite(double output) {
		set(ControlMode.PercentOutput, output);
	}

}
