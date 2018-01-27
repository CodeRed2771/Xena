
package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot {
	CubeClaw cubeClaw;
	Lift lift;
	
	@Override
	public void robotInit() {
		cubeClaw = new CubeClaw();
		lift = new Lift();
	}

	@Override
	public void autonomousInit() {
		
	}

	@Override
	public void autonomousPeriodic() {
		
	}

	
	@Override
	public void teleopPeriodic() {
	}


	@Override
	public void testPeriodic() {
	}
}
