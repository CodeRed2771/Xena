package org.usfirst.frc.team2771.libs;

import edu.wpi.first.wpilibj.DriverStation;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.usfirst.frc.team2771.libs.RobotLEDs.Color;

/**
 *
 * @author michael
 */
public class Logger {

    private static Logger instance;
    private RobotLEDs leds = null;

    public static enum Level {
        INFO, WARNING, ERROR, CRITICAL;
    }

    private Logger() {
        
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void setLEDs(RobotLEDs leds) {
        this.leds = leds;
    }

    public void log(Level level, int code, String description) {
        String s = ("[" + String.format("%1$-" + 9 + "s", levelToString(level))
                + "- " + String.format("%04d", code) + " ] " + description);
        if (level == Level.ERROR && leds != null) {
            leds.setColor(Color.YELLOW);
            leds.blinkSlow();
            leds.lock();
        } else if (level == Level.CRITICAL && leds != null) {
            leds.setColor(Color.YELLOW);
            leds.lock();
        }
        DriverStation.reportError(s + "\n", false); // ?????
        System.err.println(s);
    }
    
    public void log(String message) {
        log(Level.INFO, 000, message);
    }

    private String levelToString(Level level) {
        switch (level.ordinal()) {
            case 0:
                return "INFO";
            case 1:
                return "WARNING";
            case 2:
                return "ERROR";
            case 3:
                return "CRITICAL";
            default:
                return "UNKNOWN";
        }
    }
}
