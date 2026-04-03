// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import java.util.Optional;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Stager;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Vision;
import frc.robot.Constants.ArmConstants;
import frc.robot.commands.AimLL;
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
import frc.robot.commands.Eject; 
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.AimAndShoot;
import frc.robot.commands.IntakeWithAgitator;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.path.PathPlannerPath;
import frc.robot.commands.ShootLL;
import edu.wpi.first.wpilibj2.command.WaitCommand;



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
    public final Intake m_intake = new Intake();
    public final Hopper m_hopper = new Hopper();


    private final AimLL aimLL = new AimLL( m_turret, m_shooter, m_vision, m_arm, 0, 100);
    SendableChooser<Command> m_autoChooser = new SendableChooser<>();

    //April Tags to Read 9,10,8,5,11,2,27,26,25,18,21,24
    

    // this is where the commands for auto will go!
    public RobotContainer() {

        

        NamedCommands.registerCommand("IntakeLeft", 
            new SequentialCommandGroup(
                new WaitCommand(1.8),
                new IntakeWithAgitator(m_agitator, m_intake, m_arm, 1, 2.2)
            )
        );

        NamedCommands.registerCommand("IntakeRight",
            new SequentialCommandGroup(
                new WaitCommand(2.1),
                new IntakeWithAgitator(m_agitator, m_intake, m_arm, 1, 2.9)
            )
        );

        NamedCommands.registerCommand("ShootButGood",
            new SequentialCommandGroup(
                new AimLL(m_turret, m_shooter, m_vision, m_arm, 0, MaxAngularRate),
                new ShootLL(m_agitator, m_shooter, m_stager, drivetrain, m_arm, MaxAngularRate, MaxSpeed, 0, MaxAngularRate, m_vision)
            )
        );

        NamedCommands.registerCommand("OutpostIntake",
            new IntakeWithAgitator(m_agitator, m_intake, m_arm, 1, 3.5)
        );

        NamedCommands.registerCommand("ShootBack",
            new Shoot(m_agitator, m_shooter,m_stager, drivetrain, m_arm,5500 ,1, 0, 5)
        );

        /*NamedCommands.registerCommand("ShootSide",
            new SequentialCommandGroup(
                new WaitCommand(8.5),
                new AimLL(m_turret, m_shooter, m_vision, m_arm, 0, MaxAngularRate),
                new ShootLL(m_agitator, m_shooter, m_stager, drivetrain, m_arm, MaxAngularRate, MaxSpeed, 0, MaxAngularRate, m_vision)
            )
        );

        NamedCommands.registerCommand("ShootMiddle",
            new SequentialCommandGroup(
                new WaitCommand(2),
                new AimLL(m_turret, m_shooter, m_vision, m_arm, 0, MaxAngularRate),
                new ShootLL(m_agitator, m_shooter, m_stager, drivetrain, m_arm, MaxAngularRate, MaxSpeed, 0, MaxAngularRate, m_vision)
            )
        );*/

          

        drivetrain.configureAutoBuilder(); 


        m_autoChooser = AutoBuilder.buildAutoChooser("Get Off Line Auto");

        SmartDashboard.putData("Auto Chooser", m_autoChooser);

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



        xboxController.y().whileTrue(new Shoot(m_agitator, m_shooter,m_stager, drivetrain, m_arm,5500 ,1, 0, 100));
        xboxController.b().whileTrue(new Shoot(m_agitator, m_shooter,m_stager, drivetrain, m_arm,5000 ,.9, 0, 100));
        xboxController.a().whileTrue(new Shoot(m_agitator, m_shooter,m_stager, drivetrain, m_arm,4500 ,.8, 0, 100));
        //xboxController.a().whileTrue(new AimAndShoot(m_agitator, m_shooter,m_stager, drivetrain, m_arm,5500 ,1,m_turret,-45));
        //mr wilaj!! check your email! zack has an important message
    
        //while shooting: power of 1 ~= 5500 rpm, .9  ~= 5000 rpm, .8  ~= 4500 rpm. 
        
        xboxController.x().whileTrue(new IntakeWithAgitator(m_agitator, m_intake, m_arm, 1, 999));
         //xboxController.leftTrigger(0.5).whileTrue(Commands.run(() -> m_agitator.setAgitatorSpeed(.75), m_intake).finallyDo(() -> m_agitator.setAgitatorSpeed(0)));
         //xboxController.rightTrigger(.5).whileTrue(new Eject(m_agitator, m_intake,.75)); 
        xboxController.rightTrigger(.5).whileTrue(new AimLL(m_turret,m_shooter,m_vision,m_arm,0,5 ));
        xboxController.leftTrigger(0.5).whileTrue(new ShootLL(m_agitator, m_shooter,m_stager, drivetrain, m_arm,4000 ,1, 0, 100, m_vision));

        xboxController.leftBumper().onTrue(new PositionArm(m_arm, m_hopper, 10));
        xboxController.rightBumper().onTrue(new PositionArm(m_arm, m_hopper, 140));
        //xboxController.leftBumper().whileTrue(Commands.run(() -> m_shooter.setVelocity(6000), m_shooter).finallyDo(() -> m_shooter.runShooter(0)));

        xboxController.povRight().onTrue(Commands.run(() -> m_turret.setTargetAngleDegrees(45), m_turret).finallyDo(() -> m_turret.stop()));
        xboxController.povUp().onTrue(Commands.run(() -> m_turret.setTargetAngleDegrees(90), m_turret).finallyDo(() -> m_turret.stop()));
        xboxController.povLeft().onTrue(Commands.run(() -> m_turret.setTargetAngleDegrees(-45), m_turret).finallyDo(() -> m_turret.stop()));
        xboxController.povDown().onTrue(Commands.run(() -> m_turret.setTargetAngleDegrees(-90), m_turret).finallyDo(() -> m_turret.stop()));
        


        

        new Trigger(() -> Math.abs(xboxController.getLeftY()) > 0.1).whileTrue(Commands.run(() -> m_arm.setArmSpeed(xboxController.getLeftY() * 0.2), m_arm));
        new Trigger(() -> Math.abs(xboxController.getRightX()) > 0.1).whileTrue(Commands.run(() -> m_turret.rotate(xboxController.getRightX() * 0.2), m_turret));
        m_arm.setDefaultCommand(Commands.run(() -> m_arm.setArmSpeed(0), m_arm));
        m_turret.setDefaultCommand(Commands.run(() -> m_turret.rotate(0), m_turret));


        

    }



    public Command getAutonomousCommand() {
        
         return m_autoChooser.getSelected();

        
    }



}
