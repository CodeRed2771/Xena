//Left or right stating position to baseline

package org.usfirst.frc.team2771.robot;

import org.usfirst.frc.team2771.robot.DriveAuto.DriveSpeed;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * This auto program just drives forward across
 * the baseline.
 */

public class AutoBaseLine extends AutoBaseClass {
	private char myRobotPosition;
	
	public AutoBaseLine(char robotPosition) {
		super(robotPosition);
		myRobotPosition = robotPosition;
	}

	public void tick() {
		
		if (isRunning()) {

			SmartDashboard.putNumber("Auto Step", getCurrentStep());

			switch (getCurrentStep()) {
			case 0:
				setTimerAndAdvanceStep(6000);
				CubeClaw.setArmTravelPosition();
				if (myRobotPosition == 'C') {
					driveInches(95, 24, .35);
				} else
				{
					driveInches(90, 0, .35);
				}
				break;
			case 2:
				stop();
				break;
			}	
		}
	}
}