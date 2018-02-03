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
public class MechanumPlaceTracker extends PlaceTracker {

    private double oldRightBack, oldRightFront, oldLeftFront, oldLeftBack, oldRot;
    public final Encoder rightBackEncoder, rightFrontEncoder, leftFrontEncoder, leftBackEncoder;
    private final AnalogGyro gyro;
    private final double xScale, yScale, rotScale;

    public MechanumPlaceTracker(
            int rightBackA, int rightBackB, int rightFrontA, int rightFrontB,
            int leftFrontA, int leftFrontB, int leftBackA, int leftBackB, int gyro,
            double xScale, double yScale, double rotScale) {

        rightBackEncoder = new Encoder(rightBackA, rightBackB);
        rightFrontEncoder = new Encoder(rightFrontA, rightFrontB);
        leftFrontEncoder = new Encoder(leftFrontA, leftFrontB);
        leftBackEncoder = new Encoder(leftBackA, leftBackB);

        this.xScale = xScale;
        this.yScale = yScale;
        this.rotScale = rotScale;

        this.gyro = new AnalogGyro(gyro);
    }

    @Override
    protected double[] updatePosition() {

        double rightBackTotal = rightBackEncoder.get(),
                rightFrontTotal = rightFrontEncoder.get(),
                leftFrontTotal = leftFrontEncoder.get(),
                leftBackTotal = leftBackEncoder.get();
        double a = rightBackTotal - oldRightBack,
                b = rightFrontTotal - oldRightFront,
                c = leftFrontTotal - oldLeftFront,
                d = leftBackTotal - oldLeftBack;
        oldRightBack = rightBackTotal;
        oldRightFront = rightFrontTotal;
        oldLeftFront = leftFrontTotal;
        oldLeftBack = leftBackTotal;

        double rawRot = gyro.getAngle();
        double rot = oldRot - rawRot;
        oldRot = rawRot;

        int errorWheel = calculateWheelError(a, b, c, d, rot, 0);
        double[] xyrot = calculateXYRot(a, b, c, d, errorWheel);

        xyrot[2] = rot;
        return scale(xyrot);
    }

    private double[] scale(double[] xyrot) {
        xyrot[0] *= xScale;
        xyrot[1] *= yScale;
        //xyrot[2] *= rotScale;

        return xyrot;
    }

    private static int calculateWheelError(
            double a, double b, double c, double d,
            double rot, double threshold) {

        double[] xyrot = calculateXYRot(a, b, c, d);
        double[] abcd = calculateABCD(xyrot[0], xyrot[1], rot);
        double error
                = Math.abs(a - abcd[0]) + Math.abs(b - abcd[1])
                + Math.abs(c - abcd[2]) + Math.abs(d - abcd[3]);
        if (error > threshold) {
            double max = Math.max(
                    Math.max(Math.abs(a - abcd[0]), Math.abs(b - abcd[1])),
                    Math.max(Math.abs(c - abcd[2]), Math.abs(d - abcd[3])));
            if (Math.abs(a - abcd[0]) == max) {
                return 0;
            }
            if (Math.abs(b - abcd[1]) == max) {
                return 1;
            }
            if (Math.abs(c - abcd[2]) == max) {
                return 2;
            }
            if (Math.abs(d - abcd[3]) == max) {
                return 3;
            }
        }
        return -1;
    }

    private static double[] calculateXYRot(double a, double b, double c, double d) {
        double[] result
                = {(a - b + c - d) / 4, (a + b + c + d) / 4, (-a - b + c + d) / 4};
        return result;
    }

    private static double[] calculateABCD(double x, double y, double rot) {
        double[] result = {
            y + x - rot,
            y - x - rot,
            y + x + rot,
            y - x + rot};
        return result;
    }

    private static double[] calculateXYRot(double a, double b, double c, double d, int errorWheel) {
        if (errorWheel == -1) {
            return calculateXYRot(a, b, c, d);
        }

        double x, y, rot;

        if (errorWheel == 0 || errorWheel == 1) {
            x = (c - d) / 2;
        } else {
            x = (a - b) / 2;
        }

        if (errorWheel == 0 || errorWheel == 3) {
            y = (b + c) / 2;
        } else {
            y = (a + d) / 2;
        }

        if (errorWheel == 0 || errorWheel == 2) {
            rot = (d - b) / 2;
        } else {
            rot = (c - a) / 2;
        }

        double[] result = {x, y, rot};
        return result;
    }
}
