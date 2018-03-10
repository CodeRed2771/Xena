package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoMainSwitchOrScale extends AutoBaseClass {
	AutoBaseClass mAutoSubroutine;

	public AutoMainSwitchOrScale(int robotPosition) {
		super(robotPosition);
	}

	public void tick() {
		if (isRunning()) {

			if (robotPosition() == 2) {
				switch (getCurrentStep()) {
				case 0:
					mAutoSubroutine = new AutoBaseLine(this.robotPosition());
					mAutoSubroutine.start();
					break;
				case 3:
					if (mAutoSubroutine.hasCompleted())
						stop();
					break;
				case 4:
					break;
				}
			} else {
				if (robotPosition() == 1) {
					if (this.isSwitchRight()) {
						switch (getCurrentStep()) {
						case 0:
							mAutoSubroutine = new AutoStartToSwitch(this.robotPosition());
							mAutoSubroutine.start();
							break;
						case 3:
							if (mAutoSubroutine.hasCompleted())
								stop();
							break;
						case 4:
							break;
						}
					} else if (this.isScaleRight()) {
						switch (getCurrentStep()) {
						case 0:
							mAutoSubroutine = new AutoStartToScale(this.robotPosition());
							mAutoSubroutine.start();
							break;
						case 3:
							if (mAutoSubroutine.hasCompleted())
								stop();
							break;
						case 4:
							break;
						}
					} else {
						switch (getCurrentStep()) {
						case 0:
							mAutoSubroutine = new AutoBaseLine(this.robotPosition());
							mAutoSubroutine.start();
							break;
						case 3:
							if (mAutoSubroutine.hasCompleted())
								stop();
							break;
						case 4:
							break;
						}
					}
				}
				SmartDashboard.putNumber("Auto Base Step", getCurrentStep());
			}
		}

	}
}
