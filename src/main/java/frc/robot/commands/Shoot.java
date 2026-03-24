package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Agitator;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Stager;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.subsystems.Arm;
import edu.wpi.first.wpilibj.Timer; 


/**
 * AimAndShootCommand
 *
 * Continuously reads limelight data every loop cycle to:
 *   1. Align the turret horizontally using tx
 *   2. Spin up the shooter to the correct RPM using ty → distance → RPM lookup
 *
 * Because it re-reads tx and ty every loop, this command works while the robot
 * is moving — the turret will actively track the target and the shooter will
 * continuously adjust speed. This is the "on-the-fly shooting" behavior.
 *
 * The command never finishes on its own — it runs until the button is released
 * or it is interrupted. Wire up feed/indexer logic separately, gated on
 * isReadyToShoot() returning true.
 */

public class Shoot extends Command {

    private final Agitator agitator;
    private final Shooter shooter;
    private final Stager stager;    
    private final double speed;
    private final CommandSwerveDrivetrain drivetrain;
    private final Arm arm;
    private final Timer timer;


    




    public Shoot(Agitator agitator, Shooter shooter, Stager stager, CommandSwerveDrivetrain drivetrain,Arm arm,double speed) {
        this.agitator = agitator;
        this.shooter = shooter;
        this.stager = stager;
        this.speed = speed;
        this.drivetrain = drivetrain;
        this.arm = arm;
        timer = new Timer();
        addRequirements(agitator, shooter, stager); // Declare subsystem dependencies
    
}
@Override
public void initialize() {

    agitator.startTimer();
    arm.startOscillate();

    timer.reset();
    timer.start();
    
    


    }

@Override
public void execute() {

        Pose2d pose = drivetrain.getState().Pose;
        double x = pose.getX();
        double y = pose.getY(); // Get the robot's current position on the field, which can be used for distance-based adjustments to shooting
        double rotation = pose.getRotation().getDegrees(); // Get the robot's current rotation in degrees relative to the field, can be used to aim turret

        shooter.runShooter(speed);
        stager.setStagerSpeed(-speed);
        agitator.setAgitatorSpeed(-speed);
        //agitator.feedPeriodic(); method to run agitator and unjam if necessary

        //if timer.hasElapsed(2){
            //arm.oscillate();
        //}
         // Move the arm back and forth while shooting to help with feeding


        /* Need to add a method in arm subsystem to move the arm back and forth while shooting to help with feeding */

    }

    public void end(boolean interrupted) {
        shooter.stopShooter();
        stager.stopStager();
        agitator.stopAgitator();
        
    }


}

