package org.usfirst.frc.team2771.robot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Calibration {

	/*
	 * Drive Train
	 */
	
	//Physical Module - A
	public final static int DT_A_DRIVE_TALON_ID = 6;
	public final static int DT_A_TURN_TALON_ID = 5;
	private final static double DT_A_ABS_ZERO_INITIAL = .388766788766;
	private static double DT_A_ABS_ZERO = DT_A_ABS_ZERO_INITIAL;
	public static double GET_DT_A_ABS_ZERO() { return DT_A_ABS_ZERO; }
	
	// Physical Module - B
	public final static int DT_B_DRIVE_TALON_ID = 3;
	public final static int DT_B_TURN_TALON_ID = 4;
	private final static double DT_B_ABS_ZERO_INITIAL = .0264;
	private static double DT_B_ABS_ZERO = DT_B_ABS_ZERO_INITIAL;
	public static double GET_DT_B_ABS_ZERO() { return DT_B_ABS_ZERO; }
	
	// Physical Module - C
	public final static int DT_C_DRIVE_TALON_ID = 7;
	public final static int DT_C_TURN_TALON_ID = 8;
	private final static double DT_C_ABS_ZERO_INITIAL = .455189255189;
	private static double DT_C_ABS_ZERO = DT_C_ABS_ZERO_INITIAL;
	public static double GET_DT_C_ABS_ZERO() { return DT_C_ABS_ZERO; }
	
	// Physical Module - D
	public final static int DT_D_DRIVE_TALON_ID = 2;
	public final static int DT_D_TURN_TALON_ID = 1;
	private final static double DT_D_ABS_ZERO_INITIAL = 0.763369963369;
	private static double DT_D_ABS_ZERO = DT_D_ABS_ZERO_INITIAL;
	public static double GET_DT_D_ABS_ZERO() { return DT_D_ABS_ZERO; }
	
	//Rot PID - this is for turning the robot, not turning a module
	public final static double DT_ROT_PID_P = .007; 
	public final static double DT_ROT_PID_I =.0004;
	public final static double DT_ROT_PID_D= .000;
	public final static double DT_ROT_PID_IZONE = 18;

	public static final double DRIVE_DISTANCE_TICKS_PER_INCH = 32.313;
	
	public static final double AUTO_ROT_P = 0.03; // increased from .022 on 3/20/17 dvv
	public static final double AUTO_ROT_I = 0;
	public static final double AUTO_ROT_D = 0.067;

	public static final double AUTO_DRIVE_P = .5;
	public static final double AUTO_DRIVE_I = 0.0;
	public static final double AUTO_DRIVE_D = 0.0;
	public static final int AUTO_DRIVE_IZONE = 50;
	
	public static final double CLAW_MAX_CURRENT = 2;

	public static void loadSwerveCalibration() {
		File calibrationFile = new File("/home/lvuser/swerve.calibration");
		if (calibrationFile.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(calibrationFile));
				DT_A_ABS_ZERO = Double.parseDouble(reader.readLine());
				DT_B_ABS_ZERO = Double.parseDouble(reader.readLine());
				DT_C_ABS_ZERO = Double.parseDouble(reader.readLine());
				DT_D_ABS_ZERO = Double.parseDouble(reader.readLine());
				reader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void saveSwerveCalibration(double dt_a, double dt_b, double dt_c, double dt_d) {
		File calibrationFile = new File("/home/lvuser/swerve.calibration");
		try {
			if (calibrationFile.exists()) {
				calibrationFile.delete();
			}
			calibrationFile.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(calibrationFile));
			writer.write(String.valueOf(dt_a) + "\n");
			writer.write(String.valueOf(dt_b) + "\n");
			writer.write(String.valueOf(dt_c) + "\n");
			writer.write(String.valueOf(dt_d) + "\n");
			writer.flush();
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		System.out.println(calibrationFile.getAbsolutePath());
		
		DT_A_ABS_ZERO = dt_a;
		DT_B_ABS_ZERO = dt_b;
		DT_C_ABS_ZERO = dt_c;
		DT_D_ABS_ZERO = dt_d;
	}
	
	public static void resetSwerveDriveCalibration() {
		DT_A_ABS_ZERO = DT_A_ABS_ZERO_INITIAL;
		DT_B_ABS_ZERO = DT_B_ABS_ZERO_INITIAL;
		DT_C_ABS_ZERO = DT_C_ABS_ZERO_INITIAL;
		DT_D_ABS_ZERO = DT_D_ABS_ZERO_INITIAL;
		
		File calibrationFile = new File("/home/lvuser/swerve.calibration");
		calibrationFile.delete();
	}
}
