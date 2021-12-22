// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.RobotInstances;

public class Controller_Subsystem extends SubsystemBase {
  private XboxController xboxController;

  // Maps for XBox Xontroller Diag Screen
  private NetworkTableEntry jstick1_xaxis, jstick1_yaxis, jstick2_xaxis, jstick2_yaxis, leftTrigger, rightTrigger,
      xButton, yButton, bButton, aButton, backButton, startButton, leftBumper, rightBumper, jstick1_click,
      jstick2_click, dpadPOV, leftRumbleToggle, leftRumbleValue, rightRumbleToggle, rightRumbleValue;

  /** Creates a new Controller_Subsystem. */
  public Controller_Subsystem(RobotInstances robotInstances) {
    // This constructor is called on initializaion of the subsystem
    this.xboxController = robotInstances.getXboxController();
    initHMI();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    updateHMI();
  }

  private void initHMI() {
    ShuffleboardTab controllerDiagTab = Shuffleboard.getTab("Controller Diag");

    ShuffleboardLayout leftJoystickLayout = controllerDiagTab.getLayout("Left Joystick", "Grid Layout")
        .withPosition(0, 0).withSize(2, 3).withProperties(Map.of("number of columns", 1, "number of rows", 3));
    jstick1_xaxis = leftJoystickLayout.add("JStick 1 - X Axis", 0).withWidget(BuiltInWidgets.kNumberBar)
        .withPosition(0, 0).withProperties(Map.of("min", -1, "max", 1)).getEntry();
    jstick1_yaxis = leftJoystickLayout.add("JStick 1 - Y Axis", 0).withWidget(BuiltInWidgets.kNumberBar)
        .withPosition(0, 1).withProperties(Map.of("min", -1, "max", 1)).getEntry();
    jstick1_click = leftJoystickLayout.add("Click", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(0, 2)
        .withProperties(Map.of("color when true", "Green", "color when false", "Black")).getEntry();

    ShuffleboardLayout rightJoystickLayout = controllerDiagTab.getLayout("Right Joystick", "Grid Layout")
        .withPosition(2, 0).withSize(2, 3).withProperties(Map.of("number of columns", 1, "number of rows", 3));
    jstick2_xaxis = rightJoystickLayout.add("JStick 2 - X Axis", 0).withWidget(BuiltInWidgets.kNumberBar)
        .withPosition(0, 0).withProperties(Map.of("min", -1, "max", 1)).getEntry();
    jstick2_yaxis = rightJoystickLayout.add("JStick 2 - Y Axis", 0).withWidget(BuiltInWidgets.kNumberBar)
        .withPosition(0, 1).withProperties(Map.of("min", -1, "max", 1)).getEntry();
    jstick2_click = rightJoystickLayout.add("Click", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(0, 2)
        .withProperties(Map.of("color when true", "Green", "color when false", "Black")).getEntry();

    leftTrigger = controllerDiagTab.add("Left Trigger", 0).withWidget(BuiltInWidgets.kNumberBar).withPosition(0, 3)
        .withSize(2, 1).withProperties(Map.of("min", 0, "max", 1)).getEntry();

    rightTrigger = controllerDiagTab.add("Right Trigger", 0).withWidget(BuiltInWidgets.kNumberBar).withPosition(2, 3)
        .withSize(2, 1).withProperties(Map.of("min", 0, "max", 1)).getEntry();

    leftBumper = controllerDiagTab.add("Left Bumper", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(4, 3)
        .withSize(1, 1).withProperties(Map.of("color when true", "Green", "color when false", "Black")).getEntry();

    rightBumper = controllerDiagTab.add("Right Bumper", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(5, 3)
        .withSize(1, 1).withProperties(Map.of("color when true", "Green", "color when false", "Black")).getEntry();

    xButton = controllerDiagTab.add("X Button", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(4, 0)
        .withSize(1, 1).withProperties(Map.of("color when true", "Green", "color when false", "Black")).getEntry();

    yButton = controllerDiagTab.add("Y Button", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(5, 0)
        .withSize(1, 1).withProperties(Map.of("color when true", "Green", "color when false", "Black")).getEntry();

    aButton = controllerDiagTab.add("A Button", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(4, 1)
        .withSize(1, 1).withProperties(Map.of("color when true", "Green", "color when false", "Black")).getEntry();

    bButton = controllerDiagTab.add("B Button", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(5, 1)
        .withSize(1, 1).withProperties(Map.of("color when true", "Green", "color when false", "Black")).getEntry();

    backButton = controllerDiagTab.add("Back", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(4, 2)
        .withSize(1, 1).withProperties(Map.of("color when true", "Green", "color when false", "Black")).getEntry();

    startButton = controllerDiagTab.add("Start", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(5, 2)
        .withSize(1, 1).withProperties(Map.of("color when true", "Green", "color when false", "Black")).getEntry();

    dpadPOV = controllerDiagTab.add("DPAD POV", "Not Pressed").withWidget(BuiltInWidgets.kTextView).withPosition(6, 0)
        .getEntry();

    ShuffleboardLayout leftRumbleLayout = controllerDiagTab.getLayout("Left Rumble", "Grid Layout").withPosition(6, 2)
        .withSize(2, 2).withProperties(Map.of("number of columns", 1, "number of rows", 2));
    leftRumbleValue = leftRumbleLayout.add("Intensity", 0).withWidget(BuiltInWidgets.kNumberSlider).withPosition(0, 0)
        .withProperties(Map.of("min", 0, "max", 1)).getEntry();
    leftRumbleToggle = leftRumbleLayout.add("Toggle", false).withWidget(BuiltInWidgets.kToggleButton).withPosition(0, 1)
        .getEntry();

    ShuffleboardLayout rightRumbleLayout = controllerDiagTab.getLayout("Right Rumble", "Grid Layout").withPosition(8, 2)
        .withSize(2, 2).withProperties(Map.of("number of columns", 1, "number of rows", 2));
    rightRumbleValue = rightRumbleLayout.add("Intensity", 0).withWidget(BuiltInWidgets.kNumberSlider).withPosition(0, 0)
        .withProperties(Map.of("min", 0, "max", 1)).getEntry();
    rightRumbleToggle = rightRumbleLayout.add("Toggle", false).withWidget(BuiltInWidgets.kToggleButton)
        .withPosition(0, 1).getEntry();
  }

  private void updateHMI() {
    jstick1_xaxis.setDouble(xboxController.getX(Hand.kLeft));
    jstick1_yaxis.setDouble(xboxController.getY(Hand.kLeft));
    jstick1_click.setBoolean(xboxController.getStickButton(Hand.kLeft));

    jstick2_xaxis.setDouble(xboxController.getX(Hand.kRight));
    jstick2_yaxis.setDouble(xboxController.getY(Hand.kRight));
    jstick2_click.setBoolean(xboxController.getStickButton(Hand.kRight));

    leftTrigger.setDouble(xboxController.getTriggerAxis(Hand.kLeft));
    rightTrigger.setDouble(xboxController.getTriggerAxis(Hand.kRight));

    leftBumper.setBoolean(xboxController.getBumper(Hand.kLeft));
    rightBumper.setBoolean(xboxController.getBumper(Hand.kRight));

    xButton.setBoolean(xboxController.getXButton());
    yButton.setBoolean(xboxController.getYButton());
    aButton.setBoolean(xboxController.getAButton());
    bButton.setBoolean(xboxController.getBButton());

    backButton.setBoolean(xboxController.getBackButton());
    startButton.setBoolean(xboxController.getStartButton());

    dpadPOV.setString(xboxController.getPOV() == -1 ? "Not Pressed" : xboxController.getPOV() + " Degrees");

    if (leftRumbleToggle.getBoolean(false) == true)
      xboxController.setRumble(RumbleType.kLeftRumble, leftRumbleValue.getDouble(0.0));
    else
      xboxController.setRumble(RumbleType.kLeftRumble, 0.0);

    if (rightRumbleToggle.getBoolean(false) == true)
      xboxController.setRumble(RumbleType.kRightRumble, rightRumbleValue.getDouble(0.0));
    else
      xboxController.setRumble(RumbleType.kRightRumble, 0.0);
  }
}
