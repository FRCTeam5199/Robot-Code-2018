
package org.usfirst.frc.team5199.robot;

import java.util.ArrayList;

import Sensors.Accelerometer;
import controllers.JoystickController;
import controllers.XBoxController;
import drive.DriveBase;
import drive.DriveControl;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.SampleRobot;
import elevator.Elevator;
import elevator.ElevatorControl;
import util.ClockRegulator;

/**
 * This is a demo program showing the use of the RobotDrive class. The
 * SampleRobot class is the base of a robot application that will automatically
 * call your Autonomous and OperatorControl methods at the right time as
 * controlled by the switches on the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're
 * inexperienced, don't. Unless you know what you are doing, complex code will
 * be much more difficult under this system. Use IterativeRobot or Command-Based
 * instead if you're new.
 */
public class Robot extends SampleRobot {

	private XBoxController xBox;
	private JoystickController joy;

	private DriveBase base;
	private Elevator elevator;

	private DriveControl driveControl;
	private ElevatorControl elevatorControl;

	public Robot() {

	}

	@Override
	public void robotInit() {
		xBox = new XBoxController(0);
		joy = new JoystickController(1);

		base = new DriveBase();
		elevator = new Elevator();

		driveControl = new DriveControl(base, xBox);
		elevatorControl = new ElevatorControl(elevator, joy);
	}

	@Override
	public void autonomous() {
	}

	@Override
	public void operatorControl() {
		ClockRegulator cl = new ClockRegulator(50);
		MainLoop mainLoop = new MainLoop(cl);

		mainLoop.add(driveControl);
		mainLoop.add(elevatorControl);

		while (isEnabled() && isOperatorControl()) {
			mainLoop.update();
		}

	}

	@Override
	public void test() {
	}
}
