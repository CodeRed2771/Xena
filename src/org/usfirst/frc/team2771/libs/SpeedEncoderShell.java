package org.usfirst.frc.team2771.libs;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 *
 * @author Austin
 * @deprecated replaced with PIDDerivativeCalculator
 */
public class SpeedEncoderShell implements PIDSource {

    Encoder encoder = null;
    private long lastTime;
    private double lastPos = 0;
    private double currentSpeed = 0;

    public SpeedEncoderShell(Encoder encoder) {
        if (encoder == null) {
            throw new NullPointerException();
        }
        this.encoder = encoder;
        encoder.reset();
        //encoder.start();
        lastTime = System.currentTimeMillis();
    }

    public double pidGet() {
        currentSpeed = (lastPos - encoder.getRaw()) / ((int) (lastTime - System.currentTimeMillis()));
        lastPos = encoder.getRaw();
        lastTime = System.currentTimeMillis();
        return currentSpeed;
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
