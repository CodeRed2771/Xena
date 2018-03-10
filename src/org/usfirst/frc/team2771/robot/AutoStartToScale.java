package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoStartToScale extends AutoBaseClass {

	public AutoStartToScale(int robotPosition) {
		super(robotPosition);
	}

	public void tick() {
		if (isRunning()) {
			SmartDashboard.putNumber("Auto Step", getCurrentStep());

			switch (getCurrentStep()) {
			case 0:
				setTimerAndAdvanceStep(3000);
				CubeClaw.holdCube();
				CubeClaw.setArmTravelPosition();
				if (robotPosition() == 1) {
					this.driveInches(48, 45, .4);
				} else {
					this.driveInches(48, -45, .4);
				}
				break;
			case 1:
				if (driveCompleted())
					advanceStep();
				break;
			case 2:
				setTimerAndAdvanceStep(4000);
				this.driveInches(100, 0, .3);
				break;
			case 3:
				if (driveCompleted())
					advanceStep();
				break;
			case 4:
				setTimerAndAdvanceStep(2000);
				Lift.goHighScale();
				break;
			case 5:
				if (driveCompleted())
					advanceStep();
				break;
			case 6:
				setTimerAndAdvanceStep(2000);
				if (robotPosition() == 1) {
					this.turnDegrees(90, .5);
				} else {
					this.turnDegrees(-90, .5);
				}
				break;
			case 7:
				if (driveCompleted())
					advanceStep();
				break;
			case 8:
				setTimerAndAdvanceStep(2000);
				this.driveInches(12, 0, .5);
				CubeClaw.setArmScalePosition();
				break;
			case 9:
				if (driveCompleted())
					advanceStep();
				break;
			case 10:
				setTimerAndAdvanceStep(1000);
				CubeClaw.ejectCube();
				break;
			case 11:
				break;
			case 12:
				setTimerAndAdvanceStep(1000);
				CubeClaw.setArmTravelPosition();
				break;
			case 13:
				break;
			case 14:
				this.driveInches(-12, 0, .5);
				break;
			case 15:
				if (driveCompleted())
					advanceStep();
				break;
			case 16:
				setTimerAndAdvanceStep(2000);
				if (robotPosition() == 1) {
					this.turnDegrees(-90, .5);
				} else {
					this.turnDegrees(90, .5);
				}
				break;
			case 17:
				if (driveCompleted())
					advanceStep();
				break;
			case 18:
				setTimerAndAdvanceStep(3000);
				Lift.goStartPosition();
				this.driveInches(36, 0, .5);
				break;
			case 19:
				break;
			case 20:
				stop();
				break;
			}
		}
	}
}