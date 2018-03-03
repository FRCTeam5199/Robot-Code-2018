/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5199.robot;

import path.FollowPath;
import path.PathToolInterface;
import path.RecordPath;
import path.RecordedPaths;
import autonomous.AutonomousManager;
import controllers.XBoxController;
import drive.DriveBase;
import drive.DriveControl;
import edu.wpi.first.wpilibj.SampleRobot;
import networking.RemoteOutput;
import path.RecordedPaths;
import sensors.Location;
import sensors.Sensors;
import util.ClockRegulator;

/**
 * This is a demo program showing the use of the RobotDrive class. The
 * SampleRobot class is the base of a robot application that will automatically
 * call your Autonomous and OperatorControl methods at the right time as
 * controlled by the switches on the driver station or the field controls.
 *
 * <p>
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 *
 * <p>
 * WARNING: While it may look like a good choice to use for your code if you're
 * inexperienced, don't. Unless you know what you are doing, complex code will
 * be much more difficult under this system. Use IterativeRobot or Command-Based
 * instead if you're new.
 */
public class Robot extends SampleRobot {

	public static RemoteOutput nBroadcaster;
	public static PathToolInterface toolInterface;;
	public static Sensors sensors;

	private ClockRegulator clockRegulator;

	private XBoxController xBox;

	private RecordPath recordPath;

	private DriveBase base;
	private DriveControl driveControl;

	public Robot() {
	}

	@Override
	public void robotInit() {
		nBroadcaster = new RemoteOutput("10.51.99.206", 1180);
		Robot.nBroadcaster.println("Starting up...");
		sensors = new Sensors();

		clockRegulator = new ClockRegulator(50);

		xBox = new XBoxController(0);

		recordPath = new RecordPath(xBox);

		base = new DriveBase();
		driveControl = new DriveControl(base, xBox);
		toolInterface = new PathToolInterface("10.51.99.206", 1181);

	}

	@Override
	public void autonomous() {

		AutonomousManager autManager = new AutonomousManager(clockRegulator);

		autManager.add(new FollowPath(true, RecordedPaths.outside(), driveControl, base, xBox));
		// mainLoop.add(driveControl);

		// mainLoop.add(pathFollower);

		autManager.init();
		while (isAutonomous() && isEnabled()) {
			autManager.update();
		}

	}

	@Override
	public void operatorControl() {
		Robot.nBroadcaster.println("teleop start");
		sensors.getLocation().reset();
		MainLoop mainLoop = new MainLoop(clockRegulator);

		mainLoop.add(new FollowPath(false, RecordedPaths.outside(), driveControl, base, xBox));

		// mainLoop.add(driveControl);

		// mainLoop.add(recordPath);

		mainLoop.init();
		while (isOperatorControl() && isEnabled()) {
			xBox.setLRumble(xBox.getLTrigger());
			xBox.setRRumble(xBox.getRTrigger());
			mainLoop.update();
		}
	}

	/**
	 * Runs during test mode.
	 */
	@Override
	public void test() {
		while (isTest() && isEnabled()) {

		}
	}
}
