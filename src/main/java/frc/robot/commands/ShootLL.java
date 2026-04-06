package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.*;
//import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Timer; 

// Reads the limelight to determine shooting rpm

public class ShootLL extends Command {

    private final Agitator agitator;
    private final Shooter shooter;
    private final Stager stager;    
    //private final CommandSwerveDrivetrain drivetrain;
    private final Arm arm;
    private final Vision vision;

    private final double end;
    private final double wheelspeed; 
    private final Timer oscillatingTimer;
    public final Timer shootTimer;
    // Speed for stager and agitator when feeding balls into the shooter, can be tuned

    public ShootLL(Agitator agitator, Shooter shooter, Stager stager, CommandSwerveDrivetrain drivetrain, Arm arm,
                    Vision vision, double end, double wheelspeed) {

        this.agitator = agitator;
        this.shooter = shooter;
        this.stager = stager;
        //this.drivetrain = drivetrain;
        this.arm = arm;
        this.vision = vision;

        this.end = end;
        this.wheelspeed = wheelspeed;
        this.oscillatingTimer = new Timer();
        this.shootTimer = new Timer();

        addRequirements(agitator, shooter, stager, arm, vision); 
    
    }
    @Override
    public void initialize() {

        agitator.startTimer();

        //arm.startOscillate();
        oscillatingTimer.reset();
        oscillatingTimer.start();
        shootTimer.reset();
        shootTimer.start();

        arm.moveArmToPosition(90);

    }

@Override
public void execute() {

        /*Pose2d pose = drivetrain.getState().Pose;
        double x = pose.getX();
        double y = pose.getY(); // Get the robot's current position on the field, which can be used for distance-based adjustments to shooting
        double rotation = pose.getRotation().getDegrees(); // Get the robot's current rotation in degrees relative to the field, can be used to aim turret*/

        double distance = vision.getTy();
        shooter.setVelocityFromDistance(distance);
        
        stager.setStagerSpeed(wheelspeed);
        agitator.setAgitatorSpeed(wheelspeed);
        
        //agitator.feedPeriodic(); method to run agitator and unjam if necessary

        // moves arm back and forth to help with feeding
        /*if (oscillatingTimer.hasElapsed(1)){
            arm.oscillate();
        }*/

    }

    public boolean isFinished() {
        return shootTimer.hasElapsed(end);
    }

    public void end(boolean interrupted) {
        shooter.stopShooter();
        stager.stopStager();
        agitator.stopAgitator();
        arm.moveArmToPosition(80);
    }

}

