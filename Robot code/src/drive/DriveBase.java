package drive;

import org.usfirst.frc.team5199.robot.Robot;
import org.usfirst.frc.team5199.robot.RobotMap;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Ultrasonic;
import sensors.Location;
import sensors.UltrasonicManager;

public class DriveBase {
	private final Spark motorLeft;
	private final Spark motorRight;
	private final Solenoid gearboxPiston;

	private final ADXRS450_Gyro gyro;
	private final Encoder encoderLeft;
	private final Encoder encoderRight;
	private final Ultrasonic ultraFront;
	private final Ultrasonic ultraRear;
	private final Ultrasonic ultraLeft;
	private final Ultrasonic ultraRight;
	private final UltrasonicManager ultraManager;
	private final Location location;
	// private final BrapBrap brapBrap;

	private double PIDturn;
	private double PIDmove;

	public DriveBase() {
		// brapBrap = new BrapBrap();

		gyro = new ADXRS450_Gyro();

		motorLeft = new Spark(RobotMap.drivemotorLeft);
		motorRight = new Spark(RobotMap.drivemotorRight);
		gearboxPiston = new Solenoid(RobotMap.gearboxPiston);

		encoderLeft = new Encoder(RobotMap.leftEncoderA, RobotMap.leftEncoderB);
		encoderRight = new Encoder(RobotMap.rightEncoderA, RobotMap.rightEncoderB);
		// encoderLeft.setDistancePerPulse(RobotMap.wheelEncoderLIPP);
		// encoderRight.setDistancePerPulse(RobotMap.wheelEncoderRIPP);
		encoderLeft.setDistancePerPulse(RobotMap.wheelEncoderIPP);
		encoderRight.setDistancePerPulse(RobotMap.wheelEncoderIPP);

		ultraFront = new Ultrasonic(RobotMap.frontUltraPing, RobotMap.frontUltraEcho);
		ultraRear = new Ultrasonic(RobotMap.rearUltraPing, RobotMap.rearUltraEcho);
		ultraLeft = new Ultrasonic(RobotMap.leftUltraPing, RobotMap.leftUltraEcho);
		ultraRight = new Ultrasonic(RobotMap.rightUltraPing, RobotMap.rightUltraEcho);

		// ultraFront.setAutomaticMode(true);

		ultraManager = new UltrasonicManager();
		ultraManager.add(ultraFront);
		ultraManager.add(ultraLeft);
		ultraManager.add(ultraRight);
		ultraManager.add(ultraRear);
		ultraManager.start();

		// ultraFront.ping();

		Robot.nBroadcaster.println("Calibating gyro...");
		gyro.calibrate();
		Robot.nBroadcaster.println("done");
		gyro.reset();

		location = new Location(gyro, encoderLeft, encoderRight);
		location.start();
	}

	public ADXRS450_Gyro getGyro() {
		return gyro;
	}

	public Encoder getEncoderL() {
		return encoderLeft;
	}

	public Encoder getEncoderR() {
		return encoderRight;

	}

	public double getUltraFront() {
		if (ultraFront.isRangeValid()) {
			return ultraFront.getRangeInches() + RobotMap.frontUltraOffset;
		} else {
			return Double.NaN;
		}
	}

	public double getUltraRear() {
		if (ultraRear.isRangeValid()) {
			return ultraRear.getRangeInches() + RobotMap.rearUltraOffset;
		} else {
			return Double.NaN;
		}
	}

	public double getUltraRight() {
		if (ultraRight.isRangeValid()) {
			return ultraRight.getRangeInches() + RobotMap.rightUltraOffset;
		} else {
			return Double.NaN;
		}
	}

	public double getUltraLeft() {
		if (ultraLeft.isRangeValid()) {
			return ultraLeft.getRangeInches() + RobotMap.leftUltraOffset;
		} else {
			return Double.NaN;
		}
	}

	public Location getLocation() {
		return location;
	}

	public double getAvgDist() {
		return (encoderLeft.getDistance() + encoderRight.getDistance()) / 2;
		// return encoderLeft.getDistance();
	}

	public double getAvgRate() {
		return (encoderLeft.getRate() + encoderRight.getRate()) / 2;
		// return encoderLeft.getRate();
	}

	public void move(double left, double right) {
		motorLeft.set(-left);
		motorRight.set(right);
		// brapBrap.updateMotorPower(left, right);
		// left is reversed
	}

	public void moveArcade(double y, double x) {
		move(y - x, y + x);
	}

	public void setGearbox(boolean b) {
		gearboxPiston.set(b);
		// if(b){
		// brapBrap.start();
		// }
	}

	public void setLowGear() {
		gearboxPiston.set(true);
		// brapBrap.start();
	}

	public void setHighGear() {
		gearboxPiston.set(false);
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
