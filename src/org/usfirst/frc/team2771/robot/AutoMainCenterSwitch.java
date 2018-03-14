package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoMainCenterSwitch extends AutoBaseClass {
	public AutoMainCenterSwitch(char robotPosition) {
		super(robotPosition);
	}

	
	public void tick() {

		if (isRunning()) {

			SmartDashboard.putNumber("Auto Step", getCurrentStep());

			switch (getCurrentStep()) {
			case 0:
				setTimerAndAdvanceStep(1000);
				DriveAuto.setDriveSpeed(DriveAuto.DriveSpeed.LOW_SPEED);
				break;
			case 1:
				break;
			case 2:
				setTimerAndAdvanceStep(3000);
				CubeClaw.holdCube(); // makes sure the cylinders are engaged
				Lift.goSwitch();
				CubeClaw.setArmSwitchPosition();
				if (isScaleLeft()) {
					driveInches(110, -27, .1);
				} else {
					driveInches(110, 27, .1);
				}
				break;
			case 3:
				if (driveCompleted())
					advanceStep();
				break;
			case 4:
				setTimerAndAdvanceStep(3000);
				DriveAuto.stop();
				CubeClaw.ejectCubeSlow();
				break;
			case 5:
				break;
			case 6:
				CubeClaw.stopIntake();
				CubeClaw.setArmTravelPosition();
				break;
			}
		}
	}
}

