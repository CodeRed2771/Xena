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
				setTimerAndAdvanceStep(1000);
				DriveTrain.setAllTurnOrientiation(27);
				break;
			case 1:
				break;
			case 2:
				setTimerAndAdvanceStep(3000);
				CubeClaw.holdCube(); // makes sure the cylinders are engaged
				Lift.goSwitch();
				CubeClaw.setArmSwitchPosition();
				if (isScaleLeft()) {
					driveInches(114, 27, .1);
				} else {
					driveInches(114, -27, .1);
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
				CubeClaw.stopIntake();
				CubeClaw.setArmTravelPosition();
				break;
			}
		}
	}
}

