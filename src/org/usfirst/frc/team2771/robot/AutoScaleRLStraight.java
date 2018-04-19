package org.usfirst.frc.team2771.robot;

import org.usfirst.frc.team2771.robot.DriveAuto.DriveSpeed;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//LINE THE ROBOT UP STRAIGHT TURN ON AND THEN LINE THE ROBOT UP 90 DEGREES TO THE LEFT
// WARNING - It takes the gyro like 30 seconds to calibrate. The robot should not be moved at all during that time.

/*
 * This code starts on the right or left and goes to the scale.
 * If it is on the same side as the scale, it will drive and place.
 * If it is on the opposite side of the scale, it will drive around
 * to the correct side and  place.
 */

public class AutoScaleRLStraight extends AutoBaseClass {
	public AutoScaleRLStraight(char robotPosition) {
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
						this.driveInches(236, 0, .80);
						Lift.goHighScale();
						CubeClaw.setArmScalePosition();
					} else
						this.setStep(50);  // Go to crossfield auto code
				} else { // we're on the Right side
					if (isScaleRight()) {
						setTimerAndAdvanceStep(4000);
						this.driveInches(236, 0, .80);
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
					setTimerAndAdvanceStep(2000);
					this.turnDegrees(30, .5);
				} else { // we're on the Right side
					setTimerAndAdvanceStep(2000);
					this.turnDegrees(-30, .5);
				}
				break;
			case 5:
				if (driveCompleted())
					advanceStep();
				break;
			case 6: 
				setTimerAndAdvanceStep(300);
				CubeClaw.ejectCubeSlow();
				break;
			case 7:
				break;
			case 8:
				this.setTimerAndAdvanceStep(2000);
				if (this.robotPosition() == 'L') {
					DriveAuto.turnDegrees(125, .5);
				} else {
					DriveAuto.turnDegrees(-125, .5);
				}
				break;
			case 9:
				if (driveCompleted())
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
				this.driveInches(44, 0, .35); // drive slowly up to the cubes
				break;
			case 13:
				break;
			case 14:
				setTimerAndAdvanceStep(1000);
				CubeClaw.closeClaw();
			case 15:
				break;
			case 16:
				setTimerAndAdvanceStep(1500);
				CubeClaw.holdCube();
				CubeClaw.setArmTravelPosition();
				this.driveInches(-44, 0, .65);
				break;
			case 17:
				if(driveCompleted())
					advanceStep();
				break;
			case 18:
				this.setTimerAndAdvanceStep(2000);
				if (this.robotPosition() == 'L') {
					DriveAuto.turnDegrees(70, .5);
				} else {
					DriveAuto.turnDegrees(-70, .5);
				}
				Lift.goHighScale();
				break;
			case 19:
				if(driveCompleted())
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
			// CROSS FIELD ROUTINES
			// **************************************************************
				
			case 50: // DRIVE FORWARD TO WHERE WE CAN CROSS THE FIELD
				setTimerAndAdvanceStep(4000);
				this.driveInches(218, 0, .80);

				break;
			case 51:
				if (driveCompleted()) {
					advanceStep();
				}
				break;
			case 52: // CROSS THE FIELD
				setTimerAndAdvanceStep(4000);
				Lift.goToScaleMed();
				CubeClaw.setArmSwitchPosition();
				if (this.robotPosition() == 'L') {
					this.driveInches(172, 90, .50);
				} else { 
					this.driveInches(172, -90, .50);
				}
			case 53:
				if (driveCompleted()) {
					advanceStep();
				}
				break;
			case 54: // DRIVE UP to SCALE
				setTimerAndAdvanceStep(2500);
				Lift.goHighScale();
				this.driveInches(36, 0, .60);

				break;
			case 55:
				if (driveCompleted())
					advanceStep();
				break;

			case 56: // EJECT CUBE
				setTimerAndAdvanceStep(500);
				CubeClaw.ejectCubeSlow();
				break;
			case 57:
				break;
			case 58: // BACK AWAY
				setTimerAndAdvanceStep(2000);
				CubeClaw.setArmTravelPosition();
				this.driveInches(-30, 0, .60);
				break;
			case 59:
				if (driveCompleted()) {
					advanceStep();
				}
				break;
			case 60:
				setTimerAndAdvanceStep(1500);
				Lift.goStartPosition();
				break;
			case 61:
				stop();
			}
		}

	}
}
