package org.usfirst.frc.team2771.libs.HID;

/**
 *
 * @author Austin
 */
public class SaitekJoystick extends HID {

    public static final Axis X = new Axis(1, 0.1);
    public static final Axis Y = new Axis(2, 0.1);
    public static final Axis THORTTLE = new Axis(3);
    public static final Axis TWIST = new Axis(4, 0.2);
    public static final Axis TOPHAT_X = new Axis(5);
    public static final Axis TOPHAT_Y = new Axis(6);
    public static final Button LEFT = new AxisButton(X, true);
    public static final Button RIGHT = new AxisButton(X, false);
    public static final Button UP = new AxisButton(Y, true);
    public static final Button DOWN = new AxisButton(Y, false);
    public static final Button TWIST_LEFT = new AxisButton(TWIST, true);
    public static final Button TWIST_RIGHT = new AxisButton(TWIST, false);
    public static final Button TOPHAT_LEFT = new AxisButton(TOPHAT_X, true);
    public static final Button TOPHAT_RIGHT = new AxisButton(TOPHAT_X, false);
    public static final Button TOPHAT_UP = new AxisButton(TOPHAT_Y, true);
    public static final Button TOPHAT_DOWN = new AxisButton(TOPHAT_Y, false);
    public static final Button TRIGGER = new Button(1);
    public static final Button PADDLE_MIDDLE = new Button(2);
    public static final Button PADDLE_LEFT = new Button(3);
    public static final Button PADDLE_RIGHT = new Button(4);
    public static final Button TRIGGER_LEFT = new Button(5);
    public static final Button TRIGGER_RIGHT = new Button(6);
    public static final Button F1 = new Button(7);
    public static final Button F2 = new Button(8);
    public static final Button F3 = new Button(9);
    public static final Button F4 = new Button(10);
    public static final Button BASE_LEFT = new Button(11);
    public static final Button BASE_RIGHT = new Button(12);

    public SaitekJoystick(int port) {
        super(port);
    }
}
