// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.Map;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class AttachmentSubsystem extends SubsystemBase {
  /** Creates a new ElevatorSubsystem. */
  public final CANSparkMax elevatorMotorOne = new CANSparkMax(Constants.kElevatorMotorOneCANID, MotorType.kBrushless);
  public final CANSparkMax elevatorMotorTwo = new CANSparkMax(Constants.kElevatorMotorTwoCANID, MotorType.kBrushless);

  public final CANSparkMax frontRotatorArm = new CANSparkMax(Constants.kFrontRotatorArmMotor_CANID, MotorType.kBrushless);
  public final CANSparkMax backRotatorArm = new CANSparkMax(Constants.kBackRotatorArmMotor_CANID, MotorType.kBrushless);

  public final DigitalInput elevatorInput = new DigitalInput(0);
  public final DigitalInput frontSwitch = new DigitalInput(2);
  public final DigitalInput backSwitch = new DigitalInput(1);

  public NetworkTableEntry elevatorRunning, elevatorRotating, rotatorCalibrating, elevatorCalibrating, elevatorMotorPulseCount,
   frontMotorPulseCount, backMotorPulseCount, elevatorSwitch, elevatorHome, elevatorMax;
  
  public AttachmentSubsystem() {

    elevatorMotorOne.setIdleMode(IdleMode.kBrake);
    elevatorMotorTwo.setIdleMode(IdleMode.kBrake);
    
    elevatorMotorTwo.follow(elevatorMotorOne);

    ShuffleboardTab elevatorTab = Shuffleboard.getTab("Elevator Tab");


    elevatorRunning = elevatorTab.add("Up Down", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(0, 0)
        .withSize(1, 1).withProperties(Map.of("color when true", "Green", "color when false", "Black")).getEntry();
    elevatorRotating = elevatorTab.add("Rotating", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(1, 0)
        .withSize(1, 1).withProperties(Map.of("color when true", "Green", "color when false", "Black")).getEntry();
    rotatorCalibrating = elevatorTab.add("Rotator Calibrating", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(0, 1)
        .withSize(1, 1).withProperties(Map.of("color when true", "Green", "color when false", "Black")).getEntry();
    backMotorPulseCount = elevatorTab.add("Back Pulse Count", "").withWidget(BuiltInWidgets.kTextView).withPosition(1, 1).getEntry();
    frontMotorPulseCount = elevatorTab.add("Front Pulse Count", "").withWidget(BuiltInWidgets.kTextView).withPosition(2, 1).getEntry();

    elevatorCalibrating = elevatorTab.add("Elevator Calibrating", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(0, 2)
        .withSize(1, 1).withProperties(Map.of("color when true", "Green", "color when false", "Black")).getEntry();
    elevatorMotorPulseCount = elevatorTab.add("Elevator Pulse Count", "").withWidget(BuiltInWidgets.kTextView).withPosition(1, 2).getEntry();
    
    elevatorSwitch = elevatorTab.add("Elevator Home", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(0, 3)
        .withSize(1, 1).withProperties(Map.of("color when true", "Green", "color when false", "Black")).getEntry();
    elevatorHome = elevatorTab.add("Rotator Home", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(0, 3)
        .withSize(1, 1).withProperties(Map.of("color when true", "Green", "color when false", "Black")).getEntry();
    elevatorMax = elevatorTab.add("Rotator Max", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(0, 3)
        .withSize(1, 1).withProperties(Map.of("color when true", "Green", "color when false", "Black")).getEntry();
  }
  public void resetRotatorPulseCount(){
    rotatorCalibrating.setBoolean(true);
    backRotatorArm.getEncoder().setPosition(0.0);
    frontRotatorArm.getEncoder().setPosition(0.0);
  }
  public void resetElevatorPulseCount(){
    elevatorCalibrating.setBoolean(true);
    elevatorMotorOne.getEncoder().setPosition(0.0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    backMotorPulseCount.setString(backRotatorArm.getEncoder().getPosition() + "");
    frontMotorPulseCount.setString(frontRotatorArm.getEncoder().getPosition() + "");
    elevatorMotorPulseCount.setString(elevatorMotorOne.getEncoder().getPosition() + "");
    elevatorSwitch.setBoolean(elevatorInput.get());
    elevatorMax.setBoolean(backSwitch.get());
    elevatorHome.setBoolean(frontSwitch.get());
  }

}
