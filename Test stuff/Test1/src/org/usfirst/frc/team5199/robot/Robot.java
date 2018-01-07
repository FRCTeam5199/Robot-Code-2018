/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5199.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import networking.RemoteOutput;
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
	private Sensors sensors;

	public Robot() {
	}

	@Override
	public void robotInit() {
		nBroadcaster = new RemoteOutput("10.51.99.206", 1180);
		Robot.nBroadcaster.println("Starting up...");
		sensors = new Sensors();
	}

	@Override
	public void autonomous() {
		while (isAutonomous() && isEnabled()) {

		}
	}

	/**
	 * Runs the motors with arcade steering.
	 *
	 * <p>
	 * If you wanted to run a similar teleoperated mode with an IterativeRobot
	 * you would write:
	 *
	 * <blockquote>
	 * 
	 * <pre>
	 * {@code
	 * // This function is called periodically during operator control
	 * public void teleopPeriodic() {
	 *     myRobot.arcadeDrive(stick);
	 * }
	 * }
	 * </pre>
	 * 
	 * </blockquote>
	 */
	@Override
	public void operatorControl() {
		ClockRegulator regulator = new ClockRegulator(100);
		Location loc = sensors.getLocation();
//		while (isOperatorControl() && isEnabled()) {
//			Robot.nBroadcaster.println(loc.getLocation());
//			regulator.sync();
//		}
		loc.reset();
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
