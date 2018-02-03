package org.usfirst.frc.team2771.libs;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author Michael
 */
public class PWMController implements PIDOutput, SettableController {

    private Victor controller;
    private final int port;
    private final boolean virtualized;
    private final boolean backwards;
    private Encoder encoder;
    private long time = -1;
    private int lastEncoderReading = 0;
    private boolean encoderError = false;

    public PWMController(int port, boolean backwards) {
        this(port, backwards, null);
    }

    public PWMController(int port, boolean backwards, Encoder encoder) {
        this.encoder = encoder;
        this.port = port;
        this.backwards = backwards;
        virtualized = VirtualizationController.getInstance().isVirtualizationEnabled();
        if (!virtualized) {
            controller = new Victor(port);
        }
        if (VirtualizationController.getInstance().isMonitoringEnabled()) {
            VirtualizationController.getInstance().addPWMController(this);
        }
    }

    public int getPort() {
        return port;
    }

    @Override
    public void set(double speed) {
        if (!virtualized) {
            controller.set(speed * (backwards ? -1 : 1));
        }
        if (VirtualizationController.getInstance().isMonitoringEnabled()) {
            VirtualizationController.getInstance().setPWM(this, speed * (backwards ? -1 : 1));
        }

        if (encoder != null) {
            int encoderReading = encoder.getRaw();
            
            if (Math.abs(encoderReading - lastEncoderReading) < 4) {
                if (Math.abs(speed) > 0.3) {
                    if (time == -1) {
                        time = System.currentTimeMillis() + 250;
                    } else if (System.currentTimeMillis() > time) {
                        encoderError = true;
                    }
                } else {
                	time = -1;
                }
            } else {
                lastEncoderReading = encoderReading;
                encoderError = false;
                time = -1;
            }
        }
    }
    
    public boolean encoderHasError() {
    	return encoderError;
    }

    public Victor getWPIController() {
        return controller;
    }

    @Override
    public void pidWrite(double output) {
        set(output);
    }
}
