package org.usfirst.frc.team2771.robot;

public class Wiring {
	/*
	 * CAN Addresses
	 * 
	 * 1 thru 8 are the drive train
	 */
	public static final int LIFT_MASTER = 11;
	public static final int LIFT_FOLLLOWER = 12;
	public static final int CUBE_CLAW_LEFT_MOTOR = 14;
	public static final int CUBE_CLAW_RIGHT_MOTOR = 13;
	public static final int ARM_MOTOR = 15; 

	/*
	 * PDP PORTS
	 */
	public static final int CLAW_PDP_PORT1 = 6;
	public static final int CLAW_PDP_PORT2 = 7;
	
	/* 
	 * PCM PORTS
	 */
	public static final int CLAW_PCM_PORTA = 0;
	public static final int CLAW_PCM_PORTB = 1;
	
	public static final int LIFT_SHIFTER_PCM_PORTA = 2;
	public static final int LIFT_SHIFTER_PCM_PORTB = 3;
}
