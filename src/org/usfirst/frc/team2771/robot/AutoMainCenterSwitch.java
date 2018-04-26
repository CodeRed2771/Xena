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
				setTimerAndAdvanceStep(100); // gives us an extra 100 ms to get the gamedata correctly
				break;
			case 1:
				break;
			case 2:
				setTimerAndAdvanceStep(2800);
				CubeClaw.holdCube(); // makes sure the cylinders are engaged
				Lift.goSwitch();
				CubeClaw.setArmSwitchPosition();
				
				if (isSwitchLeft()) {
					driveInches(118, -30, .6);
				} else {
					driveInches(112, 24, .6);
				}
				break;
			case 3:
				if (driveCompleted())
					advanceStep();
				break;
			case 4:
				setStep(6);
				break;
			case 5:
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
				this.driveInches(-50, 0, .6);
				break;
			case 9:
				if(driveCompleted())
					advanceStep();
				break;
			case 10:
				setTimerAndAdvanceStep(1200);
				if (isSwitchLeft()) {
					this.turnDegrees(48, .5);
				} else {
					this.turnDegrees(-45, .5);
				}
				CubeClaw.intakeCube();
				break;
			case 11:
				if(turnCompleted())
					advanceStep();
				break;
			case 12:
				// Drive into stack
				setTimerAndAdvanceStep(2000);
				Lift.goPickSecondCubePosition();
				CubeClaw.openClaw();
				if (isSwitchLeft()) {
					this.driveInches(56, 0, .5);
				} else {
					this.driveInches(46, 0, .5);	
				}
				
				break;
			case 13:
				if(driveCompleted())
					advanceStep();
				break;
			case 14:
				setTimerAndAdvanceStep(1000);
				CubeClaw.closeClaw();
				break;
			case 15:
				break;
			case 16:
				setTimerAndAdvanceStep(2000);
				CubeClaw.holdCube();
				if (isSwitchLeft()) {
					this.driveInches(-56, 0, .5);
				} else {
					this.driveInches(-50, 0, .5);	
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
					this.turnDegrees(-48, .5);
				} else {
					this.turnDegrees(45, .5);
				}
				break;
			case 19:
				if(turnCompleted())
					advanceStep();
				break;
			case 20:
				setTimerAndAdvanceStep(2000);
				if (isSwitchLeft()) {
					this.driveInches(56, 0, .5);
				} else
					this.driveInches(50, 0, .5);
				break;
			case 21:
				if(driveCompleted())
					advanceStep();
				break;
			case 22:
				setTimerAndAdvanceStep(1000);
				CubeClaw.dropCube();
				break;
			case 23:
				break;
			case 24:
				setTimerAndAdvanceStep(2000);
				this.driveInches(-36, 0, .5);
			case 25:
				if(driveCompleted())
					advanceStep();
			case 26:
				break;
			case 27:
				CubeClaw.stopIntake();
				CubeClaw.setArmHorizontalPosition();
				break;
			}
		}
	}
}

