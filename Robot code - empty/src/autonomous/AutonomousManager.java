package autonomous;

import java.util.ArrayList;

import org.usfirst.frc.team5199.robot.Robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import interfaces.AutFunction;
import util.ClockRegulator;
import drive.DriveBase;

public class AutonomousManager {
	private final ArrayList<AutFunction> functions;
	private final ClockRegulator clockRegulator;

	private final Stop stop;

	private int step;
	private boolean done;

	public AutonomousManager(DriveBase base, ClockRegulator clockRegulator) {
		this.clockRegulator = clockRegulator;

		stop = new Stop(base);

		functions = new ArrayList<AutFunction>();
		step = 0;
	}

	public void init() {
		functions.get(0).init();
		stop.update(1);
	}

	public void update() {
		if (step < functions.size()) {
			functions.get(step).update(clockRegulator.getMsPerUpdate());
			if (functions.get(step).isDone()) {
				System.out.println(functions.get(step).getClass().getName() + " end\t");
				stop.update(1);
				step++;
				if (step < functions.size()) {
					functions.get(step).init();
				}
			}
		} else {
			if (!done) {
				System.out.println("AutonomousManager finished");
				stop.update(1);
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
