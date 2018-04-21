package org.usfirst.frc.team2771.robot;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Module {
	private WPI_TalonSRX drive, turn;
	private final double FULL_ROTATION = 4096d, TURN_P, TURN_I, TURN_D, DRIVE_P, DRIVE_I, DRIVE_D;
	private final int TURN_IZONE, DRIVE_IZONE;
	private double turnZeroPos = 0;
	private double currentDriveSetpoint = 0;
	
	/**
	 * Lets make a new module :)
	 * @param driveTalonID First I gotta know what talon we are using for driving
	 * @param turnTalonID Next I gotta know what talon we are using to turn
	 * @param tP I probably need to know the P constant for the turning PID
	 * @param tI I probably need to know the I constant for the turning PID
	 * @param tD I probably need to know the D constant for the turning PID
	 * @param tIZone I might not need to know the I Zone value for the turning PID
	 */
	public Module(int driveTalonID, int turnTalonID, double dP, double dI, double dD, int dIZone, double tP, double tI, double tD, int tIZone, double tZeroPos) {
		drive = new WPI_TalonSRX(driveTalonID);
		drive.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,0); // ?? don't know if zeros are right
		DRIVE_P = dP;
		DRIVE_I = dI;
		DRIVE_D = dD;
		DRIVE_IZONE = dIZone;

		drive.config_kP(0,  DRIVE_P, 0);
		drive.config_kI(0,  DRIVE_I, 0);
		drive.config_kD(0,  DRIVE_D, 0);
		drive.config_IntegralZone(0, DRIVE_IZONE, 0);
		drive.selectProfileSlot(0, 0);
		
		drive.configOpenloopRamp(.1, 0);
		drive.configClosedloopRamp(.05, 0);
		
		drive.configMotionCruiseVelocity(Calibration.DT_MM_VELOCITY, 0);
		drive.configMotionAcceleration(Calibration.DT_MM_ACCEL, 0);
		
		turn = new WPI_TalonSRX(turnTalonID);
		turnZeroPos = tZeroPos;
		
		turn.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,0); // ?? don't know if zeros are right
		TURN_P = tP;
		TURN_I = tI;
		TURN_D = tD;
		TURN_IZONE = tIZone;

		turn.config_kP(0,  TURN_P, 0);
		turn.config_kI(0,  TURN_I, 0);
		turn.config_kD(0,  TURN_D, 0);
		turn.config_IntegralZone(0, TURN_IZONE, 0);
		turn.selectProfileSlot(0, 0);
		
		turn.configClosedloopRamp(.1, 0);
		
	}
	
	public void setFollower(int talonToFollow) {
		if (talonToFollow != 0) {
			drive.set(ControlMode.Follower, talonToFollow);
		} else
			drive.set(ControlMode.Velocity, 0);
	}
	
	public void setDriveMMAccel(int accel) {
		drive.configMotionAcceleration(accel, 0);
	}
	
	public void setDriveMMVelocity(int velocity) {
		drive.configMotionCruiseVelocity(velocity, 0);
	}
	
	public int getDriveVelocity() {
		return drive.getSelectedSensorVelocity(0);
	}
	
	/**
	 * Setting turn motor power
	 * @param p value from -1 to 1
	 */
	public void setTurnPower(double p) {
		this.turn.set(ControlMode.PercentOutput, p); 
	}

	/**
	 * Setting drive motor power
	 * @param p value from -1 to 1
	 */
	public void setDrivePower(double p) {
		this.drive.set(p);
	}

	/**
	 * Getting the turn encoder position (not absolute)
	 * @return turn encoder position
	 */
	public double getTurnRelativePosition() {
		return turn.getSelectedSensorPosition(0);
	}

	/**
	 * Gets the absolute encoder position for the turn encoder
	 * It will be a value between 0 and 1
	 * @return turn encoder absolute position
	 */
	public double getTurnAbsolutePosition() {
		return (turn.getSensorCollection().getPulseWidthPosition() & 0xFFF)/4095d;
	}
	
	public double getTurnPosition() {
		// returns the 0 to 1 value of the turn position
		// uses the calibration value and the actual position 
		// to determine the relative turn position

		double currentPos = getTurnAbsolutePosition();
		if (currentPos - turnZeroPos > 0) {
			return currentPos - turnZeroPos;
		} else {
			return (1 - turnZeroPos) + currentPos;
		}
	}
	
	public double getTurnAngle() {
		// returns the angle in -180 to 180 range
		double turnPos = getTurnPosition();
		if (turnPos > .5) {
			return (360 - (turnPos * 360));
		} else
		return turnPos * 360;
	}
	
	public void resetTurnEnc() {
		this.turn.getSensorCollection().setQuadraturePosition(0,0); 
	}

	public int getDriveEnc() {
		return drive.getSelectedSensorPosition(0);
	}

	public void resetDriveEnc() {
		this.drive.getSensorCollection().setQuadraturePosition(0, 0);
	}
	
	public void setEncPos(int d) {
		turn.getSensorCollection().setQuadraturePosition(d, 0);
	}
	
	/**
	 * Is electrical good? Probably not.... Is the turn encoder connected?
	 * @return true if the encoder is connected
	 */
	public boolean isTurnEncConnected() {
		//return turn.isSensorPresent(FeedbackDevice.CtreMagEncoder_Relative) == FeedbackDeviceStatus.FeedbackStatusPresent;
		return true;  // didn't immediately see a compatible replacement
	}
	
	public int getTurnRotations() {
		return (int) (turn.getSelectedSensorPosition(0) / FULL_ROTATION);
	}
	
	public double getTurnOrientation() {
		return (turn.getSelectedSensorPosition(0) % FULL_ROTATION) / FULL_ROTATION;

//		SmartDashboard.putNumber("module-a-" + this.hashCode(), turn.getSelectedSensorPosition(0));
//		SmartDashboard.putNumber("module-b-" + this.hashCode(), turn.getSelectedSensorPosition(0) % FULL_ROTATION);
//		SmartDashboard.putNumber("module-c-" + this.hashCode(), (turn.getSelectedSensorPosition(0) % FULL_ROTATION) / FULL_ROTATION);
		
	}
	
	public void setDrivePIDToSetPoint(double setpoint) {
		currentDriveSetpoint = setpoint;
		drive.set(ControlMode.MotionMagic, setpoint);
	}
	
	public boolean hasDriveCompleted(int allowedError) {
		SmartDashboard.putNumber("Drive Comp diff", Math.abs(currentDriveSetpoint - getDriveEnc()));
		return Math.abs(currentDriveSetpoint - getDriveEnc()) <= allowedError;
	}
	
	public boolean hasDriveCompleted() {
		return hasDriveCompleted(0);
	}
	
	public void setTurnPIDToSetPoint(double setpoint) {
		turn.set(ControlMode.Position, setpoint);
	}
	
	/**
	 * Set turn to pos from 0 to 1 using PID
	 * @param setLoc orientation to set to
	 */	
	public void setTurnOrientation(double position) {
		double base = getTurnRotations() * FULL_ROTATION;
		if (getTurnRelativePosition() >= 0) {
			if ((base + (position * FULL_ROTATION)) - getTurnRelativePosition() < -FULL_ROTATION/2) {
				base += FULL_ROTATION;
			} else if ((base + (position * FULL_ROTATION)) - getTurnRelativePosition() > FULL_ROTATION/2) {
				base -= FULL_ROTATION;
			}
			turn.set(ControlMode.Position, (((position * FULL_ROTATION) + (base))));
		} else {
			if ((base - ((1-position) * FULL_ROTATION)) - getTurnRelativePosition() < -FULL_ROTATION/2) {
				base += FULL_ROTATION;
			} else if ((base -((1-position) * FULL_ROTATION)) - getTurnRelativePosition() > FULL_ROTATION/2) {
				base -= FULL_ROTATION;
			}
			turn.set(ControlMode.Position, (base- (((1-position) * FULL_ROTATION))));	
		}
	}
	
	
	public double getTurnError() {
		return turn.getClosedLoopError(0);
	}
	
	public double getDriveError(){
		// note that when using Motion Magic, the error is not what you'd expect
		// MM sets intermediate set points, so the error is just the error to 
		// that set point, not to the final setpoint.
		return drive.getClosedLoopError(0);
	}
	
	public void stopDriveAndTurnMotors() {
		setDrivePower(0);
		setTurnPower(0);
	}
	
	public void stopDrive() {
		setDrivePower(0);
	}
	
	public void setBrakeMode(boolean b) {
		drive.setNeutralMode(b ? NeutralMode.Brake : NeutralMode.Coast);
	}
	
	public void setDrivePIDValues(double p, double i, double d){
		drive.config_kP(0, p, 0);
		drive.config_kI(0, i, 0);
		drive.config_kD(0, d, 0);
	}
	
	public void setTurnPIDValues(double p, double i, double d){
		turn.config_kP(0, p, 0);
		turn.config_kI(0, i, 0);
		turn.config_kD(0, d, 0);
	}
	
}