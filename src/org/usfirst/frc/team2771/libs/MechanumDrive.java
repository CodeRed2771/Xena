package org.usfirst.frc.team2771.libs;

/**
 *
 * @author Michael
 */
public class MechanumDrive extends Drive {
    
    private final SettableController frontL, frontR, rearL, rearR;

    public MechanumDrive(SettableController frontL, SettableController frontR, 
            SettableController rearL, SettableController rearR) {
        this.frontL = frontL;
        this.frontR = frontR;
        this.rearL = rearL;
        this.rearR = rearR;
    }    

    @Override
    protected void recalulate(double x, double y, double rot) {
        
//         System.out.println(x);
        
        double leftf, leftb, rightf, rightb;
        double max;

        leftf = y;
        rightf = y;
        leftb = y;
        rightb = y;

        leftf += x;
        rightf += -x;
        leftb += -x;
        rightb += x;

        leftf += rot;
        rightf += -rot;
        leftb += rot;
        rightb += -rot;


        max = Math.max(Math.max(Math.abs(leftf), Math.abs(rightf)), Math.max(Math.abs(leftb), Math.abs(rightb)));

        if (max > 1) {
            leftf = leftf / max;
            rightf = rightf / max;
            leftb = leftb / max;
            rightb = rightb / max;
        }

        frontL.set(leftf);
        frontR.set(rightf);
        rearL.set(leftb);
        rearR.set(rightb);
    }
}
