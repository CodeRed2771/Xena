package org.usfirst.frc.team2771.libs;

import edu.wpi.first.wpilibj.PIDOutput;

/**
 *
 * @author Austin
 */
public class PIDIntegralCalculator implements PIDOutput {

    private double x, min, max;
    private PIDOutput pIDOut;

    public PIDIntegralCalculator(PIDOutput output, double min, double max) {
        this.pIDOut = output;
        if (max < min) {
            throw new IllegalArgumentException(
                    "max can not be smaller than min");
        }
        this.min = min;
        this.max = max;
    }

    public void reset() {
        x = 0;
        pidWrite(0);
    }

    public void pidWrite(double output) {
        x += output;
        if (x > max) {
            x = max;
        } else if (x < min) {
            x = min;
        }
        this.pIDOut.pidWrite(x);
    }
}
