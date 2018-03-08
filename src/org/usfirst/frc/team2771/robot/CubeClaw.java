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

	private static CurrentBreaker currentBreaker1;
	private static CurrentBreaker currentBreaker2;
	
	private static boolean holdingCube = false;
	private static double ejectEndTime;
	
	public static CubeClaw getInstance() {
		if (instance == null)
			instance = new CubeClaw();
		return instance;
	}

	public CubeClaw() {
		leftRollers = new TalonSRX(Wiring.CUBE_CLAW_LEFT_MOTOR);
		leftRollers.setInverted(true);
		
		rightRollers = new TalonSRX(Wiring.CUBE_CLAW_RIGHT_MOTOR);
		
		leftRollers.configOpenloopRamp(.2, 0);
		rightRollers.configOpenloopRamp(.2,0);
		
		currentBreaker1 = new CurrentBreaker(null, Wiring.CLAW_PDP_PORT1, Calibration.CLAW_MAX_CURRENT, 250, 2000); // The 2000 is pretty much irrelevant
		currentBreaker2 = new CurrentBreaker(null, Wiring.CLAW_PDP_PORT2, Calibration.CLAW_MAX_CURRENT, 250, 2000);
		resetIntakeStallDetector();
		
		arm = new TalonSRX(Wiring.ARM_MOTOR);
		//arm.setSensorPhase(true);
		
		arm.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,0);
		arm.setSelectedSensorPosition(0, 0, 0);
		
		arm.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 0);
		arm.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 0);
		
		arm.configNominalOutputForward(0, 0);
		arm.configNominalOutputReverse(0, 0);
		arm.configPeakOutputForward(1, 0);  
		arm.configPeakOutputReverse(-1, 0);
		
		arm.configClosedloopRamp(.1, 0);
		
		// added this in 3/7/18 to try to protect the arm
		arm.configPeakCurrentLimit(24, 10); 
		arm.configPeakCurrentDuration(200, 10);
		arm.configContinuousCurrentLimit(17, 10); 
		arm.enableCurrentLimit(true);
		
		arm.selectProfileSlot(0, 0);
		arm.config_kF(0, 5, 0);
		arm.config_kP(0, 5, 0);
		arm.config_kI(0, 0, 0);
		arm.config_kD(0, 0, 0);
		
		SmartDashboard.putNumber("MM Arm F", 5);
		SmartDashboard.putNumber("MM Arm P", 5);
		
		SmartDashboard.putNumber("MM Arm Velocity", 300);
		SmartDashboard.putNumber("MM Arm Acceleration", 300);
		
		arm.configMotionCruiseVelocity(300, 0);
		arm.configMotionAcceleration(300, 0);
		
		arm.setSelectedSensorPosition(0, 0, 0);
		
		clawOpenCloseSolenoid = new DoubleSolenoid(Wiring.CLAW_PCM_PORTA, Wiring.CLAW_PCM_PORTB);
		
		ejectEndTime = aDistantFutureTime();

	}
	
	/*
	 * TICK -----------------------------------------------
	 */
	public static void tick() {
		
		arm.configMotionCruiseVelocity((int)SmartDashboard.getNumber("MM Arm Velocity", 0), 0);
		arm.configMotionAcceleration((int)SmartDashboard.getNumber("MM Arm Acceleration", 0), 0);
		arm.config_kF(0, (int)SmartDashboard.getNumber("MM Arm F", 0), 0);
		arm.config_kP(0, (int)SmartDashboard.getNumber("MM Arm P", 0), 0);
		SmartDashboard.putNumber("Arm Abs Encoder: ", getArmAbsolutePosition());
		SmartDashboard.putNumber("Arm Relative Encoder ", arm.getSensorCollection().getQuadraturePosition());

		if (intakeStalled() && !holdingCube) {
			System.out.println("Intake stalled - switching to hold mode");
			holdCube();
			setArmTravelPosition(); // pop up the arm so we know we have it.
		}
		
		// this turns off the claw after starting an eject
		if (System.currentTimeMillis() > ejectEndTime) {
			System.out.println("Stopped ejecting");
			stopIntake();
			ejectEndTime = aDistantFutureTime();
		}
	}
	
	// CONTROL METHODS ------------------------------------------------

	public static void intakeCube() {
		setArmHorizontalPosition();
		holdingCube = false;
		closeClaw();
		leftRollers.set(ControlMode.PercentOutput, -.75);
		rightRollers.set(ControlMode.PercentOutput, -.75);
		resetIntakeStallDetector();
		ejectEndTime = aDistantFutureTime();
	}

	public static void holdCube() {
		holdingCube = true;
		stopIntake();
	}
	
	public static void dropCube() {
		holdingCube = false;
		openClaw();	
		resetIntakeStallDetector();
	}
	
	public static void ejectCube() {
		holdingCube = false;
		resetIntakeStallDetector();
		leftRollers.set(ControlMode.PercentOutput, .8);
		rightRollers.set(ControlMode.PercentOutput, .8);
		
		ejectEndTime = System.currentTimeMillis() + 1000;  // give it one second to eject.
	}
	
	public static void stopIntake() {
		leftRollers.set(ControlMode.PercentOutput, 0);
		rightRollers.set(ControlMode.PercentOutput, 0);
		resetIntakeStallDetector();
	}
	
	// CLAW PNEUMATICS ------------------------------------------------
	private static void turnOffClawSolenoid() {
		clawOpenCloseSolenoid.set(DoubleSolenoid.Value.kOff);
	}
	private static void openClaw() {
		clawOpenCloseSolenoid.set(DoubleSolenoid.Value.kForward);
	}
	private static void closeClaw() {
		clawOpenCloseSolenoid.set(DoubleSolenoid.Value.kReverse);
	}
	
	// ARM POSITIONING ------------------------------------------------
	
	public static void setArmHorizontalPosition() {
		System.out.println("set arm horizontal");
		arm.set(ControlMode.MotionMagic, 0); 
	}
	
	public static void setArmSwitchPosition() {
		System.out.println("set arm switch");
		arm.set(ControlMode.MotionMagic, -745);
	}
	
	public static void setArmScalePosition() {
		System.out.println("set arm scale");
		arm.set(ControlMode.MotionMagic, -900); 
	}
	
	public static void setArmTravelPosition() {
		System.out.println("set arm travel");
		arm.set(ControlMode.MotionMagic, -1819);
	}
	
	public static void setArmOverTheTopPosition() {
		System.out.println("set arm over the top");
		arm.set(ControlMode.MotionMagic, -3000);
	}
	
	// UTILITY METHODS ---------------------------------------------------------	
	
	public static boolean intakeStalled() {
		return (currentBreaker1.tripped() || currentBreaker2.tripped());
	}
	
	public static void resetIntakeStallDetector() {
		currentBreaker1.reset();
		currentBreaker2.reset();
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
		if (getInstance() == null) return;

			double offSet = 0;
			int newArmEncoderValue = 0;
			
			arm.set(ControlMode.PercentOutput, 0); // turn off the motor while we're setting encoder
			
			// first find the current absolute position of the arm encoder
			offSet = getArmAbsolutePosition();
			
			// now use the difference between the current position and the calibration zero position
			// to tell the encoder what the current relative position is (relative to the zero pos)
			newArmEncoderValue = (int) (calculatePositionDifference(offSet, Calibration.ARM_ABS_ZERO) * 4095d);
			setArmEncPos(newArmEncoderValue);
			System.out.println("arm absolute " + offSet);
			System.out.println("Setting arm encoder to " + newArmEncoderValue);

	}

	private static double calculatePositionDifference(double currentPosition, double calibrationZeroPosition) {
			return currentPosition - calibrationZeroPosition;
	}
	
	private static double aDistantFutureTime() {
		return System.currentTimeMillis() + 900000;  // 15 minutes in the future
	}

	/*
	 * TEST METHODS
	 */

	public static void armMove(double speed) {
		System.out.println("calling for arm move " + speed);
		arm.set(ControlMode.PercentOutput, speed);
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
