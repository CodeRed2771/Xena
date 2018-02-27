
package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends TimedRobot {

	KeyMap gamepad;
	Compressor compressor; 
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
	final String autoTest = "Auto Test";
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
      	autoChooser.addObject(autoTest, autoTest);
  
		SmartDashboard.putNumber("Auto P:", Calibration.AUTO_DRIVE_P);
		SmartDashboard.putNumber("Auto I:", Calibration.AUTO_DRIVE_I);
		SmartDashboard.putNumber("Auto D:", Calibration.AUTO_DRIVE_D);

		SmartDashboard.putData("Auto choices", autoChooser);


    	SmartDashboard.putNumber("Robot Position", 1);
    	
		compressor = new Compressor(0);
		compressor.setClosedLoopControl(true);
		
		CubeClaw.resetArmEncoder();

	}

	/*
	 * 
	 * TELEOP PERIODIC
	 * 
	 */
	@Override
	public void teleopPeriodic() {
		
		
		DriveTrain.fieldCentricDrive(gamepad.getSwerveYAxis(), -gamepad.getSwerveXAxis(), powerOf2PreserveSign(gamepad.getSwerveRotAxis()));
		
		if (gamepad.activateIntake()){  // 2 - right bumper
			CubeClaw.intakeCube();  // this will transition to a "hold" when the current breaker is tripped
		}
		
		if (gamepad.dropCube()) { // 2 - left bumper
			CubeClaw.dropCube();
		}
		
		if (gamepad.gotoLiftFloor()){  // 2 - A
			Lift.goStartPosition();
			CubeClaw.setArmHorizontalPosition();
		}
		
		if (gamepad.gotoLiftSwitch()){  // 2 - B
			CubeClaw.setArmSwitchPosition();
			Lift.goSwitch();
		}
		
		if (gamepad.gotoLiftScale()){  // 2 - Y
			CubeClaw.setArmScalePosition();
			Lift.goHighScale();
		}
		
		if (gamepad.getHID(0).getRawButton(1)) { // 1- A
			CubeClaw.setArmHorizontalPosition();
			//CubeClaw.close();
		}
		
		if (gamepad.getHID(0).getRawButton(2)) { // 1 - B
			CubeClaw.setArmScalePosition();
			//CubeClaw.open();
		}
		
		if (gamepad.getArmAxis() > .1 || gamepad.getArmAxis() < -.1){
			CubeClaw.armMove(gamepad.getArmAxis());
		}

		if (gamepad.goLowGear()) {  // 2 - Back
			Lift.setLowGear();
		}
		
		if (gamepad.goHighGear()) { // 2 - start
			Lift.setHighGear();
			
		}
		
		if (gamepad.manualLift() > .1 || gamepad.manualLift() < -.1){  // 2 - left stick 
			Lift.move(gamepad.manualLift());
		}

		SmartDashboard.putNumber("Lift Power", gamepad.getLiftAxis());
		SmartDashboard.putNumber("Gyro Heading", RobotGyro.getAngle());
		
		Lift.tick();
		CubeClaw.tick();
		
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
    	    case autoTest:
    	    	mAutoProgram = new AutoCalibrateDrive(robotPosition);
    	    	break;
    	} 

		DriveAuto.reset();
		DriveTrain.setAllTurnOrientiation(0);
		
		System.out.println("end of auto init");

//		if (mAutoProgram != null) {
//			mAutoProgram.start();
//		} else
//			System.out.println("No auto program started in switch statement");
	}

	@Override
	public void autonomousPeriodic() {

    	if (mAutoProgram != null) {
        	mAutoProgram.tick();
            DriveAuto.tick();
            DriveAuto.showEncoderValues();
        	SmartDashboard.putNumber("Elapsed Time TICK", System.currentTimeMillis());

    	}
    	
    	System.out.println("in auto periodic");
    	
    	SmartDashboard.putNumber("Elapsed Time PERIOD ", System.currentTimeMillis());
    	
    	DriveTrain.setDriveModulesPIDValues(SmartDashboard.getNumber("Auto P:", 0), SmartDashboard.getNumber("Drive I:", 0), SmartDashboard.getNumber("Auto D:", 0));
    	SmartDashboard.putNumber("Drive Error", DriveTrain.getAverageError());
	}

	@Override
	public void teleopInit() {
		Lift.stop();
		CubeClaw.setArmSwitchPosition();

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
		//CubeClaw.tick();
	}

	private double powerOf2PreserveSign(double v) {
		return (v > 0 ) ? Math.pow(v, 2) : -Math.pow(v, 2);
	}
}
