// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.RobotInstances;

import java.util.Map;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;

public class Drive_Subsystem extends SubsystemBase {

  // Left motors
  private final CANSparkMax m_leftMotor = new CANSparkMax(Constants.kLeftMotorMasterCANID, MotorType.kBrushless);
  private final CANSparkMax m_leftMotorFollower = new CANSparkMax(Constants.kLeftMotorFollowerCANID,
      MotorType.kBrushless);

  // Right motors
  private final CANSparkMax m_rightMotor = new CANSparkMax(Constants.kRightMotorMasterCANID, MotorType.kBrushless);
  private final CANSparkMax m_rightMotorFollower = new CANSparkMax(Constants.kRightMotorFollowerCANID,
      MotorType.kBrushless);

  AHRS ahrs;

  private NetworkTableEntry ahrsConnected, ahrsCalibrating, accelermoterXAxis, accelermoterYAxis, accelermoterZAxis,
      ahrsIsMoving, ahrsIsRotating, ahrsAccelX, ahrsAccelY, gyroYaw, gyroPitch, gyroRoll, velocityX, velocityY,
      displacementX, displacementY;

  // The robot's drive
  private final DifferentialDrive m_drive = new DifferentialDrive(m_leftMotor, m_rightMotor);

  /** Creates a new DriveTrain. */
  public Drive_Subsystem(RobotInstances robotInstances) {

    m_leftMotor.restoreFactoryDefaults();
    m_leftMotorFollower.restoreFactoryDefaults();
    m_rightMotor.restoreFactoryDefaults();
    m_rightMotorFollower.restoreFactoryDefaults();

    m_leftMotorFollower.follow(m_leftMotor);
    m_rightMotorFollower.follow(m_rightMotor);

    try {
      ahrs = new AHRS(SPI.Port.kMXP);
    } catch (RuntimeException ex) {
      DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
    }

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

    ahrsAccelX = driveDiagTab.add("World Linear Accel X", "").withWidget(BuiltInWidgets.kTextView).withPosition(2, 1).getEntry();
    ahrsAccelY = driveDiagTab.add("World Linear Accel Y", "").withWidget(BuiltInWidgets.kTextView).withPosition(3, 1).getEntry();
    
    velocityX = driveDiagTab.add("Velocity X", "").withWidget(BuiltInWidgets.kTextView).withPosition(2, 2).getEntry();
    velocityY = driveDiagTab.add("Velocity Y", "").withWidget(BuiltInWidgets.kTextView).withPosition(3, 2).getEntry();

    displacementX = driveDiagTab.add("Displacement X", "").withWidget(BuiltInWidgets.kTextView).withPosition(2, 3).getEntry();
    displacementY = driveDiagTab.add("Displacement Y", "").withWidget(BuiltInWidgets.kTextView).withPosition(3, 3).getEntry();
  }

  /**
   * Drives the robot using arcade controls.
   *
   * @param fwd the commanded forward movement
   * @param rot the commanded rotation
   */
  public void arcadeDrive(double fwd, double rot) {
    m_drive.arcadeDrive(fwd, rot);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
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

    displacementX.setString(ahrs.getDisplacementX() + "");
    displacementY.setString(ahrs.getDisplacementY() + "");

  }

  public void resetAHRS() {
    ahrs.reset();
    ahrs.resetDisplacement();
    System.out.println("RESET");
  }

  public void caibrateAHRS() {
    ahrs.calibrate();
    System.out.println("Calibrate");
  }

}
