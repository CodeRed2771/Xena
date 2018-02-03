package org.usfirst.frc.team2771.libs.HID;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author Austin
 */
public class HID {

    Joystick joystick;
    public static Button UNMAPPED = new Button(-1);
    public static Axis UNMAPPED_AXIS = new Axis(-1);
    /**
     *
     * @param port
     */
    public HID(int port) {
        joystick = new Joystick(port);
    }

    public boolean button(Button button) {
        if (button == UNMAPPED) {
            return false;
        }
        if (button instanceof AxisButton) {
            AxisButton axis = (AxisButton) button;
            return axis(axis.axis) * (axis.direction ? 1 : -1) > 0.2;
        }
        return joystick.getRawButton(button.button);
    }

    public double axis(Axis axis) {
        if (axis == UNMAPPED_AXIS) {
            return 0;
        }
        if (axis instanceof POVAxis) {
            POVAxis pov = (POVAxis) axis;
            double povDirection = joystick.getPOV(pov.pov);
            if (povDirection == -1) return 0;
            return pov.isX ? Math.sin(Math.toRadians(povDirection)) : Math.cos(Math.toRadians(povDirection));
        }
        double result = joystick.getRawAxis(axis.axis);
        boolean sign = result > 0d;
        result = Math.abs(result);
        result -= axis.deadZone;
        if (result < 0d) {
            return 0d;
        }
        result = result * (1d / (1d - axis.deadZone));
        result *= sign ? 1d : -1d;
        return result * axis.multiplier;
    }

    public boolean buttonReleased(Button button, ButtonState state) {
        boolean b = button(button);
        boolean result = !b && state.state;
        state.state = b;
        return result;
    }

    public boolean buttonPressed(Button button, ButtonState state) {
        boolean b = button(button);
        boolean result = b && !state.state;
        state.state = b;
        return result;
    }

    public boolean buttonToggled(
            Button button,
            ButtonState pressState,
            ButtonState toggleState) {
        if (buttonPressed(button, pressState)) {
            toggleState.state = !toggleState.state;
        }
        return toggleState.state;
    }

    public static ButtonState newButtonState() {
        return new ButtonState();
    }
    
    public void setRumble(Joystick.RumbleType r, float value) {
        joystick.setRumble(r, value);
    }

    public static class Button {

        private int button;

        Button(int button) {
            this.button = button;
        }
    }

    public static class Axis {

        private int axis;
        private double deadZone = 0;
        private double multiplier = 1;

        Axis(int axis) {
            this.axis = axis;
        }

        Axis(int axis, double deadZone) {
            this.axis = axis;
            this.deadZone = deadZone;
        }
        
        Axis (int axis, double deadZone, double multiplier) {
            this.axis = axis;
            this.deadZone = deadZone;
            this.multiplier = multiplier;
        }
    }

    public static class AxisButton extends Button {

        private Axis axis;
        private boolean direction;

        AxisButton(Axis axis, boolean direction) {
            super(0);
            this.axis = axis;
            this.direction = direction;
        }
    }

    public static class POVAxis extends Axis {
                
        private int pov = 0;
        private boolean isX = false;
        
        POVAxis(int pov, boolean isX) {
            super(-1);
            this.pov = pov;
            this.isX = isX;
        }
    }
    
    public static class ButtonState {

        private boolean state = false;

        private ButtonState() {
        }

    }
}
