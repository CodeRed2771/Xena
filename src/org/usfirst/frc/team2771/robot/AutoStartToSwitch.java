package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * This auto program starts from the left or right side
 * and, if it is on the same side as the switch, will 
 * drive and place a cube on the switch. Currently doesn't
 * do anything if it is NOT on the same side as the switch.
 * 
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
				setTimerAndAdvanceStep(4000);
				CubeClaw.holdCube();
				CubeClaw.setArmTravelPosition();
				if (robotPosition() == 'R') {
					this.driveInches(140, 10, .3);
				} else {
					this.driveInches(140, -10, .3);
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
				this.driveInches(36, 0, .3);
				break;
			case 5:
				if(driveCompleted())
					advanceStep();
				break;
			case 6: // DROP CUBE
				setTimerAndAdvanceStep(700);
				CubeClaw.dropCube();
				break;
			case 7:
				break;
			case 8:
				setTimerAndAdvanceStep(2000);
				this.driveInches(-24, 0, 0.3);
				break;
			case 9:
				if(driveCompleted())
					advanceStep();
				break;
			case 20:
				Lift.goStartPosition();
				this.setStep(21);
			case 21:
				stop(); // signals the end 
				break;
			}
		}
	}
}
