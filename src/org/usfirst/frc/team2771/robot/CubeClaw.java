package org.usfirst.frc.team2771.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

public class CubeClaw {
	TalonSRX leftRollers;
	TalonSRX rightRollers;
	Compressor c; 
	DoubleSolenoid clawOpenCloseSolenoid;
	
	
	public CubeClaw() {
		leftRollers = new TalonSRX(Wiring.CUBE_CLAW_LEFT_MOTOR);
		leftRollers.setInverted(true);
		rightRollers = new TalonSRX(Wiring.CUBE_CLAW_RIGHT_MOTOR);
//		try {
			c = new Compressor(0);
			c.setClosedLoopControl(true);
			clawOpenCloseSolenoid = new DoubleSolenoid(0,1);

	

	}
	
	public void off() {
		clawOpenCloseSolenoid.set(DoubleSolenoid.Value.kOff);
	}
	
	public void open() {
//		try {
			clawOpenCloseSolenoid.set(DoubleSolenoid.Value.kForward);
//		} catch (Exception ex) {
//			System.out.println("Error commanding the solenoid: " + ex.toString());
//		} 
		
	}
	public void close() {
//		try {
			clawOpenCloseSolenoid.set(DoubleSolenoid.Value.kReverse);
//		} catch (Exception ex) {
//			System.out.println("Error commanding the solenoid: " + ex.toString());
//		} 
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
