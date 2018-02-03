package org.usfirst.frc.team2771.libs;

import java.util.LinkedList;

/**
 *
 * @author michael
 */
public class BatteryMonitoringModule {

    private static final double DEAD_UNDER_LOAD_THRESHOLD = 4.5;
    private static final double INITIALLY_CHARGED_THRESHOLD = 12.8;
    private static final double DEAD_STANDING_THRESHOLD = 12;
    private static final double DEAD_STANDING_MIN_REQUIREMENT = 11;

    private boolean charged = true;
    private boolean replace = false;
    private double estimatedPercentage = 100;
    private double current;
    private double minimum = 50;
    private double avgTotal;
    private double avgAmount;
    private double avg;
    private final LinkedList<Double> history = new LinkedList<>();

    public void set(double voltage) {
        set(voltage, 0, false);
    }

    public void set(double voltage, double load) {
        set(voltage, load, true);
    }

    private void set(double voltage, double load, boolean hasLoad) {
        current = voltage;
        if (voltage < minimum) {
            minimum = voltage;
        }
        history.add(voltage);
        avgTotal += voltage;
        avgAmount++;
        if (history.size() > 600) {
            avgTotal -= history.removeFirst();
            avgTotal--;
        }
        avg = avgTotal / avgAmount;
        recalculate();
    }

    private void recalculate() {
        if (current < INITIALLY_CHARGED_THRESHOLD) {
            charged = false;
        }

        if (current < DEAD_UNDER_LOAD_THRESHOLD) {
            charged = false;
            replace = true;
        }
        
        if (current < DEAD_STANDING_THRESHOLD && minimum <= DEAD_STANDING_MIN_REQUIREMENT) {
            charged = false;
            replace = true;
        }
        
        estimatedPercentage = .4 * ((avg - 4) / 8) + .4 * ((minimum - 4) / 9) + .2 * (current / 13);
        
        if (estimatedPercentage < 90) {
            charged = false;
        }
        
        if (estimatedPercentage < 60) {
            replace = true;
        }
    }

    public boolean isCharged() {
        return charged;
    }

    public boolean shouldReplace() {
        return replace;
    }

    public double getEstimatedPercentage() {
        return estimatedPercentage;
    }

    public double getMinimum() {
        return minimum;
    }
    
    public Object[] getLast600Samples() {
        return history.toArray();
    }
}
