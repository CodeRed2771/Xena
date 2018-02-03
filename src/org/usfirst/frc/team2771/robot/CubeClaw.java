package org.usfirst.frc.team2771.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Compressor;

public class CubeClaw {
	TalonSRX leftRollers;
	TalonSRX rightRollers;
	Compressor c; 

	
	
	public CubeClaw() {
		leftRollers = new TalonSRX(Wiring.CUBE_CLAW_LEFT_MOTOR);
		rightRollers = new TalonSRX(Wiring.CUBE_CLAW_RIGHT_MOTOR);
		try {
			c = new Compressor(0);
			c.setClosedLoopControl(true);
		} catch (Exception ex) {
			System.out.println("Error creating or addressing the compressor: " + ex.toString());
		} 
		

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
	
	public void testIntakeCube(double speed) {
		leftRollers.set(ControlMode.PercentOutput, speed);
		rightRollers.set(ControlMode.PercentOutput, speed);
	}
	
	public void testEjectCube(double speed) {
		leftRollers.set(ControlMode.PercentOutput, -speed);
		rightRollers.set(ControlMode.PercentOutput, -speed);
	}
}
