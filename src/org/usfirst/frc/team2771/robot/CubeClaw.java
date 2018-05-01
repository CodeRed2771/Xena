package org.usfirst.frc.team2771.robot;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.usfirst.frc.team2771.libs.CurrentBreaker;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
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
	private static boolean intakeRunning = false;
	private static double ejectEndTime;
	private static double startReverseTime;
	private static boolean reverseAllowed = false;
	private static boolean armHasBeenCalibrated = false;

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
		rightRollers.configOpenloopRamp(.2, 0);

		leftRollers.setNeutralMode(NeutralMode.Brake);
		rightRollers.setNeutralMode(NeutralMode.Brake);

		currentBreaker1 = new CurrentBreaker(null, Wiring.CLAW_PDP_PORT1, Calibration.CLAW_MAX_CURRENT, 250, 2000); // The
																													// 2000
																													// is
																													// pretty
																													// much
																													// irrelevant
		currentBreaker2 = new CurrentBreaker(null, Wiring.CLAW_PDP_PORT2, Calibration.CLAW_MAX_CURRENT, 250, 2000);
		resetIntakeStallDetector();

		arm = new TalonSRX(Wiring.ARM_MOTOR);

		arm.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		arm.setSelectedSensorPosition(0, 0, 0);

		arm.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 0);
		arm.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 0);

		arm.configNominalOutputForward(0, 0);
		arm.configNominalOutputReverse(0, 0);
		arm.configPeakOutputForward(1, 0);
		arm.configPeakOutputReverse(-1, 0);

		arm.configClosedloopRamp(.1, 0);

		// added this in 3/7/18 to try to protect the arm
		arm.configPeakCurrentLimit(20, 10);
		arm.configPeakCurrentDuration(200, 10);
		arm.configContinuousCurrentLimit(6, 10);
		arm.enableCurrentLimit(true);

		arm.selectProfileSlot(0, 0);
		arm.config_kF(0, 5, 0);
		arm.config_kP(0, 5, 0);
		arm.config_kI(0, 0, 0);
		arm.config_kD(0, 0, 0);

		// SmartDashboard.putNumber("MM Arm F", 5);
		// SmartDashboard.putNumber("MM Arm P", 5);
		//
		// SmartDashboard.putNumber("MM Arm Velocity", 300);
		// SmartDashboard.putNumber("MM Arm Acceleration", 300);

		arm.configMotionCruiseVelocity(300, 0);
		arm.configMotionAcceleration(300, 0);

		arm.setSelectedSensorPosition(0, 0, 0);

		clawOpenCloseSolenoid = new DoubleSolenoid(Wiring.CLAW_PCM_PORTA, Wiring.CLAW_PCM_PORTB);

		ejectEndTime = aDistantFutureTime();
		startReverseTime = aDistantFutureTime();

	}

	/*
	 * TICK -----------------------------------------------
	 */
	public static void tick() {

		// arm.configMotionCruiseVelocity((int) SmartDashboard.getNumber("MM Arm
		// Velocity", 0), 0);
		// arm.configMotionAcceleration((int) SmartDashboard.getNumber("MM Arm
		// Acceleration", 0), 0);
		// arm.config_kF(0, (int) SmartDashboard.getNumber("MM Arm F", 0), 0);
		// arm.config_kP(0, (int) SmartDashboard.getNumber("MM Arm P", 0), 0);
		// SmartDashboard.putNumber("Arm Abs Encoder: ",
		// getArmAbsolutePosition());
		// SmartDashboard.putNumber("Arm Relative Encoder ",
		// arm.getSensorCollection().getQuadraturePosition());

		showArmEncoderValue();

		// SmartDashboard.putNumber("Intake Current 1",
		// round0(currentBreaker1.getCurrent()));
		// SmartDashboard.putNumber("Intake Current 2",
		// round0(currentBreaker2.getCurrent()));

		if (intakeStalled() && !holdingCube) {
			System.out.println("Intake stalled - switching to hold mode");
			holdCube();
			setArmTravelPosition(); // pop up the arm so we know we have it.
		}

		// this turns off the claw after starting an eject
		if (System.currentTimeMillis() > ejectEndTime) {
			stopIntake();
			ejectEndTime = aDistantFutureTime();
		}

		if (intakeRunning) {
			if (System.currentTimeMillis() >= startReverseTime) {
				if ((currentBreaker1.getCurrent() > 5) || (currentBreaker2.getCurrent() > 5)) {
					reverseIntake();
				}
				if (System.currentTimeMillis() >= (startReverseTime + 200)) {
					intakeCube();
				}
			}
		}

	}

	// CONTROL METHODS ------------------------------------------------

	public static void intakeCube() {
		setArmHorizontalPosition();
		holdingCube = false;
		// closeClaw();
		intakeNormal();	
	}
	
	public static void intakeNormal() {
		leftRollers.set(ControlMode.PercentOutput, -.8);
		rightRollers.set(ControlMode.PercentOutput, -.8);
		resetIntakeStallDetector();
		ejectEndTime = aDistantFutureTime();
		intakeRunning = true;
		startReverseTime = System.currentTimeMillis() + 500;
	}
	
	public static void intakeSlow() { // used for open field pickup
		leftRollers.set(ControlMode.PercentOutput, -.3);
		rightRollers.set(ControlMode.PercentOutput, -.3);
		resetIntakeStallDetector();
		ejectEndTime = aDistantFutureTime();
		intakeRunning = true;
		startReverseTime = aDistantFutureTime(); // don't reverse in this mode
	}
	
	public static void reverseIntake() {
		leftRollers.set(ControlMode.PercentOutput, .5);
	}

	public static boolean isIntakeRunning() {
		return intakeRunning;
	}

	public static void holdCube() {
		closeClaw(); // makes sure the claw is closed (esp in auto)
		stopIntake();
		leftRollers.set(ControlMode.PercentOutput, -.15);
		rightRollers.set(ControlMode.PercentOutput, -.15);
		holdingCube = true;
	}

	public static void dropCube() {
		holdingCube = false;
		openClaw();
		resetIntakeStallDetector();
		ejectCubeSlow();
	}

	public static void ejectCube() {
		holdingCube = false;
		resetIntakeStallDetector();
		CubeClaw.stopIntake();
		leftRollers.set(ControlMode.PercentOutput, .4);
		rightRollers.set(ControlMode.PercentOutput, .4);

		ejectEndTime = System.currentTimeMillis() + 750;
	}

	public static void ejectCubeFast() {
		holdingCube = false;
		resetIntakeStallDetector();
		CubeClaw.stopIntake();
		leftRollers.set(ControlMode.PercentOutput, .8);
		rightRollers.set(ControlMode.PercentOutput, .8);

		ejectEndTime = System.currentTimeMillis() + 750;

	}

	public static void ejectCubeSlow() {
		holdingCube = false;
		resetIntakeStallDetector();
		CubeClaw.stopIntake();
		leftRollers.set(ControlMode.PercentOutput, .25);
		rightRollers.set(ControlMode.PercentOutput, .25);

		ejectEndTime = System.currentTimeMillis() + 750;
	}

	public static void ejectCubeReallySlow() {
		holdingCube = false;
		resetIntakeStallDetector();
		CubeClaw.stopIntake();
		leftRollers.set(ControlMode.PercentOutput, .15);
		rightRollers.set(ControlMode.PercentOutput, .15);
		ejectEndTime = System.currentTimeMillis() + 1000;
	}

	public static void stopIntake() {
		leftRollers.set(ControlMode.PercentOutput, 0);
		rightRollers.set(ControlMode.PercentOutput, 0);
		resetIntakeStallDetector();
		intakeRunning = false;
	}

	// CLAW PNEUMATICS ------------------------------------------------
	private static void turnOffClawSolenoid() {
		clawOpenCloseSolenoid.set(DoubleSolenoid.Value.kOff);
	}

	public static void openClaw() {
		clawOpenCloseSolenoid.set(DoubleSolenoid.Value.kForward);
	}

	public static void closeClaw() {
		clawOpenCloseSolenoid.set(DoubleSolenoid.Value.kReverse);
	}

	// ARM POSITIONING ------------------------------------------------

	public static void setArmHorizontalPosition() {
		arm.set(ControlMode.MotionMagic, 0);
	}

	public static void setArmSwitchPosition() {
		arm.set(ControlMode.MotionMagic, -745);
	}

	public static void setArmScalePosition() {
		arm.set(ControlMode.MotionMagic, -900);
	}

	public static void setArmTravelPosition() {
		arm.set(ControlMode.MotionMagic, -1819);
	}

	public static void setArmOverTheTopPosition() {
		arm.set(ControlMode.MotionMagic, -3300);
	}

	// UTILITY METHODS ---------------------------------------------------------

	public static boolean armHasBeenCalibrated() {
		return armHasBeenCalibrated;
	}

	public static boolean intakeStalled() {
		return (currentBreaker1.tripped() || currentBreaker2.tripped());
	}

	public static void resetIntakeStallDetector() {
		currentBreaker1.reset();
		currentBreaker2.reset();
	}

	public static double getArmAbsolutePosition() {
		return (arm.getSensorCollection().getPulseWidthPosition() & 0xFFF) / 4095d;
	}

	// this tells the encoder that it's at a particular position
	private static void setArmEncPos(int d) {
		arm.getSensorCollection().setQuadraturePosition(d, 500);
	}

	public static void zeroArmEncoder() {
		// USE WITH CAUTION
		// Sets current position to ZERO regardless of 
		// where the arm really is.
		setArmEncPos(0);
	}

	public static void showArmEncoderValue() {
		SmartDashboard.putNumber("Arm Abs Encoder: ", round2(getArmAbsolutePosition()));
		SmartDashboard.putNumber("Arm Position", arm.getSensorCollection().getQuadraturePosition());
		// SmartDashboard.putNumber("Arm Setpoint", arm.getClosedLoopTarget(0));
	}

	/*
	 * Resets the arm encoder value relative to what we've determined to be the
	 * "zero" position. (the calibration values). This is so the rest of the
	 * program can just treat the turn encoder as if zero is the horizontal
	 * position. We don't have to always calculate based off the calibrated zero
	 * position. e.g. if the calibrated zero position is .25 and our current
	 * absolute position is .40 then we reset the encoder value to be .15 *
	 * 4095, so we know were .15 away from the zero position. The 4095 converts
	 * the position back to ticks.
	 * 
	 * Bottom line is that this is what uses the calibrated zero values to set
	 * the encoder relative values
	 */
	public static void resetArmEncoder() {
		if (getInstance() == null)
			return;

		double posDiff = 0;
		double offSet = 0;

		// first find the current absolute position of the arm encoder
		offSet = getArmAbsolutePosition();
		System.out.println("ARM ENCODER POSITION " + offSet);

		// now use the difference between the current position and the
		// calibration zero position
		// to tell the encoder what the current relative position is (relative
		// to the zero pos)
//		posDiff = -calculatePositionFromZero(offSet, Calibration.ARM_ABS_ZERO);
		posDiff = calculatePositionFromZero(offSet, Calibration.ARM_ABS_ZERO);
		System.out.println("ARM ENCODER DIFFERENCE " + posDiff);
		// if (posDiff > .7) {
		// // special case for encoder absolute position being past one
		// rotation.
		// // if calib is .02 and abs pos = .95, it's not possible and we should
		// just take the current position as the
		// // calib position
		// posDiff = 0;
		// }
		// System.out.println("posDiff adjusted " + posDiff);

		int newArmEncoderValue = (int) (posDiff * 4095d);
		setArmEncPos(newArmEncoderValue);
		System.out.println("RESETTING ARM ENCODER TO " + newArmEncoderValue);

		armHasBeenCalibrated = true;

	}


	private static double calculatePositionFromZero(double currentPosition, double calibrationZeroPosition) {
		// this needs to return the difference from the calibration position
		// and handle the wraparound
		// for Xena, the arm moving up means decreasing abs value
		// so if the currentPosition < calibration position, then
		// it should return a negative number

		// =IF(AND($H$23>=1,$H$23-1>G25),$H$22-(1-G25),IF(G25-$H$23>0,-(1-G25),G25)-$H$22)
		// where H22 is calibration value
		// H23 is calibration value + allowable "past" position (have been using
		// .1)
		// G25 is actual current position
		// the "past" position is how far positive, past zero it can go.
		// Normally the
		// arm values start at 0 and go negative, but sometimes it can go a
		// little furhter
		// down, past the calibrated zero point, so this defines what the
		// maximum position
		// would be. The formula handles that value causing an overall > 1 value
		//

		// the most the arm can go positive is .2 past zero
		double maxPositivePosition = calibrationZeroPosition + .2;

		if ((maxPositivePosition >= 1) && (maxPositivePosition - 1 > currentPosition)) {
			return calibrationZeroPosition - (1 - currentPosition);
		} else {
			if (currentPosition - maxPositivePosition > 0) {
				return (-(1 - currentPosition) - calibrationZeroPosition);
			} else
				return (currentPosition - calibrationZeroPosition);
		}

	}

	private static double calculatePositionFromZero_OLD_VERSION(double currentPosition, double calibrationZeroPosition) {
		// this needs to return the difference from the calibration position
		// and handle the wraparound
		// for Xena, the arm moving up means decreasing abs value
		// so if the currentPosition < calibration position, then
		// it should return a negative number
		// NOTE that i don't believe this is working right.
		
		
		// NOT USED NOW
		
		double curPos;
		if (calibrationZeroPosition - currentPosition > .7) {
			return calibrationZeroPosition - (1 + currentPosition);
		} else
			return calibrationZeroPosition - currentPosition;

	}

	private static double aDistantFutureTime() {
		return System.currentTimeMillis() + 900000; // 15 minutes in the future
	}

	/*
	 * TEST METHODS
	 */

	public static void armMove(double speed) {
		System.out.println("calling for arm move " + speed);
		arm.set(ControlMode.PercentOutput, speed);
	}

	public static void moveSetpoint(double direction) {
		int newSetpoint;

		if (direction < 0) {
			newSetpoint = arm.getSelectedSensorPosition(0) + 140;
			if (newSetpoint >= 0) {
				newSetpoint = 0;
			}
		} else {
			newSetpoint = arm.getSelectedSensorPosition(0) - 140;
			if (newSetpoint < -2000) {
				newSetpoint = -2000;
			}
		}

		arm.set(ControlMode.MotionMagic, newSetpoint);
	}

	public static void testIntakeCube(double speed) {
		leftRollers.set(ControlMode.PercentOutput, speed);
		rightRollers.set(ControlMode.PercentOutput, speed);
	}

	public static void testEjectCube(double speed) {
		leftRollers.set(ControlMode.PercentOutput, -speed);
		rightRollers.set(ControlMode.PercentOutput, -speed);
	}

	private static Double round2(Double val) {
		return new BigDecimal(val.toString()).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	private static Double round0(Double val) {
		// added this back in on 1/15/18
		return new BigDecimal(val.toString()).setScale(0, RoundingMode.HALF_UP).doubleValue();
	}
}
