package org.usfirst.frc.team2771.libs;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author austin
 */
public class TankDrive extends Drive {

    private final SettableController lVictor, rVictor;

    public TankDrive(SettableController lVictor, SettableController rVictor) {
        this.lVictor = lVictor;
        this.rVictor = rVictor;
    }

    @Override
    protected void recalulate(double x, double y, double rot) {
        lVictor.set(y + rot);
        rVictor.set(y - rot);
        
        SmartDashboard.putNumber("TANK DRIVE SET X", x);
        SmartDashboard.putNumber("TANK DRIVE SET Y", y);
        SmartDashboard.putNumber("TANK DRIVE SET VALUE", y + rot);
        
    }
}
