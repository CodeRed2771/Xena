/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc.team2771.libs;

import edu.wpi.first.wpilibj.PIDSource;

/**
 *
 * @author austin
 */
public class DriveMux {

    private Drive outDrive;
    private boolean isSetToA = true;

    public DriveMux(Drive outDrive) {
        this.outDrive = outDrive;
    }

    public void setA() {
        isSetToA = true;
    }

    public void setB() {
        isSetToA = false;
    }

    private void recalulate(double x, double y, double rot, boolean isA) {
        if (isSetToA == isA) {
            outDrive.recalulate(x, y, rot);
        }
    }

    public Drive getADrive() {
        return new Drive() {

            DriveMux dm;

            @Override
            protected void recalulate(double x, double y, double rot) {
                dm.recalulate(x, y, rot, true);
            }

            public Drive init(DriveMux dm) {
                this.dm = dm;
                return this;
            }
        }.init(this);
    }

    public Drive getBDrive() {
        return new Drive() {

            DriveMux dm;

            @Override
            protected void recalulate(double x, double y, double rot) {
                dm.recalulate(x, y, rot, false);
            }

            public Drive init(DriveMux dm) {
                this.dm = dm;
                return this;
            }
        }.init(this);
    }
}
