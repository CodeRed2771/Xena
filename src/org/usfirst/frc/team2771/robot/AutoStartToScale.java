package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoStartToScale extends AutoBaseClass{

	public AutoStartToScale(int robotPosition) {
		super(robotPosition);
	}
	
	public void tick() {
		if(isRunning()) {
			SmartDashboard.putNumber("Auto Step", getCurrentStep());
			
			switch(getCurrentStep()) {
			case 0:
				setTimerAndAdvanceStep(15000);
				if(robotPosition() == 1) {
					this.driveInches(140, 45, .4);
				} else {
					this.driveInches(140, -45, .4);
				}
				break;
			case 1:
				if(driveCompleted())
					advanceStep();
				break;
			case 2:
				setTimerAndAdvanceStep(1000);
				this.driveInches(62, 0, .3);
				break;
			case 3:
				if(driveCompleted())
					advanceStep();
				break;
			case 4:
				setTimerAndAdvanceStep(15000);
				if(robotPosition() == 1) {
					this.driveInches(140, 45, -.4);
				} else {
					this.driveInches(140, -45, -.4);
				}
				break;
			case 5:
				if(driveCompleted())
					advanceStep();
				break;
			case 6:
				setTimerAndAdvanceStep(1000);
				this.driveInches(62, 0, -.3);
				break;
			case 7:
				if(driveCompleted())
					advanceStep();
				break;
			case 8:
				stop();
				break;
			}
		}
	}
}