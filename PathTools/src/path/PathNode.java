package path;

import maths.Vector2;

public class PathNode {
	private Vector2 position;
	private double speed;

	public PathNode(Vector2 position, double speed) {
		this.position = position;
		this.speed = speed;
	}

	public Vector2 getPos() {
		return position;
	}

	public double getSpeed() {
		return speed;
	}

	public void setPos(Vector2 position) {
		this.position = position;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public String toString() {
		return position + "\t" + speed;
	}

	public PathNode clone() {
		return new PathNode(position, speed);
	}
}
