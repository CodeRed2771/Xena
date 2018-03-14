package org.usfirst.frc.team2771.robot;

public class AutoMethods extends AutoBaseClass{
	public AutoMethods(char mRobotPosition){
	super(mRobotPosition);
	}
	
	public void driveStartToSwitch(){
		driveInches(140,0,.5);
		this.driveCompleted();
	}
	
	
	
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

}
