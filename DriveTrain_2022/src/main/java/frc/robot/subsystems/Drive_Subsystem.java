// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.Map;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.Constants.RobotInstances;
import frc.robot.commands.DriveTrainDefaultCommand;
import edu.wpi.first.math.controller.PIDController;


public class Drive_Subsystem extends SubsystemBase  {
  private Joystick joystick;
  PIDController turnController;
  //CANSparkMax m_ElevatorMotor = new CANSparkMax(Constants.kElevatorMotorCANID, MotorType.kBrushless);
  boolean straightDrive;
  double P = 0.04;
  double I = 0.0;
  double D = 0.0;
  int integral, previous_error, setpoint = 0;
  // Left motors
  public final CANSparkMax m_leftMotor = new CANSparkMax(Constants.kLeftMotorMasterCANID, MotorType.kBrushless);
  private final CANSparkMax m_leftMotorFollower = new CANSparkMax(Constants.kLeftMotorFollowerCANID,
      MotorType.kBrushless);

  // Right motors
  public final CANSparkMax m_rightMotor = new CANSparkMax(Constants.kRightMotorMasterCANID, MotorType.kBrushless);
  private final CANSparkMax m_rightMotorFollower = new CANSparkMax(Constants.kRightMotorFollowerCANID,
      MotorType.kBrushless);

  AHRS ahrs;

  private NetworkTableEntry ahrsConnected, ahrsCalibrating, accelermoterXAxis, accelermoterYAxis, accelermoterZAxis,
      ahrsIsMoving, ahrsIsRotating, ahrsAccelX, ahrsAccelY, gyroYaw, gyroPitch, gyroRoll, velocityX, velocityY,
      leftEncoderPulseCount, leftEncoderVelocity, leftCountsPerRev, rightEncoderPulseCount, 
      rightEncoderVelocity, rightCountsPerRev;

  // The robot's drive
  public final DifferentialDrive m_drive = new DifferentialDrive(m_leftMotor, m_rightMotor);
  
  /** Creates a new DriveTrain. */
  public Drive_Subsystem(RobotInstances robotInstances) {
    this.joystick = robotInstances.getXboxController();
    
    m_leftMotor.restoreFactoryDefaults();
    m_leftMotorFollower.restoreFactoryDefaults();
    m_rightMotor.restoreFactoryDefaults();
    m_rightMotorFollower.restoreFactoryDefaults();

    m_leftMotorFollower.follow(m_leftMotor);
    m_rightMotorFollower.follow(m_rightMotor);

    shuffleboardSettings();
    
    try {
      ahrs = new AHRS(SPI.Port.kMXP);
    } catch (RuntimeException ex) {
      DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
    }
    
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
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    
    shuffleboardInputs();
  }
  
