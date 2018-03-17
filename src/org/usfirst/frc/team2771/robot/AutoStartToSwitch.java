package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * This auto program starts from the left or right side
 * and, if it is on the same side as the switch, will 
 * drive and place a cube on the switch. Currently doesn't
 * do anything if it is NOT on the same side as the switch.
 * 
 * NOTE: Consider declaring this program OBSOLETE.
 *  -The State
 */

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
