package org.usfirst.frc.team2771.libs;

/**
 *
 * @author Michael
 */
public interface VirtualRobot {

    public void robotInit();

    public void teleopInit();

    public void teleopPeriodic();

    public void autonomousInit();

    public void autonomousPeriodic();

    public void testInit();

    public void testPeriodic();

    public void disabledInit();

    public void disabledPeriodic();
}
