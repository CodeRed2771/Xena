package org.usfirst.frc.team2771.robot;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;

public class AutoThreeCube extends AutoBaseClass{
	NetworkTable networkTable = NetworkTableInstance.getDefault().getTable("limelight");
	public AutoThreeCube(int robotPosition){
		super((char) robotPosition);
		networkTable.getEntry("ledMode").forceSetNumber(1);
		networkTable.getEntry("pipeline").forceSetNumber(1);
	}
	
	public void tick(){
		if(isRunning()){
			switch(getCurrentStep()){
			case 0:
				setTimerAndAdvanceStep(2000);
				driveInches(200,0,.5);
				break;
			case 1:
				if(driveCompleted())
					advanceStep();
				break;
			case 2:
				setTimerAndAdvanceStep(2000);
				driveInches(80,0,.5);
				Lift.goHighScale();
				break;
			case 3:
				if(driveCompleted())
					advanceStep();
				break;
			case 4:
				setTimerAndAdvanceStep(2000);
				CubeClaw.ejectCube();
				break;
			case 5:
				if(driveCompleted())
					advanceStep();
				break;
			case 6:
				setTimerAndAdvanceStep(2000);
				driveInches(-80,0,.5);
				break;
			case 7:
				if(driveCompleted())
					advanceStep();
				break;
			case 8:
				setTimerAndAdvanceStep(2000);
				turnDegrees(110, .5);
				break;
			case 9:
				if(driveCompleted())
					advanceStep();
				break;
			case 10:
				if(networkTable.getEntry("tv").getNumber(0).intValue() == 0){
					
				}
			}
		}
	}
}