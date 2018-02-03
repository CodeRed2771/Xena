package org.usfirst.frc.team2771.libs;

/**
 *
 * @author Austin
 */
public class Filter {

    private double strength = 0d;
    private double data = 0d;
    private long experationTimer = 0l;

    public Filter(double strength) {
        this.strength = strength;
    }

    public double filter(double d) {
        if (experationTimer < System.currentTimeMillis()) {
            data = 0.6;
        }
        data = (data * (strength)) + (d * (1d - strength));
        experationTimer = System.currentTimeMillis() + 150;
        return data;
    }

    public double get() {
        return data;
    }
}
