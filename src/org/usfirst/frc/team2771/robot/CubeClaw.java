package org.usfirst.frc.team2771.robot;

import org.usfirst.frc.team2771.libs.CurrentBreaker;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CubeClaw {
	private static CubeClaw instance;
	private static TalonSRX leftRollers;
	private static TalonSRX rightRollers;
	private static TalonSRX arm;
	private static DoubleSolenoid clawOpenCloseSolenoid;
//	private static Compressor c; 
	private static CurrentBreaker currentBreaker;
	
	public static CubeClaw getInstance() {
		if (instance == null)
			instance = new CubeClaw();
		return instance;
	}

	public CubeClaw() {
		leftRollers = new TalonSRX(Wiring.CUBE_CLAW_LEFT_MOTOR);
		leftRollers.setInverted(true);
		
		rightRollers = new TalonSRX(Wiring.CUBE_CLAW_RIGHT_MOTOR);
		
		currentBreaker = new CurrentBreaker(null, Wiring.CLAW_PDP_PORT, Calibration.CLAW_MAX_CURRENT, 2000, 2000); //These are not real numbers.
		
		arm = new TalonSRX(Wiring.ARM_MOTOR);
		arm.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,0);
		arm.setSelectedSensorPosition(0, 0, 0);
		
		arm.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 0);
		arm.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 0);
		
		arm.configNominalOutputForward(0, 0);
		arm.configNominalOutputReverse(0, 0);
		arm.configPeakOutputForward(1, 0);
		arm.configPeakOutputReverse(-1, 0);
		
		arm.selectProfileSlot(0, 0);
		arm.config_kF(0, 0.2, 0);
		arm.config_kP(0, 0.2, 0);
		arm.config_kI(0, 0, 0);
		arm.config_kD(0, 0, 0);
		
		SmartDashboard.putNumber("MM Velocity", 15000);
		SmartDashboard.putNumber("MM Acceleration", 6000);
		
		arm.configMotionCruiseVelocity(15000, 0);
		arm.configMotionAcceleration(6000, 0);
		
		arm.setSelectedSensorPosition(0, 0, 0);
		

	//	clawOpenCloseSolenoid = new DoubleSolenoid(0,1);

//			c = new Compressor(0);
//			c.setClosedLoopControl(true);
	}
	
	public static void off() {
		//clawOpenCloseSolenoid.set(DoubleSolenoid.Value.kOff);
	}
	
	public static void open() {
//		try {
			clawOpenCloseSolenoid.set(DoubleSolenoid.Value.kForward);
//		} catch (Exception ex) {
//			System.out.println("Error commanding the solenoid: " + ex.toString());
//		} 
		
	}
	
	public static void tick() {
		arm.configMotionCruiseVelocity((int)SmartDashboard.getNumber("MM Velocity", 0), 0);
		arm.configMotionAcceleration((int)SmartDashboard.getNumber("MM Acceleration", 0), 0);
		if (currentBreaker.tripped()) {
			rightRollers.set(ControlMode.PercentOutput,0);
			leftRollers.set(ControlMode.PercentOutput, 0);
		}
	}
	
	public static void setArmVerticalPosition() {
		arm.set(ControlMode.MotionMagic, 1344);
	}
	
	public static void setArmHorizontalPosition() {
		arm.set(ControlMode.MotionMagic, 0);
	}
	
	public static void setArmSwitchPosition() {
		arm.set(ControlMode.MotionMagic, 672); //This number is not accurate. It is a guess.
	}
	
	public static void setArmScalePosition() {
		arm.set(ControlMode.MotionMagic, 1000); //This is also a guess.
	}
	
	public static void armMove(double speed) {
		System.out.println("calling for arm move " + speed);
		arm.set(ControlMode.PercentOutput, speed);
	}
	
	public static void close() {
//		try {
			clawOpenCloseSolenoid.set(DoubleSolenoid.Value.kReverse);
//		} catch (Exception ex) {
//			System.out.println("Error commanding the solenoid: " + ex.toString());
//		} 
	}
	
	public static void intakeCube() {
		leftRollers.set(ControlMode.PercentOutput, .7);
		rightRollers.set(ControlMode.PercentOutput, .7);
		currentBreaker.reset();
	}
	public static void ejectCube() {
		leftRollers.set(ControlMode.PercentOutput, -1.0);
		rightRollers.set(ControlMode.PercentOutput, -1.0);
		currentBreaker.reset();
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