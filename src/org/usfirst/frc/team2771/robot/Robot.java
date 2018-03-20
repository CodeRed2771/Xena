
package org.usfirst.frc.team2771.robot;

import java.math.BigDecimal;
import java.math.RoundingMode;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {

	KeyMap gamepad;
	Compressor compressor;
	SendableChooser<String> autoChooser;
	final String autoBaseLine = "Auto Base Line";
	final String autoCenterSwitch = "Auto Center Switch";
	final String autoSwitchOrScale = "Auto Switch or Scale";

	final String calibrateSwerveModules = "Calibrate Swerve Modules";
	final String deleteSwerveCalibration = "Delete Swerve Calibration";

	final String autoRotateTest = "Rotate Test";
	final String autoCalibrateDrive = "Auto Calibrate Drive";

	// not used (at least not yet)
	final String autoSwitch = "Auto Switch";
	final String autoCubeFollow = "Auto Cube Follow";
	final String autoScale = "Auto Scale";
	final String autoSwitchAndScale = "Auto Switch and Scale";
	final String autoStartToSwitch = "Auto start to Switch";
	
	String autoSelected;
	AutoBaseClass mAutoProgram;

	@Override
	public void robotInit() {
		gamepad = new KeyMap();
		RobotGyro.getInstance();
		DriveTrain.getInstance();
		DriveAuto.getInstance();
		CubeClaw.getInstance();
		Lift.getInstance();

		Calibration.loadSwerveCalibration();

		autoChooser = new SendableChooser<String>();
		autoChooser.addDefault(autoBaseLine, autoBaseLine);
		autoChooser.addObject(calibrateSwerveModules, calibrateSwerveModules);
		autoChooser.addObject(deleteSwerveCalibration, deleteSwerveCalibration);
		autoChooser.addObject(autoRotateTest, autoRotateTest);
		autoChooser.addObject(autoCalibrateDrive, autoCalibrateDrive);
		autoChooser.addObject(autoCenterSwitch, autoCenterSwitch);
		autoChooser.addObject(autoSwitchOrScale, autoSwitchOrScale);
//		autoChooser.addObject(autoSwitchAndScale, autoSwitchAndScale);
//		autoChooser.addObject(autoScale, autoScale);
//		autoChooser.addObject(autoStartToSwitch, autoStartToSwitch);
//		autoChooser.addObject(autoCubeFollow, autoCubeFollow);

		SmartDashboard.putData("Auto choices", autoChooser);

		SmartDashboard.putString("Robot Position", "C");

		compressor = new Compressor(0);
		compressor.setClosedLoopControl(true);

		CubeClaw.resetArmEncoder();

		DriveTrain.setDriveModulesPIDValues(Calibration.AUTO_DRIVE_P, Calibration.AUTO_DRIVE_I,
				Calibration.AUTO_DRIVE_D);

		RobotGyro.reset(); // this is also done in auto init in case it wasn't
							// settled here yet

		// SmartDashboard.putNumber("Auto P:", Calibration.AUTO_DRIVE_P);
		// SmartDashboard.putNumber("Auto I:", Calibration.AUTO_DRIVE_I);
		// SmartDashboard.putNumber("Auto D:", Calibration.AUTO_DRIVE_D);

	}

	/*
	 * 
	 * TELEOP PERIODIC
	 * 
	 */
	@Override
	public void teleopPeriodic() {

		double driveYAxisAmount = gamepad.getSwerveYAxis();

		if (Lift.driveCautionNeeded()) {
			// limit the Y axis input to slow driving down
			if (Math.abs(driveYAxisAmount) > .25) {
				if (driveYAxisAmount < 0)
					driveYAxisAmount = -.25;
				else
					driveYAxisAmount = .25;
			}
		}

		DriveTrain.fieldCentricDrive(driveYAxisAmount, -gamepad.getSwerveXAxis(),
				powerOf2PreserveSign(gamepad.getSwerveRotAxis()));

		if (gamepad.activateIntake()) { // 2 - right bumper
			CubeClaw.setArmHorizontalPosition();
			CubeClaw.intakeCube(); // this will transition to a "hold" when the
									// current breaker is tripped
		}

		if (gamepad.dropCube()) { // 2 - left bumper
			CubeClaw.stopIntake();
			CubeClaw.dropCube();
		}

		if (gamepad.armLiftModifier()) {
			System.out.println("arm modifier pressed");
		}

		if (gamepad.armLiftModifier() && gamepad.gotoLiftFloor()) {
			System.out.println("pickup high cube position");
			Lift.goPortalPosition();
			CubeClaw.setArmHorizontalPosition();
		} else if (gamepad.gotoLiftFloor()) { // 2 - A
			CubeClaw.stopIntake();
			Lift.goStartPosition();
			CubeClaw.setArmHorizontalPosition();
		}

		if (gamepad.armLiftModifier() && gamepad.gotoLiftSwitch()) {
			Lift.goPickSecondCubePosition();
			CubeClaw.setArmHorizontalPosition();
		} else if (gamepad.gotoLiftSwitch()) { // 2 - B
			CubeClaw.holdCube();
			CubeClaw.setArmSwitchPosition();
			Lift.goSwitch();
		}

		if (gamepad.gotoLiftScale()) { // 2 - Y
			CubeClaw.holdCube();
			CubeClaw.setArmScalePosition();
			Lift.goHighScale();
		}

		if (gamepad.goToTravelPosition()) {
			CubeClaw.holdCube();
			CubeClaw.setArmTravelPosition();
		}

		if (gamepad.getHID(0).getRawButton(1)) { // 1- A
			CubeClaw.setArmHorizontalPosition();
		}

		if (gamepad.getHID(0).getRawButton(2)) { // 1 - B
			CubeClaw.holdCube();
			CubeClaw.setArmTravelPosition();
		}

		if (gamepad.getArmAxis() > .1 || gamepad.getArmAxis() < -.1) {
			CubeClaw.armMove(gamepad.getArmAxis());
		}

		if (gamepad.goLowGear()) { // 2 - Back
			Lift.setLowGear();
		}

		if (gamepad.goHighGear()) { // 2 - start
			Lift.setHighGear();

		}

		if (gamepad.manualLift() > .1 || gamepad.manualLift() < -.1) { // 2 -
																		// left
																		// stick
			Lift.moveSetpoint(-gamepad.manualLift());
		}

		if (gamepad.ejectCube()) {
			CubeClaw.ejectCube();
		}

		if (gamepad.overTheTop() && Lift.isOverTheTopHeight()) {
			CubeClaw.setArmOverTheTopPosition();
		}

		SmartDashboard.putNumber("Lift Power", gamepad.getLiftAxis());
		SmartDashboard.putNumber("Gyro Heading", RobotGyro.getAngle());

		Lift.tick();
		CubeClaw.tick();

	}

	private char getSwitchPosition(String gameData) {
		return gameData.toCharArray()[0];
	}

	private char getScalePosition(String gameData) {
		return gameData.toCharArray()[1];
	}

	@Override
	public void autonomousInit() {
		String gameData = DriverStation.getInstance().getGameSpecificMessage();
		char robotPosition = SmartDashboard.getString("Robot Position", "C").toCharArray()[0];

		RobotGyro.reset();

		autoSelected = (String) autoChooser.getSelected();
		SmartDashboard.putString("Auto Selected: ", autoSelected);
		SmartDashboard.putString("GameData", gameData);

		mAutoProgram = null;

		switch (autoSelected) {
		case autoBaseLine:
			mAutoProgram = new AutoBaseLine(robotPosition);
			break;
		case autoCenterSwitch:
			mAutoProgram = new AutoMainCenterSwitch(robotPosition);
			break;
		case autoSwitchOrScale:
			if (robotPosition == 'R') {
				if (getSwitchPosition(gameData) == 'R') {
					mAutoProgram = new AutoStartToSwitch(robotPosition);
				} else if (getScalePosition(gameData) == 'R') {
					mAutoProgram = new AutoStartToScale(robotPosition);
				} else {
					mAutoProgram = new AutoBaseLine(robotPosition);
				}
			} else if (robotPosition == 'L') {
				if (getSwitchPosition(gameData) == 'L') {
					mAutoProgram = new AutoStartToSwitch(robotPosition);
				} else if (getScalePosition(gameData) == 'L') {
					mAutoProgram = new AutoStartToScale(robotPosition);
				} else {
					mAutoProgram = new AutoBaseLine(robotPosition);
				}
			} else {
				mAutoProgram = new AutoBaseLine(robotPosition);
			}

			break;
		case autoRotateTest:
			mAutoProgram = new AutoRotateTest(robotPosition);
			break;
		case autoCalibrateDrive:
			mAutoProgram = new AutoCalibrateDrive(robotPosition);
			break;
		case calibrateSwerveModules:
			double[] pos = DriveTrain.getAllAbsoluteTurnOrientations();
			Calibration.saveSwerveCalibration(pos[0], pos[1], pos[2], pos[3]);
			break;
		case deleteSwerveCalibration:
			Calibration.resetSwerveDriveCalibration();
			break;
		case autoScale:
			mAutoProgram = new AutoStartToScale(robotPosition);
			break;
		case autoStartToSwitch:
			mAutoProgram = new AutoStartToSwitch(robotPosition);
			break;
		case autoCubeFollow:
			mAutoProgram = new AutoCubeFollow(robotPosition);
			break;
		}

		DriveAuto.reset();
		DriveTrain.setAllTurnOrientiation(0);

		System.out.println("end of auto init");

		if (mAutoProgram != null) {
			mAutoProgram.start();
		} else
			System.out.println("No auto program started in switch statement");
	}

	@Override
	public void autonomousPeriodic() {

		if (mAutoProgram != null) {
			mAutoProgram.tick();
			SmartDashboard.putNumber("Elapsed Time TICK", System.currentTimeMillis());

		}

		DriveAuto.tick();
		CubeClaw.tick();
		Lift.tick();

		DriveAuto.showEncoderValues();

		// DriveTrain.setDriveModulesPIDValues(SmartDashboard.getNumber("Auto
		// P:", 0),
		// SmartDashboard.getNumber("Drive I:", 0),
		// SmartDashboard.getNumber("Auto D:", 0));
	}

	@Override
	public void teleopInit() {
		CubeClaw.resetArmEncoder();
		Lift.stop();
		CubeClaw.setArmTravelPosition();
	}

	@Override
	public void testInit() {
	}

	@Override
	public void testPeriodic() {
	}

	public void disabledInit() {
		DriveTrain.allowTurnEncoderReset(); // allows the turn encoders to be
											// reset once during disabled
											// periodic
		DriveTrain.resetDriveEncoders();
	}

	public void disabledPeriodic() {
		DriveTrain.resetTurnEncoders(); // happens only once because a flag
										// prevents multiple calls
		DriveTrain.disablePID();

		SmartDashboard.putNumber("Gyro", round2(RobotGyro.getAngle()));

		// System.out.println("arm abs " + CubeClaw.getArmAbsolutePosition());

		CubeClaw.tick();
		Lift.tick();
	}

	private double powerOf2PreserveSign(double v) {
		return (v > 0) ? Math.pow(v, 2) : -Math.pow(v, 2);
	}

	private static Double round2(Double val) {
		// added this back in on 1/15/18
		return new BigDecimal(val.toString()).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
}
