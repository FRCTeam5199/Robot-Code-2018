
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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import elevator.Elevator;
import elevator.ElevatorControl;
import gripper.Gripper;
import gripper.GripperControl;
import maths.Vector2;
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

	private Vector2 startLeft;
	private Vector2 startRight;
	private Vector2 startMid;

	public Robot() {

	}

	@Override
	public void robotInit() {

		nBroadcaster = new RemoteOutput("10.51.99.78", 5800);
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

		SmartDashboard.putString("Start pos", "");

		startLeft = new Vector2(29.69 + (34 / 2), 4);
		startRight = new Vector2(324 - (29.69 + (34 / 2)), 4);
		startMid = new Vector2(162, 4);

		toolInterface = new PathToolInterface("10.51.99.78", 5801, base.getLocation());
		Robot.nBroadcaster.println("Ready");
	}

	@Override
	public void autonomous() {
		long switchStraight = 0;
		long switchCross = 3000;
		long switchMid = 0;

		long scaleStraight = 0;
		long scaleCrossHalf = 0;
		long scaleCrossEnd = 3000;

		int scaleHeight = 80;
		int scaleMidHeight = 40;
		int switchHeight = 40;

		base.getLocation().reset();
		base.getGyro().reset();

		base.move(0, 0);
		gripper.close();
		gripper.setSpeed(0);
		elevator.setMotor(0);
		arm.moveUp();

		String plates = DriverStation.getInstance().getGameSpecificMessage();

		SmartDashboard.putString("Plates", plates);

		Robot.nBroadcaster.println(plates);

		ClockRegulator cl = new ClockRegulator(50);
		AutonomousManager autManager = new AutonomousManager(cl);

		int autSelect = autController.getAutMode();
		Robot.nBroadcaster.println("Aut controller:" + autSelect);

		autManager.add(new ReleaseWheelieBar(base));

		if (autSelect == 6 || autSelect == 2 || autSelect == 4) {
			Multi toSwitch = new Multi(2);
			if (plates.charAt(0) == 'L') {
				if (autSelect == 6) {
					// start right
					base.getLocation().set(startRight);
					toSwitch.add(0, new FollowPath(true, RecordedPaths.switchRtoL(), driveControl, base, xBox));
					toSwitch.add(1, new Sleep(switchCross));
				} else if (autSelect == 2) {
					// start left
					base.getLocation().set(startLeft);
					toSwitch.add(0, new FollowPath(true, RecordedPaths.switchLtoL(), driveControl, base, xBox));
					toSwitch.add(1, new Sleep(switchStraight));
				} else if (autSelect == 4) {
					// start mid
					base.getLocation().set(startMid);
					toSwitch.add(0, new FollowPath(true, RecordedPaths.switchMtoL(), driveControl, base, xBox));
					toSwitch.add(1, new Sleep(switchMid));
				}

			} else if (plates.charAt(0) == 'R') {
				if (autSelect == 6) {
					// start right
					base.getLocation().set(startRight);
					toSwitch.add(0, new FollowPath(true, RecordedPaths.switchRtoR(), driveControl, base, xBox));
					toSwitch.add(1, new Sleep(switchStraight));
				} else if (autSelect == 2) {
					// start left
					base.getLocation().set(startLeft);
					toSwitch.add(0, new FollowPath(true, RecordedPaths.switchLtoR(), driveControl, base, xBox));
					toSwitch.add(1, new Sleep(switchCross));
				} else if (autSelect == 4) {
					// start mid
					base.getLocation().set(startMid);
					toSwitch.add(0, new FollowPath(true, RecordedPaths.switchMtoR(), driveControl, base, xBox));
					toSwitch.add(1, new Sleep(switchMid));
				}

			}

			toSwitch.add(1, new LowerArm(elevator, armControl));
			toSwitch.add(1, new MoveElevator(switchHeight, elevator, elevatorControl));

			autManager.add(toSwitch);

			autManager.add(new BoxOut(gripper, arm, elevator));
		} else if (autSelect == 1 || autSelect == 3) {
			Multi toScale = new Multi(2);
			if (plates.charAt(1) == 'L') {
				if (autSelect == 1) {
					// start right
					base.getLocation().set(startRight);
					toScale.add(0, new FollowPath(true, RecordedPaths.scaleRtoL(), driveControl, base, xBox));
					toScale.add(1, new Sleep(scaleCrossHalf));

					toScale.add(1, new LowerArm(elevator, armControl));
					toScale.add(1, new MoveElevator(scaleMidHeight, elevator, elevatorControl));
					toScale.add(1, new Sleep(scaleCrossEnd));
					toScale.add(1, new MoveElevator(scaleHeight, elevator, elevatorControl));
				} else if (autSelect == 3) {
					// start left
					base.getLocation().set(startLeft);
					toScale.add(0, new FollowPath(true, RecordedPaths.scaleLtoL(), driveControl, base, xBox));
					toScale.add(1, new Sleep(scaleStraight));

					toScale.add(1, new LowerArm(elevator, armControl));
					toScale.add(1, new MoveElevator(scaleHeight, elevator, elevatorControl));
				}
			} else if (plates.charAt(1) == 'R') {
				if (autSelect == 1) {
					// start right
					base.getLocation().set(startRight);
					toScale.add(0, new FollowPath(true, RecordedPaths.scaleRtoR(), driveControl, base, xBox));
					toScale.add(1, new Sleep(scaleStraight));

					toScale.add(1, new LowerArm(elevator, armControl));
					toScale.add(1, new MoveElevator(scaleHeight, elevator, elevatorControl));
				} else if (autSelect == 3) {
					base.getLocation().set(startLeft);
					toScale.add(0, new FollowPath(true, RecordedPaths.scaleLtoR(), driveControl, base, xBox));
					toScale.add(1, new Sleep(scaleCrossHalf));

					toScale.add(1, new LowerArm(elevator, armControl));
					toScale.add(1, new MoveElevator(scaleMidHeight, elevator, elevatorControl));
					toScale.add(1, new Sleep(scaleCrossEnd));
					toScale.add(1, new MoveElevator(scaleHeight, elevator, elevatorControl));
				}
			}

			autManager.add(toScale);

			autManager.add(new BoxOut(gripper, arm, elevator, 1));

			autManager.add(new Move(-36, base, driveControl));
			autManager.add(new MoveElevator(0, elevator, elevatorControl));
			autManager.add(new GoTo(new Vector2(82, 263), base, driveControl));
			autManager.add(new GetBox(new Vector2(85, 222), base, driveControl, elevatorControl, gripper));
			autManager.add(new MoveInterp(-24, 6, base, driveControl));
			autManager.add(new MoveElevator(switchHeight, elevator, elevatorControl));
			autManager.add(new Move(24, base, driveControl));
			autManager.add(new BoxOut(gripper, arm, elevator));

		} else if (autSelect == 4) {
			autManager.add(new MoveInterp(132, 6, base, driveControl));
		} else if (autSelect == 5) {
			// base.getLocation().set(startRight);
			// autManager.add(new LowerArm(elevator, armControl));
			// autManager.add(new GetBox(new Vector2(162, 106), base,
			// driveControl, elevatorControl, gripper));
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

		String startPos = SmartDashboard.getString("Start pos", "");

		if (startPos.equals("L")) {
			base.getLocation().set(startLeft);
		} else if (startPos.equals("R")) {
			base.getLocation().set(startRight);
		} else if (startPos.equals("M")) {
			base.getLocation().set(startMid);
		}

		while (isEnabled() && isTest()) {
			mainLoop.update();
			Robot.nBroadcaster.println(elevator.getPosition() + "\t" + base.getEncoderL().getDistance() + "\t"
					+ base.getEncoderR().getDistance());
			cl.sync();
		}
	}
}
