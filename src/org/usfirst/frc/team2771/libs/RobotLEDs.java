package org.usfirst.frc.team2771.libs;

import edu.wpi.first.wpilibj.DriverStation;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class RobotLEDs implements Runnable {

    private final Relay greenandred;
    private final Relay blue;

    public static enum Color {

        WHITE, RED, GREEN, BLUE, YELLOW, MAGENTA, CYAN, BLACK
    }

    private Color color = Color.GREEN;
    private Color secondary = Color.RED;
    private static final Color AUTONOMOUS_COLOR = Color.WHITE;
    private static final Color TELEOP_COLOR = Color.RED;
    private static final Color TEST_COLOR = Color.YELLOW;
    private static final int SLOW_BLINK_HZ = 1;
    private static final int FAST_BLINK_HZ = 5;

    private double hz = 0; // don't blink
    private int partyTick = 0;
    private boolean strobe2color = false;
    private boolean error = false;
    private boolean locked = false;
    private int tempBlinkCount = 0;
    private long tempBlinkTimeout = 0;
    
    public RobotLEDs(int relay1, int relay2) {
        greenandred = new Relay(relay1);
        blue = new Relay(relay2);
        Thread daemon = new Thread(this);
        daemon.setDaemon(true);
        daemon.setName("LED Thread");
        daemon.setPriority(Thread.MIN_PRIORITY);
        daemon.start();
    }

    public void activateDisabled() {
        if (!locked) {
            color = Color.GREEN;
            update();
        }
    }

    public void activateTeleop() {
        if (!locked) {
            DriverStation ds = DriverStation.getInstance();
            color = DriverStation.Alliance.Blue == ds.getAlliance() ? Color.BLUE
                    : DriverStation.Alliance.Red == ds.getAlliance() ? Color.RED : Color.MAGENTA;
            update();
        }
    }

    public void activateAutonomous() {
        if (!locked) {
            color = AUTONOMOUS_COLOR;
            update();
        }
    }

    public void activateTest() {
        if (!locked) {
            color = TEST_COLOR;
            update();
        }
    }

    public void turnOff() {
        if (!locked) {
            color = Color.BLACK;
            update();
        }
    }

    public void setHz(double hz) {
        if (!locked) {
            this.hz = hz;
        }
    }

    public void blinkSlow() {
        if (!locked) {
            hz = SLOW_BLINK_HZ;
            update();
        }
    }

    public void blinkFast() {
        if (!locked) {
            hz = FAST_BLINK_HZ;
            update();
        }
    }

    public void lock() {
        locked = true;
    }

    public void unlock() {
        locked = false;
    }

//    public void error() {
//        if (!error) {
//            error = true;
//            secondary = color;
//        }
//        color = Color.MAGENTA;
//        hz = 1;
//        strobe2color = true;
//        update();
//    }
//    
//    public void encoderError() {
//        if (!error) {
//            error = true;
//            secondary = color;
//        }
//        color = Color.YELLOW;
//        hz = 1;
//        strobe2color = true;
//        update();
//    }
//    
//    public void disableError() {
//        error = false;
//        color = secondary;
//        strobe2color = false;
//        hz = 0;
//        update();
//    }
    public void party() {
        hz = 15;
        if (partyTick < 10) {
            color = Color.WHITE;
        } else if (partyTick < 20) {
            color = Color.RED;
        } else if (partyTick < 30) {
            color = Color.YELLOW;
        } else if (partyTick < 40) {
            color = Color.GREEN;
        } else if (partyTick < 50) {
            color = Color.CYAN;
        } else if (partyTick < 60) {
            color = Color.BLUE;
        } else if (partyTick < 70) {
            color = Color.MAGENTA;
        }
        if (partyTick == 70) {
            partyTick = 0;
        }
        partyTick++;
    }

    public void setColor(Color color) {
        if (!locked) {
            this.color = color;
            hz = 0;
            update();
        }
    }

    public void setColor(Color color, int hz) {
        if (!locked) {
            this.color = color;
            this.hz = hz;
            update();
        }
    }

    public void blinkThrice(Color color, int hz) {
        if (!locked) {
            tempBlinkCount = 3;
            this.hz = hz;
            this.secondary = this.color;
            this.color = color;
            update();
        }
    }

    public void blinkThrice(Color color) {
        blinkThrice(color, 1);
    }

    private synchronized void update() {
        boolean on = hz == 0 ? true : (((System.currentTimeMillis() / 1000d) * hz) % 1) < 0.3;

        if (strobe2color) {
            color = on ? color : secondary;
            on = true;
        }

        boolean r = (color == Color.WHITE || color == Color.RED || color == Color.MAGENTA
                || color == Color.YELLOW) && on;
        boolean g = (color == Color.WHITE || color == Color.GREEN || color == Color.YELLOW
                || color == Color.CYAN) && on;
        boolean b = (color == Color.WHITE || color == Color.BLUE || color == Color.MAGENTA
                || color == Color.CYAN) && on;
        greenandred.setForward(!g);
        greenandred.setReverse(r);
        blue.setReverse(b);

//        greenandred.setDirection(r && g ? Relay.Direction.kBoth
//                : r ? Relay.Direction.kForward : Relay.Direction.kReverse);
//        greenandred.set(r || g ? Relay.Value.kOn : Relay.Value.kOff);
//        blue.set(b ? Relay.Value.kOn : Relay.Value.kOff);
    }

    @Override
    public void run() {
        while (true) {
            update();

            if (tempBlinkCount != 0) {
                if (tempBlinkTimeout == 0) {
                    tempBlinkTimeout = (long) (System.currentTimeMillis() + (1000 / hz) * tempBlinkCount);
                } else if (System.currentTimeMillis() > tempBlinkTimeout) {
                    this.hz = 0;
                    this.color = this.secondary;
                    tempBlinkCount = 0;
                    tempBlinkTimeout = 0;
                    update();
                }
            }
            
            try {
                Thread.sleep((long) 20);
            } catch (InterruptedException ex) {
                Logger.getLogger(RobotLEDs.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
