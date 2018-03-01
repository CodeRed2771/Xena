package org.usfirst.frc.team2771.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.*;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Lift {
	private static Lift instance;
	private static TalonSRX liftMotor;
	private static TalonSRX liftFollower;
	private static DoubleSolenoid shifterSolenoid;
	
	public static Lift getInstance() {
		if (instance == null)
			instance = new Lift();
		return instance;
	}

	public Lift() {
		shifterSolenoid = new DoubleSolenoid(Wiring.LIFT_SHIFTER_PCM_PORTA, Wiring.LIFT_SHIFTER_PCM_PORTB);
		
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
		liftMotor.config_kF(0, 1, 0);
		liftMotor.config_kP(0, 1, 0);
		liftMotor.config_kI(0, 0, 0);
		liftMotor.config_kD(0, 0, 0);
		
		SmartDashboard.putNumber("MM Lift Velocity", 3000);
		SmartDashboard.putNumber("MM Lift Acceleration", 1500);
		SmartDashboard.putNumber("Lift F", 1);
		SmartDashboard.putNumber("Lift P", 1);
		SmartDashboard.putNumber("Lift I", 0);
		SmartDashboard.putNumber("Lif D", 0);
		
		/* set acceleration and vcruise velocity - see documentation*/
		liftMotor.configMotionCruiseVelocity(3000, 0);
		liftMotor.configMotionAcceleration(1500, 0);
		
		/* zero the sensor */
		liftMotor.setSelectedSensorPosition(0, 0, 0);
		
		setLowGear();
	}

	/*
	 * TICK ***********************************
	 */
	public static void tick() {
		
		liftMotor.configMotionCruiseVelocity((int)SmartDashboard.getNumber("MM Lift Velocity", 0), 0);
		liftMotor.configMotionAcceleration((int)SmartDashboard.getNumber("MM Lift Acceleration", 0), 0);
		liftMotor.config_kF(0, SmartDashboard.getNumber("Lift F", 1.0), 0);
		liftMotor.config_kP(0, SmartDashboard.getNumber("Lift P", 1.0), 0);
		liftMotor.config_kI(0, SmartDashboard.getNumber("Lift I", 0), 0);
		liftMotor.config_kD(0, SmartDashboard.getNumber("Lift D", 0), 0);
		SmartDashboard.putNumber("Lift Motor Encoder", liftMotor.getSensorCollection().getQuadraturePosition());
	}
	public static  void move(double speed) {
		liftMotor.set(ControlMode.PercentOutput, speed);	
	}
	public static  void goSwitch() {
		//The switch is the little one.
		liftMotor.set(ControlMode.MotionMagic, -4000); //TODO change the number to the correct height.
		System.out.println("Going to switch");
	}
	public static  void goLowScale() {
		//The scale is the big one.
		//The scale has three different positions, up, down, and level. It could be useful for autonomous.
		liftMotor.set(ControlMode.MotionMagic, -39000); //TODO change the number to the correct height. 
	}
	public static void goHighScale() {
		liftMotor.set(ControlMode.MotionMagic, -39000);
	}
	public static void goStartPosition(){
		liftMotor.set(ControlMode.MotionMagic, 5);
	}
	public static void setLowGear() {
		shifterSolenoid.set(DoubleSolenoid.Value.kForward);
	}
	public static void setHighGear() {
		shifterSolenoid.set(DoubleSolenoid.Value.kReverse);
	}
	public static void stop(){
		liftMotor.set(ControlMode.PercentOutput, 0);	
	}
	
	public static boolean isOverTheTopHeight(){
		return (liftMotor.getSensorCollection().getQuadraturePosition() < -38000);
	}
	
	// returns true if the lift is high enough that we should reduce drving speed
	public static boolean driveCautionNeeded() {
		return Math.abs(liftMotor.getSensorCollection().getPulseWidthPosition()) > 25000; 
	}
}
