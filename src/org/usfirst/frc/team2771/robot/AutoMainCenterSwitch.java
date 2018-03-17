package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * This auto program is for the middle position, and will
 * go to the right side of the switch and place a cube if
 * the switch is ours on the right side, or go to the left
 * side of the switch and place a cube of the switch is
 * ours on the left side.
 */

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
				//DriveAuto.setDriveSpeed(DriveAuto.DriveSpeed.LOW_SPEED);
				break;
			case 1:
				break;
			case 2:
				setTimerAndAdvanceStep(3000);
				CubeClaw.holdCube(); // makes sure the cylinders are engaged
				Lift.goSwitch();
				CubeClaw.setArmSwitchPosition();
				if (isSwitchLeft()) {
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
				setTimerAndAdvanceStep(2000);
				this.driveInches(-36, 0, .1);
				break;
			case 7:
				if(driveCompleted())
					advanceStep();
				break;
			case 8:
				setTimerAndAdvanceStep(2000);
				Lift.goPickSecondCubePosition();
				CubeClaw.intakeCube();
				if (isSwitchLeft()) {
					this.turnDegrees(45, .1);
				} else {
					this.turnDegrees(-45, .1);
				}
				break;
			case 9:
				if(driveCompleted())
					advanceStep();
				break;
			case 10:
				setTimerAndAdvanceStep(2000);
				this.driveInches(24, 0, .1);
				break;
			case 11:
				if(driveCompleted())
					advanceStep();
				break;
			case 12:
				setTimerAndAdvanceStep(2000);
				break;
			case 13:
				break;
			case 14:
				setTimerAndAdvanceStep(2000);
				CubeClaw.holdCube();
				this.driveInches(-24, 0, .5);
				Lift.goSwitch();
				CubeClaw.setArmSwitchPosition();
				break;
			case 15:
				if(driveCompleted())
					advanceStep();
				break;
			case 16:
				setTimerAndAdvanceStep(2000);
				if (isSwitchLeft()) {
					this.turnDegrees(-45, .1);
				} else {
					this.turnDegrees(45, .1);
				}
				break;
			case 17:
				if(driveCompleted())
					advanceStep();
				break;
			case 18:
				setTimerAndAdvanceStep(2000);
				this.driveInches(36, 0, .1);
				break;
			case 19:
				if(driveCompleted())
					advanceStep();
			case 20:
				setTimerAndAdvanceStep(2000);
				CubeClaw.ejectCubeSlow();
				break;
			case 21:
				break;
			case 22:
				CubeClaw.stopIntake();
				CubeClaw.setArmTravelPosition();
				break;
			}
		}
	}
}

