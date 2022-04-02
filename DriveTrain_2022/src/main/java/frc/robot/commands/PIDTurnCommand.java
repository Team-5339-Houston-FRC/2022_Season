// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Drive_Subsystem;

public class PIDTurnCommand extends PIDCommand {
  /** Creates a new PIDTurnCommand. */

  public PIDTurnCommand(double targetAngleDegrees, Drive_Subsystem drive) {
    // Use addRequirements() here to declare subsystem dependencies.

    super(new PIDController(0.008, 0.003, 0.0005), 
    drive::getHeading,
    targetAngleDegrees,
    output -> drive.arcadeDrive(0, output),
     drive);
    System.out.println("Start Command turnAngle[" + targetAngleDegrees + "]");

    getController().enableContinuousInput(-180, 180);
    getController().setTolerance(.5, 2);
  }
  
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    super.initialize();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    super.execute();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    super.end(false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }
}
