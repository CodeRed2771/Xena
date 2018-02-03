
package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot {
	KeyMap gamepad;
	CubeClaw cubeClaw;
	Lift lift;
	
	@Override
	public void robotInit() {
		cubeClaw = new CubeClaw();
		lift = new Lift();
		gamepad = new KeyMap();
	}

	@Override
	public void autonomousInit() {
		
	}

	@Override
	public void autonomousPeriodic() {
	
	}

	
	@Override
	public void teleopPeriodic() {
		
		lift.move(gamepad.getLiftAxis());
		SmartDashboard.putNumber("Lift Speed", gamepad.getLiftAxis());
		
		if (Math.abs(gamepad.getClawIntakeAxis())>.05) {
			cubeClaw.testIntakeCube(gamepad.getClawIntakeAxis());
			SmartDashboard.putNumber("Intake Speed", gamepad.getClawIntakeAxis());
		} else if (Math.abs(gamepad.getClawEjectAxis())>.05) {
			cubeClaw.testEjectCube(gamepad.getClawEjectAxis());
			SmartDashboard.putNumber("Eject Speed", gamepad.getClawEjectAxis());
		} else
			cubeClaw.stopIntake();
		
		if (gamepad.openClaw()) {
			SmartDashboard.putString("Claw", "open");
			cubeClaw.open();
		} else if (gamepad.closeClaw()) {
			SmartDashboard.putString("Claw", "closed");
			cubeClaw.close();
		} else 
		{
			SmartDashboard.putString("Claw", "off");
				cubeClaw.off();
		}
		
	}


	@Override
	public void testPeriodic() {
	}
}
