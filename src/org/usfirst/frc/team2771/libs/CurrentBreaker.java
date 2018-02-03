package org.usfirst.frc.team2771.libs;

import org.usfirst.frc.team2771.libs.SettableController;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CurrentBreaker {

	SettableController sc;
	PowerDistributionPanel pdp;
	int portnum;
	double currentThreshold;
	int timeOut;
	boolean motorOff = false;
	boolean tripped = false;
	long motorOffTime = -1;
	long ignoreTime = -1;
	long ignoreDuration;

	public CurrentBreaker(SettableController sc, int portnum, double currentThreshold, int timeOut,
			int ignoreDuration) {
		pdp = new PowerDistributionPanel();
		this.sc = sc == null ? new NullController() : sc;
		this.portnum = portnum;
		this.currentThreshold = currentThreshold;
		this.timeOut = timeOut;
		this.ignoreDuration = ignoreDuration;
	}

	public CurrentBreaker(SettableController sc, int portnum, double currentThreshold, int timeOut) {
		this(sc, portnum, currentThreshold, timeOut, -1);
	}

	public void checkCurrent() {
//		SmartDashboard.putBoolean("Tripped", tripped);
		
		if (System.currentTimeMillis() > ignoreTime) {
			if (!tripped) {
				tripped = (pdp.getCurrent(portnum) > currentThreshold);
				// Logger.getInstance().log(Logger.Level.ERROR, 1,
//				 String.valueOf(pdp.getCurrent(portnum)));

			}
			if (motorOffTime == -1) {
				motorOffTime = System.currentTimeMillis() + timeOut;
			}
			if (motorOff || System.currentTimeMillis() >= motorOffTime) {
				motorOff = true;
				sc.set(0.0);
			}
		}

	}

	public boolean tripped() {
		checkCurrent();

		return tripped;
	}

	public void set(double speed) {
		checkCurrent();
		if (!motorOff) {
			sc.set(speed);
		}
	}

	public void reset() {
		tripped = false;
		motorOff = false;
		motorOffTime = -1;
		if (ignoreDuration != -1) {
			ignoreTime = System.currentTimeMillis() + ignoreDuration;
//			 SmartDashboard.putNumber("ignoreDuration", ignoreDuration);
//			 SmartDashboard.putNumber("IgnoreTime", ignoreTime);
			// Logger.getInstance().log(Logger.Level.INFO,1,"ignoreDuration is
			// "+ignoreDuration);
			// Logger.getInstance().log(Logger.Level.INFO, 1, "Ignore Time Reset
			// to "+ignoreTime);
		}
	}

	public void ignoreFor(long time) {
		ignoreDuration = time;
	}

	public double getCurrent() {
		return pdp.getCurrent(portnum);
	}

	private class NullController implements SettableController {

		@Override
		public void set(double value) {

		}

	}
}
