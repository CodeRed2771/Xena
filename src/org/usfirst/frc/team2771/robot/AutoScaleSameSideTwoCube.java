
package org.usfirst.frc.team2771.robot;

import org.usfirst.frc.team2771.robot.DriveAuto.DriveSpeed;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/*
 * This code starts on the right or left and goes to the scale.
 * If it is on the same side as the scale, it will drive and place.
 * If it is on the opposite side of the scale, it will just cross baseline
 */

public class AutoScaleSameSideTwoCube extends AutoBaseClass {
	public AutoScaleSameSideTwoCube(char robotPosition) {
		super(robotPosition);
	}

	public void tick() {
		if (isRunning()) {

			SmartDashboard.putNumber("Auto Step", getCurrentStep());

			switch (getCurrentStep()) {
			case 0:
				setTimerAndAdvanceStep(100);
				CubeClaw.setArmTravelPosition();
				// DriveAuto.setDriveSpeed(DriveAuto.DriveSpeed.LOW_SPEED);
				break;
			case 1:
				break;
			case 2: // 1ST DRIVE
				if (this.robotPosition() == 'L') {
					if (isScaleLeft()) {
						setTimerAndAdvanceStep(4000);
						this.driveInches(250, 0, .7);
						Lift.goHighScale();
						CubeClaw.setArmScalePosition();
					} else
						this.setStep(50);  // Go to crossfield auto code
				} else { // we're on the Right side
					if (isScaleRight()) {
						setTimerAndAdvanceStep(4000);
						this.driveInches(250, 0, .7);
						Lift.goHighScale();
						CubeClaw.setArmScalePosition();
					} else
						this.setStep(50); // Go to crossfield auto code
				}
				break;
			case 3:
				if (driveCompleted())
					advanceStep();
				break;

			case 4:
				if (this.robotPosition() == 'L') {
					setTimerAndAdvanceStep(1000);
					this.turnDegrees(39, .5);
				} else { // we're on the Right side
					setTimerAndAdvanceStep(1000);
					this.turnDegrees(-39, .5);
				}
				break;
			case 5:
				if (turnCompleted())
					advanceStep();
				break;
			case 6: 
				setTimerAndAdvanceStep(800);
				CubeClaw.ejectCube();
				break;
			case 7:
				break;
			case 8:
				this.setTimerAndAdvanceStep(2000);
				if (this.robotPosition() == 'L') {
					DriveAuto.turnDegrees(116, .5);
				} else {
					DriveAuto.turnDegrees(-116, .5);
				}
				break;
			case 9:
				if (turnCompleted())
					advanceStep();
				break;
			case 10:
				setTimerAndAdvanceStep(1500);
				CubeClaw.setArmHorizontalPosition();
				Lift.goStartPosition();
				break;
			case 11:
				break;
			case 12:
				setTimerAndAdvanceStep(2000);
				CubeClaw.intakeCube();
				CubeClaw.openClaw();
				this.driveInches(54, 0, .35); // drive slowly up to the cubes
				break;
			case 13:
				break;
			case 14:
				setTimerAndAdvanceStep(750);
				CubeClaw.closeClaw();
			case 15:
				break;
			case 16:
				setTimerAndAdvanceStep(1500);
				CubeClaw.holdCube();
				CubeClaw.setArmTravelPosition();
				this.driveInches(-48, 0, .65);
				break;
			case 17:
				if(driveCompleted())
					advanceStep();
				break;
			case 18:
				this.setTimerAndAdvanceStep(2000);
				if (this.robotPosition() == 'L') {
					DriveAuto.turnDegrees(60, .5);
				} else {
					DriveAuto.turnDegrees(-60, .5);
				}
				Lift.goHighScale();
				break;
			case 19:
				if(turnCompleted())
					advanceStep();
				break;
			case 20:
				setTimerAndAdvanceStep(2000);
				CubeClaw.setArmOverTheTopPosition();
				break;
			case 21:
				break;
			case 22:
				setTimerAndAdvanceStep(500);
				CubeClaw.ejectCubeSlow();
				break;
			case 23:
				break;
			case 24:
				setTimerAndAdvanceStep(1000);
				CubeClaw.setArmTravelPosition();
				break;
			case 25:
				break;
			case 26:	
				stop();
				break;
				
			// **************************************************************
			// BASELINE
			// **************************************************************
				
			case 50: // DRIVE FORWARD TO WHERE WE CAN CROSS THE FIELD
				CubeClaw.setArmTravelPosition();
				driveInches(90, 0, .5);
				break;
			case 51:
				if (driveCompleted()) {
					advanceStep();
				}
				break;
			case 52:
				stop();
			}
		}

	}
}
