package org.usfirst.frc.team2771.robot;

import org.usfirst.frc.team2771.libs.HID.*;

public class KeyMap {

    // GAMEPADS
    private final HID gp1 = new HID(0);
    private final HID gp2 = new HID(1);
    private final int gamepad1 = 0;
    private final int gamepad2 = 1;

    // MANAGEMENT BOOLEANS
    private boolean singleControllerMode = false;

    // CONTROLLER 0
    private final HID.Axis swerveXAxis = LogitechF310.STICK_LEFT_X;
    private final HID.Axis swerveYAxis = LogitechF310.STICK_LEFT_Y;
    private final HID.Axis swerveRotAxis = LogitechF310.STICK_RIGHT_X;
    private final HID.Axis liftAxis = LogitechF310.STICK_LEFT_Y;
    private final HID.Axis armAxis = LogitechF310.STICK_RIGHT_Y;
    private final HID.Axis clawIntakeAxis = LogitechF310.TRIGGER_LEFT_AXIS;
    private final HID.Axis clawEjectAxis = LogitechF310.TRIGGER_RIGHT_AXIS;
    private final HID.Button clawOpenButton = LogitechF310.Y;
    private final HID.Button clawCloseButton = LogitechF310.X;
    
    
    private final HID.Button shooterIntakeButton = LogitechF310.BUMPER_RIGHT;
    private final HID.Button flipDriveButton = LogitechF310.B;
    private final HID.Axis climberAxis = LogitechF310.TRIGGER_LEFT_AXIS;
    private final HID.Axis gearPositionAxis = LogitechF310.TRIGGER_RIGHT_AXIS;
    private final HID.Button driveTrainOffButton = LogitechF310.B;

    
    // CONTROLLER 1
    
    //shooting
    private final HID.Button shooterButton = LogitechF310.BUMPER_RIGHT;
    
    //sweeping
    private final HID.Button pickupButton = LogitechF310.A;
    
    //gear pickup
    private final HID.Button gearArmPickupButton = LogitechF310.BUMPER_LEFT;
    private final HID.Button gearFingersPickupOpenButton = LogitechF310.DPAD_LEFT;
    private final HID.Button gearFingersPickupCloseButton = LogitechF310.DPAD_RIGHT;
    private final HID.Button gearGateButton = LogitechF310.Y;
    private final HID.Button cameraGearPickupButton = LogitechF310.DPAD_DOWN;
    private final HID.Button cameraRegularButton = LogitechF310.DPAD_UP;
    private final HID.Button gearArmRetractButton = LogitechF310.BACK;
    private final HID.Button gearRelease = LogitechF310.START;
    
    //gear receiver
    private final HID.Button gearReceiverOpen = LogitechF310.A;
    

    // BUTTON STATES
//    private final HID.ButtonState shooterButtonState = HID.newButtonState();
//    private final HID.ButtonState flipDriveButtonStateA = HID.newButtonState();
//    private final HID.ButtonState flipDriveButtonStateB = HID.newButtonState();
//    private final HID.ButtonState agitatorButtonState = HID.newButtonState();
//    private final HID.ButtonState pickupButtonState = HID.newButtonState();
//    private final HID.ButtonState gearArmPickupButtonState = HID.newButtonState();
//    //private final HID.ButtonState gearFingersPickupButtonState = HID.newButtonState();
//    private final HID.ButtonState gearGateButtonState = HID.newButtonState();
    
    
    public KeyMap() {

    }

    private HID getHID(int gamepad) {
        if (!singleControllerMode) {
            switch (gamepad) {
                case gamepad1:
                    return gp1;
                case gamepad2:
                    return gp2;
                default:
                    return null;
            }
        } else {
            return gp1;
        }
    }

    public double getSwerveXAxis() {
        return getHID(gamepad1).axis(swerveXAxis);
    }

    public double getSwerveYAxis() {
        return getHID(gamepad1).axis(swerveYAxis);
    }

    public double getSwerveRotAxis() {
        return getHID(gamepad1).axis(swerveRotAxis);
    }

    public double getLiftAxis() {
    	return getHID(gamepad1).axis(liftAxis);
    }
    public double getArmAxis() {
    	return getHID(gamepad1).axis(armAxis);
    }
    public double getClawIntakeAxis() {
    	return getHID(gamepad1).axis(this.clawIntakeAxis);
    }
    
    public double getClawEjectAxis() {
    	return getHID(gamepad1).axis(this.clawEjectAxis);
    }
    
    public boolean openClaw() {
    	return getHID(gamepad1).button(this.clawOpenButton);
    }
    
    public boolean closeClaw() {
    	return getHID(gamepad1).button(this.clawCloseButton);
    }
    
