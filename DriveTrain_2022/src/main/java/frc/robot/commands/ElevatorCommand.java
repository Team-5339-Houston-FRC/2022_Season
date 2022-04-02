// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.AttachmentSubsystem;

public class ElevatorCommand extends CommandBase {
  /** Creates a new ElevatorCommand. */
  private final AttachmentSubsystem m_attachmentSubsystem;
  private double m_speed;
  private double elevatorPos;
  private DigitalInput m_digitalInput;


  public ElevatorCommand(Double speed, AttachmentSubsystem attachmentSubsystem) {
    m_attachmentSubsystem = attachmentSubsystem;
    m_speed = speed;
    m_digitalInput = m_attachmentSubsystem.elevatorInput;
  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(!m_digitalInput.get() && m_speed < 0){
      m_attachmentSubsystem.elevatorMotorOne.set(m_speed);
      m_attachmentSubsystem.elevatorRunning.setBoolean(true);
    }else if(m_speed > 0){
      m_attachmentSubsystem.elevatorMotorOne.set(m_speed);
      m_attachmentSubsystem.elevatorRunning.setBoolean(true);
    }else{
      m_attachmentSubsystem.elevatorMotorOne.set(0);
      m_attachmentSubsystem.elevatorRunning.setBoolean(false);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_attachmentSubsystem.elevatorMotorOne.set(0);
    m_attachmentSubsystem.elevatorRunning.setBoolean(false);

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
