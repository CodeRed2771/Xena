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
				CubeClaw.holdCube(); // makes sure the cylinders are engaged
				Lift.goSwitch();
				CubeClaw.setArmSwitchPosition();
				if (isScaleLeft()) {
					driveInches(110, -30, .1);
				} else {
					driveInches(110, 30, .1);
				}
				break;
			case 1:
				if (driveCompleted())
					advanceStep();
				break;
			case 2:
				setTimerAndAdvanceStep(3000);
				DriveAuto.stop();
				CubeClaw.ejectCube();
				break;
			case 3:
				break;
			case 4:
				CubeClaw.stopIntake(); // probably not needed - eject will stop after a brief time
				CubeClaw.setArmTravelPosition();
				break;
			}
		}
	}
}

