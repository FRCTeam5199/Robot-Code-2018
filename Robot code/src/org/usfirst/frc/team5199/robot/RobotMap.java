package org.usfirst.frc.team5199.robot;

public class RobotMap {
	private static final int mxpOffset = 10;
	// public static final double inchesPerPulse = 6 * Math.PI * 3 * 3 / 1024;
	// public static final double inchesPerPulse = 4;
	public static final double inchesPerPulse = 6 * Math.PI / 120;

	public static final int drivemotorRight = 8;
	public static final int drivemotorLeft = 9;
	public static final int leftEncoderA = 3 + mxpOffset;
	public static final int leftEncoderB = 11 + mxpOffset;

	public static final int liftMotor = 0;
	public static final int liftEncoderA = 0 + mxpOffset;
	public static final int liftEncoderB = 1 + mxpOffset;

	public static final int gripperMotorL = 1;
	public static final int gripperMotorR = 2;
	public static final int gripperPiston = 2;

	public static final int armPiston = 0;

	public static final int climberPiston = 1;
	public static final int climberMotor = 3;

}
