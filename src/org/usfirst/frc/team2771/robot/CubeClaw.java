package org.usfirst.frc.team2771.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class CubeClaw {
	TalonSRX leftRollers;
	TalonSRX rightRollers;
	
	
	public CubeClaw() {
		leftRollers = new TalonSRX(Wiring.CUBE_CLAW_LEFT_MOTOR);
		rightRollers = new TalonSRX(Wiring.CUBE_CLAW_RIGHT_MOTOR);
		
	}
	
	public void open() {
	
	}
	public void close() {
		
	}
	public void intakeCube() {
		leftRollers.set(ControlMode.PercentOutput, .5);
		rightRollers.set(ControlMode.PercentOutput, .5);
	}
	public void ejectCube() {
		leftRollers.set(ControlMode.PercentOutput, -.5);
		rightRollers.set(ControlMode.PercentOutput, -.5);
	}
	public void stopIntake() {
		leftRollers.set(ControlMode.PercentOutput, 0);
		rightRollers.set(ControlMode.PercentOutput, 0);
	}
}
