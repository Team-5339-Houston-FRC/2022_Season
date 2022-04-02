// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.AutonSubsystem;
import frc.robot.subsystems.Drive_Subsystem;

public class AutonCom extends CommandBase {
  /** Creates a new AutonCom. */
  public Drive_Subsystem m_DriveSubsystem;
  //private final DifferentialDrive m_drive = new DifferentialDrive(m_DriveSubsystem., m_rightMotor);

  public AutonCom(Drive_Subsystem autonSubsystem) {
    m_DriveSubsystem = autonSubsystem;

  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_DriveSubsystem.m_rightMotor.getEncoder().setPosition(0);

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_DriveSubsystem.m_drive.tankDrive(.5, -.5);
    System.out.println(m_DriveSubsystem.m_rightMotor.getEncoder().getPosition());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.m_driveSubsystem.setDefaultCommand(RobotContainer.m_DriveSubsystenDefaultCommand);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(m_DriveSubsystem.m_rightMotor.getEncoder().getPosition() > -50){
      return false;
  
    }else{
      return true;
    }
  }
}
