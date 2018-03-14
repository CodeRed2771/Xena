package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoSwitchPlaceCube extends AutoBaseClass {

	public AutoSwitchPlaceCube(char robotPosition) {
		super(robotPosition);
	}

	public void tick() {

		if (isRunning()) {

			SmartDashboard.putNumber("Auto Step", getCurrentStep());

			switch (getCurrentStep()) {

			case 0:
				setTimerAndAdvanceStep(2000);
				if (robotPosition() == 1)
					turnDegrees(90, .2);
				else
					turnDegrees(-90, .2);
				Lift.goSwitch();
				break;
			case 1:
				if (driveCompleted())
					advanceStep();
				break;
			case 2:
				setTimerAndAdvanceStep(2000);
				this.driveInches(12, 0, .2);
			case 3:
				if (driveCompleted())
					advanceStep();
				break;
			case 4:
				// claw open
				CubeClaw.ejectCube();
				advanceStep();
			case 5:
				stop();
				break;
			}
		}
	}
}
