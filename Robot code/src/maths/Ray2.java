package maths;

public class Ray2 {
	private final Vector2 pos;
	private final double angle;

	public Ray2(Vector2 pos, double angle) {
		this.pos = pos;
		this.angle = angle;
	}

	public Vector2 getPos() {
		return pos;
	}

	public double getAngle() {
		return angle;
	}
}
