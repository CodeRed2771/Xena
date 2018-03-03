package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoMainCenterSwitch extends AutoBaseClass {
	public AutoMainCenterSwitch(int robotPosition) {
		super(robotPosition);
	}

	
	public void tick() {

		if (isRunning()) {

			SmartDashboard.putNumber("Auto Step", getCurrentStep());

			switch (getCurrentStep()) {
			case 0:
				setTimerAndAdvanceStep(6000);
				if (isScaleLeft()) {
					driveInches(40, -45,.5);
				} else {
					driveInches(40, 45, .5);
				}
				break;
			case 1:
				if (driveCompleted())
					advanceStep();
				break;
			case 2:
				setTimerAndAdvanceStep(3000);
				Lift.goSwitch(); 
				break;
			case 3:
				if(driveCompleted())
					advanceStep();
				break;
			case 4:
				setTimerAndAdvanceStep(3000);
				CubeClaw.setArmSwitchPosition();
				break;
			case 5:
				if(driveCompleted()) 
					advanceStep();
				break;
			case 6:
				setTimerAndAdvanceStep(3000);
				CubeClaw.ejectCube();
				break;
			}
		}
	}
}

