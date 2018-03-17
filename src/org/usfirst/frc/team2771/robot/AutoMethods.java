package org.usfirst.frc.team2771.robot;

/*
 * This program, under the authority of the state,
 * has been officially declared OBSOLETE. It is
 * scheduled for termination in the next 24 hours
 * by the manner of execution in which it chooses.
 */

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
