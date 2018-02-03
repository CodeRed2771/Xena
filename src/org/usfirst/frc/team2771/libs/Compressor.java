package org.usfirst.frc.team2771.libs;

/**
 *
 * @author Michael
 */
public class Compressor {
    
    private edu.wpi.first.wpilibj.Compressor compressor;
    private final boolean virtualized;
    
    @SuppressWarnings("LeakingThisInConstructor")
    public Compressor() {
        virtualized = VirtualizationController.getInstance().isVirtualizationEnabled();
        if (!virtualized) {
            compressor = new edu.wpi.first.wpilibj.Compressor();
        }
        if (VirtualizationController.getInstance().isMonitoringEnabled()) {
            VirtualizationController.getInstance().addCompressor(this);
        }
    }
    
    public void start() {
        if (!virtualized) {
            compressor.start();
        } 
        if (VirtualizationController.getInstance().isMonitoringEnabled()) {
            VirtualizationController.getInstance().setCompressor(true);
        }
    }
    
    public void stop() {
        if (!virtualized) {
            compressor.stop();
        } 
        if (VirtualizationController.getInstance().isMonitoringEnabled()) {
            VirtualizationController.getInstance().setCompressor(false);
        }
    }
    
    public boolean getClosedLoopControl() {
        if (!virtualized) {
            return compressor.getClosedLoopControl();
        } else {
            return true;
        }
    }
    
    public boolean getPressureSwitchValve() {
        if (!virtualized) {
            return compressor.getPressureSwitchValue();
        } else {
            return false;
        }
    }
    
    public edu.wpi.first.wpilibj.Compressor getWPICompressor() {
        return compressor;
    }
}