  private void shuffleboardSettings(){
    ShuffleboardTab driveDiagTab = Shuffleboard.getTab("Drive Diag");

    ahrsConnected = driveDiagTab.add("AHRS Connected", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(0, 0)
        .withSize(1, 1).withProperties(Map.of("color when true", "Green", "color when false", "Black")).getEntry();

    ahrsCalibrating = driveDiagTab.add("AHRS Calibrating", false).withWidget(BuiltInWidgets.kBooleanBox)
        .withPosition(1, 0).withSize(1, 1)
        .withProperties(Map.of("color when true", "Green", "color when false", "Black")).getEntry();

    ShuffleboardLayout accelerometerLayout = driveDiagTab.getLayout("Accelerometers", "Grid Layout").withPosition(0, 1)
        .withSize(1, 3).withProperties(Map.of("number of columns", 1, "number of rows", 3));
    accelermoterXAxis = accelerometerLayout.add("X Axis", 0).withWidget(BuiltInWidgets.kNumberBar).withPosition(0, 0)
        .withProperties(Map.of("min", -1, "max", 1)).getEntry();
    accelermoterYAxis = accelerometerLayout.add("Y Axis", 0).withWidget(BuiltInWidgets.kNumberBar).withPosition(0, 0)
        .withProperties(Map.of("min", -1, "max", 1)).getEntry();
    accelermoterZAxis = accelerometerLayout.add("Z Axis", 0).withWidget(BuiltInWidgets.kNumberBar).withPosition(0, 0)
        .withProperties(Map.of("min", -1, "max", 1)).getEntry();

    ShuffleboardLayout gyroLayout = driveDiagTab.getLayout("Headings", "Grid Layout").withPosition(1, 1).withSize(1, 3)
        .withProperties(Map.of("number of columns", 1, "number of rows", 3));
    gyroYaw = gyroLayout.add("Yaw", "").withWidget(BuiltInWidgets.kTextView).withPosition(0, 0).getEntry();
    gyroPitch = gyroLayout.add("Pitch", "").withWidget(BuiltInWidgets.kTextView).withPosition(0, 1).getEntry();
    gyroRoll = gyroLayout.add("Roll", "").withWidget(BuiltInWidgets.kTextView).withPosition(0, 2).getEntry();

    ahrsIsMoving = driveDiagTab.add("Is Moving", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(2, 0)
        .withSize(1, 1).withProperties(Map.of("color when true", "Green", "color when false", "Black")).getEntry();

    ahrsIsRotating = driveDiagTab.add("Is Rotating", false).withWidget(BuiltInWidgets.kBooleanBox).withPosition(3, 0)
        .withSize(1, 1).withProperties(Map.of("color when true", "Green", "color when false", "Black")).getEntry();

    velocityX = driveDiagTab.add("Velocity X", "").withWidget(BuiltInWidgets.kTextView).withPosition(2, 2).getEntry();
    velocityY = driveDiagTab.add("Velocity Y", "").withWidget(BuiltInWidgets.kTextView).withPosition(3, 2).getEntry();
    
    ahrsAccelX = driveDiagTab.add("World Linear Accel X", "").withWidget(BuiltInWidgets.kTextView).withPosition(2, 1).getEntry();
    ahrsAccelY = driveDiagTab.add("World Linear Accel Y", "").withWidget(BuiltInWidgets.kTextView).withPosition(3, 1).getEntry();

    ShuffleboardLayout leftMotorLayout = driveDiagTab.getLayout("Left", "Grid Layout").withPosition(4, 1)
    .withSize(2, 2).withProperties(Map.of("number of columns", 1, "number of rows", 3));
    leftEncoderPulseCount = leftMotorLayout.add("Encoder Pulse Count", "").withWidget(BuiltInWidgets.kTextView).withPosition(0, 0).getEntry();
    leftEncoderVelocity = leftMotorLayout.add("Encoder Velocity", "").withWidget(BuiltInWidgets.kTextView).withPosition(0, 1).getEntry();
    leftCountsPerRev = leftMotorLayout.add("Counts Per Rev", "").withWidget(BuiltInWidgets.kTextView).withPosition(0, 2).getEntry();

    ShuffleboardLayout rightMotorLayout = driveDiagTab.getLayout("Right", "Grid Layout").withPosition(6, 1)
    .withSize(2, 2).withProperties(Map.of("number of columns", 1, "number of rows", 3));
    rightEncoderPulseCount = rightMotorLayout.add("Encoder Pulse Count", "").withWidget(BuiltInWidgets.kTextView).withPosition(0, 0).getEntry();
    rightEncoderVelocity = rightMotorLayout.add("Encoder Velocity", "").withWidget(BuiltInWidgets.kTextView).withPosition(0, 1).getEntry();
    rightCountsPerRev = rightMotorLayout.add("Counts Per Rev", "").withWidget(BuiltInWidgets.kTextView).withPosition(0, 2).getEntry();
    
  }


  private void shuffleboardInputs(){
    ahrsConnected.setBoolean(ahrs.isConnected());
    ahrsCalibrating.setBoolean(ahrs.isCalibrating());

    accelermoterXAxis.setDouble(ahrs.getRawAccelX());
    accelermoterYAxis.setDouble(ahrs.getRawAccelY());
    accelermoterZAxis.setDouble(ahrs.getRawAccelZ());

    gyroYaw.setString(ahrs.getYaw() + " Degrees");
    gyroPitch.setString(ahrs.getPitch() + " Degrees");
    gyroRoll.setString(ahrs.getRoll() + " Degrees");

    ahrsIsMoving.setBoolean(ahrs.isMoving());
    ahrsIsRotating.setBoolean(ahrs.isRotating());

    ahrsAccelX.setString(ahrs.getWorldLinearAccelX() + "");
    ahrsAccelY.setString(ahrs.getWorldLinearAccelY() + "");

    velocityX.setString(ahrs.getVelocityX() + "");
    velocityY.setString(ahrs.getVelocityX() + "");

    leftEncoderPulseCount.setString((m_leftMotor.getEncoder().getPosition()*-1) + "");
    leftEncoderVelocity.setString(m_leftMotor.getEncoder().getVelocity() + "");
    leftCountsPerRev.setString(m_leftMotor.getEncoder().getCountsPerRevolution() + "");

    rightEncoderPulseCount.setString(m_rightMotor.getEncoder().getPosition() + "");
    rightEncoderVelocity.setString(m_rightMotor.getEncoder().getVelocity() + "");
    rightCountsPerRev.setString(m_rightMotor.getEncoder().getCountsPerRevolution() + "");
  }


  public void resetAHRS() {
    ahrs.reset();
    ahrs.resetDisplacement();
    System.out.println("RESET");
  }

  public void resetEncoderPos(){
    m_rightMotor.getEncoder().setPosition(0.0);
    m_leftMotor.getEncoder().setPosition(0.0);
  }

  public void caibrateAHRS() {
    ahrs.calibrate();
    System.out.println("Calibrate");
  }

  public double getHeading() {
    return ahrs.getYaw();
  }
}