    //NEW STUFF - 2017

//    public boolean flipDrive(){
//    	return getHID(gamepad1).buttonToggled(flipDriveButton, flipDriveButtonStateA, flipDriveButtonStateB);
//    }
//    
//    public boolean shooterWheels(){
//    	return getHID(gamepad1).buttonPressed(shooterButton, shooterButtonState);
//    }
//    public boolean shooterIntake(){
//    	return getHID(gamepad1).button(shooterIntakeButton);
//    }
//    public boolean gearGate() {
//    	return getHID(gamepad1).buttonPressed(gearGateButton, gearGateButtonState);
//    }
//    public boolean gearFingersOpen() {
//    	return getHID(gamepad1).button(gearFingersPickupOpenButton);
//    }
//    public boolean gearFingersClosed() {
//    	return getHID(gamepad1).button(gearFingersPickupCloseButton);
//    }
//    public boolean pickup(){
//    	return getHID(gamepad1).buttonPressed(pickupButton, pickupButtonState);
//    }
//    public boolean gearArm(){
//    	return getHID(gamepad1).buttonPressed(gearArmPickupButton, gearArmPickupButtonState);
//    }
//    public boolean cameraGearPickupView(){
//    	return getHID(gamepad1).button(cameraGearPickupButton);
//    }
//    public boolean cameraRegularView(){
//    	return getHID(gamepad2).button(cameraRegularButton);
//    }
//    public boolean retractGearArm(){
//    	return getHID(gamepad1).button(gearArmRetractButton);
//    }
//    public boolean gearRelease() {
//    	return getHID(gamepad1).button(gearRelease);
//    }
//    public boolean gearAutoPosition() {
//    	return getHID(gamepad1).axis(gearPositionAxis) > .5;
//    }
//    public boolean gearReceiverOpen(){
//    	return getHID(gamepad1).button(gearReceiverOpen);
//    }
//    public boolean driveTrainOffButton(){
//    	return getHID(gamepad1).button(driveTrainOffButton);
//    }
    //NEW STUFF - 2017

//    public boolean getReverseDriveButton() {
//        return getHID(gamepad1).buttonPressed(reverseDriveButton, reverseDriveButtonState);
//    }
//
//    public void toggleReverseDrive() {
//        reverseDrive = !reverseDrive;
//    }
//
//    public boolean getReverseDrive() {
//        return reverseDrive;
//    }

//    public boolean getReduceSpeedButton() {
//        return getHID(gamepad1).buttonPressed(reduceSpeedButton, reduceSpeedButtonState);
//    }
//
//    public void toggleReduceSpeed() {
//        reduceSpeed = !reduceSpeed;
//    }
//
//    public boolean getReduceSpeed() {
//        return reduceSpeed;
//    }
//
//    public void setSingleControllerMode(boolean state) {
//        singleControllerMode = state;
//    }
//
//    public boolean getSingleControllerMode() {
//        return singleControllerMode;
//    }
//
//    public void toggleSingleControllerMode() {
//        singleControllerMode = !singleControllerMode;
//    }
//
//    public boolean getSingleControllerToggleButton() {
//        return getHID(gamepad1).buttonPressed(singleControllerModeButton, singleControllerModeState);
//    }
//
//    public boolean getFireButton() {
//        return getHID(gamepad1).button(fireButton);
//    }
//
//    public boolean getFireOverrideButton() {
//        return getHID(gamepad1).button(fireOverrideButton);
//    }
//    
    
//    public double getClimberAxis() {
//    	return getHID(gamepad1).axis(climberAxis);
//    }
    
//    public boolean getDriverCancelFireButton() {
//        return getHID(gamepad1).button(cancelFireButton);
//    }
//    
//    public double getArmAxis() {
//        return -getHID(gamepad2).axis(armAxis);
//    }
//
//    public boolean getFeedInButton() {
//        return getHID(gamepad2).button(feedInButton);
//    }
//
//    public boolean getFeedOutButton() {
//        return getHID(gamepad2).button(feedOutButton);
//    }
//
//    public boolean getFeedStopButton() {
//        return getHID(gamepad2).button(feedStopButton);
//    }
//
//    public boolean getGotoShootPositionButton() {
//        return getHID(gamepad2).button(gotoShooterPositionButton);
//    }
//
//    public boolean getOverrideArmPIDButton() {
//        return getHID(gamepad2).button(overrideArmPIDButton);
//    }
//    
//    public boolean getOverrideDrivePIDButton() {
//        return getHID(gamepad2).button(overrideDrivePIDButton);
//    }
//    
//    public boolean getOverrideShooterPIDButton() {
//        return getHID(gamepad2).button(overrideShooterPIDButton);
//    }
}
