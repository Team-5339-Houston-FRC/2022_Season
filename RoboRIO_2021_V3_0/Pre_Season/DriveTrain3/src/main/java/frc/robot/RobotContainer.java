// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import frc.robot.Constants.RobotInstances;
import frc.robot.commands.ControllerDefaultCommand;
import frc.robot.commands.DriveTrainDefaultCommand;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.COMMS_Subsystem;
import frc.robot.subsystems.Controller_Subsystem;
import frc.robot.subsystems.Drive_Subsystem;
import frc.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  private final XboxController m_xboxController = new XboxController(0);
  private final PowerDistributionPanel m_PDP = new PowerDistributionPanel(Constants.kPowerDistributionBoardCANID);

  private final RobotInstances m_robotInstances = new RobotInstances(m_xboxController, m_PDP);

  // The robot's subsystems and commands are defined here...
  private final Drive_Subsystem m_driveSubsystem = new Drive_Subsystem(m_robotInstances);
  private final Controller_Subsystem m_controllerSubsystem = new Controller_Subsystem(m_robotInstances);
  private final COMMS_Subsystem m_commsSubsystem = new COMMS_Subsystem(m_robotInstances);

  private final ControllerDefaultCommand m_ControllerDefaultCommand = new ControllerDefaultCommand(
      m_controllerSubsystem);
  private final DriveTrainDefaultCommand m_DriveSubsystenDefaultCommand = new DriveTrainDefaultCommand(
      m_driveSubsystem);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    configureSubsystemDefaultCommands();
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    new JoystickButton(m_xboxController, Button.kBumperRight.value).whenPressed(() -> m_driveSubsystem.caibrateAHRS());

    new JoystickButton(m_xboxController, Button.kBumperLeft.value).whenPressed(() -> m_driveSubsystem.resetAHRS());
  }

  private void configureSubsystemDefaultCommands() {
    m_driveSubsystem.setDefaultCommand(
        new RunCommand(() -> m_driveSubsystem.arcadeDrive(m_xboxController.getY(GenericHID.Hand.kRight),
            m_xboxController.getX(GenericHID.Hand.kRight)), m_driveSubsystem));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    // return m_autoCommand;
    return null;
  }
}
