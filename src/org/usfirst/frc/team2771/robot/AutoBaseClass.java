package org.usfirst.frc.team2771.robot;

import org.usfirst.frc.team2771.libs.Timer;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class AutoBaseClass {
	private Timer mAutoTimer;		// note that the timer is ticked in isRunning() and hasCompleted()
	private int mRobotPosition;
	private boolean mIsRunning = false;
	
	public AutoBaseClass(int robotPosition) {
		mRobotPosition = robotPosition;
		mAutoTimer = new Timer();
	}

	public abstract void tick();

	public String getGameData() {
		return DriverStation.getInstance().getGameSpecificMessage();
	}
	
	public boolean isSwitchLeft() {
		return (getGameData().toCharArray()[0] == 'L');
	}
	public boolean isSwitchRight() {
		return (getGameData().toCharArray()[0] == 'R');
	}
	public boolean isScaleLeft() {
		return (getGameData().toCharArray()[1] == 'L');
	}
	public boolean isScaleRight() {
		return (getGameData().toCharArray()[1] == 'R');
	}
	
	public char getMySwitchSide() {
		return getGameData().charAt(0);
	}
	
	public char getMyScaleSide() {
		return getGameData().charAt(1);
	}
	
	public void start() {
		mAutoTimer.setStage(0);
		mIsRunning = true;
	}

	public void stop() {
		mIsRunning = false;
		DriveAuto.stop();
	}

	public boolean isRunning() {
		mAutoTimer.tick();  // we need to tick the timer and this is a good place to do it.
		return mIsRunning;
	}
	
	public boolean hasCompleted() {
		mAutoTimer.tick();  // we need to tick the timer and this is a good place to do it.
		return !mIsRunning;
	}

	public int getCurrentStep() {
		return mAutoTimer.getStage();
	}

	public void setStep(int step) {
		mAutoTimer.setStage(step);
	}

	public double getStepTimeRemainingInSeconds() {
		return mAutoTimer.getTimeRemainingSeconds();
	}

	public double getStepTimeRemainingInMilliSeconds() {
		return mAutoTimer.getTimeRemainingMilliseconds();
	}

	public void driveInches(double distance, double angle, double maxPower) {
		DriveAuto.driveInches(distance, angle, maxPower);
	}
	
	public boolean driveCompleted() {
		return DriveAuto.hasArrived();
	}

	public void turnDegrees(double degrees, double maxPower) {
		DriveAuto.turnDegrees(degrees, maxPower);
	}

	public void continuousTurn(double degrees, double maxPower) {
		DriveAuto.continuousTurn(degrees, maxPower);
	}
	 
	public void continuousDrive(double inches, double maxPower) {
		DriveAuto.continuousDrive(inches, maxPower);
	}
	
	public int robotPosition() {
		return mRobotPosition;
	}

	public void advanceStep() {
		mAutoTimer.stopTimerAndAdvanceStage();
	}

	// starts a timer for the time indicated and then immediately advances the
	// stage counter
	// this is typically used when starting a driving maneuver because the next
	// stage would
	// be watching to see when the maneuver was completed.
	public void setTimerAndAdvanceStep(long milliseconds) {
		mAutoTimer.setTimerAndAdvanceStage(milliseconds);
	}
	
	public void setTimer(long milliseconds) {
		mAutoTimer.setTimer(milliseconds);
	}
	
	public boolean timeExpired() {
		return mAutoTimer.timeExpired();
	}
	
}
