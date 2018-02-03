package org.usfirst.frc.team2771.libs;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Michael
 * 3/9/16 - revised by D. Van Voorst
 * This class can be used to simplify timing autonomous steps
 * Use getStage() to manage your state machine
 * Use setTimerAndAdvanceStage() to start a timer for a particular stage
 * Use advanceWhenReady() to automatically advance to the next auto stage when the timer is reached
 * Use stopTimeAndAdvanceStage() when your drive command has reached its target to prevent the advanceWhenReady from firing also
 * 
 */
public class Timer {

    private int stage;
    private long endTime;
    private boolean timerRunning = false;
    
    public Timer() {

    }

    public void setTimer(long time) {
    	endTime = System.currentTimeMillis() + time;
    	timerRunning = true;
    }
    
    public void setTimerAndAdvanceStage(long time) {
    	setTimer(time);
    	nextStage();
    }
    
    public void stopTimerAndAdvanceStage() {
    	setTimer(1000000); // push the target time way into the future so it never reaches the target time
    	nextStage();
    }
    
    public void advanceWhenTimerExpired() {
        if (timeExpired() && timerRunning) {
            stage++;
            timerRunning = false;
        }
    }
    
    public boolean timeExpired() {
        return endTime < System.currentTimeMillis();
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }
    
    public void nextStage() {
        this.stage++;
    }
    
    public double getTimeRemainingMilliseconds() {
    	return endTime - System.currentTimeMillis();
    }
    
    public double getTimeRemainingSeconds() {
    	return (endTime - System.currentTimeMillis())/1000;
    }
    
    public void tick() {
    	
    	advanceWhenTimerExpired();
    	
    	SmartDashboard.putNumber("Timer Stage", stage);
    	SmartDashboard.putBoolean("Timer Is Running", timerRunning);
    	SmartDashboard.putNumber("Timer Remaining MS",  getTimeRemainingSeconds());
    }
}
