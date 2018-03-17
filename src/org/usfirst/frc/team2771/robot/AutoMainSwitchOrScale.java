package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * This program, under the authority of the state,
 * has been officially declared OBSOLETE. It is
 * scheduled for termination in the next 24 hours
 * by the manner of execution in which it chooses.
 */

public class AutoMainSwitchOrScale extends AutoBaseClass {
	AutoBaseClass mAutoSubroutine;

	public AutoMainSwitchOrScale(char robotPosition) {
		super(robotPosition);
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * THIS IS NOT USED
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	public void tick() {
		if (isRunning()) {

			if (robotPosition() == 'C') {
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
				if (robotPosition() == 'R') {
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
