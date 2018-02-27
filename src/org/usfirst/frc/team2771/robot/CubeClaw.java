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
		arm.configPeakOutputForward(.5, 0);  // changed to half power temporarily
		arm.configPeakOutputReverse(-.5, 0);
		
		arm.selectProfileSlot(0, 0);
		arm.config_kF(0, 0.2, 0);
		arm.config_kP(0, 0.5, 0);
		arm.config_kI(0, 0, 0);
		arm.config_kD(0, 0, 0);
		
		SmartDashboard.putNumber("MM Arm F", .2);
		SmartDashboard.putNumber("MM Arm P", .5);
		
		SmartDashboard.putNumber("MM Arm Velocity", 30000);
		SmartDashboard.putNumber("MM Arm Acceleration", 12000);
		
		arm.configMotionCruiseVelocity(30000, 0);
		arm.configMotionAcceleration(12000, 0);
		
		arm.setSelectedSensorPosition(0, 0, 0);
		
		clawOpenCloseSolenoid = new DoubleSolenoid(Wiring.CLAW_PCM_PORTA, Wiring.CLAW_PCM_PORTB);

	}
	
	/*
	 * TICK ***********************************
	 */
	public static void tick() {
		
		arm.configMotionCruiseVelocity((int)SmartDashboard.getNumber("MM Arm Velocity", 0), 0);
		arm.configMotionAcceleration((int)SmartDashboard.getNumber("MM Arm Acceleration", 0), 0);
		//arm.config_kF(0, (int)SmartDashboard.getNumber("MM Arm F", 0), 0);
		//arm.config_kP(0, (int)SmartDashboard.getNumber("MM Arm P", 0), 0);
		SmartDashboard.putNumber("Arm Abs Encoder: ", getArmAbsolutePosition());

		if (currentBreaker.tripped()) {
			System.out.println("breaker tripped " + currentBreaker.getCurrent());
			holdCube();
			setArmScalePosition(); // pop up the arm so we know we have it.
		}
	}
	public static void off() {
		clawOpenCloseSolenoid.set(DoubleSolenoid.Value.kOff);
	}
	public static void open() {
		clawOpenCloseSolenoid.set(DoubleSolenoid.Value.kForward);
	}
	public static void close() {
		clawOpenCloseSolenoid.set(DoubleSolenoid.Value.kReverse);
	}
	
	public static void setArmVerticalPosition() {
		arm.set(ControlMode.MotionMagic, 0);
	}
	
	public static void setArmHorizontalPosition() {
		System.out.println("set arm horizontal");
		arm.set(ControlMode.MotionMagic, 0); //0 for competition robot?
	}
	
	public static void setArmSwitchPosition() {
		System.out.println("set arm switch");
		//arm.set(ControlMode.MotionMagic, 672); //competition robot?
		arm.set(ControlMode.MotionMagic, -1350);
	}
	
	public static void setArmScalePosition() {
		System.out.println("set arm scale");
		arm.set(ControlMode.MotionMagic, -1500); //1000 Competition robot? 
	}
	
	public static void armMove(double speed) {
		System.out.println("calling for arm move " + speed);
		arm.set(ControlMode.PercentOutput, speed);
	}
	
	public static void holdCube() {
		leftRollers.set(ControlMode.PercentOutput, .02);
		rightRollers.set(ControlMode.PercentOutput, .02);
		close();
		currentBreaker.reset();
	}
	
	public static void intakeCube() {
		close();
		leftRollers.set(ControlMode.PercentOutput, 1);
		rightRollers.set(ControlMode.PercentOutput, 1);
		currentBreaker.reset();
	}
	
	public static void dropCube() {
		open();	
		ejectCube();
		currentBreaker.reset();
	}
	public static void ejectCube() {
		currentBreaker.reset();
		leftRollers.set(ControlMode.PercentOutput, -1.0);
		rightRollers.set(ControlMode.PercentOutput, -1.0);
	}
	public static void stopIntake() {
		leftRollers.set(ControlMode.PercentOutput, 0);
		rightRollers.set(ControlMode.PercentOutput, 0);
		currentBreaker.reset();
	}
	
	public static double getArmAbsolutePosition() {
		return (arm.getSensorCollection().getPulseWidthPosition() & 0xFFF)/4095d;
	}
	
	// this tells the encoder that it's at a particular position
	private static void setArmEncPos(int d) {
		arm.getSensorCollection().setQuadraturePosition(d, 500);
	}
	

	/* 
	 * Resets the arm encoder value relative to what we've 
	 * determined to be the "zero" position. (the calibration values).
	 * This is so the rest of the program can just treat the turn encoder
	 * as if zero is the horizontal position.  We don't have to always calculate
	 * based off the calibrated zero position.
	 * e.g.  if the calibrated zero position is .25 and our current absolute position is .40
	 * then we reset the encoder value to be .15 * 4095, so we know were .15 away from the zero
	 * position.  The 4095 converts the position back to ticks.
	 * 
	 * Bottom line is that this is what applies the turn calibration values.
	 */
	public static void resetArmEncoder() {
//		if (getInstance() == null) return;
//
//			double offSet = 0;
//		
//			arm.set(ControlMode.PercentOutput, 0); // turn off the motor while we're setting encoder
//			
//			// first find the current absolute position of the arm encoder
//			offSet = getArmAbsolutePosition();
//			
//			// now use the difference between the current position and the calibration zero position
//			// to tell the encoder what the current relative position is (relative to the zero pos)
//			setArmEncPos((int) (calculatePositionDifference(offSet, Calibration.ARM_ABS_ZERO) * 4095d));

	}

	private static double calculatePositionDifference(double currentPosition, double calibrationZeroPosition) {
		if (currentPosition - calibrationZeroPosition > 0) {
			return currentPosition - calibrationZeroPosition;
		} else {
			return (1 - calibrationZeroPosition) + currentPosition;
		}
	}

	/*
	 * TEST METHODS
	 */
	public static void testIntakeCube(double speed) {
		leftRollers.set(ControlMode.PercentOutput, speed);
		rightRollers.set(ControlMode.PercentOutput, speed);
	}
	
	public static void testEjectCube(double speed) {
		leftRollers.set(ControlMode.PercentOutput, -speed);
		rightRollers.set(ControlMode.PercentOutput, -speed);
	}
}
