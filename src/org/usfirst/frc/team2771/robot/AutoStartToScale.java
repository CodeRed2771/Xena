package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * This auto program starts from the left or right side
 * and, if it is on the same side as the scale, will 
 * drive and place a cube on the scale. Currently doesn't
 * do anything if it is NOT on the same side as the scale.
 */

public class AutoStartToScale extends AutoBaseClass {

	public AutoStartToScale(char robotPosition) {
		super(robotPosition);
		System.out.println("AutoStartToScale started");
	}

	public void tick() {
		if (isRunning()) {
			SmartDashboard.putNumber("Auto Step", getCurrentStep());

			switch (getCurrentStep()) {
			case 0:
				setTimerAndAdvanceStep(2000);
				CubeClaw.holdCube();
				CubeClaw.setArmTravelPosition();
				if (robotPosition() == 1) {
					this.driveInches(48, 25, .4);
				} else {
					this.driveInches(48, -25, .4);
				}
				break;
			case 1:
				if (driveCompleted())
					advanceStep();
				break;
			case 2:
				setTimerAndAdvanceStep(3000);
				this.driveInches(160, 0, .3);
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
			case 6:
				setTimerAndAdvanceStep(2000);
				if (robotPosition() == 1) {
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
			case 8:
				setTimerAndAdvanceStep(500);
				CubeClaw.dropCube();
				CubeClaw.ejectCubeSlow();
				break;
			case 9:
				break;
			case 10:
				setTimerAndAdvanceStep(1000);
				this.driveInches(-12, 0, .5);
			case 11:
				if(driveCompleted())
					advanceStep();
			case 12:
				setTimerAndAdvanceStep(1000);
				CubeClaw.setArmTravelPosition();
				break;
			case 13:
				break;
			case 14:
				setTimerAndAdvanceStep(2000);
				if (robotPosition() == 1) {
					this.turnDegrees(-165, .5);
				} else {
					this.turnDegrees(165, .5);
				}
				break;
			case 15:
				if (driveCompleted())
					advanceStep();
				break;
			case 16:
				setTimerAndAdvanceStep(2000);
				Lift.goStartPosition();
				this.driveInches(36, 0, .5);
				break;
			case 17:
				break;
			case 18:
				stop();
				break;
			}
		}
	}
}