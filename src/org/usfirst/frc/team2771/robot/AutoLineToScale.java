//Auto line to scale.

package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoLineToScale extends AutoBaseClass {
	public AutoLineToScale(char robotPosition) {
		super(robotPosition);
	}

	public void tick() {
		
		if (isRunning()) {

			SmartDashboard.putNumber("Auto Step", getCurrentStep());

			switch (getCurrentStep()) {
			case 0:
				setTimerAndAdvanceStep(6000);
				driveInches(65.45, 0, .1);
				break;
			case 1:
				if (driveCompleted())
					advanceStep();
				break;
			case 2:
				stop();
				break;
			}	
		}
	}
}