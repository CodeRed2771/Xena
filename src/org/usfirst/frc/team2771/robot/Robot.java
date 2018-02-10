
package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends TimedRobot {
	KeyMap gamepad;
	
	@Override
	public void robotInit() {
		gamepad = new KeyMap();
		CubeClaw.getInstance();
		Lift.getInstance();
		
    	SmartDashboard.putNumber("Robot Position", 1);
	}

	@Override
	public void autonomousInit() {
		String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	int robotPosition = (int) SmartDashboard.getNumber("Robot Position",1);
	}

	@Override
	public void autonomousPeriodic() {
	
	}

	
	@Override
	public void teleopPeriodic() {
		
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
	public void testPeriodic() {
	}
}
