package autonomous;

import java.util.ArrayList;

import org.usfirst.frc.team5199.robot.Robot;

import arm.Arm;
import climber.Climber;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import elevator.Elevator;
import gripper.Gripper;
import interfaces.AutFunction;
import util.ClockRegulator;
import drive.DriveBase;

public class AutonomousManager {

	private final ArrayList<ArrayList<AutFunction>> functions;
	private final ClockRegulator clockRegulator;
	private final ArrayList<Boolean> functionsDone;
	private int step;
	private boolean done;
	private boolean moveOn;

	public AutonomousManager(ClockRegulator clockRegulator) {
		this.clockRegulator = clockRegulator;
		functionsDone = new ArrayList<Boolean>();
		functions = new ArrayList<ArrayList<AutFunction>>();
		step = 0;
	}

	public void init() {
		for (int i = 0; i < functions.size(); i++) {
			functionsDone.add(false);
			if (functions.get(i).get(0) != null) {
				functions.get(i).get(0).init();
			}
		}
		done = false;
		moveOn = true;
	}

	public void update() {
		if (step < functions.get(0).size()) {
			for (int i = 0; i < functions.size(); i++) {
				if (step < functions.get(i).size()) {
					if (functions.get(i).get(step) != null) {
						functions.get(i).get(step).update(clockRegulator.getMsPerUpdate());
						if (functions.get(i).get(step).isDone()) {
							functions.get(i).get(step).cleanUp();
							Robot.nBroadcaster.println(functions.get(step).getClass().getName() + " end\t");
							functionsDone.set(i, true);
						}
					} else {
						functionsDone.set(i, true);
					}
				}
			}
			moveOn = true;
			for (int i = 0; i < functionsDone.size(); i++) {
				if (!functionsDone.get(i)) {
					moveOn = false;
				}
			}
			if (moveOn) {
				step++;
				for (int i = 0; i < functions.size(); i++) {
					functionsDone.add(false);
					if (functions.get(i).get(step) != null) {
						if(step < functions.get(0).size()) {
						functions.get(i).get(step).init();
						}
					}
				}
			}
		} else {
			if (!done) {
				Robot.nBroadcaster.println("AutonomousManager finished");
				done = true;
			}
		}
		clockRegulator.sync();
	}

	public void add(ArrayList<AutFunction> f) {
		functions.add(f);
	}

	public boolean isDone() {
		return done;
	}

}
