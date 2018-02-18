package sensors;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;
import maths.Vector3;

public class Accelerometer {
	private BuiltInAccelerometer accel;
	private Vector3 drift;

	public Accelerometer() {
		accel = new BuiltInAccelerometer();
		accel.setRange(Range.k8G);
	}

	public void calibrate(long time) {
		System.out.println("Calibrating accelerometer...");

		Vector3 newDrift = Vector3.zero();
		int count = 0;
		time += System.currentTimeMillis();
		while (System.currentTimeMillis() < time) {
			count++;
			newDrift.setX(newDrift.getX() + accel.getX());
			newDrift.setY(newDrift.getY() + accel.getY());
			newDrift.setZ(newDrift.getZ() + accel.getZ());
		}
		newDrift = Vector3.divide(newDrift, count);

		System.out.println("Drifting at " + newDrift);

		drift = newDrift;
	}

	public Vector3 read() {
		return Vector3.subtract(new Vector3(accel.getX(), accel.getY(), accel.getZ()), drift);
	}
}
