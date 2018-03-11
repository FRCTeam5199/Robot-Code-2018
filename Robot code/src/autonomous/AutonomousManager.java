package autonomous;

import java.util.ArrayList;

import org.usfirst.frc.team5199.robot.Robot;

import interfaces.AutFunction;
import util.ClockRegulator;

public class AutonomousManager {
	private final ArrayList<AutFunction> functions;
	private final ClockRegulator clockRegulator;

	private int step;
	private boolean done;
	private boolean firstRun;

	public AutonomousManager(ClockRegulator clockRegulator) {
		this.clockRegulator = clockRegulator;

		functions = new ArrayList<AutFunction>();
		step = 0;
		firstRun = true;
		done = false;
	}

	public void update() {
		if (functions.isEmpty()) {
			return;
		}
		if (firstRun) {
			functions.get(0).init();
		}

		firstRun = false;

		if (step < functions.size()) {
			functions.get(step).update(clockRegulator.getMsPerUpdate());
			if (functions.get(step).isDone()) {
				functions.get(step).cleanUp();
				Robot.nBroadcaster.println(functions.get(step).getClass().getName() + " end\t");
				step++;
				if (step < functions.size()) {
					functions.get(step).init();
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

	public void add(AutFunction f) {
		functions.add(f);
	}

	public boolean isDone() {
		return done;
	}

}
