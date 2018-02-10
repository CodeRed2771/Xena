package org.usfirst.frc.team2771.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Lift {
	private static Lift instance;
	private static TalonSRX liftMotor;
	private static TalonSRX liftFollower;

	public static Lift getInstance() {
		if (instance == null)
			instance = new Lift();
		return instance;
	}

	public Lift() {
		liftMotor = new TalonSRX(Wiring.LIFT_MASTER);
		liftFollower = new TalonSRX(Wiring.LIFT_FOLLLOWER);
		liftFollower.follow(liftMotor);
		liftFollower.setInverted(false);
		
		/* first choose the sensor */
		liftMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0, 0);
		
		/* set the relevant frame periods to be at least as fast as periodic rate */
		liftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10,0);
		liftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10,0);

		/* set the peak and nominal outputs*/
		liftMotor.configNominalOutputForward(0, 0);
		liftMotor.configNominalOutputReverse(0, 0);
		liftMotor.configPeakOutputForward(1, 0);
		liftMotor.configPeakOutputReverse(-1, 0);
		
		/*set closed loop gains in slot0 - see documentation*/
		liftMotor.selectProfileSlot(0, 0);
		liftMotor.config_kF(0, 0.2, 0);
		liftMotor.config_kP(0, 0.2, 0);
		liftMotor.config_kI(0, 0, 0);
		liftMotor.config_kD(0, 0, 0);
		
		SmartDashboard.putNumber("MM Velocity", 15000);
		SmartDashboard.putNumber("MM Acceleration", 6000);
		
		/* set acceleration and vcruise velocity - see documentation*/
		liftMotor.configMotionCruiseVelocity(15000, 0);
		liftMotor.configMotionAcceleration(6000, 0);
		
		/* zero the sensor */
		liftMotor.setSelectedSensorPosition(0, 0, 0);
	}

	
	public static  void move(double speed) {
		liftMotor.set(ControlMode.PercentOutput, speed);
		
	}
	public static  void goSwitch() {
		//The switch is the little one.
		liftMotor.set(ControlMode.PercentOutput, .7, 12); //TODO change the number to the correct height.
	}
	public static  void goLowScale() {
		//The scale is the big one.
		//The scale has three different positions, up, down, and level. It could be useful for autonomous.
		liftMotor.set(ControlMode.MotionMagic, .7, 64); //TODO change the number to the correct height. 
	}
	public static void goHighScale() {
		liftMotor.set(ControlMode.MotionMagic, .7, 90);
	}
}
