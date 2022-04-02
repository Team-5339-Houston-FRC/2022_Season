// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.AttachmentSubsystem;
import frc.robot.subsystems.Drive_Subsystem;

public class CalibrateFrontElevatorMotor extends CommandBase {
  /** Creates a new CalibrateElevator. */
  private AttachmentSubsystem m_AttachmentSubsystem;
  public CalibrateFrontElevatorMotor(AttachmentSubsystem attachmentSubsystem) {
    m_AttachmentSubsystem = attachmentSubsystem;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_AttachmentSubsystem.frontRotatorArm.setIdleMode(IdleMode.kCoast);
    m_AttachmentSubsystem.backRotatorArm.setIdleMode(IdleMode.kBrake);
    m_AttachmentSubsystem.frontRotatorArm.set(-.1);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_AttachmentSubsystem.frontRotatorArm.set(0);
    m_AttachmentSubsystem.frontRotatorArm.setIdleMode(IdleMode.kBrake);
    m_AttachmentSubsystem.backRotatorArm.setIdleMode(IdleMode.kBrake);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
