//Left or right stating position to baseline

package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * This auto program just drives forward across
 * the baseline.
 */

public class AutoBaseLine extends AutoBaseClass {
	public AutoBaseLine(char robotPosition) {
		super(robotPosition);
	}

	public void tick() {
		
		if (isRunning()) {

			SmartDashboard.putNumber("Auto Step", getCurrentStep());

			switch (getCurrentStep()) {
			case 0:
				setTimerAndAdvanceStep(6000);
				driveInches(80, -90, .1);
			
				break;
			case 2:
				stop();
				break;
			}	
		}
	}
}