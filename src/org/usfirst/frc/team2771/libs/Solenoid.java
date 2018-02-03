package org.usfirst.frc.team2771.libs;

/**
 *
 * @author Michael
 */
public class Solenoid {

    private edu.wpi.first.wpilibj.Solenoid solenoid;
    private final int port;
    private final boolean virtualized;
    private boolean state;

    public Solenoid(int port) {
        this.port = port;
        virtualized = VirtualizationController.getInstance().isVirtualizationEnabled();
        if (!virtualized) {
            solenoid = new edu.wpi.first.wpilibj.Solenoid(port);
        } 
        if (VirtualizationController.getInstance().isMonitoringEnabled()) {
            VirtualizationController.getInstance().addSolenoid(this);
        }
    }

//    public Solenoid(int pcm, int port) {
//        this.port = port;
//        virtualized = VirtualizationController.getInstance().isVirtualizationEnabled();
//        if (virtualized) {
//            if (pcm > 0) {
//                return;
//            }
//            VirtualizationController.getInstance().addSolenoid(this);
//        } else {
//            solenoid = new edu.wpi.first.wpilibj.Solenoid(pcm, port);
//        }
//    }

    public int getPort() {
        return port;
    }

    public void set(boolean state) {
        if (virtualized) {
            this.state = state;
        } else {
            solenoid.set(state);
        }
        if (VirtualizationController.getInstance().isMonitoringEnabled()) {            
            VirtualizationController.getInstance().setSolenoid(this, state);
        }
    }

    public boolean get() {
        return virtualized ? state : solenoid.get();
    }

    public boolean isBlackListed() {
        return virtualized ? solenoid.isBlackListed() : false;
    }
    
    public edu.wpi.first.wpilibj.Solenoid getWPISolenoid() {
        return solenoid;
    }
}
