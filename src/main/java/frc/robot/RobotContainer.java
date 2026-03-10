// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Stager;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Vision;
import frc.robot.Constants.ArmConstants;
import frc.robot.commands.AimAndSetSpeed;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.Agitator;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.commands.RunStager;
import frc.robot.commands.RunAgitator;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Hopper;
import frc.robot.commands.Shoot;
import frc.robot.subsystems.Intake;
import frc.robot.commands.PositionArm;
import frc.robot.commands.RetractArm;


public class RobotContainer {
    private double MaxSpeed = 1.0 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController joystick = new CommandXboxController(0);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    private final CommandXboxController xboxController = new CommandXboxController(1);

    public final Vision m_vision = new Vision();
    public final Turret m_turret = new Turret();
    public final Shooter m_shooter = new Shooter();
    public final Stager m_stager = new Stager();
    public final Agitator m_agitator = new Agitator();
    public final Arm m_arm = new Arm();
    public final Hopper m_hopper = new Hopper();
    public final Intake m_intake = new Intake();


    private final AimAndSetSpeed aimAndSetSpeed = new AimAndSetSpeed(m_turret, m_shooter, m_vision);


    


    public RobotContainer() {
        configureBindings();


    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-joystick.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(-joystick.getLeftX() * MaxSpeed) // Drive left with negative X (left)
                    .withRotationalRate(-joystick.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );

        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        final var idle = new SwerveRequest.Idle();
        RobotModeTriggers.disabled().whileTrue(
            drivetrain.applyRequest(() -> idle).ignoringDisable(true)
        );

        joystick.a().whileTrue(drivetrain.applyRequest(() -> brake));
        joystick.b().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-joystick.getLeftY(), -joystick.getLeftX()))
        ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        joystick.back().and(joystick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        joystick.back().and(joystick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        joystick.start().and(joystick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        joystick.start().and(joystick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // Reset the field-centric heading on left bumper press.
        joystick.leftBumper().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));

        drivetrain.registerTelemetry(logger::telemeterize);

        //Controller 2 Commands
        // Aim and shoot while holding the right trigger. The stager will run when ready to shoot (wheels are at speed, target is found, turret is aimed)

        //xboxController.rightTrigger(0.5).whileTrue(
           // aimAndSetSpeed.alongWith(new RunStager(m_stager, aimAndSetSpeed::isReadyToShoot), new RunAgitator(m_agitator, aimAndSetSpeed::isReadyToShoot,.25)
           // ));

       

        //Manual controls for testing the arm, intake, agitator, and shooter
        //Use these to make sure all motors are responding and spinning in the correct direction

        xboxController.b().whileTrue(new Shoot(m_agitator, m_shooter, m_stager));
        xboxController.a().whileTrue(new RunAgitator(m_agitator,() -> true, .25));
        xboxController.x().whileTrue(Commands.run(() -> m_intake.runIntake(.2), m_intake));
        xboxController.y().onTrue(Commands.run(() -> m_hopper.openHopper(), m_hopper));

        m_arm.setDefaultCommand(Commands.run(() -> m_arm.setArmSpeed(xboxController.getLeftY()), m_arm));
        m_turret.setDefaultCommand(Commands.run(() -> m_turret.rotate((xboxController.getRightX())), m_turret));


        // controls for testing PID on the arm 
        //Make sure to set the kp in arm constants to a small value (like 0.01) before testing to avoid violent movements
        //Also make sure that ARM_EXTENDED_POSITION is set correctly in constants. You will need to look at the encoder 
        //readings when moving the arm manually to determine this value. 

        xboxController.povDown().onTrue(new PositionArm(m_arm, m_hopper, ArmConstants.ARM_EXTENDED_POSITION));








        


        
    }

    public Command getAutonomousCommand() {
        // Simple drive forward auton
        final var idle = new SwerveRequest.Idle();
        return Commands.sequence(
            // Reset our field centric heading to match the robot
            // facing away from our alliance station wall (0 deg).
            drivetrain.runOnce(() -> drivetrain.seedFieldCentric(Rotation2d.kZero)),
            // Then slowly drive forward (away from us) for 5 seconds.
            drivetrain.applyRequest(() ->
                drive.withVelocityX(0.5)
                    .withVelocityY(0)
                    .withRotationalRate(0)
            )
            .withTimeout(5.0),
            // Finally idle for the rest of auton
            drivetrain.applyRequest(() -> idle)
        );
    }
}
// test comment :)