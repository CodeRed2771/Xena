/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.usfirst.frc.team2771.libs;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 *
 * @author austin
 */
public class PIDDerivativeCalculator implements PIDSource{
    PIDSource pIDSource;
    DerivativeCalculator derivativeCalculator;

    public PIDDerivativeCalculator(PIDSource pIDSource, int minSampleTime) {
        if (pIDSource == null) throw new NullPointerException("pIDSource");
        this.pIDSource = pIDSource;
        derivativeCalculator = new DerivativeCalculator(minSampleTime);
    }

    public void sampleTimeSet(int minSampleTime) {
        derivativeCalculator.minTime = minSampleTime;
    }
    
    @Override
    public double pidGet() {
        return derivativeCalculator.calculate(pIDSource.pidGet());
    }
    

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
        // do absolutely nothing.
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kDisplacement;
    }
    
}
