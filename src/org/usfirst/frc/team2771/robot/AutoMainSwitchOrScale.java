package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoMainSwitchOrScale extends AutoBaseClass {
	AutoBaseClass mAutoSubroutine;

	public AutoMainSwitchOrScale(int robotPosition) {
		super(robotPosition);
	}

	public void tick() {
		System.out.println("in auto main switch. pos = " + robotPosition());
		SmartDashboard.putNumber("Auto Step", getCurrentStep());
		
		if (isRunning()) {

			if (robotPosition() == 2) {
				switch (getCurrentStep()) {
				case 0:
					mAutoSubroutine = new AutoBaseLine(this.robotPosition());
					mAutoSubroutine.start();
					advanceStep();
					break;
				case 1:
					if (mAutoSubroutine.hasCompleted())
						stop();
					break;
				case 2:
					break;
				}
			} else {
				if (robotPosition() == 1) {
					System.out.println("in auto main switch position = 1");
					if (this.isSwitchRight()) {
						System.out.println("switch is right step = " + getCurrentStep());
						switch (getCurrentStep()) {
						case 0:
							mAutoSubroutine = new AutoStartToSwitch(this.robotPosition());
							mAutoSubroutine.start();
							advanceStep();
							break;
						case 1:
							if (mAutoSubroutine.hasCompleted())
								stop();
							break;
						case 2:
							break;
						}
					} else if (this.isScaleRight()) {
						System.out.println("scale is right step = " + getCurrentStep());
						switch (getCurrentStep()) {
						case 0:
							System.out.println("starting autostarttoscale");
							mAutoSubroutine = new AutoStartToScale(this.robotPosition());
							mAutoSubroutine.start();
							advanceStep();
							break;
						case 1:
							if (mAutoSubroutine.hasCompleted())
								stop();
							break;
						case 2:
							break;
						}
					} else {
						switch (getCurrentStep()) {
						case 0:
							mAutoSubroutine = new AutoBaseLine(this.robotPosition());
							mAutoSubroutine.start();
							advanceStep();
							break;
						case 1:
							if (mAutoSubroutine.hasCompleted())
								stop();
							break;
						case 2:
							break;
						}
					}
				}
				
			}
		}

	}
}
