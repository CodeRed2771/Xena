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
					this.driveInches(48, 25, .5); 
				} else {
					this.driveInches(48, -25, .5);
				}
				break;
			case 1:
				if (driveCompleted())
					advanceStep();
				break;
			case 2: // DRIVE TO SCALE PT. 2
				setTimerAndAdvanceStep(3000);
				this.driveInches(182, 0, .4); // changed the distance. Needs to be tested.
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
					this.driveInches(48, -45, .5);
				} else {
					this.driveInches(48, 45, .5);
				}
				CubeClaw.setArmScalePosition();
				break;
			case 7:
				if (driveCompleted())
					advanceStep();
				break;
			case 8: // DROP CUBE
				setTimerAndAdvanceStep(500);
				CubeClaw.ejectCubeSlow();
				break;
			case 9:
				break;
			case 10: // DRIVE BACK
				setTimerAndAdvanceStep(2000);
				this.driveInches(-48, 0, .5);
				break;
			case 11:
				if(driveCompleted())
					advanceStep();
				break;
			case 12: // RESET ARM
				setTimerAndAdvanceStep(500);
				CubeClaw.setArmTravelPosition();
				Lift.goStartPosition();
				break;
			case 13:
				break;
			case 14:
				this.setStep(30);
//			case 14: // TURN AROUND AND FACE SWITCH. we should take this out. Just sayin
//				setTimerAndAdvanceStep(1200);
//				if (robotPosition() == 'R') {
//					this.turnDegrees(-165, .5);
//				} else {
//					this.turnDegrees(165, .5);
//				}
//				Lift.goStartPosition();
//				break;
//			case 15:
//				if (driveCompleted())
//					advanceStep();
//				break;
//			case 16: // DRIVE TOWARDS AND GRAB 2ND CUBE
//				setTimerAndAdvanceStep(2000);
//				CubeClaw.intakeCube();
//				this.driveInches(30, 0, .5);
//				break;
//			case 17:
//				if(driveCompleted())
//					advanceStep();
//				break;
//			case 18: // LIFT 2ND CUBE TO SCALE LEVEL
//				//setTimerAndAdvanceStep(1000);
//				//this.driveInches(24, 0, .1);
//				setTimerAndAdvanceStep(1000);
//				this.driveInches(-24, 0, .1);
//				CubeClaw.setArmScalePosition();
//				Lift.goHighScale();
//				break;
//			case 19:
//				if(driveCompleted())
//					advanceStep();
//				break;
//			case 20:
//				/*
//				if (robotPosition() == 'R') {
//					this.turnDegrees(165, .5);
//				} else {
//					this.turnDegrees(-165, .5);
//				}*/
//				
//				
//				//  THIS ABORTS THE REST OF THE AUTO
//				this.setStep(30);
//				break;
//			case 21:
//				if(driveCompleted())
//					advanceStep();
//			case 22:
//				setTimerAndAdvanceStep(1000);
//				CubeClaw.setArmOverTheTopPosition();
//			case 23:
//				break;
//			case 24:
//				setTimerAndAdvanceStep(500);
//				CubeClaw.dropCube();
//			case 25:
//				break;
//			case 26:
//				CubeClaw.setArmTravelPosition();
//				this.setStep(30);
//				break;
			case 30:
				stop();
				break;
			}
		}
	}
}