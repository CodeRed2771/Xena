package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoStartToScale extends AutoBaseClass {

	public AutoStartToScale(char robotPosition) {
		super(robotPosition);
		System.out.println("AutoStartToScale started");
	}

	public void tick() {
		if (isRunning()) {
			SmartDashboard.putNumber("Auto Step", getCurrentStep());

			switch (getCurrentStep()) {
			case 0: // DRIVE TO SCALE
				setTimerAndAdvanceStep(2000);
				CubeClaw.holdCube();
				CubeClaw.setArmTravelPosition();
				if (robotPosition() == 'R') {
					this.driveInches(36, 25, .4); // remember to add two to three inches to this before competition
				} else {
					this.driveInches(36, -25, .4); // this one too
				}
				break;
			case 1:
				if (driveCompleted())
					advanceStep();
				break;
			case 2: // DRIVE TO SCALE PT. 2
				setTimerAndAdvanceStep(3000);
				this.driveInches(170, 0, .3);
				Lift.goHighScale();
				break;
			case 3:
				if (driveCompleted())
					advanceStep();
				break;
			case 4:
				setStep(6);
//				Lift.goHighScale();
				break;
			case 5:
				if (driveCompleted())
					advanceStep();
				break;
			case 6: // DRIVE TO SCALE PT. 3
				setTimerAndAdvanceStep(2000);
				if (robotPosition() == 'R') {
					this.driveInches(44, -45, .5);
				} else {
					this.driveInches(44, 45, .5);
				}
				CubeClaw.setArmScalePosition();
				break;
			case 7:
				if (driveCompleted())
					advanceStep();
				break;
			case 8: // DROP CUBE
				setTimerAndAdvanceStep(500);
				CubeClaw.dropCube();
				break;
			case 9:
				break;
			case 10: // DRIVE BACK
				setTimerAndAdvanceStep(2000);
				this.driveInches(-24, 0, .5);
				CubeClaw.setArmTravelPosition(); // RESET ARM
				break;
			case 11:
				if(driveCompleted())
					advanceStep();
				break;
			case 12: // TURN AROUND AND FACE SWITCH
				setTimerAndAdvanceStep(2000);
				if (robotPosition() == 'R') {
					this.turnDegrees(-100, .4);
				} else {
					this.turnDegrees(100, .4);
				}
				Lift.goStartPosition();
				break;
			case 13:
				if (driveCompleted())
					advanceStep();
				break;
			case 14: // DRIVE TOWARDS AND GRAB 2ND CUBE
				
				setStep(30);  // cancels the rest of the auto for now
				
				
				setTimerAndAdvanceStep(2000);
				CubeClaw.intakeCube();
				this.driveInches(48, 0, .5);
				break;
			case 17:
				if(driveCompleted())
					advanceStep();
				break;
			case 18: // LIFT 2ND CUBE TO SCALE LEVEL
				//setTimerAndAdvanceStep(1000);
				//this.driveInches(24, 0, .1);
				setTimerAndAdvanceStep(1000);
				this.driveInches(-24, 0, .1);
				Lift.goHighScale();
				break;
			case 19:
				if(driveCompleted())
					advanceStep();
				break;
			case 20:
				/*
				if (robotPosition() == 'R') {
					this.turnDegrees(165, .5);
				} else {
					this.turnDegrees(-165, .5);
				}*/
				this.setStep(21);
				break;
			case 21:
				if(driveCompleted())
					advanceStep();
			case 22:
				CubeClaw.setArmOverTheTopPosition();
			case 23:
				break;
			case 24:
				CubeClaw.dropCube();
			case 25:
				this.setStep(30);
			case 30:
				stop();
				break;
			}
		}
	}
}