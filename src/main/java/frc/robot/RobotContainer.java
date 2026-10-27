// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.io.File;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.SwerveSubsystem;
import swervelib.SwerveInputStream;

public class RobotContainer {
  private final SwerveSubsystem mSwerveDrive = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "swerve"));

  private final CommandXboxController mDriverController = new CommandXboxController(0);

  SwerveInputStream mDriveFieldOriented = SwerveInputStream.of( //
      mSwerveDrive.getSwerveDrive(), //
      () -> mDriverController.getLeftY() * -1, //
      () -> -mDriverController.getLeftX()) //
      .withControllerRotationAxis(() -> -mDriverController.getRightX()) //
      .deadband(0.1) //
      .scaleTranslation(0.5) //
      .allianceRelativeControl(false);

  SwerveInputStream mDriveRobotOriented = mDriveFieldOriented.copy() //
      .robotRelative(true) //
      .allianceRelativeControl(false);

  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {
    var defaultDriveCommand = mSwerveDrive.driveFieldOrientedCommand(mDriveRobotOriented);

    mSwerveDrive.setDefaultCommand(defaultDriveCommand);

    mDriverController.a().onTrue(mSwerveDrive.zeroGyroCommand());
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
