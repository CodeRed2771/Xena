
package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends TimedRobot {

	KeyMap gamepad;
	SendableChooser<String> autoChooser;
	final String calibrateSwerveModules = "Calibrate Swerve Modules";
	final String deleteSwerveCalibration = "Delete Swerve Calibration";
	String autoSelected;
	AutoBaseClass mAutoProgram;

	@Override
	public void robotInit() {
		gamepad = new KeyMap();
		RobotGyro.getInstance();
		DriveTrain.getInstance();
		CubeClaw.getInstance();
		Lift.getInstance();

		Calibration.loadSwerveCalibration();

		RobotGyro.reset();  // this may need to be done later to give Gyro time to initialize


		autoChooser = new SendableChooser<String>();
		autoChooser.addObject(calibrateSwerveModules, calibrateSwerveModules);
		autoChooser.addObject(deleteSwerveCalibration, deleteSwerveCalibration);

		SmartDashboard.putNumber("Auto P:", Calibration.AUTO_DRIVE_P);
		SmartDashboard.putNumber("Auto I:", Calibration.AUTO_DRIVE_I);
		SmartDashboard.putNumber("Auto D:", Calibration.AUTO_DRIVE_D);

		SmartDashboard.putData("Auto choices", autoChooser);

    	SmartDashboard.putNumber("Robot Position", 1);
	}

	@Override
	public void autonomousInit() {
		String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	int robotPosition = (int) SmartDashboard.getNumber("Robot Position",1);

		autoSelected = (String) autoChooser.getSelected();
		SmartDashboard.putString("Auto Selected: ", autoSelected);
		SmartDashboard.putString("GameData", gameData);

		mAutoProgram = null;

		switch(autoSelected) {
			case calibrateSwerveModules:
				double[] pos = DriveTrain.getAllAbsoluteTurnOrientations();
				Calibration.saveSwerveCalibration(pos[0], pos[1], pos[2], pos[3]);
				break;
			case deleteSwerveCalibration:
				Calibration.resetSwerveDriveCalibration();
				break;
		}

		DriveAuto.reset();
		DriveTrain.setAllTurnOrientiation(0);

		if (mAutoProgram != null) {
			mAutoProgram.start();
		}
	}

	@Override
	public void autonomousPeriodic() {
	
	}

	@Override
	public void teleopInit() {

	}

	@Override
	public void teleopPeriodic() {
		DriveTrain.fieldCentricDrive(gamepad.getSwerveYAxis(), -gamepad.getSwerveXAxis(), powerOf2PreserveSign(gamepad.getSwerveRotAxis()));

		Lift.move(gamepad.getLiftAxis());
		SmartDashboard.putNumber("Lift Speed", gamepad.getLiftAxis());
		
		if (Math.abs(gamepad.getClawIntakeAxis())>.05) {
			CubeClaw.intakeCube();
//			CubeClaw.testIntakeCube(gamepad.getClawIntakeAxis());
//			SmartDashboard.putNumber("Intake Speed", gamepad.getClawIntakeAxis());
		} else if (Math.abs(gamepad.getClawEjectAxis())>.05) {
			CubeClaw.ejectCube();
//			CubeClaw.testEjectCube(gamepad.getClawEjectAxis());
//			SmartDashboard.putNumber("Eject Speed", gamepad.getClawEjectAxis());
		} else
			CubeClaw.stopIntake();
		
		
		CubeClaw.armMove(gamepad.getArmAxis());
		
		if (gamepad.openClaw()) {
			CubeClaw.open();
		} else if (gamepad.closeClaw()) {
			CubeClaw.close();
		} else 
		{
			CubeClaw.off();
		}
		
	}

	@Override
	public void testInit() {

	}

	@Override
	public void testPeriodic() {
	}


	public void disabledInit() {
		DriveTrain.allowTurnEncoderReset(); // allows the turn encoders to be reset once during disabled periodic
		DriveTrain.resetDriveEncoders();
	}

	public void disabledPeriodic() {
		DriveTrain.resetTurnEncoders();   // happens only once because a flag prevents multiple calls
		DriveTrain.disablePID();
	}

	private double powerOf2PreserveSign(double v) {
		return (v > 0 ) ? Math.pow(v, 2) : -Math.pow(v, 2);
	}
}
