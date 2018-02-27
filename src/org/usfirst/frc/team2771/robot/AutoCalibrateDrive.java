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
				driveInches(30, 45, 1);
				break;
			case 1:
				if (driveCompleted())
					advanceStep();
				break;
			case 2:
				setTimerAndAdvanceStep(2000);
				driveInches(50, -45, 1);
				break;
			case 3:
				if (driveCompleted())
					advanceStep();
				break;
			case 4:
				setTimerAndAdvanceStep(2000);
				driveInches(-50, 45, 1);
				break;
			case 5:
				if (driveCompleted())
					advanceStep();
				break;
			case 6:
				setTimerAndAdvanceStep(2000);
				driveInches(-100, -45, 1);
				break;
			case 7:
				if (driveCompleted())
					advanceStep();
				break;
				
			case 8:
				setTimerAndAdvanceStep(2000);
				driveInches(-50, 45, 1);
				break;
			case 9:
				if (driveCompleted())
					advanceStep();
				break;
			case 10:
				setTimerAndAdvanceStep(2000);
				driveInches(50, -45, 1);
				break;
			case 11:
				if (driveCompleted())
					advanceStep();
				break;
				
			case 12:
				setTimerAndAdvanceStep(2000);
				driveInches(50, 45, 1);
				break;		
			case 13:
				if (driveCompleted())
					advanceStep();
				break;
			}
		}

	}

}
