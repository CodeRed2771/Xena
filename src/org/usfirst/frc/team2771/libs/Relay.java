package org.usfirst.frc.team2771.libs;

/**
 *
 * @author Austin
 */
public class Relay {

    private edu.wpi.first.wpilibj.Relay relay;
    private boolean forward, reverse;
    private final int port;
    private boolean virtualized = false;

    @SuppressWarnings("LeakingThisInConstructor")
    public Relay(int port) {
        this.port = port;
        virtualized = VirtualizationController.getInstance().isVirtualizationEnabled();
        if (!virtualized) {
            relay = new edu.wpi.first.wpilibj.Relay(port);
            relay.set(edu.wpi.first.wpilibj.Relay.Value.kOff);
        } 
        if (VirtualizationController.getInstance().isMonitoringEnabled()) {
            VirtualizationController.getInstance().addRelay(this);
        }
    }

    public int getPort() {
        return port;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
        refresh();
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
        refresh();
    }

    public boolean getForward() {
        return forward;
    }

    public boolean getReverse() {
        return reverse;
    }

    private void refresh() {
        if (!virtualized) {
            if (forward || reverse) {
                if (forward && reverse) {
                    relay.setDirection(edu.wpi.first.wpilibj.Relay.Direction.kBoth);
                } else if (forward && !reverse) {
                    relay.setDirection(edu.wpi.first.wpilibj.Relay.Direction.kForward);
                } else if (!forward && reverse) {
                    relay.setDirection(edu.wpi.first.wpilibj.Relay.Direction.kReverse);
                }
                relay.set(edu.wpi.first.wpilibj.Relay.Value.kOn);
            } else {
                relay.set(edu.wpi.first.wpilibj.Relay.Value.kOff);
            }
        }
        if (VirtualizationController.getInstance().isMonitoringEnabled()) {
            VirtualizationController.getInstance().setRelay(this, forward, reverse);
        }
    }
    
    public edu.wpi.first.wpilibj.Relay getWPIRelay() {
        return relay;
    }
}
