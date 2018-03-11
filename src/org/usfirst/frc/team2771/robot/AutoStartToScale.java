package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoStartToScale extends AutoBaseClass {

	public AutoStartToScale(int robotPosition) {
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
				this.driveInches(130, 0, .3);
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
				if (driveCompleted())
					advanceStep();
				break;
			case 9:
				setTimerAndAdvanceStep(1000);
				CubeClaw.dropCube();
				break;
			case 10:
				setTimerAndAdvanceStep(2000);
				this.driveInches(-12, 0, .5);
			case 11:
				if(driveCompleted())
					advanceStep();
			case 12:
				break;
			case 13:
				setTimerAndAdvanceStep(1000);
				CubeClaw.setArmTravelPosition();
				break;
			case 14:
				break;
			case 15:
				setTimerAndAdvanceStep(1000);
				this.driveInches(-12, 0, .5);
				break;
			case 16:
				if (driveCompleted())
					advanceStep();
				break;
			case 17:
				setTimerAndAdvanceStep(2000);
				if (robotPosition() == 1) {
					this.turnDegrees(-90, .5);
				} else {
					this.turnDegrees(90, .5);
				}
				break;
			case 18:
				if (driveCompleted())
					advanceStep();
				break;
			case 19:
				setTimerAndAdvanceStep(3000);
				Lift.goStartPosition();
				this.driveInches(36, 0, .5);
				break;
			case 20:
				break;
			case 21:
				stop();
				break;
			}
		}
	}
}