package org.usfirst.frc.team2771.libs;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 *
 * @author Michael
 */
public class VirtualizableAnalogInput implements PIDSource {
    
    private AnalogInput analogInput;
    private final boolean virtualized;
    private final int port;
    private double state = 0;

    @SuppressWarnings("LeakingThisInConstructor")
    public VirtualizableAnalogInput(int port) {
        this.port = port;
        virtualized = VirtualizationController.getInstance().isVirtualizationEnabled();
        if (virtualized) {
            VirtualizationController.getInstance().addAnalogInput(this);
        } else {
            analogInput = new AnalogInput(port);
        }
    }

    public int getPort() {
        return port;
    }

    public double get() {
        if (virtualized) {
            return state;
        } else {
            return analogInput.getVoltage();
        }
    }

    void set(double state) {
        this.state = state;
    }

    @Override
    public double pidGet() {
        return get();
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
