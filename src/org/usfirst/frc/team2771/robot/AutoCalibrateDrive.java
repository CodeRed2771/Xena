package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoCalibrateDrive extends AutoBaseClass {

	public AutoCalibrateDrive(char robotPosition) {
		super(robotPosition);
		System.out.println("AutoCalibrateDrive started");
		
	}

	public void tick() {
		
		if (isRunning()) {		
			SmartDashboard.putNumber("Auto Step", getCurrentStep());

			switch (getCurrentStep()) {
			case 0:
				setTimerAndAdvanceStep(5000);
				driveInches(100, 0, .50);
				
				break;
			case 1:
				if (driveCompleted())
					advanceStep();
				break;
			case 2:
				setTimerAndAdvanceStep(2000);
				break;
			case 3:
				break;
			case 4:
				setTimerAndAdvanceStep(5000);
				driveInches(-100, 0, .50);
				break;
			case 5:
				if(driveCompleted())
					advanceStep();
				break;
			case 6:
				setTimerAndAdvanceStep(1200);				
//				driveInches(30, 0, .5);
				
//			case 0:
//				setTimerAndAdvanceStep(10000);
//				driveInches(100,0,.5);
//				break;
//			case 1:
//				if (driveCompleted())
//					advanceStep();
//				break;
//			case 2:
//				setTimerAndAdvanceStep(10000);
//				driveInches(-100,0,.5);
//				break;
//			case 3:
//				if (driveCompleted())
//					advanceStep();
//				break;
//			case 4:
//				this.setStep(0);
//				break;
			}
		}
	}
}
