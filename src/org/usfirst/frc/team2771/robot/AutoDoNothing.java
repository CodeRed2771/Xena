//Left or right stating position to baseline

package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * This auto program does nothing - which gives the compressor time to run
 * Meant only for charging the system before a match.
 */

public class AutoDoNothing extends AutoBaseClass {
	public AutoDoNothing(char robotPosition) {
		super(robotPosition);
	}

	public void tick() {
		
		if (isRunning()) {

			SmartDashboard.putNumber("Auto Step", getCurrentStep());

			switch (getCurrentStep()) {
			case 0:
				break;

			}	
		}
	}
}