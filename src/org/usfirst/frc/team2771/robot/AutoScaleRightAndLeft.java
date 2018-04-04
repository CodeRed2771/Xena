package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//LINE THE ROBOT UP STRAIGHT TURN ON AND THEN LINE THE ROBOT UP 90 DEGREES TO THE LEFT
// WARNING - It takes the gyro like 30 seconds to calibrate. The robot should not be moved at all during that time.

/*
 * This code starts on the right or left and goes to the scale.
 * If it is on the same side as the scale, it will drive and place.
 * If it is on the opposite side of the scale, it will drive around
 * to the correct side and  place.
 */

public class AutoScaleRightAndLeft extends AutoBaseClass {
	public AutoScaleRightAndLeft(char robotPosition) {
		super(robotPosition);
	}

	public void tick() {
		if (isRunning()) {

			SmartDashboard.putNumber("Auto Step", getCurrentStep());

			switch (getCurrentStep()) {
			case 0:
				setTimerAndAdvanceStep(100);
				//DriveAuto.setDriveSpeed(DriveAuto.DriveSpeed.LOW_SPEED);
				break;
			case 1:
				break;
			case 2: // 1ST DRIVE
				if (this.robotPosition() == 'L') {
					if (isScaleLeft()) {
						setTimerAndAdvanceStep(4000);
						this.driveInches(300, -90, .5);
						Lift.goHighScale();
					} else { // scale is on other side (the right side)
						setTimerAndAdvanceStep(3000);
						this.driveInches(235, -90, .5);
					}
				} else { // we're on the Right side
					if (isScaleRight()) {
						setTimerAndAdvanceStep(4000);
						this.driveInches(300, -90, .5);
						Lift.goHighScale();
					} else { // scale is on other side (the left side)
						setTimerAndAdvanceStep(3000);
						this.driveInches(235, -90, .5);
					}
				}
				break;
			case 3:
				if (driveCompleted())
					advanceStep();
				break;
			case 4: // SAME: EJECT CUBE
					// DIFFERENT: DRIVE TO SCALE
				if (this.robotPosition() == 'L') {
					if (isScaleLeft()) {
						setTimerAndAdvanceStep(250);
						CubeClaw.ejectCubeFast();
					} else {
						setTimerAndAdvanceStep(4000);
						this.driveInches(200, 90, .5); // drive to other side
					}
				} else { // we're on the right side
					if (isScaleRight()) {
						setTimerAndAdvanceStep(250);
						CubeClaw.ejectCubeFast();
					} else if (isScaleLeft()) {
						setTimerAndAdvanceStep(4000);
						this.driveInches(200, -90, .5); // drive to other side
					}
				}

				break;
			case 5:
				if (driveCompleted())
					advanceStep();
				break;
			case 6: // SAME: DO NOTHING
					// DIFFERENT: TURN TO SCALE
				if (this.robotPosition() == 'L') {
					if (isScaleLeft()) {
						this.setStep(this.getCurrentStep()+2);
						break;
					} else { // we're by the right side scale now
						setTimerAndAdvanceStep(1500);
						this.turnDegrees(-90, 1);
						Lift.goHighScale();
					}
				} else { // we started on the right side
					if (isScaleRight()) {
						this.setStep(this.getCurrentStep()+2);
						break;
					} else { // we're by left side scale now
						setTimerAndAdvanceStep(1500);
						this.turnDegrees(90, 1);
						Lift.goHighScale();
					}
				}

				break;
			case 7:
				if (driveCompleted()) {
					advanceStep();
				}
				break;
			case 8: // SAME: DO NOTHING
					// DIFFERENT: GET CLOSE ENOUGH TO SCALE
				if (this.robotPosition() == 'L') {
					if (isScaleLeft()) {
						this.setStep(this.getCurrentStep()+2);
						break;
					} else if (isScaleRight()) {
						setTimerAndAdvanceStep(2000);
						this.driveInches(66, 0, .5);
					}	
				} else {
					if (isScaleRight()) {
						this.setStep(this.getCurrentStep()+2);
						break;
					} else {
						setTimerAndAdvanceStep(2000);
						this.driveInches(66, 0, .5);
					}	
				}
				break;
			case 9:
				if (driveCompleted()) {
					advanceStep();
				}
				break;
			case 10: // SAME AND DIFFERENT: EJECT CUBE
				setTimerAndAdvanceStep(250);
				CubeClaw.ejectCube();
				break;
			case 11:
				break;
			case 12:
				CubeClaw.setArmTravelPosition();
				if (this.robotPosition() == 'L') {
					if (isScaleLeft()) {
						this.setStep(this.getCurrentStep()+2);
						break;
					} else if (isScaleRight()) {
						setTimerAndAdvanceStep(2000);
						this.driveInches(-24, 0, .5);
					}	
				} else {
					if (isScaleRight()) {
						this.setStep(this.getCurrentStep()+2);
						break;
					} else {
						setTimerAndAdvanceStep(2000);
						this.driveInches(-24, 0, .5);
					}	
				}
				break;
			case 13:
				if (driveCompleted()) {
					advanceStep();
				}
				break;
			case 14:
				setTimerAndAdvanceStep(1500);
				Lift.goStartPosition();
			case 15:
				stop();
				break;
			}
		}

	}
}
