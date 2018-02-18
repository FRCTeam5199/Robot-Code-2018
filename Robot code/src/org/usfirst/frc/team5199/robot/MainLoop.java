package org.usfirst.frc.team5199.robot;

import java.util.ArrayList;

import interfaces.LoopModule;
import util.ClockRegulator;;

public class MainLoop {
	private final ArrayList<LoopModule> objects;
	private final ClockRegulator clockRegulator;

	public MainLoop(ClockRegulator clockRegulator) {
		objects = new ArrayList<LoopModule>();
		this.clockRegulator = clockRegulator;
	}

	public void update() {
		for (LoopModule o : objects) {
			o.update(clockRegulator.getMsPerUpdate());
		}
		clockRegulator.sync();
	}

	public void init() {
		for (LoopModule o : objects) {
			o.init();
		}
	}

	public void add(LoopModule o) {
		objects.add(o);
	}

	public void remove(LoopModule o) {
		objects.remove(o);
	}
}
