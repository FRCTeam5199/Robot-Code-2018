
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
import led.LED;
import led.LEDControl;
import led.LEDControl.Color;
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
	private LED led;

	private DriveControl driveControl;
	private ElevatorControl elevatorControl;
	private GripperControl gripperControl;
	private ArmControl armControl;
	private ClimberControl climberControl;
	private WheelieBarControl wheelieBarControl;
	private LEDControl ledControl;

	private RecordPath recordPath;

	private Vector2 startLeft;
	private Vector2 startRight;
	private Vector2 startMid;
	private Vector2 stackBoxPos;
	private Vector2 backMR;
	private Vector2 backML;
	private Vector2 backMM;

	public Robot() {

	}

	@Override
	public void robotInit() {

		nBroadcaster = new RemoteOutput("10.51.99.213", 5800);
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
		led = new LED();

		driveControl = new DriveControl(base, xBox);
		elevatorControl = new ElevatorControl(elevator, joy);
		gripperControl = new GripperControl(gripper, joy);
		armControl = new ArmControl(arm, elevator.getEncoder(), joy);
		climberControl = new ClimberControl(climber, joy);
		wheelieBarControl = new WheelieBarControl(wheelieBar, joy);
		ledControl = new LEDControl(led);

		recordPath = new RecordPath(xBox, base.getLocation());

		SmartDashboard.putString("Start pos", "");

		startLeft = new Vector2(29.69 + (34 / 2), 4);
		startRight = new Vector2(324 - (29.69 + (34 / 2)), 4);
		startMid = new Vector2(162, 4);
		stackBoxPos = new Vector2(162, 108);
		backML = new Vector2(42, 168);
		backMR = new Vector2(282, 168);
		backMM = Vector2.add(startMid, new Vector2(0, 18));

		toolInterface = new PathToolInterface("10.51.99.213", 5801, base.getLocation());
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

		int scaleHeight = 83;
		int scaleMidHeight = 40;
		int switchHeight = 40;

		int boxLeft = 90;
		int boxRight = 234;

		base.getGyro().reset();
		base.getLocation().reset();

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
					toSwitch.add(0, new FollowPath(true, false, RecordedPaths.switchRtoL(), driveControl, base, xBox));
					toSwitch.add(1, new Sleep(switchCross));
				} else if (autSelect == 2) {
					// start left
					base.getLocation().set(startLeft);
					toSwitch.add(0, new FollowPath(true, false, RecordedPaths.switchLtoL(), driveControl, base, xBox));
					toSwitch.add(1, new Sleep(switchStraight));
				} else if (autSelect == 4) {
					// start mid
					base.getLocation().set(startMid);
					toSwitch.add(0, new FollowPath(true, false, RecordedPaths.switchMtoL(), driveControl, base, xBox));
					toSwitch.add(1, new Sleep(switchMid));
				}

			} else if (plates.charAt(0) == 'R') {
				if (autSelect == 6) {
					// start right
					base.getLocation().set(startRight);
					toSwitch.add(0, new FollowPath(true, false, RecordedPaths.switchRtoR(), driveControl, base, xBox));
					toSwitch.add(1, new Sleep(switchStraight));
				} else if (autSelect == 2) {
					// start left
					base.getLocation().set(startLeft);
					toSwitch.add(0, new FollowPath(true, false, RecordedPaths.switchLtoR(), driveControl, base, xBox));
					toSwitch.add(1, new Sleep(switchCross));
				} else if (autSelect == 4) {
					// start mid
					base.getLocation().set(startMid);
					toSwitch.add(0, new FollowPath(true, false, RecordedPaths.switchMtoR(), driveControl, base, xBox));
					toSwitch.add(1, new Sleep(switchMid));
				}

			}

			toSwitch.add(1, new LowerArm(elevator, armControl));
			toSwitch.add(1, new MoveElevator(switchHeight, elevator, elevatorControl));

			autManager.add(toSwitch);

			autManager.add(new BoxOut(.5, gripper, arm, elevator));

			// if (autSelect == 4) {
			// // autManager.add(new TurnTo(0, base, driveControl));
			//
			// Multi toStack = new Multi(2);
			// toStack.add(0, new Sleep(500));
			// toStack.add(0, new RaiseArm(elevator, armControl));
			// toStack.add(0, new MoveElevator(1, elevator, elevatorControl));
			// toStack.add(1, new GoToReverse(backMM, base, driveControl));
			// toStack.add(1, new GetBox(stackBoxPos, base, driveControl,
			// elevatorControl, gripper));
			// autManager.add(toStack);
			// Multi toSwitch2 = new Multi(2);
			// toSwitch2.add(0, new MoveElevator(switchHeight, elevator,
			// elevatorControl));
			// toSwitch2.add(1, new GoToReverse(backMM, base, driveControl));
			// if (plates.charAt(0) == 'R') {
			// toSwitch2.add(1, new FollowPath(true, false,
			// RecordedPaths.switchMtoR(), driveControl, base, xBox));
			// } else if (plates.charAt(0) == 'L') {
			// toSwitch2.add(1, new FollowPath(true, false,
			// RecordedPaths.switchMtoL(), driveControl, base, xBox));
			// }
			// autManager.add(toSwitch2);
			//
			// autManager.add(new BoxOut(.5, gripper, arm, elevator));
			// }

		} else if (autSelect == 1 || autSelect == 3) {
			Multi toScale = new Multi(2);
			if (plates.charAt(1) == 'L') {
				if (autSelect == 1) {
					// start right
					base.getLocation().set(startRight);
					toScale.add(0, new FollowPath(true, false, RecordedPaths.scaleRtoL(), driveControl, base, xBox));
					toScale.add(1, new Sleep(scaleCrossHalf));

					toScale.add(1, new LowerArm(elevator, armControl));
					toScale.add(1, new MoveElevator(scaleMidHeight, elevator, elevatorControl));
					toScale.add(1, new Sleep(scaleCrossEnd));
					toScale.add(1, new MoveElevator(scaleHeight, elevator, elevatorControl));
				} else if (autSelect == 3) {
					// start left
					base.getLocation().set(startLeft);
					toScale.add(0, new FollowPath(true, false, RecordedPaths.scaleLtoL(), driveControl, base, xBox));
					toScale.add(1, new Sleep(scaleStraight));

					toScale.add(1, new LowerArm(elevator, armControl));
					toScale.add(1, new MoveElevator(scaleHeight, elevator, elevatorControl));
				}
			} else if (plates.charAt(1) == 'R') {
				if (autSelect == 1) {
					// start right
					base.getLocation().set(startRight);
					toScale.add(0, new FollowPath(true, false, RecordedPaths.scaleRtoR(), driveControl, base, xBox));
					toScale.add(1, new Sleep(scaleStraight));

					toScale.add(1, new LowerArm(elevator, armControl));
					toScale.add(1, new MoveElevator(scaleHeight, elevator, elevatorControl));
				} else if (autSelect == 3) {
					base.getLocation().set(startLeft);
					toScale.add(0, new FollowPath(true, false, RecordedPaths.scaleLtoR(), driveControl, base, xBox));
					toScale.add(1, new Sleep(scaleCrossHalf));

					toScale.add(1, new LowerArm(elevator, armControl));
					toScale.add(1, new MoveElevator(scaleMidHeight, elevator, elevatorControl));
					toScale.add(1, new Sleep(scaleCrossEnd));
					toScale.add(1, new MoveElevator(scaleHeight, elevator, elevatorControl));
				}
			}

			autManager.add(toScale);
			autManager.add(new BoxOut(.33, gripper, arm, elevator));

			// Multi moveBack = new Multi(2);
			// moveBack.add(0, new Sleep(2000));
			// moveBack.add(0, new MoveElevator(1, elevator, elevatorControl));
			//
			// if (plates.charAt(1) == 'L') {
			// moveBack.add(1, new GoToReverse(backML, base, driveControl));
			// } else if (plates.charAt(1) == 'R') {
			// moveBack.add(1, new GoToReverse(backMR, base, driveControl));
			// }
			// autManager.add(moveBack);

		} else if (autSelect == 5) {
			autManager.add(new MoveInterp(132, .04, base, driveControl));
		} else if (autSelect == 7) {
			autManager.add(new GoToReverse(new Vector2(-24, -36), base, driveControl));
		} else if (autSelect == 8) {
			autManager.add(new GoToReverse(new Vector2(0, -36), base, driveControl));
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

		mainLoop.cleanUp();

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
		} else if (startPos.equals("MR")) {
			base.getLocation().set(startMid, 180);
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int color = 0;

		long changeTime = System.currentTimeMillis();

		while (isEnabled() && isTest()) {
			mainLoop.update();
			// Robot.nBroadcaster.println(elevator.getPosition() + "\t" +
			// elevator.getVelocity() + "\t"
			// + base.getEncoderL().getDistance() + "\t" +
			// base.getEncoderR().getDistance());
			// Robot.nBroadcaster.println(base.getUltraFront());
			if (System.currentTimeMillis() > changeTime) {
				changeTime += 1000;
				switch (color) {
				case 0:
					ledControl.setColor(Color.BLACK);
					break;
				case 1:
					ledControl.setColor(color);
				}
				Robot.nBroadcaster.println(color);
				color++;
				if (color >= 8) {
					color = 0;
				}
			}
			cl.sync();
		}
	}
}
