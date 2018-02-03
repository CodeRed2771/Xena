package org.usfirst.frc.team2771.libs;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 *
 * @author Michael
 */
public class VirtualizableDigitalInput {

    private DigitalInput digitalInput;
    private final boolean virtualized;
    private final int port;
    private boolean state = false;

    @SuppressWarnings("LeakingThisInConstructor")
    public VirtualizableDigitalInput(int port) {
        this.port = port;
        virtualized = VirtualizationController.getInstance().isVirtualizationEnabled();
        if (virtualized) {
            VirtualizationController.getInstance().addDigitalInput(this);
        } else {
            digitalInput = new DigitalInput(port);
        }
    }

    public int getPort() {
        return port;
    }

    public boolean get() {
        if (virtualized) {
            return state;
        } else {
            return digitalInput.get();
        }
    }

    void set(boolean state) {
        this.state = state;
    }
}
