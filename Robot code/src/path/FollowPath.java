package path;

import org.usfirst.frc.team5199.robot.Robot;

import controllers.XBoxController;
import drive.DriveBase;
import drive.DriveControl;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.PIDSourceType;
import interfaces.AutFunction;
import interfaces.LoopModule;
import maths.Vector2;
import sensors.Location;

public class FollowPath implements AutFunction, LoopModule {

	private final double crossTrackP = 6.5;
	private final double maxAbortSpeed = 1;
	private final long abortTimeout = 2000;
	private final int maxAbortDistance = 12;

	private final double radToDeg = 180 / Math.PI;

	private final XBoxController controller;
	private final DriveControl driveControl;
	private final DriveBase base;
	private final Location loc;
	private final Path path;
	private final boolean isAutonomous;

	private double[] nodeLineSlopes;
	private double[] nodeLineAngles;

	private PathNode currentCheckpoint;
	private PathNode lastCheckpoint;
	private Vector2 lastMovedPos;
	private long lastMovedTimeout;
	private int checkpointIndex;

	private boolean isDone;

	public FollowPath(boolean isAutonomous, Path path, DriveControl driveControl, DriveBase base,
			XBoxController controller) {
		this.controller = controller;
		this.driveControl = driveControl;
		this.base = base;
		this.path = path;

		this.isAutonomous = isAutonomous;
		isDone = false;
		loc = base.getLocation();
	}

	@Override
	public void update(long deltaTime) {
		if (base.getAvgRate() < maxAbortSpeed) {
			// robot isn't moving...
			Robot.nBroadcaster
					.println("Robot stuck. Abort in " + (lastMovedTimeout - System.currentTimeMillis()) + " ms");
			if (System.currentTimeMillis() > lastMovedTimeout
					&& Vector2.distance(lastMovedPos, loc.getLocation()) < maxAbortDistance) {
				// robot hasn't moved for abortTimeout ms and robot is close to
				// target
				Robot.nBroadcaster.println("Close enough meh");
				isDone = true;
			} else {
				// welp
			}
		} else {
			// robot is moving, set last moved time and pos
			lastMovedTimeout = System.currentTimeMillis() + abortTimeout;
			lastMovedPos = loc.getLocation();
		}

		if (!isDone) {
			double error = getCrossTrackError();

			Robot.nBroadcaster.println(clamp(error * crossTrackP, 180, -180));

			double dCurrent = Vector2.distance(loc.getLocation(), currentCheckpoint.getPos());
			double dLast = Vector2.distance(loc.getLocation(), lastCheckpoint.getPos());

			double interpAngle;
			double interpSpeed;

			if (dCurrent + dLast != 0 && checkpointIndex > 0) {
				interpSpeed = (dCurrent * lastCheckpoint.getSpeed() + dLast * currentCheckpoint.getSpeed())
						/ (dCurrent + dLast);

				double lastAngle = nodeLineAngles[checkpointIndex - 1];
				double currentAngle = nodeLineAngles[checkpointIndex];

				if (lastAngle - currentAngle > Math.PI) {
					currentAngle += 2 * Math.PI;
				} else if (currentAngle - lastAngle > Math.PI) {
					lastAngle += 2 * Math.PI;
				}

				interpAngle = (dCurrent * lastAngle + dLast * currentAngle) / (dCurrent + dLast);
			} else {
				interpSpeed = currentCheckpoint.getSpeed();
				interpAngle = nodeLineAngles[checkpointIndex];
			}

			interpAngle *= radToDeg;

			driveControl.setTurnPID(interpAngle - clamp(error * crossTrackP, 90, -90));
			driveControl.setMovePID(interpSpeed);

			if (isAutonomous || controller.getButton(1)) {
				base.applyPID();
			} else {
				base.move(0, 0);
			}
			while (!isDone && checkpointDone()) {
				Robot.nBroadcaster.println("Checkpoint " + checkpointIndex + " done");
				Robot.toolInterface.setCheckpointIndex(checkpointIndex);
				checkpointIndex++;
				if (checkpointIndex < path.getLength()) {
					lastCheckpoint = currentCheckpoint;
					currentCheckpoint = path.getCheckpoint(checkpointIndex);
				} else {
					isDone = true;
					base.move(0, 0);
				}

			}
		}
	}

