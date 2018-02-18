package drive;

import org.usfirst.frc.team5199.robot.Robot;
import org.usfirst.frc.team5199.robot.RobotMap;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;

public class DriveBase {
	private Spark motorLeft;
	private Spark motorRight;

	private final ADXRS450_Gyro gyro;
	private final Encoder encoderLeft;

	private double PIDturn;
	private double PIDmove;

	public DriveBase() {
		gyro = new ADXRS450_Gyro();
		encoderLeft = new Encoder(RobotMap.leftEncoderA, RobotMap.leftEncoderB);
		
		encoderLeft.setDistancePerPulse(RobotMap.inchesPerPulse);

		motorLeft = new Spark(RobotMap.drivemotorLeft);
		motorRight = new Spark(RobotMap.drivemotorRight);

		Robot.nBroadcaster.println("Calibating gyro...");
		gyro.calibrate();
		Robot.nBroadcaster.println("done");
		gyro.reset();
	}

	public ADXRS450_Gyro getGyro() {
		return gyro;
	}

	public Encoder getEncoderL() {
		return encoderLeft;
	}
	
	public double getAvgDist(){
		return encoderLeft.getDistance();
	}
	
	public double getAvgRate(){
		return encoderLeft.getRate();
	}

	public void move(double left, double right) {
		motorLeft.set(-left);
		motorRight.set(right);
		// left is reversed
	}

	public void moveArcade(double y, double x) {
		move(y - x, y + x);
	}

	public void stop() {
		move(0, 0);
	}

	public void setPIDTurn(double d) {
		PIDturn = d;
	}

	public void setPIDMove(double d) {
		PIDmove = d;
	}

	public void applyPID() {
		moveArcade(PIDmove, PIDturn);
	}

	public void applyTurnPID(double y) {
		moveArcade(y, PIDturn);
	}

}
