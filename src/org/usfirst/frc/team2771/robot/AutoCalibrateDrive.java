package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoCalibrateDrive extends AutoBaseClass {

	public AutoCalibrateDrive(int robotPosition) {
		super(robotPosition);
		System.out.println("AutoCalibrateDrive started");
		
	}

	public void tick() {
		
		if (isRunning()) {		
			SmartDashboard.putNumber("Auto Step", getCurrentStep());

			switch (getCurrentStep()) {
			case 0:
				setTimerAndAdvanceStep(10000);
				driveInches(100,0,1);
				break;
			case 1:
				if (driveCompleted())
					advanceStep();
				break;
			case 2:
				setTimerAndAdvanceStep(10000);
				driveInches(-100,0,1);
				break;
			case 3:
				if (driveCompleted())
					advanceStep();
				break;
			case 4:
				this.setStep(0);
				break;
			}
		}
	}
}
