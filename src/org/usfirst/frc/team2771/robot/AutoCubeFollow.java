package org.usfirst.frc.team2771.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

/*
 * This auto program is a test bed for vision
 * code. The commented out lines will lock on
 * and follow a cube with turning. The uncom-
 * mented lines maintain a certain distance
 * away from the cube.
 */

public class AutoCubeFollow extends AutoBaseClass{
	
	NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
	private final double kP = 1.0;
	
	public AutoCubeFollow(char robotPosition) {
		super(robotPosition);
		table.getEntry("ledMode").forceSetNumber(1); // 0 - on, 1 = off, 2 - blink
		table.getEntry("pipeline").forceSetNumber(1);
	}
	
	public void tick(){
		if(table.getEntry("tv").getNumber(0).intValue() == 1){
		//double targetOffsetAngle_Horizontal = table.getEntry("tx").getDouble(0);
		//continuousTurn(targetOffsetAngle_Horizontal*kP,.4);
		double targetArea = table.getEntry("ta").getDouble(0);
		continuousDrive(-2*(targetArea - 14), .4);
		}
		
	}

}
