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
				DriveAuto.setDriveSpeed(DriveSpeed.LOW_SPEED);
				CubeClaw.setArmTravelPosition();
				if (myRobotPosition == 'C') {
					driveInches(80, 24, .1);
				} else
				{
					driveInches(80, 0, .1);
				}
				break;
			case 2:
				stop();
				break;
			}	
		}
	}
}