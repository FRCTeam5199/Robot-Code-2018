package path;

import java.util.ArrayList;

import maths.Vector2;
import networking.ByteUtils;

public class Path {

	private final double radToDeg = 180d / Math.PI;
	private final double radiusBufferDist = 24;
	private final double turnRadiusSpeedK = 6;
	private final double maxAccel = 6; // inches per second squared
	private final double startEndSpeed = 2;
	// private final double maxSpeed = 55;
	private final double maxSpeed = 100;

	private PathNode[] checkpoints;

	int index = -1;

	public Path(PathNode[] checkpoints) {
		this.checkpoints = checkpoints;
	}

	public Path(String data) {
		checkpoints = parseStringData(data);
		checkpoints = calcTargetSpeed(checkpoints);
		checkpoints = calcSpeed(checkpoints);
	}

	public Path(byte[] data) {
		checkpoints = new PathNode[data.length / 24];
		for (int i = 0; i < checkpoints.length; i++) {
			int blockStart = i * 24;
			double x = ByteUtils.toDouble(ByteUtils.portionOf(data, blockStart, blockStart + 8));
			double y = ByteUtils.toDouble(ByteUtils.portionOf(data, blockStart + 8, blockStart + 16));
			double speed = ByteUtils.toDouble(ByteUtils.portionOf(data, blockStart + 16, blockStart + 24));

			checkpoints[i] = new PathNode(new Vector2(x, y), speed);
		}
	}

	private PathNode[] calcTargetSpeed(PathNode[] path) {

		for (int i = 0; i < path.length; i++) {
			int aheadLead = 0;
			int behindLag = 0;

			Vector2 current = path[i].getPos();
			Vector2 ahead = path[i + aheadLead].getPos();
			Vector2 behind = current;

			Vector2 last = current;

			double aheadDist = 0;
			double behindDist = 0;
			while (aheadDist <= radiusBufferDist / 2 && i + aheadLead < path.length - 1) {
				aheadLead++;
				last = ahead;
				ahead = path[i + aheadLead].getPos();
				aheadDist += Vector2.distance(last, ahead);
			}

			last = current;

			while (behindDist <= radiusBufferDist / 2 && i - behindLag > 0) {
				behindLag++;
				last = behind;
				behind = path[i - behindLag].getPos();
				behindDist += Vector2.distance(last, ahead);
			}

			path[i].setSpeed(Math.sqrt(circleRadius(ahead, current, behind))*turnRadiusSpeedK);

		}

		path[0].setSpeed(startEndSpeed);
		path[path.length - 1].setSpeed(startEndSpeed);

		for (int i = 0; i < path.length; i++) {
			path[i].setSpeed(clamp(path[i].getSpeed(), 0, maxSpeed));
		}

		return path;
	}

	private double circleRadius(Vector2 a, Vector2 b, Vector2 c) {
		// r = ABC/4K
		double ab = Vector2.distance(a, b);
		double bc = Vector2.distance(b, c);
		double ca = Vector2.distance(c, a);
		return (ab * bc * ca) / (4 * triangleArea(a, b, c));
	}

	private double triangleArea(Vector2 a, Vector2 b, Vector2 c) {
		// A = 1/2*abs((x2-x1)(y3-y1)-(x3-x1)(y2-y1))
		return .5 * Math
				.abs((b.getX() - a.getX()) * (c.getY() - a.getY()) - (c.getX() - a.getX()) * (b.getY() - a.getY()));
	}

	private PathNode[] calcTargetSpeedOld(PathNode[] path) {
		Vector2 n0 = path[0].getPos();
		Vector2 n1 = path[1].getPos();
		Vector2 last = n0;

		ArrayList<Double> radiusBuffer = new ArrayList<Double>();
		ArrayList<Double> distBufferF = new ArrayList<Double>();
		ArrayList<Double> distBufferR = new ArrayList<Double>();
		double distSumF = 0;
		double distSumR = 0;
		double radiusSum = 0;

		int fBufferLead = -1;

		double lastTheta = radToDeg * Math.atan2(n1.getY() - n0.getY(), n1.getX() - n0.getY());
		for (int i = 1; i < path.length - 1; i++) {
			double absRadius = 0;

			while (distSumF < radiusBufferDist / 2) {
				fBufferLead++;

				if (i + fBufferLead >= path.length) {
					radiusBuffer.add(0.0);
					double lastDist = distBufferF.get(distBufferF.size() - 1);
					distBufferF.add(lastDist);
					distSumF += lastDist;
					break;
				}

				Vector2 current = path[i + fBufferLead].getPos();
				double d = Vector2.distance(last, current);
				double theta = radToDeg * Math.atan2(current.getY() - last.getY(), current.getX() - last.getX());

				double dTheta = theta - lastTheta;

				absRadius = Math.abs((180 * d) / (dTheta * Math.PI));

				lastTheta = theta;
				last = current;

				radiusBuffer.add(absRadius);
				radiusSum += absRadius;
				distBufferF.add(d);
				distSumF += d;
			}
			while (distSumF > radiusBufferDist / 2) {
				double distRm = distBufferF.remove(0);
				distSumF -= distRm;
				distBufferR.add(distRm);
				distSumR += distRm;
				fBufferLead--;
			}

			while (distSumR > radiusBufferDist / 2 && radiusBuffer.size() > 1) {
				double distRm = distBufferR.remove(0);
				distSumR -= distRm;
				double radRm = radiusBuffer.remove(0);
				radiusSum -= radRm;
			}

			path[i].setSpeed(Math.sqrt(radiusSum / radiusBuffer.size()) * turnRadiusSpeedK);

			System.out.println(distSumF + "\t" + distBufferF.size() + "\t|\t" + distSumR + "\t" + distBufferR.size());

			if (path[i].getSpeed() == Double.NaN || Double.isInfinite(path[i].getSpeed())) {
				System.out.println("==========================================================");
				System.out.println(radiusSum + "\t" + radiusBuffer.size() + "\t" + radiusSum / radiusBuffer.size());
				System.out.println(path[i - 1]);
				System.out.println("*" + path[i]);
				System.out.println(path[i + 1]);
				System.exit(-1);
			}
		}

		path[0].setSpeed(startEndSpeed);
		path[path.length - 1].setSpeed(startEndSpeed);

		for (int i = 0; i < path.length; i++) {
			path[i].setSpeed(clamp(path[i].getSpeed(), 0, maxSpeed));
		}

		return path;

	}

