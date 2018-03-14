package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoStartToSwitch extends AutoBaseClass {
	public AutoStartToSwitch(char robotPosition) {
		super(robotPosition);
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
				setTimerAndAdvanceStep(2000);
				if(robotPosition() == 'R') {
					this.turnDegrees(90, .5);
				} else if(robotPosition() == 'L') {
					this.turnDegrees(-90, .5);
				}
				break;
			case 3:
				if(driveCompleted())
					advanceStep();
				break;
			case 4:
				setTimerAndAdvanceStep(2000);
				this.driveInches(24, 0, .5);
				break;
			case 5:
				if(driveCompleted())
					advanceStep();
				break;
			case 6:
				setTimerAndAdvanceStep(700);
				CubeClaw.ejectCube();
			case 7:
				break;
			case 8:
				this.setStep(20);
			case 20:
				stop(); // signals the end 
				break;
			}
		}
	}
}
