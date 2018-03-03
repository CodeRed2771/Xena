package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoCalibrateDrive extends AutoBaseClass {

	public AutoCalibrateDrive(int robotPosition) {
		super(robotPosition);
		
	}

	public void tick() {
		
		if (isRunning()) {		
			SmartDashboard.putNumber("Auto Step", getCurrentStep());

			switch (getCurrentStep()) {
			case 0:
				setTimerAndAdvanceStep(2000);
				driveInches(50,0,1);
				break;
			case 1:
				if (driveCompleted())
					advanceStep();
				break;
			}
		}
	}
}