	private double clamp(double n, double lClamp, double hClamp) {
		if (n < lClamp) {
			return lClamp;
		} else if (n > hClamp) {
			return hClamp;
		} else {
			return n;
		}
	}

	private PathNode[] calcSpeed(PathNode[] checkpoints) {
		for (int i = 1; i < checkpoints.length; i++) {
			double dist = Vector2.distance(checkpoints[i].getPos(), checkpoints[i - 1].getPos());
			double vf = checkpoints[i].getSpeed();
			double vi = checkpoints[i - 1].getSpeed();
			double reachableSpeed = reachableSpeed(vf, vi, dist, maxAccel);

			if (reachableSpeed == vf) {
				// nothing
			} else if (reachableSpeed < vf) {
				checkpoints[i].setSpeed(reachableSpeed);
			} else if (reachableSpeed > vf) {
				int prevIndex = i;
				int distSum = 0;
				double startingSpeed = Double.NEGATIVE_INFINITY;
				boolean madeChange = true;
				while (prevIndex > 0 && madeChange) {
					madeChange = false;
					distSum += Vector2.distance(checkpoints[prevIndex].getPos(), checkpoints[prevIndex - 1].getPos());
					vi = checkpoints[prevIndex - 1].getSpeed();

					startingSpeed = startingSpeed(vf, distSum, -maxAccel);

					if (startingSpeed < vi) {
						checkpoints[prevIndex - 1].setSpeed(startingSpeed);
						madeChange = true;
					}

					prevIndex--;
				}
			}
		}

		return checkpoints;
	}

	private PathNode[] parseStringData(String data) {
		ArrayList<Vector2> checkpointData = new ArrayList<>();
		int separatorIndex = data.indexOf('|');
		while (separatorIndex >= 0) {
			int nextSeparator = data.indexOf('|', separatorIndex + 1);
			if (nextSeparator >= 0) {
				int commaPos = data.indexOf(',', separatorIndex);

				double x = Double.parseDouble(data.substring(separatorIndex + 1, commaPos));
				double y = Double.parseDouble(data.substring(commaPos + 1, nextSeparator));

				checkpointData.add(new Vector2(x, y));
			}
			separatorIndex = nextSeparator;
		}

		PathNode[] checkpoints = new PathNode[checkpointData.size()];

		for (int i = 0; i < checkpoints.length; i++) {
			checkpoints[i] = new PathNode(checkpointData.get(i), -1);
		}
		return checkpoints;
	}

	private double startingSpeed(double vf, double d, double maxAccel) {
		return Math.sqrt(vf * vf - (2 * maxAccel * d));
	}

	private double reachableSpeed(double vf, double vi, double d, double maxAccel) {
		if (vf < vi) {
			// dec
			double minSpeed = Math.sqrt(vi * vi + 2 * -maxAccel * d);
			if (minSpeed > vf) {
				return minSpeed;
			} else {
				return vf;
			}
		} else if (vf > vi) {
			// accel
			double maxSpeed = Math.sqrt(vi * vi + 2 * maxAccel * d);
			if (maxSpeed < vf) {
				return maxSpeed;
			} else {
				return vf;
			}
		} else {
			// nothing
			return vf;
		}
	}

	public PathNode getCheckpoint() {
		index++;
		if (index < checkpoints.length) {
			return checkpoints[index];
		} else {
			return null;
		}
	}

	public PathNode getCheckpoint(int n) {
		if (n < checkpoints.length) {
			return checkpoints[n];
		} else {

			// interpolate another point
			Vector2 lastPos = getCheckpoint(n - 1).getPos();
			Vector2 delta = Vector2.subtract(lastPos, getCheckpoint(n - 2).getPos());
			return new PathNode(Vector2.add(lastPos, delta), 0);
		}
	}

	public int getLength() {
		return checkpoints.length;
	}

	public boolean isLastCheckpoint() {
		return index == checkpoints.length;
	}

	public void reset() {
		index = -1;
	}
}