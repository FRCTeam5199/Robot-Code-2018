/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maths;

/**
 *
 * @author 18wakayamats
 */
public class Vector2I implements Comparable<Vector2I> {

	public static final Vector2I ZERO = new Vector2I(0, 0);
	public static final Vector2I FORWARDS = new Vector2I(0, 1);
	public static final Vector2I BACKWARDS = new Vector2I(0, -1);
	public static final Vector2I LEFT = new Vector2I(-1, 0);
	public static final Vector2I RIGHT = new Vector2I(1, 0);

	private static final double degToRad = Math.PI / 180;

	private int x;
	private int y;

	public Vector2I() {
		this.x = 0;
		this.y = 0;
	}

	public Vector2I(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vector2I(Vector2I other) {
		this.x = other.x;
		this.y = other.y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public double length() {
		return Math.sqrt((x * x) + (y * y));
	}

	// Deprecated now that we have the copy constructor?
	public Vector2I clone() {
		return new Vector2I(x, y);
	}

	@Override
	public String toString() {
		return x + "," + y;
	}

	@Override
	public int compareTo(Vector2I other) {
		if (this.x == other.x) {
			return (int) (this.y - other.y);
		} else {
			return (int) (this.x - other.x);
		}
	}

	public static Vector2I add(Vector2I v1, Vector2I v2) {
		return new Vector2I(v1.x + v2.x, v1.y + v2.y);
	}

	public static Vector2I subtract(Vector2I v1, Vector2I v2) {
		return new Vector2I(v1.x - v2.x, v1.y - v2.y);
	}

	public static Vector2I multiply(Vector2I v1, Vector2I v2) {
		return new Vector2I(v1.x * v2.x, v1.y * v2.y);
	}

	public static Vector2I multiply(Vector2I v1, int n) {
		return new Vector2I(v1.x * n, v1.y * n);
	}

	public static Vector2I divide(Vector2I v1, Vector2I v2) {
		return new Vector2I(v1.x / v2.x, v1.y / v2.y);
	}

	public static Vector2I divide(Vector2I v, int n) {
		return new Vector2I(v.x / n, v.y / n);
	}

	// public static Vector2I rotate(Vector2I v, double d) {
	// double sin = Math.sin(d);
	// double cos = Math.cos(d);
	// return new Vector2I(v.getX() * cos - v.getY() * sin, v.getX() * sin +
	// v.getY() * cos);
	// }
	//
	// public static Vector2I rotateCW(Vector2I v, double d) {
	// return rotate(v, -d);
	// }
	//
	// public static Vector2I rotateDeg(Vector2I v, double d) {
	// return rotate(v, d * degToRad);
	// }
	//
	// public static Vector2I rotateDegCW(Vector2I v, double d) {
	// return rotateDeg(v, -d);
	// }

}
