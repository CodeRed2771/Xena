package org.usfirst.frc.team2771.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Lift {
	TalonSRX lift1;
	TalonSRX lift2;
	
	public Lift() {
		lift1 = new TalonSRX(Wiring.LIFT_1);
		lift2 = new TalonSRX(Wiring.LIFT_2);
		lift2.setInverted(true);
	}
	
	public void move(double speed) {
		lift1.set(ControlMode.PercentOutput, speed);
		lift2.set(ControlMode.PercentOutput, speed);
		
	}
	public void goToSwitchHeight() {
		//The switch is the little one.
		lift1.set(ControlMode.PercentOutput, .7, 12); //TODO change the number to the correct height.
		lift2.set(ControlMode.PercentOutput, .7, 12);
	}
	public void goToScaleHeight() {
		//The scale is the big one.
		//The scale has three different positions, up, down, and level. It could be useful for autonomous.
		lift1.set(ControlMode.PercentOutput, .7, 64); //TODO change the number to the correct height. 
		lift2.set(ControlMode.PercentOutput, .7, 64);
	}
}
