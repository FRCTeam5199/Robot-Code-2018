
package org.usfirst.frc.team5199.robot;

import java.util.ArrayList;

import arm.Arm;
import arm.ArmControl;
import autonomous.*;
import climber.Climber;
import climber.ClimberControl;
import controllers.JoystickController;
import controllers.XBoxController;
import drive.DriveBase;
import drive.DriveControl;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SampleRobot;
import elevator.Elevator;
import elevator.ElevatorControl;
import gripper.Gripper;
import gripper.GripperControl;
import networking.RemoteOutput;
import path.FollowPath;
import path.PathToolInterface;
import path.RecordPath;
import path.RecordedPaths;
import util.ClockRegulator;
import wheelieBar.WheelieBar;
import wheelieBar.WheelieBarControl;

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

	public static RemoteOutput nBroadcaster;
	public static PathToolInterface toolInterface;
	// public static SmartDashboard dashboard;

	private XBoxController xBox;
	private JoystickController joy;

	private DriveBase base;
	private Elevator elevator;
	private Gripper gripper;
	private Arm arm;
	private Climber climber;
	private WheelieBar wheelieBar;

	private DriveControl driveControl;
	private ElevatorControl elevatorControl;
	private GripperControl gripperControl;
	private ArmControl armControl;
	private ClimberControl climberControl;
	private WheelieBarControl wheelieBarControl;

	private RecordPath recordPath;

	public Robot() {

	}

	@Override
	public void robotInit() {
		nBroadcaster = new RemoteOutput("10.51.99.78", 1180);
		Robot.nBroadcaster.println("Starting up...");

		// dashboard = new SmartDashboard();
		new Compressor();

		xBox = new XBoxController(0);
		joy = new JoystickController(1);

		base = new DriveBase();
		elevator = new Elevator();
		gripper = new Gripper();
		arm = new Arm();
		climber = new Climber();
		wheelieBar = new WheelieBar();

		driveControl = new DriveControl(base, xBox);
		elevatorControl = new ElevatorControl(elevator, joy);
		gripperControl = new GripperControl(gripper, joy);
		armControl = new ArmControl(arm, elevator.getEncoder(), joy);
		climberControl = new ClimberControl(climber, joy);
		wheelieBarControl = new WheelieBarControl(wheelieBar, joy);

		recordPath = new RecordPath(xBox, base.getLocation());

		toolInterface = new PathToolInterface("10.51.99.78", 1181, base.getLocation());
		Robot.nBroadcaster.println("Ready");
	}

	@Override
	public void autonomous() {
		ClockRegulator cl = new ClockRegulator(50);
		AutonomousManager autManager = new AutonomousManager(cl);

		// autManager.add(new LowerArm(elevator, armControl));
		// autManager.add(new MoveElevator(30, elevator, elevatorControl));
		autManager.add(new FollowPath(true, RecordedPaths.main2(), driveControl, base, xBox));
		// autManager.add(new BoxOut(gripper, arm, elevator));
		while (isEnabled() && isAutonomous() && !autManager.isDone()) {
			autManager.update();
		}
	}

	@Override
	public void operatorControl() {
		ClockRegulator cl = new ClockRegulator(50);
		MainLoop mainLoop = new MainLoop(cl);

		mainLoop.add(driveControl);
		mainLoop.add(elevatorControl);
		mainLoop.add(gripperControl);
		mainLoop.add(armControl);
		mainLoop.add(climberControl);
		mainLoop.add(wheelieBarControl);

		mainLoop.init();

		while (isEnabled() && isOperatorControl()) {
			mainLoop.update();
		}

	}

	@Override
	public void test() {
		ClockRegulator cl = new ClockRegulator(50);
		MainLoop mainLoop = new MainLoop(cl);
		mainLoop.add(driveControl);
		mainLoop.add(recordPath);
		mainLoop.init();
		while (isEnabled() && isTest()) {
			mainLoop.update();
			cl.sync();
		}
	}
}
