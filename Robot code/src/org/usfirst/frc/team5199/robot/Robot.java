
package org.usfirst.frc.team5199.robot;

import arm.Arm;
import arm.ArmControl;
import autonomous.*;
import climber.Climber;
import climber.ClimberControl;
import controllers.AutController;
import controllers.JoystickController;
import controllers.XBoxController;
import drive.DriveBase;
import drive.DriveControl;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
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
import vision.Camera;
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
	private AutController autController;

	private Camera camera;

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
		nBroadcaster = new RemoteOutput("10.51.99.213", 1180);
		Robot.nBroadcaster.println("Starting up...");

		// dashboard = new SmartDashboard();
		new Compressor();

		xBox = new XBoxController(0);
		joy = new JoystickController(1);
		autController = new AutController(2);

		camera = new Camera();

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

		toolInterface = new PathToolInterface("10.51.99.213", 1181, base.getLocation());
		Robot.nBroadcaster.println("Ready");
	}

	@Override
	public void autonomous() {
		base.getLocation().reset();
		base.getGyro().reset();

		base.move(0, 0);
		gripper.close();
		arm.moveUp();

		String plates = DriverStation.getInstance().getGameSpecificMessage();

		ClockRegulator cl = new ClockRegulator(50);
		AutonomousManager autManager = new AutonomousManager(cl);

		// switch (autController.getAutMode()) {
		switch (1) {
		case 6:
			Multi toSwitch = new Multi(2);
			if (plates.charAt(0) == 'L') {
				toSwitch.add(0, new FollowPath(true, RecordedPaths.switchL(), driveControl, base, xBox));
				toSwitch.add(1, new Sleep(3000));

			} else if (plates.charAt(0) == 'R') {
				toSwitch.add(0, new FollowPath(true, RecordedPaths.switchR(), driveControl, base, xBox));
				toSwitch.add(1, new Sleep(1000));

			}

			toSwitch.add(1, new LowerArm(elevator, armControl));
			toSwitch.add(1, new MoveElevator(40, elevator, elevatorControl));

			autManager.add(toSwitch);

			autManager.add(new BoxOut(gripper, arm, elevator));
			break;
		case 1:
			Multi toScale = new Multi(2);
			if (plates.charAt(1) == 'L') {
				toScale.add(0, new FollowPath(true, RecordedPaths.scaleL(), driveControl, base, xBox));
				toScale.add(1, new Sleep(3000));
			} else if (plates.charAt(1) == 'R') {
				toScale.add(0, new FollowPath(true, RecordedPaths.scaleR(), driveControl, base, xBox));
				toScale.add(1, new Sleep(1000));
			}

			toScale.add(1, new LowerArm(elevator, armControl));
			toScale.add(1, new MoveElevator(80, elevator, elevatorControl));

			autManager.add(toScale);

			autManager.add(new BoxOut(gripper, arm, elevator));
			if (plates.charAt(1) == 'L') {
				//autManager.add(new FollowPath(true, RecordedPaths.scaleRSwitch(), driveControl, base, xBox));
			}else if (plates.charAt(1)=='R'){
				autManager.add(new FollowPath(true, RecordedPaths.scaleRSwitch(), driveControl, base, xBox));
			}
			

			break;
		}
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
		mainLoop.add(elevatorControl);
		mainLoop.add(gripperControl);
		mainLoop.add(armControl);
		mainLoop.add(recordPath);
		mainLoop.init();
		while (isEnabled() && isTest()) {
			mainLoop.update();
			Robot.nBroadcaster.println(elevator.getPosition());
			cl.sync();
		}
	}
}
