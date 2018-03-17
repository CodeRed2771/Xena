package org.usfirst.frc.team2771.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * This auto program will put a cube in 
 * the switch if we are on the same side
 * as the switch, or in the scale if we
 * are on the same side as the scale.
 * It prioritizes the switch.
 */

public class AutoMainSwitchOrScale extends AutoBaseClass {
	AutoBaseClass mAutoSubroutine;

	public AutoMainSwitchOrScale(char robotPosition) {
		super(robotPosition);
	}
	
	public void tick() {
		if (isRunning()) {

			if (robotPosition() == getGameData().toCharArray()[0]) { // If we are on the left, and the switch is on the left, drop a cube in the switch
				mAutoSubroutine = new AutoStartToSwitch(this.robotPosition());
				mAutoSubroutine.start();
			} else if (robotPosition() == getGameData().toCharArray()[1]) { // If we are on the left, and the scale is on the left, drop  a cube in the scale
				mAutoSubroutine = new AutoStartToScale(this.robotPosition());
				mAutoSubroutine.start();
			} else {
				//Put code in me!
			}
		}
	}
}
