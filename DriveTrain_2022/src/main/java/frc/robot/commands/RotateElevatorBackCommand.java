// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.AttachmentSubsystem;

public class RotateElevatorBackCommand extends CommandBase {
  public AttachmentSubsystem m_elevatorSubsystem;
  public double m_speed;
  public double backPos;
  private DigitalInput m_digitalInput;
  public RotateElevatorBackCommand(double speed, AttachmentSubsystem attachmentSubsystem) {
    m_speed = speed;
    m_elevatorSubsystem = attachmentSubsystem;
    m_digitalInput = attachmentSubsystem.backSwitch;

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    backPos = m_elevatorSubsystem.backRotatorArm.getEncoder().getPosition();
    if(!m_digitalInput.get()){
      m_elevatorSubsystem.frontRotatorArm.setIdleMode(IdleMode.kCoast);
      m_elevatorSubsystem.backRotatorArm.setIdleMode(IdleMode.kBrake);
      m_elevatorSubsystem.backRotatorArm.set(m_speed);
    }else{
      m_elevatorSubsystem.backRotatorArm.set(0);
    }
    
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_elevatorSubsystem.backRotatorArm.set(0);
    m_elevatorSubsystem.frontRotatorArm.set(0);
    m_elevatorSubsystem.frontRotatorArm.setIdleMode(IdleMode.kBrake);

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
