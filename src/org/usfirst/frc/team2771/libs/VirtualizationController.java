package org.usfirst.frc.team2771.libs;

//import com.coderedrobotics.libs.dashboard.communications.listeners.SubsocketListener;
//import com.coderedrobotics.libs.dashboard.api.gui.Bar;
//import com.coderedrobotics.libs.dashboard.api.gui.Indicator;
//import com.coderedrobotics.libs.dashboard.communications.Connection;
//import com.coderedrobotics.libs.dashboard.communications.PrimitiveSerializer;
//import com.coderedrobotics.libs.dashboard.communications.Subsocket;
//import com.coderedrobotics.libs.dashboard.communications.exceptions.InvalidRouteException;
//import com.coderedrobotics.libs.dashboard.communications.exceptions.NotMultiplexedException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class VirtualizationController {

    private static VirtualizationController vc;
    private boolean virtualizationEnabled = false;
    private boolean monitoringEnabled = false;

//    private final Bar[] pwm;
//    private Subsocket dio;
//    private Subsocket analogin;
//    private final Indicator[][] relays;
//    private final Indicator[][] pneumatics;
//    private Indicator compressor;
//    private final VirtualizableDigitalInput[] digitalInputs;
//    private final VirtualizableAnalogInput[] analogInputs;

    @SuppressWarnings("LeakingThisInConstructor")
    private VirtualizationController() {
//        pwm = new Bar[10];
//        relays = new Indicator[4][2];
//        pneumatics = new Indicator[8][2];
//        digitalInputs = new VirtualizableDigitalInput[10];
//        analogInputs = new VirtualizableAnalogInput[4];
//        try {
//            Connection.getInstance().getRootSubsocket().enableMultiplexing().createNewRoute("root.virtualrobot").enableMultiplexing();
//            dio = Connection.getInstance().getRootSubsocket().createNewRoute("root.virtualrobot.dio");
//            analogin = Connection.getInstance().getRootSubsocket().createNewRoute("root.virtualrobot.analogin");
//            dio.addListener(this);
//            analogin.addListener(this);
//        } catch (NotMultiplexedException | InvalidRouteException ex) {
//            Logger.getLogger(VirtualizationController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void addPWMController(PWMController controller) {
//        pwm[controller.getPort()] = new Bar("root.virtualrobot.pwm" + String.valueOf(controller.getPort()));
//        System.out.println("ADDED PWM: " + pwm[controller.getPort()]);
    }
    
    public void addCompressor(Compressor compressor) {
//        this.compressor = new Indicator("root.virtualrobot.compressor");
    }
    
    public void addSolenoid(Solenoid solenoid) {
//        int port = solenoid.getPort();
//        pneumatics[port][0] = new Indicator("root.virtualrobot.pneumatics" + String.valueOf(port) + "b");
//        pneumatics[port][1] = new Indicator("root.virtualrobot.pneumatics" + String.valueOf(port) + "f");
    }
    
    public void addRelay(Relay relay) {
//        int port = relay.getPort();
//        relays[port][0] = new Indicator("root.virtualrobot.r" + String.valueOf(port) + "b");
//        relays[port][1] = new Indicator("root.virtualrobot.r" + String.valueOf(port) + "f");
    }
    
    public void addDigitalInput(VirtualizableDigitalInput vdi) {
//        digitalInputs[vdi.getPort()] = vdi;
    }

    public void addAnalogInput(VirtualizableAnalogInput vai) {
//        analogInputs[vai.getPort() - 1] = vai;
    }
    
    public void setVirtualizationEnabled(boolean state) {
//        virtualizationEnabled = state;
//        if (state) {
//            monitoringEnabled = true;
//        }
    }

    public boolean isVirtualizationEnabled() {
        return virtualizationEnabled;
    }
    
    /**
     * Warning, if Virtualization is enabled you will definitely not want to do this
     * @param state 
     */
    public void setMonitoringEnabled(boolean state) {
//        monitoringEnabled = state;
    }
    
    public boolean isMonitoringEnabled() {
        return monitoringEnabled;
    }

    public void setPWM(PWMController controller, double speed) {
//        pwm[controller.getPort()].setPercentage(speed);
    }
    
    public void setCompressor(boolean compressorRunning) {
//        compressor.setState(compressorRunning);
    }
    
    public void setSolenoid(Solenoid solenoid, boolean state) {
//        int port = solenoid.getPort();
//        System.out.println("SET SOLENOID " + port + " to " + state);
//        pneumatics[port][0].setState(!state);
//        pneumatics[port][1].setState(state);
    }
    
    public void setRelay(Relay relay, boolean forward, boolean backward) {
//        int port = relay.getPort();
//        relays[port][0].setState(backward);
//        relays[port][1].setState(forward);
    }

    public static VirtualizationController getInstance() {
        if (vc == null) {
            vc = new VirtualizationController();
        }
        return vc;
    }

//    @Override
//    public void incomingData(byte[] data, Subsocket subsocket) {
//        if (subsocket == dio) {
//            int port = (int) data[0];
//            boolean value = data[1] == 1;
//            digitalInputs[port].set(value);
//        } else if (subsocket == analogin) {
//            int port = (int) data[0];
//            byte[] valuebytes = new byte[data.length - 1];
//            System.arraycopy(data, 1, valuebytes, 0, valuebytes.length);
//            double value = PrimitiveSerializer.bytesToDouble(valuebytes);
//            analogInputs[port - 1].set(value);
//        }
//    }
}