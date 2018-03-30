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
				setTimerAndAdvanceStep(100);
				DriveAuto.setDriveSpeed(DriveAuto.DriveSpeed.LOW_SPEED);
				break;
			case 1:
				break;
			case 2:
				setTimerAndAdvanceStep(2800);
				CubeClaw.holdCube(); // makes sure the cylinders are engaged
				Lift.goSwitch();
				CubeClaw.setArmSwitchPosition();
				if (isSwitchLeft()) {
					driveInches(110, -30, .1);
				} else {
					driveInches(102, 24, .1);
				}
				break;
			case 3:
				if (driveCompleted())
					advanceStep();
				break;
			case 4:
				if (isSwitchLeft()){
//					setTimerAndAdvanceStep(2000);
//					driveInches(18, 0, 0.1);
					setStep(6);
				} else
					this.setStep(6);
				break;
			case 5:
				if (driveCompleted())
					advanceStep();

				break;
			case 6:
				setTimerAndAdvanceStep(500);
				DriveAuto.stop();
				CubeClaw.dropCube();
				break;
			case 7:
				break;
			case 8:
				setTimerAndAdvanceStep(2000);
				this.driveInches(-48, 0, .1);
				break;
			case 9:
				if(driveCompleted())
					advanceStep();
				break;
			case 10:
				setTimerAndAdvanceStep(1200);
				if (isSwitchLeft()) {
					this.turnDegrees(45, .5);
				} else {
					this.turnDegrees(-45, .5);
				}
				break;
			case 11:
				if(driveCompleted())
					advanceStep();
				break;
			case 12:
				setTimerAndAdvanceStep(2000);
				Lift.goPickSecondCubePosition();
				CubeClaw.intakeCube();
				if (isSwitchLeft()) {
					this.driveInches(50, 0, .1);
				} else {
					this.driveInches(60, 0, .1);	
				}
				
				break;
			case 13:
				if(driveCompleted())
					advanceStep();
				break;
			case 14:
				setTimerAndAdvanceStep(750);
				break;
			case 15:
				break;
			case 16:
				setTimerAndAdvanceStep(2000);
				CubeClaw.holdCube();
				if (isSwitchLeft()) {
					this.driveInches(-50, 0, .5);
				} else {
					this.driveInches(-60, 0, .5);	
				}
				Lift.goSwitch();
				CubeClaw.setArmSwitchPosition();
				break;
			case 17:
				if(driveCompleted())
					advanceStep();
				break;
			case 18:
				setTimerAndAdvanceStep(1200);
				if (isSwitchLeft()) {
					this.turnDegrees(-45, .5);
				} else {
					this.turnDegrees(45, .5);
				}
				break;
			case 19:
				if(driveCompleted())
					advanceStep();
				break;
			case 20:
				setTimerAndAdvanceStep(2000);
				if (isSwitchLeft()) {
					this.driveInches(65, 0, .1);
				} else
					this.driveInches(60, 0, .1);
				break;
			case 21:
				if(driveCompleted())
					advanceStep();
				break;
			case 22:
				setTimerAndAdvanceStep(2000);
				CubeClaw.dropCube();
				break;
			case 23:
				setTimerAndAdvanceStep(2000);
				this.driveInches(-36, 0, .1);
			case 24:
				if(driveCompleted())
					advanceStep();
			case 25:
				break;
			case 26:
				CubeClaw.stopIntake();
				CubeClaw.setArmHorizontalPosition();
				break;
			}
		}
	}
}

