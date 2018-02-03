/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team2771.libs;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 *
 * @author laptop
 */
public abstract class PlaceTracker {

    private double x, y, rot;
    private double totalLateral, totalLinear;
    private double destinationX, destinationY, destinationRot;

    public PlaceTracker() {
    }

    protected abstract double[] updatePosition();

    public void step() {
        double[] update = updatePosition();

        totalLateral += update[0];
        totalLinear += update[1];

        rot += update[2] / 2;

        x += Math.cos(Math.toRadians(rot)) * update[1];
        y += Math.sin(Math.toRadians(rot)) * update[1];

        x += Math.sin(Math.toRadians(rot)) * update[0];
        y += Math.cos(Math.toRadians(rot)) * update[0];

        rot += update[2] / 2;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRot() {
        return rot;
    }

    public void goTo(double x, double y, double rot) {
        destinationX = x;
        destinationY = y;
        destinationRot = rot % 360;
    }

    public void goTo(double x, double y) {
        destinationX = x;
        destinationY = y;
        destinationRot = -1;
    }

    public PIDSource getLateralPIDSource() {
        return new PIDSource() {

            PlaceTracker pt;

            @Override
            public double pidGet() {
                return pt.totalLateral;
            }

            public PIDSource init(PlaceTracker pt) {
                this.pt = pt;
                return this;
            }

            @Override
            public void setPIDSourceType(PIDSourceType pidSource) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public PIDSourceType getPIDSourceType() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }.init(this);
    }

    public PIDSource getLinearPIDSource() {
        return new PIDSource() {

            PlaceTracker pt;

            @Override
            public double pidGet() {
                return pt.totalLinear;
            }

            public PIDSource init(PlaceTracker pt) {
                this.pt = pt;
                return this;
            }

            @Override
            public void setPIDSourceType(PIDSourceType pidSource) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public PIDSourceType getPIDSourceType() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }.init(this);
    }

    public PIDSource getRotPIDSource() {
        return new PIDSource() {

            PlaceTracker pt;

            @Override
            public double pidGet() {
                return pt.getRot();
            }

            public PIDSource init(PlaceTracker pt) {
                this.pt = pt;
                return this;
            }

            @Override
            public void setPIDSourceType(PIDSourceType pidSource) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public PIDSourceType getPIDSourceType() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }.init(this);
    }

    public double getRotToDestination() {
        double result;
        if (destinationRot == -1) {
            double xError = destinationX - x, yError = destinationY - y;

            result = (rot - Math.toDegrees(Math.atan2(yError, xError)));
        } else {
            result = (rot - destinationRot);
        }
        result = result % 360;
        if (result < 180) {
            return result;
        } else {
            return result - 180;
        }
    }

    public double getDistanceToDestination() {
        double xError = destinationX - x, yError = destinationY - y;

        double speed;

        if (destinationRot == -1) {
            double rotError = (rot - Math.toDegrees(Math.atan2(yError, xError))) % 360;

            if (rotError > 180) {
                rotError -= 360;
            }

            speed = (90 - Math.abs(rotError)) / 90;
        } else {
            speed = 1;
        }

        double displacement = Math.sqrt(Math.pow(xError, 2) + Math.pow(yError, 2));

        return speed * displacement;
    }
}
