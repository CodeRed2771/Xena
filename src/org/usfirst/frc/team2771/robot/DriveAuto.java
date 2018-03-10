package org.usfirst.frc.team2771.robot;

import java.math.BigDecimal;
import java.math.RoundingMode;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveAuto {
	// private PIDControllerAIAO drivePID;
	// private PIDControllerAIAO rotDrivePID;
	// private PIDController drivePID;
	private static DriveAuto instance;
	private static PIDController rotDrivePID;

	private static double minDriveStartPower = .1;

	private static double maxPowerAllowed = 1;
	private static double curPowerSetting = 1;
	private static boolean isDriveInchesRunning = false;
	private static int heading = 0;

    private static double strafeAngle = 0;
    
	public static DriveAuto getInstance() {
		if (instance == null)
			instance = new DriveAuto();
		return instance;
	}

	public DriveAuto() {
		DriveTrain.getInstance();

		// drivePID = new PIDController(Calibration.AUTO_DRIVE_P,
		// Calibration.AUTO_DRIVE_I, Calibration.AUTO_DRIVE_D,
		// pidInputForDrive,
		// speed -> DriveTrain.setDrivePower(speed, speed, speed, speed));
		rotDrivePID = new PIDController(Calibration.AUTO_ROT_P, Calibration.AUTO_ROT_I, Calibration.AUTO_ROT_D,
				RobotGyro.getGyro(), rot -> DriveTrain.autoSetRot(rot));

		// drivePID.setAbsoluteTolerance(Calibration.DRIVE_DISTANCE_TICKS_PER_INCH);
		// // 1" tolerance
		rotDrivePID.setAbsoluteTolerance(1.5); // degrees off

		// rotDrivePID.setToleranceBuffer(3);
		// drivePID.setToleranceBuffer(3);

		// These are applied to the PID in the tick method
		SmartDashboard.putNumber("AUTO DRIVE P", Calibration.AUTO_DRIVE_P);
		SmartDashboard.putNumber("AUTO DRIVE I", Calibration.AUTO_DRIVE_I);
		SmartDashboard.putNumber("AUTO DRIVE D", Calibration.AUTO_DRIVE_D);

		// drivePID.setSetpoint(0);
		// drivePID.reset();
	}

	public static void driveInches(double inches, double angle, double maxPower, double startPowerLevel) {
        strafeAngle = angle;

		maxPowerAllowed = maxPower;
		curPowerSetting = startPowerLevel; // the minimum power required to
											// start moving. (Untested)
		isDriveInchesRunning = true;

		SmartDashboard.putNumber("DRIVE INCHES", inches);

		setPowerOutput(curPowerSetting);
		SmartDashboard.putNumber("Speed Called For", curPowerSetting);

		rotDrivePID.disable();

		DriveTrain.setAllTurnOrientiation(-DriveTrain.angleToLoc(strafeAngle)); // angle at which the wheels turn

		DriveTrain.setAllDrivePosition(DriveTrain.getDriveEnc() + convertToTicks(inches));
		// drivePID.setSetpoint(drivePID.getSetpoint() +
		// convertToTicks(inches));
		// drivePID.enable();
	}

	public static void driveInches(double inches, double angle, double maxPower) {
		driveInches(inches, angle, maxPower, minDriveStartPower);
	}

	public static void reset() {
		DriveTrain.resetDriveEncoders();
		// drivePID.reset();
		// drivePID.setSetpoint(0);
		rotDrivePID.reset();
		rotDrivePID.setSetpoint(0);
		// drivePID.enable();

	}

	public static void stop() {
		// drivePID.setSetpoint(drivePID.get());
		rotDrivePID.setSetpoint(rotDrivePID.get());
		isDriveInchesRunning = false;
	}

	public static void turnDegrees(double degrees, double maxPower) {
		// Turns using the Gyro, relative to the current position
		// Use "turnCompleted" method to determine when the turn is done
		isDriveInchesRunning = false;
		heading += degrees;

		SmartDashboard.putNumber("TURN DEGREES CALL", degrees);

		maxPowerAllowed = maxPower;
		curPowerSetting = .18;
		// drivePID.disable();
		rotDrivePID.setSetpoint(rotDrivePID.getSetpoint() + degrees);
		rotDrivePID.enable();
		setPowerOutput(curPowerSetting);
	}

	public static void continuousTurn(double degrees, double maxPower) {
		// drivePID.disable();
		rotDrivePID.setSetpoint(RobotGyro.getAngle() + degrees);
		rotDrivePID.enable();
		setPowerOutput(maxPower);
	}

	public static void continuousDrive(double inches, double maxPower) {
		setPowerOutput(maxPower);

		DriveTrain.setTurnOrientation(DriveTrain.angleToLoc(0), DriveTrain.angleToLoc(0), DriveTrain.angleToLoc(0),
				DriveTrain.angleToLoc(0));
		rotDrivePID.disable();
		// drivePID.setSetpoint(DriveTrain.getDriveEnc()+
		// convertToTicks(inches));
		// drivePID.enable();
	}

	public static void tick() {
		// this is called roughly 50 times per second

    	if(strafeAngle == 0) { // currently this routine only works when driving straight forward.
        	if (isDriveInchesRunning){
        		if (DriveTrain.getDriveError() > 0)  // directional difference
        			DriveTrain.setTurnOrientation(DriveTrain.angleToLoc((RobotGyro.pidGet()-heading)*.5), DriveTrain.angleToLoc(-(RobotGyro.pidGet()-heading)*.5),
        					DriveTrain.angleToLoc(-(RobotGyro.pidGet()-heading)*.5), DriveTrain.angleToLoc((RobotGyro.pidGet()-heading)*.5));
        		else
        			DriveTrain.setTurnOrientation(strafeAngle + DriveTrain.angleToLoc(-(RobotGyro.pidGet()-heading)*.5), DriveTrain.angleToLoc((RobotGyro.pidGet()-heading)*.5),
        					DriveTrain.angleToLoc((RobotGyro.pidGet()-heading)*.5), DriveTrain.angleToLoc(-(RobotGyro.pidGet()-heading)*.5));
        	}
    	}


		// check for ramping up
		if (curPowerSetting < maxPowerAllowed) { // then increase power a notch
			curPowerSetting += .02; // was .007 evening of 4/5 // to figure out
									// how fast this would be, multiply by 50 to
									// see how much it would increase in 1
									// second.
			if (curPowerSetting > maxPowerAllowed) {
				curPowerSetting = maxPowerAllowed;
			}
		}
		// now check if we're ramping down
		if (curPowerSetting > maxPowerAllowed) {
			curPowerSetting -= .03;
			if (curPowerSetting < 0) {
				curPowerSetting = 0;
			}
		}
		setPowerOutput(curPowerSetting);

		SmartDashboard.putNumber("CurPower", curPowerSetting);

		// Sets the PID values based on input from the SmartDashboard
		// This is only needed during tuning
		rotDrivePID.setPID(SmartDashboard.getNumber("ROT P", Calibration.AUTO_ROT_P),
				SmartDashboard.getNumber("ROT I", Calibration.AUTO_ROT_I),
				SmartDashboard.getNumber("ROT D", Calibration.AUTO_ROT_D));
	}

	private static void setPowerOutput(double powerLevel) {
		// drivePID.setOutputRange(-powerLevel, powerLevel);
		rotDrivePID.setOutputRange(-powerLevel, powerLevel);
	}

	public static void setMaxPowerOutput(double maxPower) {
		maxPowerAllowed = maxPower;
		// "tick" will take care of implementing this power level
	}

	public static double getDistanceTravelled() {
		return Math.abs(convertTicksToInches(DriveTrain.getDriveEnc()));
	}

	public static boolean hasArrived() {
		return false;
		// return drivePID.onTarget() ;//&& rotDrivePID.onTarget();
	}

	public static boolean turnCompleted() {
		return hasArrived();
	}

	public static void setPIDstate(boolean isEnabled) {
		if (isEnabled) {
			// drivePID.enable();
			rotDrivePID.enable();
		} else {
			// drivePID.disable();
			rotDrivePID.disable();
		}
	}

	public static void disable() {
		setPIDstate(false);
	}

	private static int convertToTicks(double inches) {
		return (int) (inches * Calibration.DRIVE_DISTANCE_TICKS_PER_INCH);
	}

	private static double convertTicksToInches(int ticks) {
		return ticks / Calibration.DRIVE_DISTANCE_TICKS_PER_INCH;
	}

	public static void showEncoderValues() {
		// SmartDashboard.putNumber("Drive PID Setpoint: ",
		// drivePID.getSetpoint());
		// SmartDashboard.putNumber("Drive PID Get: ", drivePID.get());
		// SmartDashboard.putNumber("Drive PID Error: ", drivePID.getError());
		// SmartDashboard.putBoolean("Drive On Target", drivePID.onTarget());
		SmartDashboard.putNumber("Drive Encoder", DriveTrain.getDriveEnc());
		
		SmartDashboard.putNumber("Drive PID Error", DriveTrain.getDriveError());

		SmartDashboard.putNumber("Gyro", round2(RobotGyro.getAngle()));
		SmartDashboard.putNumber("Gyro PID Setpoint", rotDrivePID.getSetpoint());
		SmartDashboard.putNumber("Gyro PID error", round2(rotDrivePID.getError()));

		// SmartDashboard.putNumber("Left Drive Encoder Raw: ",
		// -mainDrive.getLeftEncoderObject().get());
		// SmartDashboard.putNumber("Right Drive Encoder Raw: ",
		// -mainDrive.getRightEncoderObject().get());

		// SmartDashboard.putNumber("Right PID error",
		// rightDrivePID.getError());
		// SmartDashboard.putNumber("Left Drive Encoder Get: ",
		// mainDrive.getLeftEncoderObject().get());
		// SmartDashboard.putNumber("Right Drive Encoder Get: ",
		// mainDrive.getRightEncoderObject().get());
		// SmartDashboard.putNumber("Left Drive Distance: ",
		// leftEncoder.getDistance());
		// SmartDashboard.putNumber("Right Drive Distance: ",
		// rightEncoder.getDistance());
		//SmartDashboard.putNumber("Right Drive Encoder Raw: ",
		//DriveTrain.getDriveEnc());
		// SmartDashboard.putNumber("Right Setpoint: ",
		// rightDrivePID.getSetpoint());

	}

	private static Double round2(Double val) {
		// added this back in on 1/15/18
		return new BigDecimal(val.toString()).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
}
