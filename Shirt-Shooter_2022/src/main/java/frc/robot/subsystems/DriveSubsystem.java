// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DriveSubsystem extends SubsystemBase  {
  // Left motors
  public final WPI_TalonSRX m_leftMotor = new WPI_TalonSRX(Constants.kLeftMotorMasterCANID);
  public final WPI_TalonSRX m_leftMotorFollower = new WPI_TalonSRX(Constants.kLeftMotorFollowerCANID);
  public final WPI_TalonSRX m_rightMotor = new WPI_TalonSRX(Constants.kRightMotorMasterCANID);
  public final WPI_TalonSRX m_rightMotorFollower = new WPI_TalonSRX(Constants.kRightMotorFollowerCANID);

  // Right motors
  

  // The robot's drive
  DifferentialDrive m_drive = new DifferentialDrive(m_leftMotor, m_rightMotor);
  
  /** Creates a new DriveTrain. */
  public DriveSubsystem() {
    m_leftMotorFollower.follow(m_leftMotor);
    m_rightMotorFollower.follow(m_rightMotor);
    
  }

  /**
   * Drives the robot using arcade controls.
   *
   * @param fwd the commanded forward movement
   * @param rot the commanded rotation
   */
  public void arcadeDrive(double fwd, double rot) {

    m_drive.arcadeDrive(fwd/1.5, rot*-1);
  }
}
