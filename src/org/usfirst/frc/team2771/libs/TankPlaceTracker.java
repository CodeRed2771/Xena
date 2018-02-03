/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team2771.libs;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.AnalogGyro;

/**
 *
 * @author austin
 */
public class TankPlaceTracker extends PlaceTracker {

    private double oldLeft, oldRight, oldRot;
    private final Encoder rightEncoder, leftEncoder;
    private final AnalogGyro gyro;

    public TankPlaceTracker(int leftA, int leftB, int rightA, int rightB) {
        leftEncoder = new Encoder(leftB, leftA);
        rightEncoder = new Encoder(rightA, rightB);
        gyro = new AnalogGyro(0);
    }

    @Override
    protected double[] updatePosition() {
        double leftTotal = leftEncoder.get(), rightTotal = rightEncoder.get();
        double left = leftTotal - oldLeft, right = rightTotal - oldRight;
        oldLeft = leftTotal;
        oldRight = rightTotal;
        double average = (left + right) / 2;

        double rawRot = gyro.getAngle();
        double rot = oldRot - rawRot;
        oldRot = rawRot;

        double[] result = {0, average, rot};
        return result;
    }
}
