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
				setTimerAndAdvanceStep(3000);
				if (isScaleLeft()) {
					driveInches(80, -25, .1);
				} else {
					driveInches(80, 25, 1);
				}
				Lift.goSwitch(); 
				CubeClaw.setArmSwitchPosition();
				break;
			case 1:
				if (driveCompleted())
					advanceStep();
				break;
			case 2:
				setTimerAndAdvanceStep(3000);
				CubeClaw.ejectCube();
				break;
			case 3:
				break;
			case 4:
				CubeClaw.stopIntake();
				CubeClaw.setArmTravelPosition();
				break;
			}
		}
	}
}

