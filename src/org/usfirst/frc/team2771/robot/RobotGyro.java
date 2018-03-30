package org.usfirst.frc.team2771.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SerialPort;

public class RobotGyro implements PIDSource  {
	private static RobotGyro instance;
	private static AHRS mGyro;
    
	public static RobotGyro getInstance() {
		if (instance == null) {
			try {
				instance = new RobotGyro();
			} catch (Exception ex) {
				System.out.println("RobotGyro could not be initialized due to the following error: ");
				System.out.println(ex.getMessage());
				System.out.println(ex.getStackTrace());
				return null;
			}
		}

		return instance;
	}
	
	public RobotGyro() {
		mGyro = new AHRS(SerialPort.Port.kUSB);
		//mGyro = new AHRS(I2C.Port.kMXP);
	
	}
	
	public static AHRS getGyro() {
		if (getInstance() == null) return null;

		return mGyro;
	}
	
	public static double getAngle() {
		if (getInstance() == null) return 0.0;

		return mGyro.getAngle();
	}
	
	public static void reset() {
		if (getInstance() == null) return;

		mGyro.reset();
	}
	
	public static double getGyroAngleInRad() {
		if (getInstance() == null) return 0.0;

		double adjustedAngle = -Math.floorMod((long)mGyro.getAngle(), 360);
		if (adjustedAngle>180) 
			adjustedAngle = -(360-adjustedAngle);
		
		return adjustedAngle * (Math.PI / 180d);
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		mGyro.setPIDSourceType(pidSource);
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return mGyro.getPIDSourceType();
	}

	public double pidGet() {
		return mGyro.getAngle();
	}
	

}
