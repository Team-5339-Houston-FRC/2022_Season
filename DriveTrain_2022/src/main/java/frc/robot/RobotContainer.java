// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.XboxController.Button;
import frc.robot.Constants.RobotInstances;
import frc.robot.commands.AutonCom;
import frc.robot.commands.CalibrateBackElevatorMotor;
import frc.robot.commands.CalibrateFrontElevatorMotor;
import frc.robot.commands.ControllerDefaultCommand;
import frc.robot.commands.DriveTrainDefaultCommand;
import frc.robot.commands.ElevatorCommand;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.PIDTurnCommand;
import frc.robot.commands.RotateElevatorBackCommand;
import frc.robot.commands.RotateElevatorCommand;
import frc.robot.subsystems.COMMS_Subsystem;
import frc.robot.subsystems.Controller_Subsystem;
import frc.robot.subsystems.Drive_Subsystem;
import frc.robot.subsystems.AttachmentSubsystem;
import frc.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  
  public final static Joystick m_driveController = new Joystick(1);
  public final static Joystick m_joystickController = new Joystick(0);
  private final static PowerDistribution m_PDP = new PowerDistribution(Constants.kPowerDistributionBoardCANID, PowerDistribution.ModuleType.kRev);
  private final static RobotInstances m_robotInstances = new RobotInstances(m_driveController, m_PDP);

  

  // The robot's subsystems and commands are defined here...
  public final static Drive_Subsystem m_driveSubsystem = new Drive_Subsystem(m_robotInstances);
  public final static DriveTrainDefaultCommand m_DriveSubsystenDefaultCommand = new DriveTrainDefaultCommand(
    m_driveSubsystem);
  private final Controller_Subsystem m_controllerSubsystem = new Controller_Subsystem(m_robotInstances);
  private final COMMS_Subsystem m_commsSubsystem = new COMMS_Subsystem(m_robotInstances);
  private final AttachmentSubsystem m_AttachmentSubsystem = new AttachmentSubsystem();

  //Limit Switches
  


  private final ControllerDefaultCommand m_ControllerDefaultCommand = new ControllerDefaultCommand(
      m_controllerSubsystem);
  
  final static AutonCom m_AutonCom = new AutonCom(m_driveSubsystem);

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
    //Controller Buttons
    JoystickButton button1 = new JoystickButton(m_joystickController, 1),
          button2 = new JoystickButton(m_joystickController, 2),
          button3 = new JoystickButton(m_joystickController, 3),
          button4 = new JoystickButton(m_joystickController, 4),
          button5 = new JoystickButton(m_joystickController, 5),
          button6 = new JoystickButton(m_joystickController, 6),
          button7 = new JoystickButton(m_joystickController, 7),
          button8 = new JoystickButton(m_joystickController, 8),
          button9 = new JoystickButton(m_joystickController, 9),
          button10 = new JoystickButton(m_joystickController, 10),
          button11 = new JoystickButton(m_joystickController, 11),
          button12 = new JoystickButton(m_joystickController, 12);

          
          
    new JoystickButton(m_driveController, Button.kRightBumper.value).whenPressed(() -> m_driveSubsystem.caibrateAHRS());
    new JoystickButton(m_driveController, Button.kLeftBumper.value).whenPressed(() -> m_driveSubsystem.resetAHRS());

    button11.whenPressed(() -> m_AttachmentSubsystem.resetElevatorPulseCount());
    button12.whenPressed(() -> m_AttachmentSubsystem.resetRotatorPulseCount());
  
    // new JoystickButton(m_xboxController, Button.kY.value).whenPressed(new PIDTurnCommand(0, m_driveSubsystem));
    // new JoystickButton(m_xboxController, Button.kX.value).whenPressed(new PIDTurnCommand(-90, m_driveSubsystem));
    // new JoystickButton(m_xboxController, Button.kB.value).whenPressed(new PIDTurnCommand(90, m_driveSubsystem));
    // new JoystickButton(m_xboxController, Button.kA.value).whenPressed(new PIDTurnCommand(180, m_driveSubsystem));
    
    //Elevator Up/Down
    button5.whileHeld(new ElevatorCommand(0.1, m_AttachmentSubsystem));
    button3.whileHeld(new ElevatorCommand(-0.3, m_AttachmentSubsystem));
    //Calibrate Elevator
    button7.whileHeld(new CalibrateFrontElevatorMotor(m_AttachmentSubsystem));
    button8.whileHeld(new CalibrateBackElevatorMotor(m_AttachmentSubsystem));
    //Rotate elevator
    button6.whileHeld(new RotateElevatorCommand(.2, m_AttachmentSubsystem));
    button4.whileHeld(new RotateElevatorBackCommand(.2, m_AttachmentSubsystem));

  }


  private void configureSubsystemDefaultCommands() {
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    // return m_autoCommand;
    return m_AutonCom;
  }
}
