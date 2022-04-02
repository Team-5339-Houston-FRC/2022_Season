// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.DigitalInput;
//import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.AttachmentSubsystem;

public class RotateElevatorCommand extends CommandBase {
  /** Creates a new RotateArmCommand. */
  public AttachmentSubsystem m_elevatorSubsystem;
  public double m_speed;
  public double frontPos;
  private DigitalInput m_digitalInput;
  public RotateElevatorCommand(double speed, AttachmentSubsystem attachmentSubsystem) {
    m_speed = speed;
    m_elevatorSubsystem = attachmentSubsystem;
    m_digitalInput = attachmentSubsystem.frontSwitch;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    frontPos = m_elevatorSubsystem.frontRotatorArm.getEncoder().getPosition();
    if(!m_digitalInput.get()){
      m_elevatorSubsystem.frontRotatorArm.setIdleMode(IdleMode.kBrake);
      m_elevatorSubsystem.backRotatorArm.setIdleMode(IdleMode.kCoast);
      m_elevatorSubsystem.frontRotatorArm.set(m_speed*-1);
    }else{
      m_elevatorSubsystem.frontRotatorArm.set(0);
    }
    
    

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_elevatorSubsystem.frontRotatorArm.set(0);
    m_elevatorSubsystem.backRotatorArm.set(0);
    m_elevatorSubsystem.backRotatorArm.setIdleMode(IdleMode.kBrake);

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
