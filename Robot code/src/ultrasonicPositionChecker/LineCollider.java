package ultrasonicPositionChecker;

import maths.Ray2;
import maths.Vector2;

public class LineCollider implements Collider {

	private final Vector2 point;
	private final double slope;

	public LineCollider(Vector2 v1, Vector2 v2) {
		point = v1;
		slope = (v1.getY() - v2.getY()) / (v1.getX() - v2.getX());
	}

	@Override
	public double castRay(Ray2 ray) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}
