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
			case 0: // DRIVE FORWARD
				setTimerAndAdvanceStep(2500);
				CubeClaw.holdCube();
				CubeClaw.setArmTravelPosition();
				if (robotPosition() == 'R') {
					this.driveInches(135, 10, .4);
				} else {
					this.driveInches(135, -10, .4);
				}
				break;
			case 1:
				if (driveCompleted())
					advanceStep();
				break;
			case 2: // TURN TOWARDS SWITCH
				setTimerAndAdvanceStep(2000);
				CubeClaw.setArmSwitchPosition();
				Lift.goSwitch();
				if(robotPosition() == 'R') {
					this.turnDegrees(-90, .5);
				} else if(robotPosition() == 'L') {
					this.turnDegrees(90, .5);
				}
				break;
			case 3:
				if(driveCompleted())
					advanceStep();
				break;
			case 4: // DRIVE TO SWITCH
				setTimerAndAdvanceStep(2000);
				this.driveInches(36, 0, .5);
				break;
			case 5:
				if(driveCompleted())
					advanceStep();
				break;
			case 6: // DROP CUBE
				setTimerAndAdvanceStep(700);
				CubeClaw.ejectCube();
				break;
			case 7:
				break;
			case 8:
				setTimerAndAdvanceStep(1000);
				this.driveInches(-24, 0, 0.4);
				break;
			case 9:
				if(driveCompleted())
					advanceStep();
				break;
			case 20:
				this.setStep(21);
			case 21:
				stop(); // signals the end 
				break;
			}
		}
	}
}
