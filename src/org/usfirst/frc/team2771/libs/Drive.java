package org.usfirst.frc.team2771.libs;

import edu.wpi.first.wpilibj.PIDOutput;

public abstract class Drive {

    private double x, y, rot;

    public Drive() {
    }

    public void setX(double x) {
        this.x = x;
        recalulate();
    }

    public void setY(double y) {
        this.y = y;
        recalulate();
    }

    public void setRot(double rot) {
        this.rot = rot;
        recalulate();
    }

    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
        recalulate();
    }

    public void setXRot(double x, double rot) {
        this.x = x;
        this.rot = rot;
        recalulate();
    }

    public void setYRot(double y, double rot) {
        this.y = y;
        this.rot = rot;
        recalulate();
    }

    public void setXYRot(double x, double y, double rot) {
        this.x = x;
        this.y = y;
        this.rot = rot;
        recalulate();
    }

    private void recalulate() {
        recalulate(x, y, rot);
    }

    protected abstract void recalulate(double x, double y, double rot);

    public PIDOutput getRotPIDOutput() {
        return new PIDOutput() {

            Drive drive;

            @Override
            public void pidWrite(double rot) {
                drive.setRot(rot);
            }

            public PIDOutput init(Drive drive) {
                this.drive = drive;
                return this;
            }
        }.init(this);
    }

    public PIDOutput getXPIDOutput() {
        return new PIDOutput() {

            Drive drive;

            @Override
            public void pidWrite(double x) {
                drive.setX(x);
            }

            public PIDOutput init(Drive drive) {
                this.drive = drive;
                return this;
            }
        }.init(this);
    }

    public PIDOutput getYPIDOutput() {
        return new PIDOutput() {

            Drive drive;

            @Override
            public void pidWrite(double y) {
                drive.setY(y);
            }

            public PIDOutput init(Drive drive) {
                this.drive = drive;
                return this;
            }
        }.init(this);
    }
}
