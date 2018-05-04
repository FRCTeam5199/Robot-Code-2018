package ultrasonicPositionChecker;

import maths.Ray2;
import maths.Vector2;

public class BoxCollider implements Collider {
	private final Vector2 pos;
	private final Vector2 dim;

	public BoxCollider(Vector2 pos, Vector2 dim) {
		this.pos = pos;
		this.dim = dim;
	}

	public BoxCollider(double xPos, double yPos, double xDim, double yDim) {
		this(new Vector2(xPos, yPos), new Vector2(xDim, yDim));
	}

	@Override
	public double castRay(Ray2 ray) {
		Vector2 centerPos = Vector2.add(pos, Vector2.divide(dim, 2));

		// double xMin = pos.getX();
		// double yMin = pos.getY();
		// double xMax = xMin + dim.getX();
		// double yMax = yMin + dim.getY();

		double xLine = pos.getX();
		double yLine = pos.getY();

		if (ray.getPos().getX() > centerPos.getX()) {
			xLine += dim.getX();
		}

		if (ray.getPos().getY() > centerPos.getY()) {
			yLine += dim.getY();
		}

		double slope = Math.tan(Math.PI / 2 - ray.getAngle());
		double yInt = lineEQInv(yLine, ray.getPos(), slope);
		double xInt = lineEQ(xLine, ray.getPos(), slope);

		if (yInt > pos.getX() && yInt < pos.getX() + dim.getX()) {
			if (yInt > ray.getPos().getX()) {
				if (Math.sin(ray.getAngle()) >= 0) {
					return dist(yInt, yLine, ray.getPos());
				}
			} else {
				if (Math.sin(ray.getAngle()) <= 0) {
					return dist(yInt, yLine, ray.getPos());
				}
			}
		} else if (xInt > pos.getY() && xInt < pos.getY() + dim.getY()) {
			if (xInt > ray.getPos().getY()) {
				if (Math.cos(ray.getAngle()) <= 0) {
					return dist(xLine, xInt, ray.getPos());
				}
			} else {
				if (Math.cos(ray.getAngle()) >= 0) {
					return dist(xLine, xInt, ray.getPos());
				}
			}

		}

		return Double.POSITIVE_INFINITY;

	}

	private double dist(double x, double y, Vector2 v) {
		return Math.sqrt(Math.pow(x - v.getX(), 2) + Math.pow(y - v.getY(), 2));
	}

	private double lineEQ(double x, Vector2 p, double slope) {
		// y = m (x-x1) + y1
		return slope * (x - p.getX()) + p.getY();
	}

	private double lineEQInv(double y, Vector2 p, double slope) {
		// x = x1 - (y + y1) / m
		// y - y1 = m (x - x1)
		// y - y1 = mx - mx1
		// mx = y - y1 + mx1
		return p.getX() + (y - p.getY()) / slope;
	}
}
