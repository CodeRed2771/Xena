package org.usfirst.frc.team2771.libs.HID;

public class LogitechRumblepad2 extends HID {

    public static final Axis STICK_LEFT_X = new Axis(1, 0.1);
    public static final Axis STICK_LEFT_Y = new Axis(2, 0.1);
    public static final Axis STICK_RIGHT_X = new Axis(3, 0.1);
    public static final Axis STICK_RIGHT_Y = new Axis(4, 0.1);
    public static final Axis DPAD_X = new Axis(5, 0.1);
    public static final Axis DPAD_Y = new Axis(6, 0.1);
    public static final Button STICK_LEFT_LEFT = new AxisButton(STICK_LEFT_X, true);
    public static final Button STICK_LEFT_RIGHT = new AxisButton(STICK_LEFT_X, false);
    public static final Button STICK_LEFT_UP = new AxisButton(STICK_LEFT_Y, true);
    public static final Button STICK_LEFT_DOWN = new AxisButton(STICK_LEFT_Y, false);
    public static final Button STICK_RIGHT_LEFT = new AxisButton(STICK_RIGHT_X, true);
    public static final Button STICK_RIGHT_RIGHT = new AxisButton(STICK_RIGHT_X, false);
    public static final Button STICK_RIGHT_UP = new AxisButton(STICK_RIGHT_Y, true);
    public static final Button STICK_RIGHT_DOWN = new AxisButton(STICK_RIGHT_Y, false);
    public static final Button DPAD_LEFT = new AxisButton(DPAD_X, true);
    public static final Button DPAD_RIGHT = new AxisButton(DPAD_X, false);
    public static final Button DPAD_UP = new AxisButton(DPAD_Y, true);
    public static final Button DPAD_DOWN = new AxisButton(DPAD_Y, false);
    public static final Button B1 = new Button(1);
    public static final Button B2 = new Button(2);
    public static final Button B3 = new Button(3);
    public static final Button B4 = new Button(4);
    public static final Button BUMPER_LEFT = new Button(5);
    public static final Button BUMPER_RIGHT = new Button(6);
    public static final Button TRIGGER_LEFT = new Button(7);
    public static final Button TRIGGER_RIGHT = new Button(8);
    public static final Button B9 = new Button(9);
    public static final Button B10 = new Button(10);
    public static final Button STICK_LEFT = new Button(11);
    public static final Button STICK_RIGHT = new Button(12);

    public LogitechRumblepad2(int port) {
        super(port);
    }
}
