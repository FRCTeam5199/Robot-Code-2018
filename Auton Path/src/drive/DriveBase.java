package drive;

import org.usfirst.frc.team5199.robot.Robot;
import org.usfirst.frc.team5199.robot.RobotMap;

import edu.wpi.first.wpilibj.Spark;

public class DriveBase {
	private Spark left;
	private Spark right;

	private double PIDturn;
	private double PIDmove;

	public DriveBase() {
		left = new Spark(RobotMap.leftMotor);
		right = new Spark(RobotMap.rightMotor);
	}

	public void move(double r, double l) {
		// right is reversed

		left.set(l);
		right.set(-r);
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
