/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team2771.libs;

/**
 *
 * @author austin
 */
public class DerivativeCalculator {

    long time = 0;
    long minTime = 1;
    double old = 0;
    double oldValue = 0;

    public double calculate(double value) {
        long newTime = System.currentTimeMillis();
        long delta = newTime - time;
        if (delta >= minTime) {
            time = newTime;
            oldValue = (old - (old = value))
                    / delta;
        }
        return oldValue;
    }

    public DerivativeCalculator(long minTime) {
        this.minTime = minTime;
    }

    public DerivativeCalculator() {
    }
}
