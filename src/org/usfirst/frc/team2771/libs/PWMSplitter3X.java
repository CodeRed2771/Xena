package org.usfirst.frc.team2771.libs;

import edu.wpi.first.wpilibj.Encoder;

/**
 *
 * @author michael
 */
public class PWMSplitter3X implements SettableController {
    
    private PWMController controllerA;
    private PWMController controllerB;
    private PWMController controllerC;
    
    public PWMSplitter3X(int portA, int portB, int portC, boolean backwards) {
        this(portA, portB, portC, backwards, null);
    }
    
    public PWMSplitter3X(int portA, int portB, int portC, boolean backwards, Encoder encoder) {
        controllerA = new PWMController(portA, backwards, encoder);
        controllerB = new PWMController(portA, backwards, encoder);
        controllerC = new PWMController(portA, backwards, encoder);
    }
    
    @Override
    public void set(double speed) {
        controllerA.set(speed);
        controllerB.set(speed);
        controllerC.set(speed);
    }
    
    public PWMController getPWMControllerA() {
        return controllerA;
    }
    
    public PWMController getPWMControllerB() {
        return controllerB;
    }
    
    public PWMController getPWMControllerC() {
        return controllerC;
    }
    
}
