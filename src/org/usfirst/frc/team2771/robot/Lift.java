package org.usfirst.frc.team2771.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Lift {
	TalonSRX liftMotor;
	TalonSRX liftFollower;
	
	public Lift() {
		liftMotor = new TalonSRX(Wiring.LIFT_MASTER);
		liftFollower = new TalonSRX(Wiring.LIFT_FOLLLOWER);
		liftFollower.follow(liftMotor);
		liftFollower.setInverted(true);
	}
	
	public void move(double speed) {
		liftMotor.set(ControlMode.PercentOutput, speed);
		
	}
	public void goToSwitchHeight() {
		//The switch is the little one.
		liftMotor.set(ControlMode.PercentOutput, .7, 12); //TODO change the number to the correct height.
	}
	public void goToScaleHeight() {
		//The scale is the big one.
		//The scale has three different positions, up, down, and level. It could be useful for autonomous.
		liftMotor.set(ControlMode.PercentOutput, .7, 64); //TODO change the number to the correct height. 
	}
}
