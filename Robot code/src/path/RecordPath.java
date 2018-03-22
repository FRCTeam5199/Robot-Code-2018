package path;

import org.usfirst.frc.team5199.robot.Robot;

import controllers.XBoxController;
import interfaces.LoopModule;
import maths.Vector2;
import sensors.Location;

public class RecordPath implements LoopModule {

	private final XBoxController xBox;

	private final int recMoveThreshold = 6;

	private final Location loc;

	private Vector2 lastPos;
	private String recording;

	public RecordPath(XBoxController xBox, Location location) {
		this.xBox = xBox;

		loc = location;

		lastPos = loc.getLocation();

		recording = "";
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(long delta) {
		Vector2 currentPos = loc.getLocation();

		if (Vector2.distance(currentPos, lastPos) > recMoveThreshold) {
			recording += "|" + currentPos;
			lastPos = currentPos;
		}

		if (xBox.getButton(8)) {
			System.out.println(recording + "|" + loc.getLocation() + "|");
			recording = "";
		}

	}
}
