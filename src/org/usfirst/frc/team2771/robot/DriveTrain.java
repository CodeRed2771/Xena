package org.usfirst.frc.team2771.robot;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain implements PIDOutput {

	private static DriveTrain instance;
	private static Module moduleA, moduleB, moduleC, moduleD;
	private static PIDController pidControllerRot;

	public static DriveTrain getInstance() {
		if (instance == null) {
			try {
				instance = new DriveTrain();
			} catch (Exception ex) {
				System.out.println("Drive train could not be initialized due to the following error: ");
				System.out.println(ex.getMessage());
				System.out.println(ex.getStackTrace());
				return null;
			}
		}
		return instance;
	}

	// define robot dimensions. L=wheel base W=track width
	private static final double l = 22, w = 21, r = Math.sqrt((l * l) + (w * w));

	private DriveTrain() {

		moduleA = new Module(Calibration.DT_A_DRIVE_TALON_ID, Calibration.DT_A_TURN_TALON_ID, Calibration.AUTO_DRIVE_P,
				Calibration.AUTO_DRIVE_I, Calibration.AUTO_DRIVE_D, Calibration.AUTO_DRIVE_IZONE, Calibration.TURN_P,
				Calibration.TURN_I, Calibration.TURN_D, 200, Calibration.GET_DT_A_ABS_ZERO()); // Front right
		moduleB = new Module(Calibration.DT_B_DRIVE_TALON_ID, Calibration.DT_B_TURN_TALON_ID, Calibration.AUTO_DRIVE_P,
				Calibration.AUTO_DRIVE_I, Calibration.AUTO_DRIVE_D, Calibration.AUTO_DRIVE_IZONE, Calibration.TURN_P,
				Calibration.TURN_I, Calibration.TURN_D, 200, Calibration.GET_DT_B_ABS_ZERO()); // Back left
		moduleC = new Module(Calibration.DT_C_DRIVE_TALON_ID, Calibration.DT_C_TURN_TALON_ID, Calibration.AUTO_DRIVE_P,
				Calibration.AUTO_DRIVE_I, Calibration.AUTO_DRIVE_D, Calibration.AUTO_DRIVE_IZONE, Calibration.TURN_P,
				Calibration.TURN_I, Calibration.TURN_D, 200, Calibration.GET_DT_C_ABS_ZERO()); // Back right
		moduleD = new Module(Calibration.DT_D_DRIVE_TALON_ID, Calibration.DT_D_TURN_TALON_ID, Calibration.AUTO_DRIVE_P,
				Calibration.AUTO_DRIVE_I, Calibration.AUTO_DRIVE_D, Calibration.AUTO_DRIVE_IZONE, Calibration.TURN_P,
				Calibration.TURN_I, Calibration.TURN_D, 200, Calibration.GET_DT_D_ABS_ZERO()); // Front left

		// PID is for PID drive not for the modules
		// DVV - I don't believe we're using a mode that uses this
		pidControllerRot = new PIDController(Calibration.DT_ROT_PID_P, Calibration.DT_ROT_PID_I,
				Calibration.DT_ROT_PID_D, RobotGyro.getGyro(), this);
		pidControllerRot.setInputRange(-180.0f, 180.0f);
		pidControllerRot.setOutputRange(-1.0, 1.0);
		pidControllerRot.setContinuous(true);

	}

	public static void setFollowerFix(boolean enable) {
		if (enable)
			moduleB.setFollower(Calibration.DT_D_DRIVE_TALON_ID);
		else
			moduleB.setFollower(0);
	}

	public static void setDrivePower(double modAPower, double modBPower, double modCPower, double modDPower) {
		if (getInstance() == null)
			return;

		moduleA.setDrivePower(modAPower);
		moduleB.setDrivePower(modBPower);
		moduleC.setDrivePower(modCPower);
		moduleD.setDrivePower(modDPower);
	}

	public static void setDriveMMAccel(int accel) {
		moduleA.setDriveMMAccel(accel);
		moduleB.setDriveMMAccel(accel);
		moduleC.setDriveMMAccel(accel);
		moduleD.setDriveMMAccel(accel);
	}

	public static void setDriveMMVelocity(int velocity) {
		moduleA.setDriveMMVelocity(velocity);
		moduleB.setDriveMMVelocity(velocity);
		moduleC.setDriveMMVelocity(velocity);
		moduleD.setDriveMMVelocity(velocity);
	}

	public static int getDriveVelocity() {
		return (moduleA.getDriveVelocity() + moduleB.getDriveVelocity() + moduleC.getDriveVelocity()
				+ moduleD.getDriveVelocity()) / 4;
	}
	
	public static boolean hasDriveCompleted(int allowedError) {
		// just checking two of the modules to see if they are done moving
		return moduleB.hasDriveCompleted(allowedError) && moduleA.hasDriveCompleted(allowedError);
	}

	public static boolean hasDriveCompleted() {
		return hasDriveCompleted(0);
	}

	public static void setTurnPower(double modAPower, double modBPower, double modCPower, double modDPower) {
		if (getInstance() == null)
			return;

		moduleA.setTurnPower(modAPower);
		moduleB.setTurnPower(modBPower);
		moduleC.setTurnPower(modCPower);
		moduleD.setTurnPower(modDPower);
	}

	public static void setTurnOrientation(double modAPosition, double modBPosition, double modCPosition,
			double modDPosition) {
		if (getInstance() == null)
			return;

		// position is a value from 0 to 1 that indicates
		// where in the rotation of the module the wheel should be set.
		// e.g. a value of .5 indicates a half turn from the zero position
		moduleA.setTurnOrientation(modAPosition);
		moduleB.setTurnOrientation(modBPosition);
		moduleC.setTurnOrientation(modCPosition);
		moduleD.setTurnOrientation(modDPosition);

	}

	public static void setAllTurnOrientiation(double loc) {
		if (getInstance() == null)
			return;
		setTurnOrientation(loc, loc, loc, loc);
	}

	public static void setAllDrivePosition(int position) {
		if (getInstance() == null)
			return;
		setDrivePosition(position, position, position, position);
	}

	public static void setDrivePosition(int modAPosition, int modBPosition, int modCPosition, int modDPosition) {
		if (getInstance() == null)
			return;
		SmartDashboard.putNumber("Drive A Setpoint", modAPosition);
		moduleA.setDrivePIDToSetPoint(modAPosition);
		moduleB.setDrivePIDToSetPoint(modBPosition);
		moduleC.setDrivePIDToSetPoint(modCPosition);
		moduleD.setDrivePIDToSetPoint(modDPosition);
	}

	public static void addToAllDrivePositions(int ticks) {
		if (getInstance() == null)
			return;

		setDrivePosition(moduleA.getDriveEnc() + ticks, moduleB.getDriveEnc() + ticks, moduleC.getDriveEnc() + ticks,
				moduleD.getDriveEnc() + ticks);
	}

	public static int getDriveEnc() {
		if (getInstance() == null)
			return 0;
		return (moduleA.getDriveEnc() + moduleB.getDriveEnc() + moduleC.getDriveEnc() + moduleD.getDriveEnc()) / 4;
	}

	public static void autoSetRot(double rot) {
		if (getInstance() == null)
			return;
		swerveDrive(0, 0, rot);
	}

	public static void setAllTurnPower(double power) {
		if (getInstance() == null)
			return;
		setTurnPower(power, power, power, power);
	}

	public static void setAllDrivePower(double power) {
		if (getInstance() == null)
			return;
		setDrivePower(power, power, power, power);
	}

	public static boolean isModuleATurnEncConnected() {
		if (getInstance() == null)
			return false;
		return moduleA.isTurnEncConnected();
	}

	public static boolean isModuleBTurnEncConnected() {
		if (getInstance() == null)
			return false;
		return moduleB.isTurnEncConnected();
	}

	public static boolean isModuleCTurnEncConnected() {
		if (getInstance() == null)
			return false;
		return moduleC.isTurnEncConnected();
	}

	public static boolean isModuleDTurnEncConnected() {
		if (getInstance() == null)
			return false;
		return moduleD.isTurnEncConnected();
	}

	public static void resetDriveEncoders() {
		if (getInstance() == null)
			return;

		moduleA.resetDriveEnc();
		moduleB.resetDriveEnc();
		moduleC.resetDriveEnc();
		moduleD.resetDriveEnc();
	}

	public static void stopDriveAndTurnMotors() {
		if (getInstance() == null)
			return;

		moduleA.stopDriveAndTurnMotors();
		moduleB.stopDriveAndTurnMotors();
		moduleC.stopDriveAndTurnMotors();
		moduleD.stopDriveAndTurnMotors();
	}

	public static void stopDrive() {
		if (getInstance() == null)
			return;

		moduleA.stopDrive();
		moduleB.stopDrive();
		moduleC.stopDrive();
		moduleD.stopDrive();
	}

	public static double angleToLoc(double angle) {
		if (angle < 0) {
			return .5d + ((180d - Math.abs(angle)) / 360d);
		} else {
			return angle / 360d;
		}
	}

	private static boolean allowTurnEncoderReset = false;

	public static void allowTurnEncoderReset() {
		allowTurnEncoderReset = false;
	}

	/*
	 * Resets the turn encoder values relative to what we've determined to be
	 * the "zero" position. (the calibration values). This is so the rest of the
	 * program can just treat the turn encoder as if zero is the straight
	 * position. We don't have to always calculate based off the calibrated zero
	 * position. e.g. if the calibrated zero position is .25 and our current
	 * absolute position is .40 then we reset the encoder value to be .15 *
	 * 4095, so we know were .15 away from the zero position. The 4095 converts
	 * the position back to ticks.
	 * 
	 * Bottom line is that this is what applies the turn calibration values.
	 */
	public static void resetTurnEncoders() {
		if (getInstance() == null)
			return;

		if (!allowTurnEncoderReset) {
			double modAOff = 0, modBOff = 0, modCOff = 0, modDOff = 0;

			moduleA.setTurnPower(0);
			moduleC.setTurnPower(0);
			moduleB.setTurnPower(0);
			moduleD.setTurnPower(0);

			// first find the current absolute position of the turn encoders
			modAOff = DriveTrain.moduleA.getTurnAbsolutePosition();
			modBOff = DriveTrain.moduleB.getTurnAbsolutePosition();
			modCOff = DriveTrain.moduleC.getTurnAbsolutePosition();
			modDOff = DriveTrain.moduleD.getTurnAbsolutePosition();

			// now use the difference between the current position and the
			// calibration zero
			// position
			// to tell the encoder what the current relative position is
			// (relative to the
			// zero pos)
			moduleA.setEncPos((int) (calculatePositionDifference(modAOff, Calibration.GET_DT_A_ABS_ZERO()) * 4095d));
			moduleB.setEncPos((int) (calculatePositionDifference(modBOff, Calibration.GET_DT_B_ABS_ZERO()) * 4095d));
			moduleC.setEncPos((int) (calculatePositionDifference(modCOff, Calibration.GET_DT_C_ABS_ZERO()) * 4095d));
			moduleD.setEncPos((int) (calculatePositionDifference(modDOff, Calibration.GET_DT_D_ABS_ZERO()) * 4095d));

			allowTurnEncoderReset = true;
		}
	}

	private static double calculatePositionDifference(double currentPosition, double calibrationZeroPosition) {
		if (currentPosition - calibrationZeroPosition > 0) {
			return currentPosition - calibrationZeroPosition;
		} else {
			return (1 - calibrationZeroPosition) + currentPosition;
		}
	}

	public static void setDriveBrakeMode(boolean b) {
		if (getInstance() == null)
			return;

		moduleA.setBrakeMode(b);
		moduleB.setBrakeMode(b);
		moduleC.setBrakeMode(b);
		moduleD.setBrakeMode(b);
	}

	public static double getAverageTurnError() {
		if (getInstance() == null)
			return 0.0;

		return (Math.abs(moduleA.getTurnError()) + Math.abs(moduleB.getTurnError()) + Math.abs(moduleC.getTurnError())
				+ Math.abs(moduleD.getTurnError())) / 4d;
	}

	public static double getAverageDriveError() {
		if (getInstance() == null)
			return 0.0;

		return (Math.abs(moduleA.getDriveError()) + Math.abs(moduleB.getDriveError())
				+ Math.abs(moduleC.getDriveError()) + Math.abs(moduleD.getDriveError())) / 4d;
	}

	public static double getDriveError() {
		if (getInstance() == null)
			return 0.0;
		return moduleA.getDriveError();
	}

	/*
	 * 
	 * Drive methods
	 */
	public static void swerveDrive(double fwd, double strafe, double rot) {
		if (getInstance() == null)
			return;

		double a = strafe - (rot * (l / r));
		double b = strafe + (rot * (l / r));
		double c = fwd - (rot * (w / r));
		double d = fwd + (rot * (w / r));

		double ws1 = Math.sqrt((b * b) + (c * c)); // front_right (CHECK THESE
													// AGAINST OUR BOT)
		double ws2 = Math.sqrt((b * b) + (d * d)); // front_left
		double ws3 = Math.sqrt((a * a) + (d * d)); // rear_left
		double ws4 = Math.sqrt((a * a) + (c * c)); // rear_right

		double wa1 = Math.atan2(b, c) * 180 / Math.PI;
		double wa2 = Math.atan2(b, d) * 180 / Math.PI;
		double wa3 = Math.atan2(a, d) * 180 / Math.PI;
		double wa4 = Math.atan2(a, c) * 180 / Math.PI;

		double max = ws1;
		max = Math.max(max, ws2);
		max = Math.max(max, ws3);
		max = Math.max(max, ws4);
		if (max > 1) {
			ws1 /= max;
			ws2 /= max;
			ws3 /= max;
			ws4 /= max;
		}

		SmartDashboard.putNumber("swerve rot", rot);

		// SmartDashboard.putNumber("swerve a", a);
		// SmartDashboard.putNumber("swerve b", b);
		// SmartDashboard.putNumber("swerve c", c);
		// SmartDashboard.putNumber("swerve d", d);
		// SmartDashboard.putNumber("swerve wa1", wa1);
		// SmartDashboard.putNumber("swerve wa2", wa2);
		// SmartDashboard.putNumber("swerve wa3", wa3);
		// SmartDashboard.putNumber("swerve wa4", wa4);

		DriveTrain.setTurnOrientation(angleToLoc(wa4), angleToLoc(wa2), angleToLoc(wa1), angleToLoc(wa3));
		DriveTrain.setDrivePower(ws4, ws2, ws1, ws3);
	}

	public static void showDriveEncodersOnDash() {
		SmartDashboard.putNumber("Mod A Drive Enc", moduleA.getDriveEnc());
		SmartDashboard.putNumber("Mod B Drive Enc", moduleB.getDriveEnc());
		SmartDashboard.putNumber("Mod C Drive Enc", moduleC.getDriveEnc());
		SmartDashboard.putNumber("Mod D Drive Enc", moduleD.getDriveEnc());

	}

	public static void showTurnEncodersOnDash() {
		SmartDashboard.putNumber("TURN A RAW", round(moduleA.getTurnAbsolutePosition(), 3));
		SmartDashboard.putNumber("TURN B RAW", round(moduleB.getTurnAbsolutePosition(), 3));
		SmartDashboard.putNumber("TURN C RAW", round(moduleC.getTurnAbsolutePosition(), 3));
		SmartDashboard.putNumber("TURN D RAW", round(moduleD.getTurnAbsolutePosition(), 3));

		SmartDashboard.putNumber("TURN A POS", round(moduleA.getTurnPosition(), 2));
		SmartDashboard.putNumber("TURN B POS", round(moduleB.getTurnPosition(), 2));
		SmartDashboard.putNumber("TURN C POS", round(moduleC.getTurnPosition(), 2));
		SmartDashboard.putNumber("TURN D POS", round(moduleD.getTurnPosition(), 2));

		SmartDashboard.putNumber("TURN A ANGLE", round(moduleA.getTurnAngle(),0));
		SmartDashboard.putNumber("TURN B ANGLE", round(moduleB.getTurnAngle(),0));
		SmartDashboard.putNumber("TURN C ANGLE", round(moduleC.getTurnAngle(),0));
		SmartDashboard.putNumber("TURN D ANGLE", round(moduleD.getTurnAngle(),0));

		SmartDashboard.putNumber("TURN A ERR", moduleA.getTurnError());
		SmartDashboard.putNumber("TURN B ERR", moduleB.getTurnError());
		SmartDashboard.putNumber("TURN C ERR", moduleC.getTurnError());
		SmartDashboard.putNumber("TURN D ERR", moduleD.getTurnError());
	}

	public static void humanDrive(double fwd, double str, double rot) {
		if (getInstance() == null)
			return;

		if (Math.abs(rot) < 0.01)
			rot = 0;

		if (Math.abs(fwd) < .15 && Math.abs(str) < .15 && Math.abs(rot) < 0.01) {
			// setOffSets();
			setDriveBrakeMode(true);
			stopDrive();
		} else {
			setDriveBrakeMode(false);
			swerveDrive(fwd, str, rot);
			// resetOffSet();
		}
	}

	public static void pidDrive(double fwd, double strafe, double angle) {
		if (getInstance() == null)
			return;

		double temp = (fwd * Math.cos(RobotGyro.getGyroAngleInRad()))
				+ (strafe * Math.sin(RobotGyro.getGyroAngleInRad()));
		strafe = (-fwd * Math.sin(RobotGyro.getGyroAngleInRad())) + (strafe * Math.cos(RobotGyro.getGyroAngleInRad()));
		fwd = temp;
		if (!pidControllerRot.isEnabled())
			pidControllerRot.enable();
		if (Math.abs(fwd) < .15 && Math.abs(strafe) < .15) {
			pidFWD = 0;
			pidSTR = 0;
		} else {
			setDriveBrakeMode(false);
			pidFWD = fwd;
			pidSTR = strafe;
		}
		pidControllerRot.setSetpoint(angle);
	}

	public static void fieldCentricDrive(double fwd, double strafe, double rot) {
		if (getInstance() == null)
			return;

		double temp = (fwd * Math.cos(RobotGyro.getGyroAngleInRad()))
				+ (strafe * Math.sin(RobotGyro.getGyroAngleInRad()));
		strafe = (-fwd * Math.sin(RobotGyro.getGyroAngleInRad())) + (strafe * Math.cos(RobotGyro.getGyroAngleInRad()));
		fwd = temp;
		humanDrive(fwd, strafe, rot);
	}

	public static void tankDrive(double left, double right) {
		if (getInstance() == null)
			return;
		setAllTurnOrientiation(0);
		setDrivePower(right, left, right, left);
	}

	/*
	 * 
	 * PID Stuff
	 */

	@Override
	public void pidWrite(double output) {
		if (getInstance() == null)
			return;

		if (Math.abs(pidControllerRot.getError()) < Calibration.DT_ROT_PID_IZONE) {
			pidControllerRot.setPID(Calibration.DT_ROT_PID_P, Calibration.DT_ROT_PID_I, Calibration.DT_ROT_PID_D);
		} else {
			// I Zone
			pidControllerRot.setPID(Calibration.DT_ROT_PID_P, 0, Calibration.DT_ROT_PID_D);
			pidControllerRot.setContinuous(true);
		}
		swerveDrive(pidFWD, pidSTR, output);
	}

	public static void disablePID() {
		if (getInstance() == null)
			return;
		pidControllerRot.disable();
	}

	public static double[] getAllAbsoluteTurnOrientations() {
		if (getInstance() == null)
			return new double[4];
		return new double[] { moduleA.getTurnAbsolutePosition(), moduleB.getTurnAbsolutePosition(),
				moduleC.getTurnAbsolutePosition(), moduleD.getTurnAbsolutePosition() };
	}

	public static void setDrivePIDValues(double p, double i, double d) {
		if (getInstance() == null)
			return;

		moduleA.setDrivePIDValues(p, i, d);
		moduleB.setDrivePIDValues(p, i, d);
		moduleC.setDrivePIDValues(p, i, d);
		moduleD.setDrivePIDValues(p, i, d);
	}
	
	public static void setTurnPIDValues(double p, double i, double d) {
		if (getInstance() == null)
			return;

		moduleA.setTurnPIDValues(p, i, d);
		moduleB.setTurnPIDValues(p, i, d);
		moduleC.setTurnPIDValues(p, i, d);
		moduleD.setTurnPIDValues(p, i, d);
	}

	private static volatile double pidFWD = 0, pidSTR = 0;
	
	private static Double round(Double val, int scale) {
		return new BigDecimal(val.toString()).setScale(scale, RoundingMode.HALF_UP).doubleValue();
	}

}