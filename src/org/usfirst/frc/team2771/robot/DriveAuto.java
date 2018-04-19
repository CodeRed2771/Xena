package org.usfirst.frc.team2771.robot;

import java.math.BigDecimal;
import java.math.RoundingMode;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveAuto {
	private static DriveAuto instance;
	private static PIDController rotDrivePID;
	private static boolean isDriveInchesRunning = false;
	private static int heading = 0;
	// private static double motionStartTime = 0;
	private static boolean hasStartedMoving = false;
	private static double strafeAngle = 0;
	private static int zeroVelocityCount = 0;

	public static enum DriveSpeed {
		VERY_LOW_SPEED, LOW_SPEED, MED_SPEED, HIGH_SPEED
	};

	public static DriveAuto getInstance() {
		if (instance == null)
			instance = new DriveAuto();
		return instance;
	}

	public DriveAuto() {
		DriveTrain.getInstance();

		rotDrivePID = new PIDController(Calibration.AUTO_ROT_P, Calibration.AUTO_ROT_I, Calibration.AUTO_ROT_D,
				RobotGyro.getInstance(), rot -> DriveTrain.autoSetRot(rot));

		rotDrivePID.setAbsoluteTolerance(1.5); // degrees off
		// rotDrivePID.setToleranceBuffer(3);

		DriveTrain.setDriveMMAccel(Calibration.DT_MM_ACCEL);
		DriveTrain.setDriveMMVelocity(Calibration.DT_MM_VELOCITY);

		SmartDashboard.putNumber("AUTO DRIVE P", Calibration.AUTO_DRIVE_P);
		SmartDashboard.putNumber("AUTO DRIVE I", Calibration.AUTO_DRIVE_I);
		SmartDashboard.putNumber("AUTO DRIVE D", Calibration.AUTO_DRIVE_D);

		SmartDashboard.putNumber("TURN P", Calibration.TURN_P);
		SmartDashboard.putNumber("TURN I", Calibration.TURN_I);
		SmartDashboard.putNumber("TURN D", Calibration.TURN_D);

		SmartDashboard.putNumber("DRIVE MM VELOCITY", Calibration.DT_MM_VELOCITY);
		SmartDashboard.putNumber("DRIVE MM ACCEL", Calibration.DT_MM_ACCEL);

		SmartDashboard.putNumber("ROT P", Calibration.AUTO_ROT_P);
		SmartDashboard.putNumber("ROT I", Calibration.AUTO_ROT_I);
		SmartDashboard.putNumber("ROT D", Calibration.AUTO_ROT_D);

	}

	public static void driveInches(double inches, double angle, double speedFactor) {

		SmartDashboard.putNumber("DRIVE INCHES", inches);

		strafeAngle = angle;

		isDriveInchesRunning = true;

		DriveTrain.setDriveMMVelocity((int) (Calibration.DT_MM_VELOCITY * speedFactor));

		rotDrivePID.disable();

		// angle at which the wheel modules should be turned
		DriveTrain.setAllTurnOrientiation(-DriveTrain.angleToLoc(strafeAngle));

		// give it just a little time to get the modules turned to position
		// before starting the drive
		// this helps to get accurate encoder readings too since the drive
		// encoder values are affected by turning the modules
		try {
			Thread.sleep(150);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// set the new drive distance setpoint
		DriveTrain.addToAllDrivePositions(convertToTicks(inches));

		// motionStartTime = System.currentTimeMillis();
		hasStartedMoving = false;

	}

	public static void reset() {
		DriveTrain.resetDriveEncoders();
		rotDrivePID.reset();
		rotDrivePID.setSetpoint(0);
		heading = 0;
	}

	public static void stop() {
		rotDrivePID.setSetpoint(rotDrivePID.get());
		isDriveInchesRunning = false;
		DriveTrain.stopDriveAndTurnMotors();
		rotDrivePID.disable();
	}

	public static void turnDegrees(double degrees, double turnSpeedFactor) {
		// Turns using the Gyro, relative to the current position
		// Use "turnCompleted" method to determine when the turn is done
		// The PID controller for this sends a rotational value to the
		// standard swerve drive method to make the bot rotate

		isDriveInchesRunning = false;
		heading += degrees; // this is used later to help us drive straight
							// after rotating

		SmartDashboard.putNumber("TURN DEGREES CALL", degrees);
		SmartDashboard.putNumber("ROT SETPOINT", rotDrivePID.getSetpoint() + degrees);

		rotDrivePID.setSetpoint(rotDrivePID.getSetpoint() + degrees);
		rotDrivePID.enable();
		setRotationalPowerOutput(turnSpeedFactor);

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// motionStartTime = System.currentTimeMillis();
		hasStartedMoving = false;

	}

	// public static void continuousTurn(double degrees, double maxPower) {
	// motionStartTime = System.currentTimeMillis();
	//
	// rotDrivePID.setSetpoint(RobotGyro.getAngle() + degrees);
	// rotDrivePID.enable();
	// setRotationalPowerOutput(maxPower);
	// }
	//
	public static void continuousDrive(double inches, double maxPower) {
		// motionStartTime = System.currentTimeMillis();
		hasStartedMoving = false;

		setRotationalPowerOutput(maxPower);

		DriveTrain.setTurnOrientation(DriveTrain.angleToLoc(0), DriveTrain.angleToLoc(0), DriveTrain.angleToLoc(0),
				DriveTrain.angleToLoc(0));
		rotDrivePID.disable();
	}

	public static void tick() {
		// this is called roughly 50 times per second

		// Use the gyro to try to drive straight. This only works if we're not
		// strafing because it turns the modules a little bit to keep the robot
		// straight.
		// The "heading" reflects any rotation we may have done to the
		// "straight" driving will be at the angle that the robot has been
		// rotated to.

		// if (strafeAngle == 0) { // currently this routine only works when

		if (Math.abs(strafeAngle) < 60) { // not effective for high strafe
											// angles
			if (isDriveInchesRunning) {
				// this gets a -180 to 180 value i believe
				double rawGyroPidGet = RobotGyro.getGyro().pidGet();

				double adjust = (rawGyroPidGet - heading) * .5;

				// THIS IS THE GYRO CORRECTION I WANT TO TRY
				if (DriveTrain.getDriveVelocity() > 0) // driving forward or
														// backward
					DriveTrain.setTurnOrientation(DriveTrain.angleToLoc(-strafeAngle + adjust),
							DriveTrain.angleToLoc(-strafeAngle - adjust), DriveTrain.angleToLoc(-strafeAngle - adjust),
							DriveTrain.angleToLoc(-strafeAngle + adjust));
				else
					DriveTrain.setTurnOrientation(DriveTrain.angleToLoc(-strafeAngle - adjust),
							DriveTrain.angleToLoc((-strafeAngle + adjust)),
							DriveTrain.angleToLoc((-strafeAngle + adjust)),
							DriveTrain.angleToLoc(-strafeAngle - adjust));

				SmartDashboard.putNumber("Angle Adjustment", adjust);
				SmartDashboard.putNumber("Adjusted Angle", strafeAngle - adjust);
				// ORIGINAL
				// Also include the strafeAngle == 0

				// if (DriveTrain.getDriveError() > 0) // directional difference
				// DriveTrain.setTurnOrientation(DriveTrain.angleToLoc(adjust),
				// DriveTrain.angleToLoc(-adjust),
				// DriveTrain.angleToLoc(-adjust),
				// DriveTrain.angleToLoc(adjust));
				// else
				// DriveTrain.setTurnOrientation(DriveTrain.angleToLoc(-adjust),
				// DriveTrain.angleToLoc(adjust),
				// DriveTrain.angleToLoc(adjust),
				// DriveTrain.angleToLoc(-adjust));
			}
		}

		SmartDashboard.putNumber("ROT PID ERROR", rotDrivePID.getError());
		SmartDashboard.putNumber("Drive Train Velocity", DriveTrain.getDriveVelocity());
		SmartDashboard.putBoolean("HasArrived", hasArrived());

		SmartDashboard.putNumber("Drive PID Error", DriveTrain.getDriveError());

		DriveTrain.showDriveEncodersOnDash();
		if (SmartDashboard.getBoolean("Show Turn Encoders", false)) {
			DriveTrain.showTurnEncodersOnDash();
		}

		// Sets the PID values based on input from the SmartDashboard
		// This is only needed during tuning
		// rotDrivePID.setPID(SmartDashboard.getNumber("ROT P",
		// Calibration.AUTO_ROT_P),
		// SmartDashboard.getNumber("ROT I", Calibration.AUTO_ROT_I),
		// SmartDashboard.getNumber("ROT D", Calibration.AUTO_ROT_D));
		//
		// DriveTrain.setDrivePIDValues(SmartDashboard.getNumber("AUTO DRIVE P",
		// Calibration.AUTO_DRIVE_P),
		// SmartDashboard.getNumber("AUTO DRIVE I", Calibration.AUTO_DRIVE_I),
		// SmartDashboard.getNumber("AUTO DRIVE D", Calibration.AUTO_DRIVE_D));
		//
		// DriveTrain.setTurnPIDValues(SmartDashboard.getNumber("TURN P",
		// Calibration.TURN_P),
		// SmartDashboard.getNumber("TURN I", Calibration.TURN_I),
		// SmartDashboard.getNumber("TURN D", Calibration.TURN_D));

		// DriveTrain.setDriveMMAccel((int) SmartDashboard.getNumber("DRIVE MM
		// ACCEL", Calibration.DT_MM_ACCEL));
		// DriveTrain.setDriveMMVelocity((int) SmartDashboard.getNumber("DRIVE
		// MM VELOCITY", Calibration.DT_MM_VELOCITY));

	}

	private static void setRotationalPowerOutput(double powerLevel) {
		rotDrivePID.setOutputRange(-powerLevel, powerLevel);
	}

	public static double getDistanceTravelled() {
		return Math.abs(convertTicksToInches(DriveTrain.getDriveEnc()));
	}

	public static boolean hasArrived() {
		// check to see if we've been moving for a little while (> 600 ms) and
		// if the
		// velocity is nearing zero.....if so, then we have arrived at our
		// endpoint.
		boolean driveTrainStopped = false;

		// boolean startupDelayCompleted = System.currentTimeMillis() >
		// motionStartTime + 600;
		// driveTrainStopped = Math.abs(DriveTrain.getDriveVelocity()) <= 3;

		// new way of determining
		if (hasStartedMoving) {
			if (Math.abs(DriveTrain.getDriveVelocity()) <= 3) 
				zeroVelocityCount++;
			driveTrainStopped = zeroVelocityCount > 5;
		} else { // see if we've started moving now
			if (Math.abs(DriveTrain.getDriveVelocity()) > 20) {
				hasStartedMoving = true;
				zeroVelocityCount = 0;
			}
		}

		// return (startupDelayCompleted && driveTrainStopped);
		return (driveTrainStopped);
		// return (false);
	}

	public static boolean turnCompleted() {
		return hasArrived();
	}

	public static void setPIDstate(boolean isEnabled) {
		if (isEnabled) {
			rotDrivePID.enable();
		} else {
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
		SmartDashboard.putNumber("Drive Encoder", DriveTrain.getDriveEnc());

		SmartDashboard.putNumber("Drive PID Error", DriveTrain.getDriveError());
		SmartDashboard.putNumber("Drive Avg Error", DriveTrain.getAverageDriveError());

		SmartDashboard.putNumber("Gyro", round2(RobotGyro.getAngle()));
		// SmartDashboard.putNumber("Gyro PID Setpoint",
		// rotDrivePID.getSetpoint());
		// SmartDashboard.putNumber("Gyro PID error",
		// round2(rotDrivePID.getError()));

		// SmartDashboard.putBoolean("Has Arrived", hasArrived());

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
		// SmartDashboard.putNumber("Right Drive Encoder Raw: ",
		// DriveTrain.getDriveEnc());
		// SmartDashboard.putNumber("Right Setpoint: ",
		// rightDrivePID.getSetpoint());

	}

	private static Double round2(Double val) {
		// added this back in on 1/15/18
		return new BigDecimal(val.toString()).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
}
