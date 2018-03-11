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
						// Switch is ours, so go for it
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
						// Scale is ours, but the switch wasn't, so go for the scale
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
						// Neither switch or scale is ours - so just do baseline
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
				} else { /* Position 3 */
					if (this.isSwitchRight()) {
						// Switch is ours, so go for it
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
						// Scale is ours, but the switch wasn't, so go for the scale
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
						// Neither switch or scale is ours - so just do baseline
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
					SmartDashboard.putNumber("Auto Base Step", getCurrentStep());
				}
			}

		}

	}
}
