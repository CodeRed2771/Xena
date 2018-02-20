
package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends TimedRobot {

	KeyMap gamepad;
	SendableChooser<String> autoChooser;
	final String autoDriveDoubleDiamond = "Auto Drive Double Diamond";
	final String autoRotateTest = "Auto Rotate Test";
	final String calibrateSwerveModules = "Calibrate Swerve Modules";
	final String deleteSwerveCalibration = "Delete Swerve Calibration";
	final String autoSwitch = "Auto Switch";
	final String autoCubeFollow = "Auto Cube Follow";
	final String autoBaseLine = "Auto Base Line";
	final String visionAuto = "Vision Auto";
	final String autoScale = "Auto Scale";
	final String autoSwitchToScale = "Auto Switch to Scale";
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

		RobotGyro.reset();  // this is also done in auto init in case it wasn't settled here yet

     	autoChooser = new SendableChooser<String>();
      	autoChooser.addDefault(autoBaseLine, autoBaseLine);
      	autoChooser.addObject(calibrateSwerveModules, calibrateSwerveModules);
      	autoChooser.addObject(deleteSwerveCalibration, deleteSwerveCalibration);
      	autoChooser.addObject(autoDriveDoubleDiamond, autoDriveDoubleDiamond);
      	autoChooser.addObject(autoRotateTest, autoRotateTest);
      	//autoChooser.addObject(autoCubeFollow, autoCubeFollow);
      	autoChooser.addObject(autoSwitch, autoSwitch);
      	autoChooser.addObject(autoScale, autoScale);
  
		SmartDashboard.putNumber("Auto P:", Calibration.AUTO_DRIVE_P);
		SmartDashboard.putNumber("Auto I:", Calibration.AUTO_DRIVE_I);
		SmartDashboard.putNumber("Auto D:", Calibration.AUTO_DRIVE_D);

		SmartDashboard.putData("Auto choices", autoChooser);

		CubeClaw.tick();

    	SmartDashboard.putNumber("Robot Position", 1);
	}

	/*
	 * 
	 * TELEOP PERIODIC
	 * 
	 */
	@Override
	public void teleopPeriodic() {
		DriveTrain.fieldCentricDrive(gamepad.getSwerveYAxis(), -gamepad.getSwerveXAxis(), powerOf2PreserveSign(gamepad.getSwerveRotAxis()));
		
		if(gamepad.activateIntake()){  // 2 - right bumper
			CubeClaw.intakeCube();  // this will transition to a "hold" when the current breaker is tripped
		}
		
		if (gamepad.dropCube()) { // 2 - left bumper
			CubeClaw.dropCube();
		}
		
		if(gamepad.gotoLiftFloor()){  // 2 - A
			CubeClaw.setArmHorizontalPosition();
		}
		
		if(gamepad.gotoLiftSwitch()){  // 2 - B
			CubeClaw.setArmSwitchPosition();
		}
		
		if(gamepad.gotoLiftScale()){  // 2 - Y
			CubeClaw.setArmScalePosition();
		}
			
		Lift.move(gamepad.getLiftAxis());  // 2 - left stick
		CubeClaw.armMove(gamepad.getArmAxis());  // 2 - right stick

		SmartDashboard.putNumber("Lift Power", gamepad.getLiftAxis());
//		
//		if (Math.abs(gamepad.getClawIntakeAxis())>.05) {
//			CubeClaw.intakeCube();
////			CubeClaw.testIntakeCube(gamepad.getClawIntakeAxis());
////			SmartDashboard.putNumber("Intake Speed", gamepad.getClawIntakeAxis());
//		} else if (Math.abs(gamepad.getClawEjectAxis())>.05) {
//			CubeClaw.ejectCube();
////			CubeClaw.testEjectCube(gamepad.getClawEjectAxis());
////			SmartDashboard.putNumber("Eject Speed", gamepad.getClawEjectAxis());
//		} else
//			CubeClaw.stopIntake();
		
	}
	
	@Override
	public void autonomousInit() {
		String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	int robotPosition = (int) SmartDashboard.getNumber("Robot Position",1);
		
    	RobotGyro.reset(); 
		
    	autoSelected = (String) autoChooser.getSelected();
		SmartDashboard.putString("Auto Selected: ", autoSelected);
		SmartDashboard.putString("GameData", gameData);

    	mAutoProgram = null;
    	
    	switch(autoSelected){
    	    case autoDriveDoubleDiamond:
        		mAutoProgram = new AutoCalibrateDrive(robotPosition);
        		break;
    	    case autoRotateTest:
    	    	mAutoProgram = new AutoRotateTest(robotPosition);
    	    	break;
    	    case calibrateSwerveModules:
    	    	double[] pos = DriveTrain.getAllAbsoluteTurnOrientations();
    	    	Calibration.saveSwerveCalibration(pos[0], pos[1], pos[2], pos[3]);
    	    	break;
    	    case deleteSwerveCalibration:
    	    	Calibration.resetSwerveDriveCalibration();
    	    	break;
    	    case autoSwitch:
    	    	mAutoProgram = new AutoMainSwitch(robotPosition);
        		break;
    	    case autoScale:
    	    	mAutoProgram = new AutoStartToScale(robotPosition);
    	    	break;
    	    case autoCubeFollow:
    	    	mAutoProgram = new AutoCubeFollow(robotPosition);
    	    	break;
    	    case autoBaseLine:
    	    	mAutoProgram = new AutoBaseLine(robotPosition);
    	    	break;
    	} 

		DriveAuto.reset();
		DriveTrain.setAllTurnOrientiation(0);

		if (mAutoProgram != null) {
			mAutoProgram.start();
		} else
			System.out.println("No auto program started in switch statement");
	}

	@Override
	public void autonomousPeriodic() {

    	if (mAutoProgram != null) {
        	mAutoProgram.tick();
            DriveAuto.tick();
            DriveAuto.showEncoderValues();
    	}
    	
    	DriveTrain.setDriveModulesPIDValues(SmartDashboard.getNumber("Auto P:", 0), SmartDashboard.getNumber("Drive I:", 0), SmartDashboard.getNumber("Auto D:", 0));
    	SmartDashboard.putNumber("Drive Error", DriveTrain.getAverageError());
	}

	@Override
	public void teleopInit() {
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
