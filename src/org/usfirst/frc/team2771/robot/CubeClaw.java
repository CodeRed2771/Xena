package org.usfirst.frc.team2771.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

public class CubeClaw {
	private static CubeClaw instance;
	private static TalonSRX leftRollers;
	private static TalonSRX rightRollers;
	private static TalonSRX armMotor;
	private static DoubleSolenoid clawOpenCloseSolenoid;
//	private static Compressor c; 
	
	public static CubeClaw getInstance() {
		if (instance == null)
			instance = new CubeClaw();
		return instance;
	}

	public CubeClaw() {
		leftRollers = new TalonSRX(Wiring.CUBE_CLAW_LEFT_MOTOR);
		leftRollers.setInverted(true);
		rightRollers = new TalonSRX(Wiring.CUBE_CLAW_RIGHT_MOTOR);
		armMotor = new TalonSRX(Wiring.ARM_MOTOR);
		armMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,0);
		armMotor.setSelectedSensorPosition(0, 0, 0);

		clawOpenCloseSolenoid = new DoubleSolenoid(0,1);

//			c = new Compressor(0);
//			c.setClosedLoopControl(true);
	}
	
	public static void off() {
		clawOpenCloseSolenoid.set(DoubleSolenoid.Value.kOff);
	}
	
	public static void open() {
//		try {
			clawOpenCloseSolenoid.set(DoubleSolenoid.Value.kForward);
//		} catch (Exception ex) {
//			System.out.println("Error commanding the solenoid: " + ex.toString());
//		} 
		
	}
	
	public static void extend() {
		
	}
	
	public static void retract() {
		
	}
	
	public static void armMove(double speed) {
		armMotor.set(ControlMode.PercentOutput, speed);
	}
	
	public static void close() {
//		try {
			clawOpenCloseSolenoid.set(DoubleSolenoid.Value.kReverse);
//		} catch (Exception ex) {
//			System.out.println("Error commanding the solenoid: " + ex.toString());
//		} 
	}
	
	public static void intakeCube() {
		leftRollers.set(ControlMode.PercentOutput, .5);
		rightRollers.set(ControlMode.PercentOutput, .5);
	}
	public static void ejectCube() {
		leftRollers.set(ControlMode.PercentOutput, -.5);
		rightRollers.set(ControlMode.PercentOutput, -.5);
	}
	public static void stopIntake() {
		leftRollers.set(ControlMode.PercentOutput, 0);
		rightRollers.set(ControlMode.PercentOutput, 0);
	}
	
	public static void testIntakeCube(double speed) {
		leftRollers.set(ControlMode.PercentOutput, speed);
		rightRollers.set(ControlMode.PercentOutput, speed);
	}
	
	public static void testEjectCube(double speed) {
		leftRollers.set(ControlMode.PercentOutput, -speed);
		rightRollers.set(ControlMode.PercentOutput, -speed);
	}
}
