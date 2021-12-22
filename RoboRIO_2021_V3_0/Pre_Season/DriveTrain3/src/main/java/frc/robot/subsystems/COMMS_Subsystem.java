// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.Map;

import edu.wpi.first.hal.can.CANStatus;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.RobotInstances;

public class COMMS_Subsystem extends SubsystemBase {

    private NetworkTableEntry statusCAN_BusOffCount, statusCAN_PercentBusUtilization, statusCAN_ReceiveErrorCount,
            statusCAN_TransmitErrorCount, statusCAN_TXFullCount;

    /** Creates a new COMMS_Subsystem. */
    public COMMS_Subsystem(RobotInstances robotInstances) {
        ShuffleboardTab commsDiagTab = Shuffleboard.getTab("COMMS Diag");

        ShuffleboardLayout statusCANLayout = commsDiagTab.getLayout("CAN Status", "Grid Layout").withPosition(0, 0)
                .withSize(2, 3)
                .withProperties(Map.of("number of columns", 1, "number of rows", 5, "Label position", "LEFT"));
        statusCAN_BusOffCount = statusCANLayout.add("Off Cnt (S)", 0).withWidget(BuiltInWidgets.kTextView)
                .withPosition(0, 0).getEntry();
        statusCAN_PercentBusUtilization = statusCANLayout.add("% Util.", 0).withWidget(BuiltInWidgets.kTextView)
                .withPosition(0, 0).getEntry();
        statusCAN_ReceiveErrorCount = statusCANLayout.add("RX Error Cnt", 0).withWidget(BuiltInWidgets.kTextView)
                .withPosition(0, 0).getEntry();
        statusCAN_TransmitErrorCount = statusCANLayout.add("TX Error Cnt", 0).withWidget(BuiltInWidgets.kTextView)
                .withPosition(0, 0).getEntry();
        statusCAN_TXFullCount = statusCANLayout.add("TX Full Cnt", 0).withWidget(BuiltInWidgets.kTextView)
                .withPosition(0, 0).getEntry();

    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run

        CANStatus statusCAN = RobotController.getCANStatus();
        statusCAN_BusOffCount.setDouble(statusCAN.busOffCount);
        statusCAN_PercentBusUtilization.setDouble(statusCAN.percentBusUtilization);
        statusCAN_ReceiveErrorCount.setDouble(statusCAN.receiveErrorCount);
        statusCAN_TransmitErrorCount.setDouble(statusCAN.transmitErrorCount);
        statusCAN_TXFullCount.setDouble(statusCAN.txFullCount);

    }
}