	public boolean checkpointDone() {
		Vector2 pos = loc.getLocation();
		double nodePerpSlope = -1 / nodeLineSlopes[checkpointIndex];
		double yLine = lineEquation(pos.getX(), nodePerpSlope, currentCheckpoint.getPos());
		Vector2 nextCheckpointPos = path.getCheckpoint(checkpointIndex + 1).getPos();
		boolean aboveLine = nextCheckpointPos.getY() > lineEquation(nextCheckpointPos.getX(), nodePerpSlope,
				currentCheckpoint.getPos());

		return (pos.getY() > yLine && aboveLine) || (pos.getY() < yLine && !aboveLine);
	}

	public double getCrossTrackError() {
		Vector2 nodePos = currentCheckpoint.getPos();
		Vector2 robotPos = loc.getLocation();
		double slope = nodeLineSlopes[checkpointIndex];
		double perpSlope = -1 / slope;

		double crossX = getXCross(slope, nodePos, perpSlope, robotPos);

		Vector2 cross = new Vector2(crossX, lineEquation(crossX, slope, nodePos));

		int sign;

		double yPos = lineEquation(robotPos.getX(), slope, nodePos);

		if (nodeLineAngles[checkpointIndex] <= Math.PI) {
			sign = sign(yPos - robotPos.getY());
		} else {
			sign = sign(robotPos.getY() - yPos);
		}

		return sign * Vector2.subtract(cross, robotPos).length();
	}

	public double lineEquation(double x, double m, Vector2 p) {
		return m * (x - p.getX()) + p.getY();
	}

	public double getXCross(double m1, Vector2 p1, double m2, Vector2 p2) {
		return (m1 * p1.getX() - p1.getY() - m2 * p2.getX() + p2.getY()) / (m1 - m2);
	}

	public int sign(double d) {
		if (d > 0) {
			return 1;
		} else if (d < 0) {
			return -1;
		} else {
			return 0;
		}
	}

	@Override
	public void init() {
		nodeLineSlopes = new double[path.getLength()];

		nodeLineAngles = new double[path.getLength()];

		for (int i = 0; i < nodeLineAngles.length - 1; i++) {
			Vector2 segment = Vector2.subtract(path.getCheckpoint(i + 1).getPos(), path.getCheckpoint(i).getPos());
			nodeLineAngles[i] = Math.atan2(segment.getX(), segment.getY());
			if (nodeLineAngles[i] < 0) {
				nodeLineAngles[i] += Math.PI * 2;
			}
		}

		nodeLineAngles[nodeLineAngles.length - 1] = nodeLineAngles[nodeLineAngles.length - 2];

		for (int i = 1; i < path.getLength() - 1; i++) {
			nodeLineSlopes[i] = 1 / Math.tan((nodeLineAngles[i - 1] + nodeLineAngles[i]) / 2d);
		}

		nodeLineSlopes[0] = Vector2.getSlope(path.getCheckpoint(0).getPos(), path.getCheckpoint(1).getPos());
		nodeLineSlopes[path.getLength() - 1] = Vector2.getSlope(path.getCheckpoint(path.getLength() - 2).getPos(),
				path.getCheckpoint(path.getLength() - 1).getPos());

		Robot.toolInterface.sendPath(path);

		driveControl.setTurnPIDDisplace();
		driveControl.setMovePIDRate();
		driveControl.enableTurnPID();
		driveControl.enableMovePID();
		currentCheckpoint = path.getCheckpoint();
		lastCheckpoint = currentCheckpoint;
		checkpointIndex = 0;

		lastMovedTimeout = System.currentTimeMillis() + abortTimeout;
		lastMovedPos = loc.getLocation();

		path.reset();
	}

	private double clamp(double n, double max, double min) {
		if (n > max) {
			return max;
		} else if (n < min) {
			return min;
		} else {
			return n;
		}
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void cleanUp() {
		base.stop();
	}

}
