// This file sets up autos, creates swerve drive, and binds controller buttons to commands.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.*;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

import frc.robot.subsystems.*;
import frc.robot.Constants.*;
import frc.robot.commands.*;
import frc.robot.generated.TunerConstants;

import com.pathplanner.lib.auto.*;
import com.pathplanner.lib.path.PathPlannerPath;

public class RobotContainer {

    // Very basic things about the robot. Driving, controllers, & subsystems.

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

    public final Vision vision = new Vision();
    public final Turret turret = new Turret();
    public final Shooter shooter = new Shooter();
    public final Stager stager = new Stager();
    public final Agitator agitator = new Agitator();
    public final Arm arm = new Arm();
    public final Intake intake = new Intake();
    public final Hopper hopper = new Hopper();
    public final CANRange canRange = new CANRange();


    //private final AimLL aimLL = new AimLL(turret, shooter, vision, arm, 0, 100);
    SendableChooser<Command> autoChooser = new SendableChooser<>();

    //April Tags to Read: 9,10,8,5,11,2,27,26,25,18,21,24
        // Since we only want the april tags on the hub, we ignore the rest
    

    // Everything here is used for autos in the PathPlanner app
    // FIGURE OUT WHERE THE SHOOTING DEAD ZONES ARE!!
    public RobotContainer() {

        NamedCommands.registerCommand("IntakeLeft", 
            Commands.sequence(
                Commands.waitSeconds(1.1),
                new PositionArm(arm, hopper, intake, 140, 1),
                new IntakeCommand(intake, 2.3)
            )
        );

        NamedCommands.registerCommand("IntakeRight",
            Commands.sequence(
                Commands.waitSeconds(1.1),
                new PositionArm(arm, hopper, intake, 140, 1),
                new IntakeCommand(intake, 2.3) // maybe change the time?
            )
        );

        NamedCommands.registerCommand("OutpostIntake",
            Commands.sequence(
                new PositionArm(arm, hopper, intake, 140, 1),
                new IntakeCommand(intake, 2.5)
            )
        );

        NamedCommands.registerCommand("ShootBackIntake",
            Commands.sequence(
                Commands.waitSeconds(2.1),
                new PositionArm(arm, hopper, intake, 140, 1),
                new IntakeCommand(intake, 1.5)
            )
        );

        NamedCommands.registerCommand("ShootButGood",
            Commands.sequence(
                new AimLL(turret, shooter, vision, 1),
                new ShootLL(agitator, shooter, stager, drivetrain, vision, canRange, 8, 1)
            )
        );

        NamedCommands.registerCommand("ShootBack",
            new Shoot(agitator, shooter, stager, arm, canRange, 4500, 1, 10)
        );

        NamedCommands.registerCommand("ShootButGoodButGooder",
            Commands.sequence(
                new AimLL(turret, shooter, vision, 1),
                //new Unjam(agitator, 1.25),
                new ShootLL(agitator, shooter, stager, drivetrain, vision, canRange, 4.5, 1),
                //new Unjam(agitator, 1.25),
                new ShootLL(agitator, shooter, stager, drivetrain, vision, canRange, 4.5, 1),
                //new Unjam(agitator, 1.25),
                new ShootLL(agitator, shooter, stager, drivetrain, vision, canRange, 4.5, 1)
            )
        );

        NamedCommands.registerCommand("Unjam?",
            new Unjam(agitator, 2.5)
        );

        NamedCommands.registerCommand("OpenHopper",
            new PositionArm(arm, hopper, intake, 140, 1)
        );

        NamedCommands.registerCommand("CloseHopperALittle",
            new PositionArm(arm, hopper, intake, 70, 1)
        );

        NamedCommands.registerCommand("RunAgitatorForOutpost",
            new RunAgitator(agitator, 0.3, 5)
        );

        drivetrain.configureAutoBuilder(); 


        autoChooser = AutoBuilder.buildAutoChooser("Get Off Line Auto");

        SmartDashboard.putData("Auto Chooser", autoChooser);

        configureBindings();
    }


    // Swerve drive. Created automatically.

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
        joystick.rightBumper().whileTrue(new Shoot(agitator, shooter, stager, arm, canRange, 1250, 1, 999));

        drivetrain.registerTelemetry(logger::telemeterize);

        // Setting up the controls for the copilot


        xboxController.y().whileTrue(new Shoot(agitator, shooter,stager, arm, canRange, 5500, 1, 100));
        xboxController.b().whileTrue(new Shoot(agitator, shooter,stager, arm, canRange, 4500, 1, 100));
        xboxController.a().whileTrue(new Shoot(agitator, shooter,stager, arm, canRange, 3500, 1, 100));

        xboxController.x().whileTrue(new IntakeCommand(intake, 999));

        xboxController.rightTrigger(0.5).whileTrue(new AimLL(turret, shooter, vision, 100));
        xboxController.leftTrigger(0.5).whileTrue(new ShootLL(agitator, shooter, stager, drivetrain, vision, canRange, 100, 1));
        
        // potentially make a seperate auto/teleop command
        xboxController.leftBumper().onTrue(new PositionArm(arm, hopper, intake, 5, 999));
        xboxController.rightBumper().onTrue(new PositionArm(arm, hopper, intake, 140, 999));

        // D-pad controls (all do the same thing lol)
        xboxController.povRight().whileTrue(new Unjam(agitator, 999));
        xboxController.povUp().whileTrue(new Unjam(agitator, 999));
        xboxController.povLeft().whileTrue(new Unjam(agitator, 999));
        xboxController.povDown().whileTrue(new Unjam(agitator, 999));


        
        // Setting up the main driver's controls

        // on intitialize for the manual arm movement, disable the PID.
        // on end for the manual arm movement, enable the PID.
        // in the arm subsystem's period, have a boolean like shouldUsePID and if it's true then calculate the PID thing!
        new Trigger(() -> Math.abs(xboxController.getLeftY()) > 0.1).whileTrue(Commands.run(() -> arm.setArmSpeed(xboxController.getLeftY() * 0.2), arm));
        new Trigger(() -> Math.abs(xboxController.getRightX()) > 0.1).whileTrue(Commands.run(() -> turret.rotate(xboxController.getRightX() * 0.2), turret));
        arm.setDefaultCommand(Commands.run(() -> arm.setArmSpeed(0), arm));
        turret.setDefaultCommand(Commands.run(() -> turret.rotate(0), turret));
    }

    public Command getAutonomousCommand() {
        
         return autoChooser.getSelected();

    }

}
